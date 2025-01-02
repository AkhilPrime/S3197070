package uk.ac.tees.mad.univid.ui.navigationgraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import uk.ac.tees.mad.univid.authentication.viewmodel.AuthViewModel
import uk.ac.tees.mad.univid.mainscreen.viewmodel.HomeViewModel
import uk.ac.tees.mad.univid.ui.authentication.CustomSplashScreen
import uk.ac.tees.mad.univid.ui.homescreen.HomeScreen
import uk.ac.tees.mad.univid.ui.authentication.LogInScreen
import uk.ac.tees.mad.univid.ui.authentication.SignUpScreen
import uk.ac.tees.mad.univid.ui.homescreen.EditUserInformation
import uk.ac.tees.mad.univid.ui.homescreen.FavouriteDetailScreen
import uk.ac.tees.mad.univid.ui.homescreen.FavouriteScreen
import uk.ac.tees.mad.univid.ui.homescreen.ProfileScreen
import uk.ac.tees.mad.univid.ui.homescreen.WordDetailScreen


@Composable
fun CentralNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel
){

    NavHost(
        navController = navController,
        startDestination = "splash_graph"
    ) {
        navigation(startDestination = "splash_screen", route = "splash_graph"){
            composable("splash_screen"){
                CustomSplashScreen(
                    authViewModel = authViewModel,
                    navController = navController
                )
            }
        }
        navigation(startDestination = "login_screen", route = "auth_graph"){
            composable("login_screen"){
                LogInScreen(authViewModel, navController)
            }
            composable("signup_screen"){
                SignUpScreen(authViewModel, navController)
            }
        }

        navigation(startDestination = "home_screen", route = "home_graph"){
            composable("home_screen"){
                HomeScreen(authViewModel, navController, homeViewModel)
            }
            composable("profile_screen") {
                ProfileScreen(authViewModel, navController, homeViewModel)
            }
            composable("edit_screen") {
                EditUserInformation(
                    authViewModel = authViewModel,
                    navController = navController,
                    homeViewModel= homeViewModel
                )
            }
            composable("wordDetailsScreen") {
                WordDetailScreen(
                    homeViewModel,
                    navController
                )
            }
            composable("favourite_screen") {
                FavouriteScreen(homeViewModel, navController)
            }
            composable("favourite_details_screen") {
                FavouriteDetailScreen(homeViewModel = homeViewModel, navController = navController)
            }
        }
    }
}