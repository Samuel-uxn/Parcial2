package com.aplications.parcial2.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aplications.parcial2.presentation.view.AgregarLugar
import com.aplications.parcial2.presentation.view.CrearViaje
import com.aplications.parcial2.presentation.view.DetalleViaje
import com.aplications.parcial2.presentation.view.PantallaInicio

// Rutas que ya usan las pantallas de Sebastián: "crear", "detalle/{id}" y "agregar/{id}"
private val tripIdArg = listOf(navArgument("tripId") { type = NavType.StringType })

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "inicio") {
        composable("inicio") {
            PantallaInicio(navController)
        }
        composable("crear") {
            CrearViaje(navController)
        }
        composable("detalle/{tripId}", arguments = tripIdArg) { entry ->
            val tripId = entry.arguments?.getString("tripId") ?: return@composable
            DetalleViaje(navController, tripId)
        }
        composable("agregar/{tripId}", arguments = tripIdArg) { entry ->
            val tripId = entry.arguments?.getString("tripId") ?: return@composable
            AgregarLugar(navController, tripId)
        }
    }
}