package yosel.dev.facturascan.core.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.content.FileProvider
import yosel.dev.facturascan.core.models.ai.BillAiResponse
import yosel.dev.facturascan.core.models.model.BillModel
import yosel.dev.facturascan.core.models.model.UserModel
import yosel.dev.facturascan.core.models.request.BillRequest
import yosel.dev.facturascan.core.models.response.BillResponse
import yosel.dev.facturascan.core.models.response.UserResponse
import yosel.dev.facturascan.core.room.tables.bill.BillEntity
import yosel.dev.facturascan.core.room.tables.user.UserEntity
import java.io.File
import java.lang.System
import java.text.DecimalFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun BillResponse.toModel(): BillModel{
    return BillModel(
        id = id,
        createdAt = createdAt?.time ?: System.currentTimeMillis(),
        companyName = companyName,
        vendorTaxId = vendorTaxId,
        customerTaxId = customerTaxId,
        invoiceNumber = invoiceNumber,
        serialNumber = serialNumber,
        authorizationNumber = authorizationNumber,
        issueDate = issueDate,
        totalAmount = totalAmount,
        description = description,
        imageUrl = imageUrl,
        status = status
    )
}

fun BillResponse.toEntity(): BillEntity = BillEntity(
    id = id,
    createdAt = createdAt?.time ?: System.currentTimeMillis() ,
    companyName = companyName,
    vendorTaxId = vendorTaxId,
    customerTaxId = customerTaxId,
    invoiceNumber = invoiceNumber,
    serialNumber = serialNumber,
    authorizationNumber = authorizationNumber,
    issueDate = issueDate,
    totalAmount = totalAmount,
    description = description,
    imageUrl = imageUrl,
    status = status
)

fun BillAiResponse.toModel(imageUrl: String = ""): BillModel {
    return BillModel(
        companyName = companyName,
        vendorTaxId = vendorTaxId,
        customerTaxId = customerTaxId,
        invoiceNumber = invoiceNumber,
        serialNumber = serialNumber,
        authorizationNumber = authorizationNumber,
        issueDate = issueDate,
        totalAmount = totalAmount,
        description = description,
        imageUrl = imageUrl
    )
}

fun BillModel.toEntity(): BillEntity = BillEntity(
    id = id,
    createdAt = createdAt,
    companyName = companyName,
    vendorTaxId = vendorTaxId,
    customerTaxId = customerTaxId,
    invoiceNumber = invoiceNumber,
    serialNumber = serialNumber,
    authorizationNumber = authorizationNumber,
    issueDate = issueDate,
    totalAmount = totalAmount,
    description = description,
    imageUrl = imageUrl,
    status = status
)

fun BillModel.toRequest(): BillRequest = BillRequest(
    id = id,
    createdAt = null,
    companyName = companyName,
    vendorTaxId = vendorTaxId,
    customerTaxId = customerTaxId,
    invoiceNumber = invoiceNumber,
    serialNumber = serialNumber,
    authorizationNumber = authorizationNumber,
    issueDate = issueDate,
    totalAmount = totalAmount,
    description = description,
    imageUrl = imageUrl,
    status = status
)

fun BillModel.toMap(): Map<String, Any?> {
    return mapOf(
        "companyName" to companyName,
        "vendorTaxId" to vendorTaxId,
        "customerTaxId" to customerTaxId,
        "invoiceNumber" to invoiceNumber,
        "serialNumber" to serialNumber,
        "issueDate" to issueDate,
        "totalAmount" to totalAmount,
        "description" to description,
    )
}

fun BillEntity.toModel(): BillModel = BillModel(
    id = id,
    createdAt = createdAt,
    companyName = companyName,
    vendorTaxId = vendorTaxId,
    customerTaxId = customerTaxId,
    invoiceNumber = invoiceNumber,
    serialNumber = serialNumber,
    authorizationNumber = authorizationNumber,
    issueDate = issueDate,
    totalAmount = totalAmount,
    description = description,
    imageUrl = imageUrl,
    status = status
)

fun UserEntity.toModel(): UserModel = UserModel(
    id = id,
    name = name,
    accessCode = accessCode,
    firstDevice = firstDevice,
    devices = devices,
    status = status
)

fun UserResponse.toEntity(): UserEntity = UserEntity(
    id = id,
    name = name,
    accessCode = accessCode,
    firstDevice = firstDevice,
    devices = devices,
    status = status
)

fun UserResponse.toModel(): UserModel = UserModel(
    id = id,
    name = name,
    accessCode = accessCode,
    firstDevice = firstDevice,
    devices = devices,
    status = status
)
fun List<BillResponse>.toBillListResponseToModel(): List<BillModel>{
    return map { it.toModel() }
}

// Formateador de moneda (reutilizable)
private val currencyFormatExact = DecimalFormat("Q#,##0")
private val currencyFormatDecimal = DecimalFormat("Q#,##0.00")

fun Double.formatAmount(): String {
    return if (this % 1.0 == 0.0) {
        currencyFormatExact.format(this)
    } else {
        currencyFormatDecimal.format(this)
    }
}

// Formateador de fecha moderno e inmutable
private val spanishLocale = Locale.forLanguageTag("es-ES")
private val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yy", spanishLocale)

fun Long.formatDate(): String {
    if (this <= 0L) return "sin fecha"

    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .format(dateFormatter)
        .lowercase()
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun Context.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }.also { startActivity(it) }
}

fun Context.createTempUri(
    prefix: String = "temp_",
    suffix: String = ".jpg"
): Uri {
    val file = File.createTempFile(prefix, suffix, cacheDir)
    return FileProvider.getUriForFile(
        this,
        "$packageName.fileprovider",
        file
    )
}