package myapp.aishari.aishari_oblig2.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import myapp.aishari.aishari_oblig2.ui.theme.Aishari_oblig2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Aishari_oblig2Theme {
                val viewModel by viewModels<AlpacaViewModel>()
                MyApp(viewModel)
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyApp(viewModel: AlpacaViewModel) {
    Scaffold(
        modifier = Modifier.semantics {
        },
        content = { innerPadding ->
            AlpacaScreen(modifier = Modifier.padding(innerPadding), viewModel = viewModel)
        })
}



