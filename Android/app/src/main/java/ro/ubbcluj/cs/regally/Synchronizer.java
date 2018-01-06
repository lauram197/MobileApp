package ro.ubbcluj.cs.regally;
import java.util.List;




public class Synchronizer {
    private HotelDB database;

    public List<Hotel> getList() {
        return database.hotelDAO().getHotels();
    }

    public Synchronizer(HotelDB database) {
        this.database = database;
    }

    public void add(Hotel np) {
        database.hotelDAO().addHotel(np);
    }

    public void delete(Hotel np) {
        database.hotelDAO().deleteHotel(np);
    }

    public void update(Hotel np) {
        database.hotelDAO().updateHotel(np);
    }
}
