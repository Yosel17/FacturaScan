package yosel.dev.facturascan.splash.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import yosel.dev.facturascan.core.data_source.UserDataSource
import yosel.dev.facturascan.core.models.model.UserModel
import yosel.dev.facturascan.core.room.tables.user.UserDao
import yosel.dev.facturascan.core.utils.toEntity
import yosel.dev.facturascan.core.utils.toModel
import yosel.dev.facturascan.splash.domain.SplashRepository
import javax.inject.Inject

class SplashRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val userDataSource: UserDataSource
): SplashRepository {

    override suspend fun getInfoUser(): Result<UserModel?> {
        return withContext(Dispatchers.IO) {
            try {
                val user = userDao.getUser()
                if (user == null){
                    Result.success(null)
                }else{
                    Result.success(user.toModel())
                }
            } catch (e: Exception) {
                Result.failure(exception = e)
            }
        }
    }

    override suspend fun getInfoUserFromFirestoreAndSync(id: String): Result<UserModel> {
        return withContext(Dispatchers.IO) {
            try {
                val user = userDataSource.getUser(id = id)
                if (user == null){
                    Result.failure(exception = Exception("Usuario no encontrado desde la nube"))
                }else{
                    userDao.updateUser(user = user.toEntity())
                    Result.success(user.toModel())
                }

            } catch (e: Exception) {
                Result.failure(exception = e)
            }
        }
    }
}