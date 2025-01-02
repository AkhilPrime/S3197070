package uk.ac.tees.mad.univid.mainscreen.roomdb.recents

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface RecentDao {

    @Upsert
    suspend fun insert(word: SearchedWord)

    @Query("SELECT * FROM searched_words ORDER BY id DESC")
    fun getAllwords(): Flow<List<SearchedWord>>

    @Query("DELETE FROM searched_words WHERE word = :query")
    suspend fun deleteByWord(query: String)
}