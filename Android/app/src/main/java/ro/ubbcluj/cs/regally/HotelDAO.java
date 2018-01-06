package ro.ubbcluj.cs.regally;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;




@Dao
public interface HotelDAO {
    @Query("SELECT * FROM hotels")
    List<Hotel> getHotels();

    @Insert
    void addHotel(Hotel hotel);

    @Delete
    void deleteHotel(Hotel hotel);

    @Update
    void updateHotel(Hotel hotel);
}
