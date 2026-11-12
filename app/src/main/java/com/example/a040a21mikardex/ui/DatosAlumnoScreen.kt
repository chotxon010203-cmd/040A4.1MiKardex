package com.example.a040a21mikardex.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.a040a21mikardex.R
import com.example.a040a21mikardex.data.DatosAlumno

/**
 * Pantalla para capturar el nombre y el número de control del alumno.
 * Se muestra solo cuando todavía no hay datos guardados.
 */
@Composable
fun DatosAlumnoScreen(
    alumno: DatosAlumno,
    onGuardado: () -> Unit,
) {
    var nombre by remember { mutableStateOf(alumno.nombre) }
    var control by remember { mutableStateOf(alumno.numeroControl) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(stringResource(R.string.alumno_titulo), style = MaterialTheme.typography.headlineMedium)
        Text(
            stringResource(R.string.alumno_ayuda),
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text(stringResource(R.string.alumno_nombre)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = control,
            onValueChange = { control = it },
            label = { Text(stringResource(R.string.alumno_numero_control)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                alumno.nombre = nombre.trim()
                alumno.numeroControl = control.trim()
                onGuardado()
            },
            enabled = nombre.isNotBlank() && control.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.accion_guardar))
        }
    }
}
