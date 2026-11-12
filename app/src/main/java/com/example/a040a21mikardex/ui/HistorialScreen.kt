package com.example.a040a21mikardex.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.a040a21mikardex.R
import com.example.a040a21mikardex.data.Materia

/**
 * Lista de materias guardadas. Se actualiza sola: observa el Flow del DAO.
 */
@Composable
fun HistorialScreen(
    materias: List<Materia>,
    onEliminar: (Long) -> Unit,
) {
    if (materias.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                stringResource(R.string.historial_vacio),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                stringResource(R.string.historial_vacio_ayuda),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        return
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(materias, key = { it.id }) { materia ->
            ListItem(
                headlineContent = { Text(materia.nombre) },
                supportingContent = {
                    Text(
                        stringResource(
                            R.string.historial_detalle,
                            materia.semestre,
                            materia.califNormal.comoCalificacion(),
                            materia.califRepeticion.comoCalificacion(),
                            materia.califEspecial.comoCalificacion(),
                        )
                    )
                },
                trailingContent = {
                    TextButton(onClick = { onEliminar(materia.id) }) {
                        Text(stringResource(R.string.historial_borrar))
                    }
                },
            )
            HorizontalDivider()
        }
    }
}
