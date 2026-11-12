package com.example.a040a21mikardex.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.a040a21mikardex.R
import com.example.a040a21mikardex.data.MateriaDao
import com.example.a040a21mikardex.data.Validacion
import com.example.a040a21mikardex.data.validarMateria
import kotlinx.coroutines.launch

/**
 * Formulario para capturar una materia con sus tres calificaciones posibles.
 * Todas las calificaciones son opcionales, pero al menos una tiene que venir.
 */
@Composable
fun CapturaMateriaScreen(
    dao: MateriaDao,
    onGuardada: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var nombre by remember { mutableStateOf("") }
    var semestre by remember { mutableStateOf("1") }
    var normal by remember { mutableStateOf("") }
    var repeticion by remember { mutableStateOf("") }
    var especial by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(stringResource(R.string.captura_titulo), style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text(stringResource(R.string.captura_nombre)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = semestre,
            onValueChange = { semestre = it.filter(Char::isDigit) },
            label = { Text(stringResource(R.string.captura_semestre)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Text(
            stringResource(R.string.captura_calificaciones),
            style = MaterialTheme.typography.titleLarge,
        )
        CalificacionField(R.string.captura_normal, normal) { normal = it }
        CalificacionField(R.string.captura_repeticion, repeticion) { repeticion = it }
        CalificacionField(R.string.captura_especial, especial) { especial = it }

        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        Button(
            onClick = {
                when (val validacion = validarMateria(nombre, semestre, normal, repeticion, especial)) {
                    is Validacion.Error -> error = validacion.mensaje
                    is Validacion.Ok -> {
                        error = null
                        scope.launch {
                            dao.insertar(validacion.materia)
                            onGuardada()
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.captura_guardar))
        }
    }
}

@Composable
private fun CalificacionField(
    @StringRes etiqueta: Int,
    valor: String,
    onCambio: (String) -> Unit,
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(stringResource(etiqueta)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
    )
}
