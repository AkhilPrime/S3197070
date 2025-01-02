package uk.ac.tees.mad.univid.mainscreen.roomdb.favourite

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [FavouriteWord::class],
    version = 2
)
abstract class FavouriteDatabase: RoomDatabase() {
    abstract fun FavouriteDao(): FavouriteDao
}