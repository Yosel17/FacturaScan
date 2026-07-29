package yosel.dev.facturascan.screens.upload_bill.data

import android.content.Context
import android.graphics.Bitmap
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

                val prompt = """
                    Analiza la imagen de esta factura y extrae los datos clave.
                    Devuelve ÚNICAMENTE un objeto JSON estrictamente válido sin formato markdown ni texto adicional.

                    Estructura requerida:
                    {
                      "company_name": "Nombre de la empresa o emisor",
                      "vendor_tax_id": "NIT o RUC del emisor",
                      "customer_tax_id": "NIT o RUC del cliente",
                      "invoice_number": "Número de factura",
                      "serial_number": "Serie de la factura",
                      "authorization_number": "Número de autorización / DTE",
                      "issue_date": "Fecha de emisión (YYYY-MM-DD)",
                      "total_amount": 0.0,
                      "description": "Breve resumen de los ítems o concepto"
                    }
                """.trimIndent()

                val inputContent = content {
                    image(bitmap)
                    text(prompt)
                }

                val response = generativeModel.generateContent(inputContent)
                val rawText = response.text ?: throw Exception("Respuesta vacía de la IA")

                // Limpieza por si la IA incluye bloques ```json ... ```
                val cleanedJson = rawText
                    .replace("```json", "")
                    .replace("```", "")
                    .trim()

                val aiResponse = json.decodeFromString<BillAiResponse>(cleanedJson)
                Result.success(aiResponse.toModel(imageUrl = imageUri.toString()))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}