package yosel.dev.facturascan.screens.upload_bill.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.type.content
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import yosel.dev.facturascan.core.models.ai.BillAiResponse
import yosel.dev.facturascan.core.models.model.BillModel
import yosel.dev.facturascan.core.utils.toModel
import yosel.dev.facturascan.screens.upload_bill.domain.UploadBillRepository
import javax.inject.Inject

class UploadBillRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val generativeModel: GenerativeModel
):  UploadBillRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    override suspend fun processInvoice(imageUri: Uri): Result<BillModel> {
        return withContext(Dispatchers.IO) {
            try {
                val bitmap = uriToBitmap(imageUri)
                    ?: return@withContext Result.failure(Exception("No se pudo cargar la imagen"))

                // 🚀 El prompt ahora solo dicta la estructura, las reglas ya están en AIModule
                val prompt = """
                    Extrae la información de esta factura en el siguiente formato JSON:
                    {
                      "company_name": "Nombre de la empresa o emisor",
                      "vendor_tax_id": "NIT o RUC del emisor",
                      "customer_tax_id": "NIT o RUC del cliente",
                      "invoice_number": "Número de factura",
                      "serial_number": "Serie de la factura",
                      "authorization_number": "Número de autorización / DTE",
                      "issue_date": "Fecha de emisión (YYYY-MM-DD)",
                      "total_amount": 0.0,
                    }
                """.trimIndent()

                val inputContent = content {
                    image(bitmap)
                    text(prompt)
                }

                val response = generativeModel.generateContent(inputContent)

                // 🚀 Como el mimeType es "application/json", rawText es un JSON puro, sin bloques markdown
                val rawText = response.text ?: throw Exception("Respuesta vacía de la IA")

                val aiResponse = json.decodeFromString<BillAiResponse>(rawText)
                Result.success(aiResponse.toModel(imageUrl = imageUri.toString()))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    // 🚀 Redimensionamiento y optimización nativa (ahorra RAM y tokens sin perder lectura)
    private fun uriToBitmap(uri: Uri): Bitmap? {
        val maxDimension = 1536 // Límite ideal para escaneo OCR

        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
                    // Usar allocador de software previene crasheos en IA de "Hardware bitmaps not supported"
                    decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE

                    val width = info.size.width
                    val height = info.size.height

                    if (width > maxDimension || height > maxDimension) {
                        val scale = maxDimension.toFloat() / maxOf(width, height)
                        decoder.setTargetSize((width * scale).toInt(), (height * scale).toInt())
                    }
                }
            } else {
                // Fallback clásico para APIs antiguas (< API 28)
                val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                context.contentResolver.openInputStream(uri)?.use {
                    BitmapFactory.decodeStream(it, null, options)
                }

                val (width, height) = options.outWidth to options.outHeight
                var inSampleSize = 1
                if (width > maxDimension || height > maxDimension) {
                    val halfHeight = height / 2
                    val halfWidth = width / 2
                    while (halfHeight / inSampleSize >= maxDimension && halfWidth / inSampleSize >= maxDimension) {
                        inSampleSize *= 2
                    }
                }

                options.inJustDecodeBounds = false
                options.inSampleSize = inSampleSize
                context.contentResolver.openInputStream(uri)?.use {
                    BitmapFactory.decodeStream(it, null, options)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}