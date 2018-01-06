package ro.ubbcluj.cs.regally;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;




 @Entity(tableName = "hotels")
public class Hotel {
	@PrimaryKey @ColumnInfo(name="address")
    private String location;
	
	@ColumnInfo(name="name")
    private String name;
	
	@ColumnInfo(name="description")
    private String description;
	
	@ColumnInfo(name="rating")
    private Double rating;

    @Ignore
    private Integer noPeopleWhoRated;

    public Hotel(String location, String name, String description, Double rating) {
        this.location = location;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.noPeopleWhoRated = 0;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Double getRating() {
        return rating;
    }
    public void setRating(Double rating) {
        this.rating = (this.rating * this.noPeopleWhoRated + rating) / ++this.noPeopleWhoRated;
    }

    @Override
    public String toString() {
        return this.location;
    }
}
