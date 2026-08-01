package yosel.dev.facturascan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import yosel.dev.facturascan.core.navigation.AppNavigation
import yosel.dev.facturascan.splash.ui.AccountDisabledScreen
import yosel.dev.facturascan.splash.ui.BootErrorScreen
import yosel.dev.facturascan.splash.ui.SplashViewModel
import yosel.dev.facturascan.ui.theme.FacturaScanTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        splashScreen.setKeepOnScreenCondition {
            splashViewModel.isLoading.value
        }
        setContent {

            val startDestination by splashViewModel.startDestination.collectAsStateWithLifecycle()
            val initializationError by splashViewModel.initializationError.collectAsStateWithLifecycle()
            val deactivatedUser by splashViewModel.deactivatedUser.collectAsStateWithLifecycle()

            FacturaScanTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (deactivatedUser != null){
                        AccountDisabledScreen()
                    } else if (startDestination != null){
                        AppNavigation(startDestination = startDestination!!)
                    } else if(initializationError != null){
                        BootErrorScreen(
                            error = initializationError!!,
                        )
                    }
                }
            }
        }
    }
}

