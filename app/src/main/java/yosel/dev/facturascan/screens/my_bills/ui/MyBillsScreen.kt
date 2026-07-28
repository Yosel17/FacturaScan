package yosel.dev.facturascan.screens.my_bills.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import yosel.dev.facturascan.core.components.SnackBarError
import yosel.dev.facturascan.core.components.TopBarGlobal
import yosel.dev.facturascan.core.navigation.Screens
import yosel.dev.facturascan.ui.theme.FacturaScanTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MyBillsScreen(
    modifier: Modifier = Modifier,
    state: MyBillsState,
    snackBarHostState: SnackbarHostState,
    onNavigation:(Screens) -> Unit
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState){ data ->
                SnackBarError(data = data)
            }
        },
        topBar = {
            TopBarGlobal(
                title = "FacturaScan"
            )
        },
        floatingActionButton = {
            if (!state.isLoading && state.myBills.isNotEmpty()){
                ExtendedFloatingActionButton(
                    onClick = {
                        onNavigation(Screens.UploadBill)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Photo,
                            contentDescription = "Photo"
                        )
                    },
                    text = { Text(text = "Escanear Factura") },
                    expanded = true
                )
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            AnimatedContent(
                targetState = state,
                transitionSpec = {
                    fadeIn(
                        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                    ) + slideInVertically(
                        initialOffsetY = { fullHeight -> fullHeight / 12 },
                        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                    ) togetherWith fadeOut(
                        animationSpec = tween(durationMillis = 150)
                    )
                },
                contentKey = { targetState ->
                    when {
                        targetState.isLoading -> "LOADING"
                        targetState.myBills.isEmpty() -> "EMPTY"
                        else -> "CONTENT"
                    }
                },
                label = "MyBillsScreenStateTransition"
            ) { targetState ->
                when{
                    targetState.isLoading ->{
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            LoadingIndicator(
                                modifier = Modifier.size(75.dp)
                            )
                        }
                    }
                    targetState.myBills.isEmpty() -> {
                        EmptyBillsState(
                            onScanClick = {
                                onNavigation(Screens.UploadBill)
                            }
                        )
                    }
                    else -> {
                        BodyMyBills(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 24.dp),
                            state = targetState,
                            onBillClick = { bill ->

                            }
                        )
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Screen() {
    FacturaScanTheme{
        MyBillsScreen(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            state = MyBillsState(
                isLoading = false,
                totalAmount = 1234.56,
            ),
            snackBarHostState = SnackbarHostState(),
            onNavigation = {}
        )
    }
}