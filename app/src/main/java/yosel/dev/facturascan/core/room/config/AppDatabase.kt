package yosel.dev.facturascan.core.room.config

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import yosel.dev.facturascan.core.room.tables.bill.BillDao
import yosel.dev.facturascan.core.room.tables.bill.BillEntity
import yosel.dev.facturascan.core.room.tables.user.UserDao
import yosel.dev.facturascan.core.room.tables.user.UserEntity

@Database(
    entities = [BillEntity::class, UserEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun billDao(): BillDao

    abstract fun userDao(): UserDao
}