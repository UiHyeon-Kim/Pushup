package com.hanhyo.presentation.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.hanhyo.presentation.navigation.Route

@Composable
fun HomeScreen(
    navController: NavController
) {
    Column {
        Button(
            onClick = {
                navController.navigate(Route.PUSHUP)
            }
        ) {
            Text("go pushup")
        }
    }
}

