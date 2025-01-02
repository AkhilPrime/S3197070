package uk.ac.tees.mad.univid.ui.homescreen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.univid.mainscreen.roomdb.favourite.FavouriteWord
import uk.ac.tees.mad.univid.mainscreen.viewmodel.HomeViewModel
import uk.ac.tees.mad.univid.ui.theme.poppinsFam


@Composable
fun FavouriteScreen(
    homeViewModel: HomeViewModel,
    navController: NavHostController
){

    val favouriteWords by homeViewModel.favouriteWords.collectAsState()

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.weight(0.1f))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Favourite Words",
            fontSize = 20.sp,
            fontFamily = poppinsFam,
            textAlign = TextAlign.Center
        )
        Log.i("The Favourite: ", favouriteWords.toString())

        LazyColumn (
            modifier = Modifier
                .fillMaxWidth(0.88f)
        ){
            items(favouriteWords){words->
                FavouriteItem(words,homeViewModel,navController)
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}



@Composable
fun FavouriteItem(
    word: FavouriteWord,
    homeViewModel: HomeViewModel,
    navController: NavHostController
){
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.elevatedCardElevation(10.dp),
        onClick = {
            homeViewModel.fetchWords(word.word)
            navController.navigate("favourite_details_screen")
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
                navController.navigate("home_screen"){
                    popUpTo(navController.graph.startDestinationId){
                        inclusive=true
                    }
                }
                homeViewModel.deleteFavourite(word.word)
                Toast.makeText(context, "Favourite removed", Toast.LENGTH_LONG).show()
            }) {
                Icon(imageVector = Icons.Outlined.Close, contentDescription = "Delete the word")
            }
        }
    }
}