package ro.ubbcluj.cs.regally;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;




@Database(entities = {Hotel.class}, version = 1)
public abstract class HotelDB extends RoomDatabase {
    public abstract HotelDAO hotelDAO();
}
