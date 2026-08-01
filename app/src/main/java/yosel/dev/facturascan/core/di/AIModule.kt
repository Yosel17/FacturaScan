package yosel.dev.facturascan.core.di

import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AIModule {

    @Provides
    @Singleton
    fun provideGenerativeModel(): GenerativeModel {
        val config = generationConfig {
            temperature = 0.1f
            responseMimeType = "application/json"
        }

        return Firebase.ai.generativeModel(
            modelName = "gemini-3.5-flash-lite",
            generationConfig = config,
            systemInstruction = content {
                text("""
                    Eres un asistente especializado en extraer datos de facturas.
                    Reglas estrictas:
                    1. Si la imagen NO es una factura o es completamente ilegible, devuelve TODOS los campos de texto como string vacío ("") y los campos numéricos como 0.0.
                    2. Si un dato específico no se encuentra o no se puede leer, devuelve un string vacío ("") para ese campo.
                    3. No inventes ni deduzcas información. Extrae solo lo visible.
                """.trimIndent())
            }
        )
    }
}