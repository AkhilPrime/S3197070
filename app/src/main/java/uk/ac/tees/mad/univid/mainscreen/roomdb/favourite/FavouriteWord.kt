package uk.ac.tees.mad.univid.mainscreen.roomdb.favourite

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavouriteWord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val word: String
)
