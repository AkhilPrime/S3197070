package uk.ac.tees.mad.univid.ui.homescreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import uk.ac.tees.mad.univid.R
import uk.ac.tees.mad.univid.authentication.viewmodel.AuthViewModel
import uk.ac.tees.mad.univid.ui.theme.merrisFam
import uk.ac.tees.mad.univid.ui.theme.poppinsFam

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    navController: NavHostController
) {

    var menuVisi by remember{ mutableStateOf(false) }
    val currentUser by authViewModel.currentUser.collectAsState()

    LaunchedEffect(key1 = Unit) {
        authViewModel.fetchCurrentUser()
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        fontSize = 30.sp,
                        fontFamily = merrisFam
                    )
                },
                actions = {
                    IconButton(onClick = {
                        menuVisi = true
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings"
                        )
                    }
                    DropdownMenu(expanded = menuVisi,
                        onDismissRequest = { menuVisi = false }) {
                        DropdownMenuItem(text = {
                            Text(
                                modifier = Modifier.padding(10.dp),
                                text = "Edit Profile",
                                fontSize = 14.sp,
                                fontFamily = poppinsFam
                            )
                        }, onClick = { })

                        DropdownMenuItem(text = {
                            Text(
                                modifier = Modifier.padding(10.dp),
                                text = "Toggle Theme",
                                fontSize = 14.sp,
                                fontFamily = poppinsFam
                            )
                        }, onClick = {

                        })

                        DropdownMenuItem(text = {
                            Text(
                                modifier = Modifier.padding(10.dp),
                                text = "Log Out",
                                fontSize = 14.sp,
                                fontFamily = poppinsFam
                            )
                        }, onClick = {
                            authViewModel.signOut()
                            navController.navigate("auth_graph"){
                                popUpTo(navController.graph.startDestinationId){
                                    inclusive=true
                                }
                            }
                        })
                    }
                }
            )
        }
    ){innerpadding->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Spacer(modifier = Modifier.weight(0.3f))

            Card (
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                shape = RoundedCornerShape(20.dp)
            ){
                Image(
                    painter = painterResource(id = R.drawable.avatar),
                    contentDescription = "Profile picture"
                )
            }
            Spacer(modifier = Modifier.weight(0.2f))
            Card (
                modifier = Modifier
                    .fillMaxWidth(0.88f),
                elevation = CardDefaults.elevatedCardElevation(
                    10.dp
                )
            ){
                currentUser?.name?.let {
                    Text(
                        modifier = Modifier
                            .padding(10.dp),
                        text = it,
                        fontSize = 15.sp,
                        fontFamily = poppinsFam
                    )
                }
            }
            Spacer(modifier = Modifier.weight(0.1f))
            Card (
                modifier = Modifier
                    .fillMaxWidth(0.88f),
                elevation = CardDefaults.elevatedCardElevation(
                    10.dp
                )
            ){
                currentUser?.email?.let {
                    Text(
                        modifier = Modifier
                            .padding(10.dp),
                        text = it,
                        fontSize = 15.sp,
                        fontFamily = poppinsFam
                    )
                }
            }
            Spacer(modifier = Modifier.weight(0.2f))
            Button(
                modifier = Modifier.fillMaxWidth(0.77f),
                onClick = {

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Gray
                ),
                border = BorderStroke(1.dp, Color.Gray)
            ) {
                Text(
                    text = "Show Favourites",
                    fontFamily = poppinsFam,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}