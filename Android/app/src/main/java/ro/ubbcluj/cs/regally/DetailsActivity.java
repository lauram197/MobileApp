package ro.ubbcluj.cs.regally;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;




public class DetailsActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private static final String TAG = "DetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TabLayout layout = findViewById(R.id.detailsTabs);

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

        Intent intent = getIntent();
        final String currentLocation = intent.getStringExtra("location");
        TextView textView = findViewById(R.id.locationText);
        textView.setText(currentLocation);
        textView.setTextSize((float)20);

        Hotel hotel = getHotel(currentLocation);

        final EditText nameText = findViewById(R.id.nameText);
        nameText.setText(hotel.getName());

        final EditText descriptionText = findViewById(R.id.descriptionText);
        descriptionText.setText(hotel.getDescription());

        final NumberPicker editTextRating = findViewById(R.id.pickerRating);
        editTextRating.setMinValue(0);
        editTextRating.setMaxValue(10);
        editTextRating.setValue((int) Math.round(hotel.getRating()));

        editTextRating.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                numberPicker.setValue(i1);
            }
        });

        Button editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final String name = nameText.getText().toString();
                    final String description = descriptionText.getText().toString();
                    final Double rating = Double.valueOf(editTextRating.getValue());

                    setHotel(currentLocation, name, description, rating);

                    ((BaseAdapter)ViewListActivity.staticMyList.getAdapter()).notifyDataSetChanged();

                    // sync with server and local storage
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            MainActivity.synchronizer.update(new Hotel(currentLocation, name, description, rating));
                            return null;
                        }
                    }.execute();

                    Toast.makeText(DetailsActivity.this, "Successfully edited!", Toast.LENGTH_LONG).show();
                    onBackPressed();

                } catch(RuntimeException e) {
                    Toast.makeText(DetailsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
                builder.setMessage("Are you sure you want to delete this entry?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @SuppressLint("StaticFieldLeak")
                    public void onClick(DialogInterface dialog, int id) {
                        Hotel oldHotel = DetailsActivity.this.getHotel(currentLocation);
                        final Hotel savedHotel = new Hotel(oldHotel.getLocation(), oldHotel.getName(), oldHotel.getDescription(), oldHotel.getRating());

                        DetailsActivity.this.deleteHotel(currentLocation);

                        ((BaseAdapter)ViewListActivity.staticMyList.getAdapter()).notifyDataSetChanged();

                        // sync with server and local storage
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... voids) {
                                MainActivity.synchronizer.delete(savedHotel);
                                return null;
                            }
                        }.execute();

                        Toast.makeText(DetailsActivity.this, "Successfully deleted!", Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "User canceled deletion");
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }));

        PieChart pieChart = findViewById(R.id.pieChart);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(45f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setDrawCenterText(true);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(false);
        pieChart.setHighlightPerTapEnabled(true);

        // add values into pie chart data set
        ArrayList<PieEntry> arrayValues = new ArrayList<>();
        Integer v1 = editTextRating.getValue();
        Integer v2 = 10 - editTextRating.getValue();
        arrayValues.add(new PieEntry((float)(v1)/(float)(v2), 0));
        arrayValues.add(new PieEntry((float)(v2)/(float)(v2), 1));
        PieDataSet dataSet = new PieDataSet(arrayValues, "");

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(ColorTemplate.rgb("#1c7293")); // green for
        colors.add(ColorTemplate.rgb("990000")); // red for
        dataSet.setColors(colors);

        // make pie data
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());

        // pieChart legend
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setData(data);
    }

    Hotel getHotel(String location) {
        for(int i = 0; i < MainActivity.hotels.size(); i++) {
            if(location.compareTo(MainActivity.hotels.get(i).getLocation()) == 0) {
                return MainActivity.hotels.get(i);
            }
        }
        return null;
    }

    void setHotel(String location, String name, String description, Double rating) {
        for(int i = 0; i < MainActivity.hotels.size(); i++) {
            if(location.compareTo(MainActivity.hotels.get(i).getLocation()) == 0) {
                MainActivity.hotels.get(i).setName(name);
                MainActivity.hotels.get(i).setDescription(description);
                MainActivity.hotels.get(i).setRating(rating);
            }
        }
    }

    void deleteHotel(String location) {
        for(int i = 0; i < MainActivity.hotels.size(); i++) {
            if(location.compareTo(MainActivity.hotels.get(i).getLocation()) == 0) {
                MainActivity.hotels.remove(i);
                break;
            }
        }
    }
}
