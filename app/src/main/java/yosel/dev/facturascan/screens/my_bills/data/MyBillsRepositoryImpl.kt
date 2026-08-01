package yosel.dev.facturascan.screens.my_bills.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import yosel.dev.facturascan.core.data_source.BillsDataSource
import yosel.dev.facturascan.core.models.model.BillModel
import yosel.dev.facturascan.core.room.tables.bill.BillDao
import yosel.dev.facturascan.core.utils.toBillListResponseToModel
import yosel.dev.facturascan.core.utils.toEntity
import yosel.dev.facturascan.core.utils.toModel
import yosel.dev.facturascan.screens.my_bills.domain.MyBillsRepository
import javax.inject.Inject

class MyBillsRepositoryImpl @Inject constructor(
    private val billsDataSource: BillsDataSource,
    private val billDao: BillDao
): MyBillsRepository {

    override fun getAllBills(): Flow<List<BillModel>> {
        return billDao.getAllBills()
            .map { entities ->
                entities.map { it.toModel()}
            }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun syncBills(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val remoteBills = billsDataSource.getAllBills()
                val entities = remoteBills.map { it.toEntity() }

                billDao.upsertBills(entities)

                Result.success(Unit)
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) throw e
                Result.failure(e)
            }
        }
    }
}