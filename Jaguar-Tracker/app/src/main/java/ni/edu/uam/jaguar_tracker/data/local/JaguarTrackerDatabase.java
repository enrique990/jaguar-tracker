package ni.edu.uam.jaguar_tracker.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ni.edu.uam.jaguar_tracker.data.local.session.UserSessionDao;
import ni.edu.uam.jaguar_tracker.data.local.session.UserSessionEntity;

@Database(
        entities = {UserSessionEntity.class},
        version = 1,
        exportSchema = false
)
public abstract class JaguarTrackerDatabase extends RoomDatabase {
    public abstract UserSessionDao userSessionDao();
}