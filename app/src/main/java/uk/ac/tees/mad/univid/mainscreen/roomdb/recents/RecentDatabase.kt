package uk.ac.tees.mad.univid.mainscreen.roomdb.recents

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [SearchedWord::class],
    version = 1
)
abstract class RecentDatabase: RoomDatabase() {
    abstract fun RecentDao(): RecentDao
}