package yosel.dev.facturascan.splash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import yosel.dev.facturascan.core.navigation.Screens
import yosel.dev.facturascan.splash.domain.SplashRepository
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val splashRepository: SplashRepository
): ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _startDestination = MutableStateFlow<Screens?>(null)
    val startDestination = _startDestination.asStateFlow()

    private val _initializationError = MutableStateFlow<String?>(null)
    val initializationError = _initializationError.asStateFlow()

    init {
        getInfoUser()
    }

    private fun getInfoUser(){
        viewModelScope.launch {
            splashRepository.getInfoUser()
                .onSuccess { user ->
                    if (user == null){
                        _startDestination.value = Screens.Register
                    }else{
                        //aqui recuperaremos la info del user de la nube y la actualizares en room
                    }
                }.onFailure { error ->
                    handleInitializationError(error)
                }
        }
    }

    private fun handleInitializationError(error: Throwable) {
        _initializationError.value = error.localizedMessage ?: "Error al cargar información inicial"
        _isLoading.value = false
    }
}