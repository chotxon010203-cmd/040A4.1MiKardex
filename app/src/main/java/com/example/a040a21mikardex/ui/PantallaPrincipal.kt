package com.example.a040a21mikardex.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.a040a21mikardex.R
import com.example.a040a21mikardex.data.CardexDb
import com.example.a040a21mikardex.data.DatosAlumno
import kotlinx.coroutines.launch

/** Las tres secciones de la app. */
enum class Seccion(@StringRes val etiqueta: Int) {
    CAPTURA(R.string.seccion_capturar),
    HISTORIAL(R.string.seccion_historial),
    PROMEDIOS(R.string.seccion_promedios),
}

/**
 * Pantalla principal: una fila de tabs con las tres secciones y el contenido de la elegida.
 * La sección vive en un estado local; no hace falta una librería de navegación para esto.
 */
@Composable
fun PantallaPrincipal(alumno: DatosAlumno) {
    val contexto = LocalContext.current
    val dao = remember(contexto) { CardexDb.obtener(contexto).materias() }
    val materias by dao.observarTodas().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    var seccion by remember { mutableStateOf(Seccion.CAPTURA) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
    ) {
        Text(
            text = "${alumno.nombre} · ${alumno.numeroControl}",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        )
        PrimaryTabRow(selectedTabIndex = seccion.ordinal) {
            Seccion.entries.forEach { opcion ->
                Tab(
                    selected = seccion == opcion,
                    onClick = { seccion = opcion },
                    text = { Text(stringResource(opcion.etiqueta)) },
                )
            }
        }
        when (seccion) {
            Seccion.CAPTURA -> CapturaMateriaScreen(dao = dao, onGuardada = { seccion = Seccion.HISTORIAL })
            Seccion.HISTORIAL -> HistorialScreen(
                materias = materias,
                onEliminar = { id -> scope.launch { dao.eliminar(id) } },
            )
            Seccion.PROMEDIOS -> PromediosScreen(materias = materias)
        }
    }
}
