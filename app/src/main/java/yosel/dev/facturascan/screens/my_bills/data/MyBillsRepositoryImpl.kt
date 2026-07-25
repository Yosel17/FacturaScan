package yosel.dev.facturascan.screens.my_bills.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import yosel.dev.facturascan.core.data_source.BillsDataSource
import yosel.dev.facturascan.core.models.model.BillModel
import yosel.dev.facturascan.core.utils.toBillListResponseToModel
import yosel.dev.facturascan.screens.my_bills.domain.MyBillsRepository
import javax.inject.Inject

class MyBillsRepositoryImpl @Inject constructor(
    private val billsDataSource: BillsDataSource
): MyBillsRepository {

    override suspend fun getAllBills(): Result<List<BillModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val bills = billsDataSource.getAllBills()
                Result.success(bills.toBillListResponseToModel())
            } catch (e: Exception) {
                Result.failure(exception = e)
            }
        }
    }
}