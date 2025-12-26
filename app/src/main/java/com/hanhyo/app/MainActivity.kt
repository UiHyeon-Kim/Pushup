package com.hanhyo.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hanhyo.presentation.designsystem.theme.PushupTheme
import com.hanhyo.presentation.navigation.PushupNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PushupTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PushupApp()
                }
            }
        }
    }
}

@Composable
fun PushupApp() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        PushupNavGraph(
            modifier = Modifier.padding(innerPadding)
        )
    }
}
