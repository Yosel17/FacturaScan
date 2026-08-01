package yosel.dev.facturascan.screens.register.ui

sealed interface RegisterAction {
    data class OnNameChanged(val name: String) : RegisterAction
    data class OnAccessCodeChanged(val code: String) : RegisterAction
    data object OnTogglePasswordVisibility : RegisterAction
    data object OnSubmitClick : RegisterAction
    data object OnDismissErrorDialog : RegisterAction
}