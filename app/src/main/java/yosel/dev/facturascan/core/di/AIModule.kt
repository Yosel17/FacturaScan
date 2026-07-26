package yosel.dev.facturascan.core.di

import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
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
            temperature = 0.7f
            topP = 0.95f
            topK = 40
        }

        return Firebase.ai.generativeModel(
            modelName = "gemini-3.5-flash-lite",
            generationConfig = config
        )
    }
}