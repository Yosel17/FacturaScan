package yosel.dev.facturascan.core.room.tables.bill

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface BillDao {

    @Upsert
    suspend fun upsertBill(bill: BillEntity)

    @Query("SELECT * FROM bills WHERE id = :id")
    suspend fun getBillById(id: String): BillEntity?

    @Query("SELECT * FROM bills ORDER BY createdAt DESC")
    fun getAllBills(): Flow<List<BillEntity>>

    @Upsert
    suspend fun upsertBills(bills: List<BillEntity>)

    @Query("DELETE FROM bills WHERE id = :id")
    suspend fun deleteBillById(id: String)

    @Update
    suspend fun updateBill(bill: BillEntity)
}