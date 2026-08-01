package yosel.dev.facturascan.core.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import yosel.dev.facturascan.core.room.config.AppDatabase
import yosel.dev.facturascan.core.room.tables.bill.BillDao
import yosel.dev.facturascan.core.room.tables.user.UserDao
import yosel.dev.facturascan.core.utils.Constants
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            Constants.TABLE_NAME
        ).fallbackToDestructiveMigration(false)
            .build()
    }

    @Singleton
    @Provides
    fun provideBillDao(appDatabase: AppDatabase): BillDao = appDatabase.billDao()

    @Singleton
    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao = appDatabase.userDao()
}