package ro.ubbcluj.cs.regally;
import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.List;




public class MainActivity extends AppCompatActivity {
    Boolean isSubjectChanged = false;
    static List<Hotel> hotels;
	static HotelDB databaseSingleton;
    static Synchronizer synchronizer;
	@SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TabLayout layout = findViewById(R.id.mainTabs);
        layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 1) {
                    layout.getTabAt(0).select();
                    startActivity(new Intent(MainActivity.this, ViewListActivity.class));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

		// init the db
        MainActivity.databaseSingleton = Room.databaseBuilder(getApplicationContext(), HotelDB.class, "dummy-database").build();
        MainActivity.synchronizer = new Synchronizer(MainActivity.databaseSingleton);

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                MainActivity.hotels = MainActivity.synchronizer.getList();
                return null;
            }
        }.execute();
    }

    public void onSendClick(View v) {
        TextView subjectView = findViewById(R.id.editText3);
        TextView messageView = findViewById(R.id.editText2);

        String subject = subjectView.getText().toString();
        String message = messageView.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"lauramarginean197@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        startActivity(Intent.createChooser(intent, "Send Email"));
    }
}
