package yosel.dev.facturascan.core.models.model

data class UserModel(
    val id: String = "",
    val name: String = "",
    val accessCode: String = "",
    val firstDevice: String = "",
    val devices: List<String> = emptyList(),
    val status: Int = 0
)
