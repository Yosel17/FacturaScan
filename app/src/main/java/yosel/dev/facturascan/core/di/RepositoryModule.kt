package yosel.dev.facturascan.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import yosel.dev.facturascan.screens.detail_bill.data.DetailBillRepositoryImpl
import yosel.dev.facturascan.screens.detail_bill.domain.DetailBillRepository
import yosel.dev.facturascan.screens.my_bills.data.MyBillsRepositoryImpl
import yosel.dev.facturascan.screens.my_bills.domain.MyBillsRepository
import yosel.dev.facturascan.screens.upload_bill.data.UploadBillRepositoryImpl
import yosel.dev.facturascan.screens.upload_bill.domain.UploadBillRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMyBillsRepository(
        impl: MyBillsRepositoryImpl
    ): MyBillsRepository

    @Binds
    @Singleton
    abstract fun bindUploadBillRepository(
        impl: UploadBillRepositoryImpl
    ): UploadBillRepository

    @Binds
    @Singleton
    abstract fun bindDetailBillRepository(
        impl: DetailBillRepositoryImpl
    ): DetailBillRepository
}