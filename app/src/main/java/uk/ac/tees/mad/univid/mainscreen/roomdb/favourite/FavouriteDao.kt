package uk.ac.tees.mad.univid.mainscreen.roomdb.favourite

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface FavouriteDao {

    @Upsert
    suspend fun addNewWord(word: FavouriteWord)

    @Query("SELECT * FROM favouriteword ORDER BY id DESC")
    fun getAllWords(): Flow<List<FavouriteWord>>

    @Query("DELETE FROM favouriteword WHERE word = :query")
    suspend fun deleteByWord(query: String)
}