package com.juraj.gamebrowser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.juraj.gamebrowser.navigation.AppNavGraph
import com.juraj.gamebrowser.shared.theme.GameBrowserTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameBrowserTheme {
                AppNavGraph()
            }
        }
    }
}
