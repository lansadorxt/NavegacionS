package com.example.myappreg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

data class Producto(val id: String, val nombre: String, val precio: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val listaProductos = remember { mutableStateListOf<Producto>() }

    NavHost(navController = navController, startDestination = "pantalla_principal") {
        composable("pantalla_principal") {
            PantallaPrincipal(
                productos = listaProductos,
                onAgregarClick = { navController.navigate("registro_producto") }
            )
        }
        composable("registro_producto") {
            PantallaRegistro(
                onRegistrar = { id, nombre, precio ->
                    listaProductos.add(Producto(id, nombre, precio))
                    navController.popBackStack()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(
    productos: List<Producto>,
    onAgregarClick: () -> Unit
) {
    var busqueda by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My App Reg") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAgregarClick) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text("Buscar", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { Text("Ingrese búsqueda...") }
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Lista de productos", style = MaterialTheme.typography.titleMedium)

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(productos.filter { it.nombre.contains(busqueda, ignoreCase = true) }) { prod ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ID: ${prod.id}")
                            Text("Nombre: ${prod.nombre}")
                            Text("Precio: $${prod.precio}")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistro(
    onRegistrar: (String, String, String) -> Unit
) {
    var id by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de producto") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("ID", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = id,
                    onValueChange = { id = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Nombre", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Precio", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Button(
                onClick = {
                    if (id.isNotBlank() && nombre.isNotBlank() && precio.isNotBlank()) {
                        onRegistrar(id, nombre, precio)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Registrar")
            }
        }
    }
}