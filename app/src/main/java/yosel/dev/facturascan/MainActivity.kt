package yosel.dev.facturascan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import yosel.dev.facturascan.core.navigation.AppNavigation
import yosel.dev.facturascan.core.navigation.Screens
import yosel.dev.facturascan.ui.theme.FacturaScanTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FacturaScanTheme {
                AppNavigation(startDestination = Screens.MyBills)
            }
        }
    }
}

