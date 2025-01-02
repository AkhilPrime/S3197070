package uk.ac.tees.mad.univid.ui.homescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.univid.authentication.viewmodel.AuthViewModel
import uk.ac.tees.mad.univid.mainscreen.roomdb.recents.SearchedWord
import uk.ac.tees.mad.univid.mainscreen.viewmodel.HomeViewModel
import uk.ac.tees.mad.univid.ui.theme.merrisFam
import uk.ac.tees.mad.univid.ui.theme.poppinsFam


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    navController: NavHostController,
    homeViewModel: HomeViewModel
) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Home",
                        fontSize = 30.sp,
                        fontFamily = merrisFam
                    )
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("profile_screen")
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Profile"
                        )
                    }
                }
            )
        }

    ){inner->
        var searchquery by remember{ mutableStateOf("") }
        var active by remember{ mutableStateOf(false) }
        val recentWords by homeViewModel.recentwords.collectAsState()

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(inner),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Spacer(modifier = Modifier.weight(0.1f))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(0.88f),
                value = searchquery,
                onValueChange = {
                    searchquery = it
                },
                shape = RoundedCornerShape(15.dp),
                trailingIcon = {
                    IconButton(onClick = {
                        if (searchquery.isNotBlank())searchquery=""
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancel Button"
                        )
                    }
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Search, contentDescription = "Searching")
                }
            )

            Spacer(modifier = Modifier.size(3.dp))
            Button(onClick = {
                homeViewModel.fetchWords(searchquery)
                homeViewModel.addRecent(searchquery)
                navController.navigate("wordDetailsScreen")
            }) {
                Text(text = "Search word")
            }

            Spacer(modifier = Modifier.size(3.dp))
            Button(onClick = {
                navController.navigate("favourite_screen")
            }) {
                Text(text = "Favourite Words")
            }

            Spacer(modifier = Modifier.weight(0.1f))
            Text(
                text = "Recent Searched",
                fontSize = 14.sp,
                fontFamily = poppinsFam,
                fontWeight = FontWeight.Bold
            )
            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth(0.88f)
            ){
                items(recentWords){words->
                    RecentItem(words,homeViewModel,navController)
                }
            }


            Spacer(modifier = Modifier.weight(1f))
        }
    }
}


@Composable
fun RecentItem(
    word: SearchedWord,
    homeViewModel: HomeViewModel,
    navController: NavHostController
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.elevatedCardElevation(10.dp),
        onClick = {
            homeViewModel.fetchWords(word.word)
            navController.navigate("wordDetailsScreen")
        }
    ) {
        Row {
            Text(
                modifier = Modifier.padding(10.dp),
                text = word.word,
                fontSize = 13.sp,
                fontFamily = poppinsFam
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                homeViewModel.deleteRecent(word.word)
            }) {
                Icon(imageVector = Icons.Outlined.Close, contentDescription = "Delete the word")
            }
        }
    }
}