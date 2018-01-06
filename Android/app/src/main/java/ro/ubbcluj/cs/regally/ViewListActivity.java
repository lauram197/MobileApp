package ro.ubbcluj.cs.regally;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;



public class ViewListActivity extends AppCompatActivity {
    public static ListView staticMyList;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);

		ViewListActivity.staticMyList = findViewById(R.id.myList);
        TabLayout layout = findViewById(R.id.viewListTabs);
        layout.getTabAt(1).select();

        populateList();

        layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0) {
                    finish();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

		Button createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewListActivity.this, CreateActivity.class));
            }
        });
    }
    
    void populateList() {
        ListView listView = findViewById(R.id.myList);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, MainActivity.hotels));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Hotel item = (Hotel) parent.getItemAtPosition(position);
                String location = item.getLocation();

                Intent intent = new Intent(ViewListActivity.this, DetailsActivity.class);
                intent.putExtra("location", location);
                startActivity(intent);
            }
        });
    }
}
