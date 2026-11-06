package com.devtermo.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.devtermo.app.ui.navigation.DevTermoNavHost
import com.devtermo.app.ui.theme.DevTermoTheme

/** Responsavel: Integrante 1. */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = (application as DevTermoApp).viewModelFactory
        setContent {
            DevTermoTheme {
                DevTermoNavHost(factory = factory)
            }
        }
    }
}
