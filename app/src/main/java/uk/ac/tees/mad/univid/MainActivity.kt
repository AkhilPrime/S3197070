package uk.ac.tees.mad.univid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.tees.mad.univid.authentication.viewmodel.AuthViewModel
import uk.ac.tees.mad.univid.mainscreen.viewmodel.HomeViewModel
import uk.ac.tees.mad.univid.ui.authentication.LogInScreen
import uk.ac.tees.mad.univid.ui.authentication.SignUpScreen
import uk.ac.tees.mad.univid.ui.homescreen.EditUserInformation
import uk.ac.tees.mad.univid.ui.homescreen.ProfileScreen
import uk.ac.tees.mad.univid.ui.navigationgraph.CentralNavigation
import uk.ac.tees.mad.univid.ui.theme.DictionaryTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel by viewModels<AuthViewModel>()
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val isDarkTheme = isSystemInDarkTheme()

            LaunchedEffect(key1 = Unit) {
                homeViewModel.initializeTheme(isDarkTheme)
            }

            val darkTheme by homeViewModel.darkTheme.collectAsState()

            val navController = rememberNavController()

            DictionaryTheme(darkTheme = darkTheme) {
                CentralNavigation(
                    navController = navController,
                    authViewModel = authViewModel,
                    homeViewModel = homeViewModel
                )
            }
        }
    }
}
