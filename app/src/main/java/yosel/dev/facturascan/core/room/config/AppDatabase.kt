package yosel.dev.facturascan.core.room.config

import androidx.room.Database
import androidx.room.RoomDatabase
import yosel.dev.facturascan.core.room.tables.bill.BillDao
import yosel.dev.facturascan.core.room.tables.bill.BillEntity

@Database(
    entities = [BillEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun billDao(): BillDao
}