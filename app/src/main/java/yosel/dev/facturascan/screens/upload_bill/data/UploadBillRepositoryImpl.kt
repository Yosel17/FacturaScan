package yosel.dev.facturascan.screens.upload_bill.data

import android.content.Context
import android.net.Uri
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.type.content
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import yosel.dev.facturascan.core.data_source.BillsDataSource
import yosel.dev.facturascan.core.models.ai.BillAiResponse
import yosel.dev.facturascan.core.models.model.BillModel
import yosel.dev.facturascan.core.room.tables.bill.BillDao
import yosel.dev.facturascan.core.utils.toEntity
import yosel.dev.facturascan.core.utils.toModel
import yosel.dev.facturascan.core.utils.toRequest
import yosel.dev.facturascan.core.utils.uriToBitmap
import yosel.dev.facturascan.screens.upload_bill.domain.UploadBillRepository
import javax.inject.Inject

class UploadBillRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val generativeModel: GenerativeModel,
    private val billDao: BillDao,
    private val billsDataSource: BillsDataSource
):  UploadBillRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    override suspend fun processInvoice(imageUri: Uri): Result<BillModel> {
        return withContext(Dispatchers.IO) {
            try {
                val bitmap = uriToBitmap(context = context, uri = imageUri)
                    ?: return@withContext Result.failure(Exception("No se pudo cargar la imagen"))

                // 🚀 El prompt ahora solo dicta la estructura, las reglas ya están en AIModule
                val prompt = """
                    Extrae la información de esta factura en el siguiente formato JSON estricto.

                    Reglas para el campo "issue_date":
                    - Debe ser en español y en minúsculas.
                    - Día a 2 dígitos (ej: 07 o 30).
                    - Mes abreviado a 3 letras sin punto al final (ej: ene, feb, mar, abr, may, jun, jul, ago, sep, oct, nov, dic).
                    - Año a 4 dígitos.
                    - Ejemplo válido: "30 jul 2026", "07 ago 2026".
                    
                    Estructura JSON:
                    {
                      "company_name": "Nombre de la empresa o emisor",
                      "vendor_tax_id": "NIT o RUC del emisor",
                      "customer_tax_id": "NIT o RUC del cliente",
                      "invoice_number": "Número de factura",
                      "serial_number": "Serie de la factura",
                      "authorization_number": "Número de autorización / DTE",
                      "issue_date": "Fecha de emision ejemplo 30 jul 2026",
                      "total_amount": 0.0
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

    override suspend fun saveBillRoom(bill: BillModel): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val billEntity = bill.toEntity()
                billDao.upsertBill(bill = billEntity)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(exception = e)
            }
        }
    }

    override suspend fun saveBillFirestore(bill: BillModel): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val billRequest = bill.toRequest()
                billsDataSource.createBill(request = billRequest)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(exception = e)
            }
        }
    }
}