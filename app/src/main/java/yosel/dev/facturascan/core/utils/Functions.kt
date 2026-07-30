package yosel.dev.facturascan.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import kotlin.time.Instant

fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
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

private val spanishMonths = listOf(
    "ene", "feb", "mar", "abr", "may", "jun",
    "jul", "ago", "sep", "oct", "nov", "dic"
)

/**
 * Convierte milisegundos UTC a texto con formato "30 jul 2026" usando kotlin.time.Instant
 */
fun Long.toFormattedIssueDate(): String {
    if (this <= 0L) return ""

    val instant = Instant.fromEpochMilliseconds(this)
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.forLanguageTag("es-ES")).apply {
        timeInMillis = instant.toEpochMilliseconds()
    }

    val day = calendar.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')
    val monthIndex = calendar.get(Calendar.MONTH) // 0 - 11
    val monthStr = spanishMonths.getOrElse(monthIndex) { "ene" }
    val year = calendar.get(Calendar.YEAR)

    return "$day $monthStr $year"
}

/**
 * Parsea un texto como "30 jul 2026" (o "2026-07-30") a milisegundos UTC usando kotlin.time.Instant
 */
fun String.parseIssueDateToMillis(): Long? {
    if (this.isBlank()) return null
    val cleanInput = this.replace(".", "").trim().lowercase()

    // 1. Manejo de formato "30 jul 2026" o "7 ago 2026"
    val parts = cleanInput.split(" ")
    if (parts.size == 3) {
        val day = parts[0].toIntOrNull() ?: return null
        val monthStr = parts[1]
        val year = parts[2].toIntOrNull() ?: return null

        val monthIndex = spanishMonths.indexOf(monthStr)
        if (monthIndex == -1) return null

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            clear()
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, monthIndex)
            set(Calendar.DAY_OF_MONTH, day)
        }

        return Instant.fromEpochMilliseconds(calendar.timeInMillis).toEpochMilliseconds()
    }

    // 2. Fallback para formato ISO "2026-07-30" por si la IA devuelve ese formato
    if (cleanInput.contains("-")) {
        val isoParts = cleanInput.split("-")
        if (isoParts.size == 3) {
            val year = isoParts[0].toIntOrNull() ?: return null
            val monthIndex = (isoParts[1].toIntOrNull() ?: return null) - 1
            val day = isoParts[2].toIntOrNull() ?: return null

            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                clear()
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, monthIndex)
                set(Calendar.DAY_OF_MONTH, day)
            }

            return Instant.fromEpochMilliseconds(calendar.timeInMillis).toEpochMilliseconds()
        }
    }

    return null
}