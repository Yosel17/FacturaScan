package yosel.dev.facturascan.splash.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import yosel.dev.facturascan.core.models.model.UserModel
import yosel.dev.facturascan.core.room.tables.user.UserDao
import yosel.dev.facturascan.core.utils.toModel
import yosel.dev.facturascan.splash.domain.SplashRepository
import javax.inject.Inject

class SplashRepositoryImpl @Inject constructor(
    private val userDao: UserDao
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
}