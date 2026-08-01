package yosel.dev.facturascan.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Screens: NavKey {

    @Serializable
    data object MyBills: Screens

    @Serializable
    data object UploadBill: Screens

    @Serializable
    data class DetailBill(val idBill: String): Screens

    @Serializable
    data object Register: Screens
}