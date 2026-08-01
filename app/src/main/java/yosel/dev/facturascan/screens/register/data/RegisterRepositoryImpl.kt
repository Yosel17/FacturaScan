package yosel.dev.facturascan.screens.register.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import yosel.dev.facturascan.core.data_source.UserDataSource
import yosel.dev.facturascan.core.room.tables.user.UserDao
import yosel.dev.facturascan.core.utils.Constants
import yosel.dev.facturascan.core.utils.toEntity
import yosel.dev.facturascan.core.utils.toMap
import yosel.dev.facturascan.screens.register.domain.RegisterRepository
import java.util.UUID
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val userDao: UserDao
): RegisterRepository {

    override suspend fun registerUser(name: String, accessCode: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val user = userDataSource.getUserByName(name = name)
                    ?: return@withContext Result.failure(Exception("El usuario no existe"))

                if (user.accessCode != accessCode) {
                    return@withContext Result.failure(Exception("El código de acceso es incorrecto"))
                }

                if (user.status == Constants.DEACTIVATED_USER_STATE){
                    return@withContext Result.failure(Exception("El usuario se encuentra desactivado"))
                }

                val idDevice = UUID.randomUUID().toString()

                val newUser = if (user.firstDevice.isEmpty()) {
                    user.copy(firstDevice = idDevice)
                } else {
                    user.copy(devices = user.devices + idDevice)
                }

                userDao.upsertUser(newUser.toEntity())
                userDataSource.updateUser(id = newUser.id, updates = newUser.toMap())

                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(exception = e)
            }
        }
    }
}