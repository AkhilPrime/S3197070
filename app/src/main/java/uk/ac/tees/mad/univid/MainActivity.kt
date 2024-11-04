package uk.ac.tees.mad.univid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.tees.mad.univid.authentication.viewmodel.AuthViewModel
import uk.ac.tees.mad.univid.ui.authentication.LogInScreen
import uk.ac.tees.mad.univid.ui.authentication.SignUpScreen
import uk.ac.tees.mad.univid.ui.navigationgraph.CentralNavigation
import uk.ac.tees.mad.univid.ui.theme.DictionaryTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()

            DictionaryTheme {
                CentralNavigation(
                    navController = navController,
                    authViewModel = authViewModel
                )
            }
        }
    }
}
