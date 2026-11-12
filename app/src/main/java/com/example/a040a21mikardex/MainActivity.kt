package com.example.a040a21mikardex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.a040a21mikardex.data.DatosAlumno
import com.example.a040a21mikardex.ui.DatosAlumnoScreen
import com.example.a040a21mikardex.ui.PantallaPrincipal
import com.example.a040a21mikardex.ui.theme.MiCardexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiCardexTheme {
                MiCardexApp()
            }
        }
    }
}

/**
 * Raíz de la app: si faltan los datos del alumno los pide; si ya están, muestra el cardex.
 */
@Composable
fun MiCardexApp() {
    val contexto = LocalContext.current
    val alumno = remember(contexto) { DatosAlumno(contexto) }
    var registrado by remember { mutableStateOf(alumno.registrado) }

    if (registrado) {
        PantallaPrincipal(alumno = alumno)
    } else {
        DatosAlumnoScreen(
            alumno = alumno,
            onGuardado = { registrado = true },
        )
    }
}
