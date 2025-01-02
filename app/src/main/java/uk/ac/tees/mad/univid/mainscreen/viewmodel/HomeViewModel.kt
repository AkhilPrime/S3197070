package uk.ac.tees.mad.univid.mainscreen.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uk.ac.tees.mad.univid.mainscreen.model.wordsInfoItem
import uk.ac.tees.mad.univid.mainscreen.requests.RetrofitInstance
import uk.ac.tees.mad.univid.mainscreen.roomdb.favourite.FavouriteDatabase
import uk.ac.tees.mad.univid.mainscreen.roomdb.favourite.FavouriteWord
import uk.ac.tees.mad.univid.mainscreen.roomdb.recents.RecentDatabase
import uk.ac.tees.mad.univid.mainscreen.roomdb.recents.SearchedWord

class HomeViewModel(
    application: Application
): AndroidViewModel(application) {

    //For Recents
    private val recent = Room.databaseBuilder(
        application,
        RecentDatabase::class.java,
        "recent.db"
    ).fallbackToDestructiveMigration().build()
    private val recentdao = recent.RecentDao()


    //For favourites
    private val favourite = Room.databaseBuilder(
        application,
        FavouriteDatabase::class.java,
        "recent.db"
    ).fallbackToDestructiveMigration().build()
    private val favDao = favourite.FavouriteDao()


    private val _recentwords = MutableStateFlow<List<SearchedWord>>(emptyList())
    val recentwords = _recentwords.asStateFlow()

    private val _favouritewords = MutableStateFlow<List<FavouriteWord>>(emptyList())
    val favouriteWords = _favouritewords.asStateFlow()

    private val _word = MutableStateFlow<List<wordsInfoItem>>(emptyList())
    val word = _word.asStateFlow()

    private val _darkTheme = MutableStateFlow(false)
    val darkTheme = _darkTheme.asStateFlow()

    init {
        Log.i("The Function:","Started calling")
        getAllRecents()
        getAllFavourites()
        Log.i("Function called", "$recentwords $favouriteWords")
    }


    fun fetchWords(query: String){
        viewModelScope.launch {
            try {
                val fetchedData = RetrofitInstance.api.getWords(query)
                _word.value = fetchedData
                Log.i("The response from api: ", fetchedData.toString())
            }catch (e: Exception){
                Log.i("Error in data: ", "The error is ${e.message}")
            }
        }
    }

    //Insert the recent words
    fun addRecent(query: String){
        viewModelScope.launch {
            recentdao.insert(SearchedWord(0, query))
        }
    }

    //List of recent searched words
    private fun getAllRecents() {
        viewModelScope.launch {
            recentdao.getAllwords()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = emptyList()
                ).collect { words ->
                    _recentwords.value = words
                }
        }
    }

    //Delete the recent word
    fun deleteRecent(query: String){
        viewModelScope.launch {
            Log.i("Deleting the word:", "The deleted word $query")
            recentdao.deleteByWord(query)
        }
    }


    //Theme toggling
    fun initializeTheme(isDark: Boolean){
        _darkTheme.value = isDark
    }

    fun changeTheme(){
        _darkTheme.value = !_darkTheme.value!!
    }


    //Add favourite words.
    fun addFavourite(word: String){
        viewModelScope.launch {
            favDao.addNewWord(FavouriteWord(0, word))
        }
    }

    //List of favourite searched words
    fun getAllFavourites() {
        viewModelScope.launch {
            favDao.getAllWords()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = emptyList()
                ).collect { words ->
                    _favouritewords.value = words
                }
        }
    }

    //Delete the recent word
    fun deleteFavourite(query: String){
        viewModelScope.launch {
            Log.i("Deleting the word:", "The deleted word $query")
            favDao.deleteByWord(query)
        }
    }

}