package yosel.dev.facturascan.screens.detail_bill.ui

import android.content.ClipData
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.BrokenImage
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.CorporateFare
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import yosel.dev.facturascan.core.utils.Constants
import yosel.dev.facturascan.core.utils.parseIssueDateToMillis
import yosel.dev.facturascan.core.utils.toFormattedIssueDate
import yosel.dev.facturascan.ui.theme.FacturaScanTheme


@Composable
fun BodyDetailBill(
    modifier: Modifier = Modifier,
    state: DetailBillState,
    onAction: (DetailBillAction) -> Unit
) {

    val clipboard = LocalClipboard.current
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        item {
            InvoiceImageHeader(
                imageUri = state.currentBill.imageUrl
            )
        }

        item {
            BillDataForm(
                formState = state.formState,
                onFormStateChange = { value, field ->
                    onAction(DetailBillAction.OnChangeValueFormState(value, field))
                },
                onCopyClick = { label, value ->
                    onAction(DetailBillAction.OnCopyFieldClick(label, value))
                }
            )
        }

        if (state.currentBill.status == Constants.DRAFT_STATUS){
            item {
                DataReviewAlertCard()
            }

        }

        item {
            val isActionButtonEnabled = if (state.currentBill.status == Constants.DRAFT_STATUS) {
                state.isSaveEnabled
            } else {
                state.isEditEnabled
            }

            BillActionButtons(
                billStatus = state.currentBill.status,
                actionEnabled = isActionButtonEnabled,
                onCopyAllClick = {
                    val textToCopy = state.formState.formattedCopyText
                    if (textToCopy.isNotEmpty()) {
                        coroutineScope.launch {
                            val clipData = ClipData.newPlainText("Datos de Factura", textToCopy)
                            clipboard.setClipEntry(ClipEntry(clipData))
                            onAction(DetailBillAction.OnCopyAllClick)
                        }
                    }
                },
                onSaveToHistoryClick = {
                    onAction(DetailBillAction.SaveBill)
                },
                onEditClick = {
                    onAction(DetailBillAction.UpdateBill)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun InvoiceImageHeader(
    imageUri: String?,
    modifier: Modifier = Modifier
) {
    val imageShape = RoundedCornerShape(16.dp)

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUri)
            .crossfade(true)
            .build(),
        contentDescription = "Vista previa de la factura",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 10f)
            .clip(imageShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        loading = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator(
                    modifier = Modifier.size(32.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        error = {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.BrokenImage,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = "Error al cargar la imagen",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    )
}

@Composable
fun EmptyBillDetailsState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.SearchOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))


        Text(
            text = "Sin información disponible",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))


        Text(
            text = "No logramos obtener los datos de esta factura. Intenta consultar otra o revisa el documento.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun BillInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: ImageVector,
    onCopyClick: (label: String, value: String) -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 1,
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    onClick: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val clipboard = LocalClipboard.current
    val interactionSource = remember { MutableInteractionSource() }
    val coroutineScope = rememberCoroutineScope()

    if (readOnly && onClick != null) {
        LaunchedEffect(interactionSource) {
            interactionSource.interactions.collect { interaction ->
                if (interaction is PressInteraction.Release) {
                    onClick()
                }
            }
        }
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
            )
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            val clipData = ClipData.newPlainText(label, value)
                            clipboard.setClipEntry(ClipEntry(clipData))
                            onCopyClick(label, value)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ContentCopy,
                        contentDescription = "Copiar $label",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        isError = isError,
        supportingText = if (isError && !errorMessage.isNullOrEmpty()) {
            { Text(text = errorMessage) }
        } else null,
        readOnly = readOnly,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        keyboardOptions = keyboardOptions,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyLarge,
        colors = OutlinedTextFieldDefaults.colors(
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            errorLeadingIconColor = MaterialTheme.colorScheme.error
        )
    )
}

@Composable
fun BillDatePickerDialog(
    initialDateMillis: Long?,
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis ?: System.currentTimeMillis()
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        onDateSelected(millis.toFormattedIssueDate())
                    }
                    onDismiss()
                }
            ) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun BillDataForm(
    formState: DetailBillFormState,
    onFormStateChange: (String, Int) -> Unit,
    onCopyClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Datos Extraídos",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        BillInputField(
            label = "Nombre de la empresa",
            value = formState.companyName,
            onValueChange = { onFormStateChange(it, Constants.COMPANY_NAME_FIELD) },
            leadingIcon = Icons.Outlined.CorporateFare,
            isError = formState.companyName.isBlank(),
            errorMessage = "Este campo no puede estar vacío",
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            onCopyClick = onCopyClick,
        )

        BillInputField(
            label = "Número de Factura",
            value = formState.billNumber,
            onValueChange = { onFormStateChange(it, Constants.BILL_NUMBER_FIELD) },
            leadingIcon = Icons.Outlined.Receipt,
            isError = formState.billNumber.isBlank(),
            errorMessage = "Este campo no puede estar vacío",
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            onCopyClick = onCopyClick,
        )

        BillInputField(
            label = "Número de Serie",
            value = formState.serialNumber,
            onValueChange = { onFormStateChange(it, Constants.SERIAL_NUMBER_FIELD) },
            leadingIcon = Icons.Outlined.Key,
            isError = formState.serialNumber.isBlank(),
            errorMessage = "Este campo no puede estar vacío",
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            onCopyClick = onCopyClick,
        )

        BillInputField(
            label = "Fecha de Emisión",
            value = formState.issueDate,
            onValueChange = { onFormStateChange(it, Constants.ISSUE_DATE_FIELD) },
            leadingIcon = Icons.Outlined.CalendarToday,
            isError = formState.issueDate.isBlank(),
            errorMessage = "Este campo no puede estar vacío",
            readOnly = true,
            onClick = { showDatePicker = true },
            onCopyClick = onCopyClick,
        )

        BillInputField(
            label = "NIT del Proveedor",
            value = formState.vendorTaxId,
            onValueChange = { onFormStateChange(it, Constants.VENDOR_TAX_ID_FIELD) },
            leadingIcon = Icons.Outlined.Badge,
            isError = formState.vendorTaxId.isBlank(),
            errorMessage = "Este campo no puede estar vacío",
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            onCopyClick = onCopyClick,
        )

        BillInputField(
            label = "NIT Organización (Receptor)",
            value = formState.customerTaxId,
            onValueChange = { onFormStateChange(it, Constants.CUSTOMER_TAX_ID) },
            leadingIcon = Icons.Outlined.Business,
            isError = formState.customerTaxId.isBlank(),
            errorMessage = "Este campo no puede estar vacío",
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            onCopyClick = onCopyClick,
        )

        BillInputField(
            label = "Total de la factura",
            value = formState.totalAmount,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(Regex("""^\d*\.?\d*$"""))) {
                    onFormStateChange(newValue, Constants.TOTAL_AMOUNT_FIELD)
                }
            },
            leadingIcon = Icons.Outlined.Payments,
            isError = formState.totalAmount.isBlank(),
            errorMessage = "Este campo no puede estar vacío",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            onCopyClick = onCopyClick,
        )

        BillInputField(
            label = "Descripción (opcional)",
            value = formState.description,
            onValueChange = { onFormStateChange(it, Constants.DESCRIPTION_FIELD) },
            leadingIcon = Icons.Outlined.Description,
            singleLine = false,
            minLines = 1,
            maxLines = 4,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Default
            ),
            onCopyClick = onCopyClick,
        )
    }

    if (showDatePicker) {
        val initialMillis = remember(formState.issueDate) {
            formState.issueDate.parseIssueDateToMillis()
        }
        BillDatePickerDialog(
            initialDateMillis = initialMillis,
            onDateSelected = { formattedDate ->
                onFormStateChange(formattedDate, Constants.ISSUE_DATE_FIELD)
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@Composable
fun DataReviewAlertCard(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Outlined.Info,
    title: String = "¡Revisa los datos!",
    description: String = "Por favor, revise que los datos coincidan con el documento físico antes de guardar."
) {

    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Icon(
                imageVector = icon,
                contentDescription = "Icono de aviso",
                tint = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier
                    .size(24.dp)
                    .padding(top = 2.dp)
            )


            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f),
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Composable
fun BillActionButtons(
    onCopyAllClick: () -> Unit,
    onSaveToHistoryClick: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    actionEnabled: Boolean = true,
    billStatus: Int
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onCopyAllClick,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(
                imageVector = Icons.Outlined.ContentCopy,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Copiar todo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        if (billStatus == Constants.DRAFT_STATUS) {
            Button(
                onClick = onSaveToHistoryClick,
                enabled = actionEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Cloud,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Guardar en la nube",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else {
            Button(
                onClick = onEditClick,
                enabled = actionEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Editar factura",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    FacturaScanTheme {
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
            ) {
                BillActionButtons(
                    onCopyAllClick = {},
                    onSaveToHistoryClick = {},
                    billStatus = Constants.SAVE_STATUS,
                    onEditClick = {}
                )
            }
        }
    }
}