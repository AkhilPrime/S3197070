package uk.ac.tees.mad.univid.mainscreen.roomdb.recents

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "searched_words")
data class SearchedWord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val word: String
)
