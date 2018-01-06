package ro.ubbcluj.cs.regally;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;




public class CreateActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private final static String TAG = "CreateActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        TabLayout layout = findViewById(R.id.createTabs);

        layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                finish();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                finish();
            }
        });

        final NumberPicker editRating = findViewById(R.id.createRating);
        editRating.setMinValue(0);
        editRating.setMaxValue(10);
        editRating.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) { numberPicker.setValue(i1); }
        });

        Button createButton = findViewById(R.id.createDone);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String location = ((EditText)findViewById(R.id.createLocation)).getText().toString();
                final String name = ((EditText)findViewById(R.id.createName)).getText().toString();
                final String description = ((EditText)findViewById(R.id.createDescription)).getText().toString();
                final Double rating = Double.valueOf(((NumberPicker)findViewById(R.id.createRating)).getValue());

                if(!createNewHotel(location, name, description, rating)) {
                    Toast.makeText(CreateActivity.this, "Location already exists!", Toast.LENGTH_LONG).show();
                    return;
                }
                ((BaseAdapter)ViewListActivity.staticMyList.getAdapter()).notifyDataSetChanged();

                // sync with server and local storage
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        MainActivity.synchronizer.add(new Hotel(location, name, description, rating));
                        return null;
                    }
                }.execute();

                Toast.makeText(CreateActivity.this, "Successfully created!", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        });
    }

    public boolean createNewHotel(String location, String name, String description, Double rating) {
        for(int i = 0; i < MainActivity.hotels.size(); i++) {
            if (location.compareTo(MainActivity.hotels.get(i).getLocation()) == 0) {
                return false;
            }
        }

        MainActivity.hotels.add(new Hotel(location, name, description, rating));
        return true;
    }
}
