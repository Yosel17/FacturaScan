package yosel.dev.facturascan.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import yosel.dev.facturascan.screens.my_bills.data.MyBillsRepositoryImpl
import yosel.dev.facturascan.screens.my_bills.domain.MyBillsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMyBillsRepository(
        impl: MyBillsRepositoryImpl
    ): MyBillsRepository
}