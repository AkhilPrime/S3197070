package uk.ac.tees.mad.univid.ui.homescreen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.univid.mainscreen.model.wordsInfoItem
import uk.ac.tees.mad.univid.mainscreen.viewmodel.HomeViewModel
import uk.ac.tees.mad.univid.ui.theme.poppinsFam


@Composable
fun WordDetailScreen(
    homeViewModel: HomeViewModel,
    navController: NavHostController
){
    val words by homeViewModel.word.collectAsState()

    if (words.isEmpty()){
        Text(
            text = "Loading",
            fontSize = 20.sp,
            fontFamily = poppinsFam
        )
    }
    else{
        Log.i("The word data: ", words.toString())
        LazyColumn {
            items(words){word->
                AllwordInfo(word,
                    homeViewModel,
                    navController)
            }
        }

    }
}


@Composable
fun AllwordInfo(
    word: wordsInfoItem,
    homeViewModel: HomeViewModel,
    navController: NavHostController
){
    val context = LocalContext.current

    Column (
        modifier = Modifier.padding(10.dp,50.dp)
    ){
        Text(
            modifier = Modifier.fillMaxWidth(0.9f),
            text = word.word,
            fontSize = 25.sp,
            fontFamily = poppinsFam,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(0.1f))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                homeViewModel.addFavourite(word.word)
                navController.navigate("home_screen"){
                    popUpTo(navController.graph.startDestinationId){
                        inclusive=true
                    }
                }
                Toast.makeText(context, "Favourite added", Toast.LENGTH_LONG).show()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Gray
            ),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Text(
                text = "Add to favourites",
                fontFamily = poppinsFam,
                fontSize = 15.sp
            )
        }
        Spacer(modifier = Modifier.weight(0.1f))

        word.meanings.forEach {meaning->
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = meaning.partOfSpeech,
                fontSize = 22.sp,
                fontFamily = poppinsFam,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
            meaning.definitions.forEach {def->

                Text(
                    text = "Definition: ",
                    fontSize = 18.sp,
                    fontFamily = poppinsFam
                )
                Text(
                    text = def.definition,
                    fontSize = 14.sp,
                    fontFamily = poppinsFam
                )

                Text(
                    text = "Example: ",
                    fontSize = 18.sp,
                    fontFamily = poppinsFam
                )
                Text(
                    text = " ${def.example}\n",
                    fontSize = 14.sp,
                    fontFamily = poppinsFam
                )

            }
            Text(
                text = "Antonyms: ${meaning.antonyms}\n",
                fontSize = 18.sp,
                fontFamily = poppinsFam
            )
            Text(
                text = "Synonyms: ${meaning.synonyms}\n",
                fontSize = 18.sp,
                fontFamily = poppinsFam
            )

        }

    }
}