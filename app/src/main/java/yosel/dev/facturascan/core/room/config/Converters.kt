package yosel.dev.facturascan.core.room.config

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class Converters {

    @TypeConverter
    fun fromListToString(list: List<String>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun toListFromString(value: String): List<String> {
        return if (value.isBlank()) {
            emptyList()
        } else {
            Json.decodeFromString(value)
        }
    }
}