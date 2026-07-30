package yosel.dev.facturascan.screens.detail_bill.ui

import android.content.ClipData
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.BrokenImage
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import yosel.dev.facturascan.core.utils.Constants
import yosel.dev.facturascan.ui.theme.FacturaScanTheme


@Composable
fun BodyDetailBill(
    modifier: Modifier = Modifier,
    state: DetailBillState,
    onAction: (DetailBillAction) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            InvoiceImageHeader(
                imageUri = state.currentBill.imageUrl
            )
        }

        item {
            Text(
                "Datos Extraídos",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        item {
            BillDataForm(
                formState = state.formState,
                onFormStateChange = { value, field ->
                    onAction(DetailBillAction.OnChangeValueFormState(value, field))
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
    // Definimos el contenedor redondeado según la interfaz de usuario de la imagen
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
            .aspectRatio(16f / 10f) // Mantiene la relación de aspecto responsiva alineada a la UI
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
            // Estado de fallback cuando la URI no existe, expiró o no se pudo cargar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.layout.Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.BrokenImage,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = "Error al cargar la imagen", // p. ej. "No se pudo cargar la imagen"
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
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    val clipboard = LocalClipboard.current
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

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
                            Toast.makeText(context, "$label copiado", Toast.LENGTH_SHORT).show()
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
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyLarge,
        colors = OutlinedTextFieldDefaults.colors(
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@Composable
fun BillDataForm(
    formState: DetailBillFormState,
    onFormStateChange: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 1. Número de Serie
        BillInputField(
            label = "Número de Serie",
            value = formState.serialNumber,
            onValueChange = { onFormStateChange(it, Constants.SERIAL_NUMBER_FIELD) },
            leadingIcon = Icons.Outlined.Key,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        // 2. Número de Factura
        BillInputField(
            label = "Número de Factura",
            value = formState.billNumber,
            onValueChange = { onFormStateChange(it, Constants.BILL_NUMBER_FIELD) },
            leadingIcon = Icons.Outlined.Receipt,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        // 3. Fecha de Emisión
        BillInputField(
            label = "Fecha de Emisión",
            value = formState.issueDate,
            onValueChange = { onFormStateChange(it, Constants.ISSUE_DATE_FIELD) },
            leadingIcon = Icons.Outlined.CalendarToday,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )

        // 4. NIT del Proveedor
        BillInputField(
            label = "NIT del Proveedor",
            value = formState.vendorTaxId,
            onValueChange = { onFormStateChange(it, Constants.VENDOR_TAX_ID_FIELD) },
            leadingIcon = Icons.Outlined.Badge,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        // 5. NIT Organización (Receptor)
        BillInputField(
            label = "NIT Organización (Receptor)",
            value = formState.customerTaxId,
            onValueChange = { onFormStateChange(it, Constants.CUSTOMER_TAX_ID) },
            leadingIcon = Icons.Outlined.Business,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        // 6. Total de la factura (Numérico/Decimal)
        BillInputField(
            label = "Total de la factura",
            value = formState.totalAmount,
            onValueChange = { newValue ->
                // Filtro para aceptar solo números y un único punto decimal
                if (newValue.isEmpty() || newValue.matches(Regex("""^\d*\.?\d*$"""))) {
                    onFormStateChange(newValue, Constants.TOTAL_AMOUNT_FIELD)
                }
            },
            leadingIcon = Icons.Outlined.Payments,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            )
        )

        // 7. Descripción (Multilínea, icono arriba y teclado con 'Done')
        BillInputField(
            label = "Descripción",
            value = formState.description,
            onValueChange = { onFormStateChange(it, Constants.DESCRIPTION_FIELD) },
            leadingIcon = Icons.Outlined.Description,
            singleLine = false,
            minLines = 1,
            maxLines = 4,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            )
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    FacturaScanTheme {
        Text(
            "Datos Extraídos",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}