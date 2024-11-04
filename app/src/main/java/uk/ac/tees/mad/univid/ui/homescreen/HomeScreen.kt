package uk.ac.tees.mad.univid.ui.homescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import uk.ac.tees.mad.univid.authentication.viewmodel.AuthViewModel


@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    navController: NavHostController
) {
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Button(onClick = {
            authViewModel.signOut()
            navController.navigate("auth_graph")
        }) {
            Text(text = "LogOut")
        }
    }
}