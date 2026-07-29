package yosel.dev.facturascan.screens.upload_bill.domain

import android.net.Uri
import yosel.dev.facturascan.core.models.model.BillModel

interface UploadBillRepository {

    suspend fun processInvoice(imageUri: Uri): Result<BillModel>
}