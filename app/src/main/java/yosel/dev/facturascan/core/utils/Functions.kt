package yosel.dev.facturascan.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build

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