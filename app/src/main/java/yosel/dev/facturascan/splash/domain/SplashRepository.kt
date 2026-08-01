package yosel.dev.facturascan.splash.domain

import yosel.dev.facturascan.core.models.model.UserModel

interface SplashRepository {

    suspend fun getInfoUser(): Result<UserModel?>

}