package com.example.a040a21mikardex.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.a040a21mikardex.R
import com.example.a040a21mikardex.data.Materia
import com.example.a040a21mikardex.data.Promedios

/** Promedio general y desglose por semestre. */
@Composable
fun PromediosScreen(materias: List<Materia>) {
    val general = Promedios.general(materias)
    val porSemestre = Promedios.porSemestre(materias)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            stringResource(R.string.promedios_general),
            style = MaterialTheme.typography.labelLarge,
        )
        Text(
            text = general.comoPromedio(),
            style = MaterialTheme.typography.displaySmall,
        )

        HorizontalDivider()

        if (porSemestre.isEmpty()) {
            Text(
                stringResource(R.string.promedios_vacio),
                style = MaterialTheme.typography.bodyMedium,
            )
            return@Column
        }

        Text(
            stringResource(R.string.promedios_por_semestre),
            style = MaterialTheme.typography.labelLarge,
        )
        porSemestre.forEach { promedio ->
            ListItem(
                headlineContent = {
                    Text(stringResource(R.string.promedios_semestre, promedio.semestre))
                },
                supportingContent = {
                    Column {
                        Text(
                            pluralStringResource(
                                R.plurals.promedios_cantidad,
                                promedio.cantidad,
                                promedio.cantidad,
                            )
                        )
                        Text(promedio.materias.joinToString { it.nombre })
                    }
                },
                trailingContent = {
                    Text(
                        text = promedio.promedio.comoPromedio(),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
            )
            HorizontalDivider()
        }
    }
}
