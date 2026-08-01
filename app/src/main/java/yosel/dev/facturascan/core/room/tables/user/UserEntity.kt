package yosel.dev.facturascan.core.room.tables.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val id: String = "",
    val name: String = "",
    val accessCode: String = "",
    val firstDevice: String = "",
    val devices: List<String> = emptyList(),
    val status: Int = 0
)