package com.ao.kandillidepremcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.ao.kandillidepremcompose.ui.theme.KandilliDepremComposeTheme
import com.ao.kandillidepremcompose.view.navigation.NavigationPage
import org.osmdroid.config.Configuration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Configuration.getInstance().load(this, this.getPreferences(MODE_PRIVATE))

        setContent {
            KandilliDepremComposeTheme(darkTheme = false, dynamicColor = false) {
                val navController = rememberNavController()
                NavigationPage(navHostController = navController)
            }
        }
    }
}