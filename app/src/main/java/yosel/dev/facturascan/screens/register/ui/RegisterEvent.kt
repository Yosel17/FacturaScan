package yosel.dev.facturascan.screens.register.ui

import yosel.dev.facturascan.core.navigation.Screens

sealed interface RegisterEvent {
    data class ShowErrorSnackbar(val message: String) : RegisterEvent
    data class OnNavigation(val screen: Screens) : RegisterEvent
}