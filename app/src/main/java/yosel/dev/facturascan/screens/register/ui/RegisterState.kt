package yosel.dev.facturascan.screens.register.ui

data class RegisterState(
    val name: String = "",
    val accessCode: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val enableFields: Boolean = true
) {
    val isSubmitEnabled: Boolean
        get() = name.isNotBlank() && accessCode.isNotBlank() && !isLoading
}
