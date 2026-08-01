package yosel.dev.facturascan.core.room.tables.bill

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bills")
data class BillEntity(
    @PrimaryKey
    val id: String,
    val createdAt: Long,
    val companyName: String,
    val vendorTaxId: String,
    val customerTaxId: String,
    val invoiceNumber: String,
    val serialNumber: String,
    val authorizationNumber: String,
    val issueDate: String,
    val totalAmount: Double,
    val description: String,
    val imageUrl: String,
    val status: Int
)
