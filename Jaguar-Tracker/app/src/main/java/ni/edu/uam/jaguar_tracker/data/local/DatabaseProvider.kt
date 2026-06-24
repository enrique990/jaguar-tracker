package ni.edu.uam.jaguar_tracker.data.local

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var database: JaguarTrackerDatabase? = null

    fun getDatabase(context: Context): JaguarTrackerDatabase {
        return database ?: synchronized(this) {
            database ?: Room.databaseBuilder(
                context.applicationContext,
                JaguarTrackerDatabase::class.java,
                "jaguar_tracker_db"
            ).build().also {
                database = it
            }
        }
    }
}