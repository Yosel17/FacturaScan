package yosel.dev.facturascan.screens.register.domain

interface RegisterRepository {

    suspend fun registerUser(name: String, accessCode: String): Result<Unit>
}