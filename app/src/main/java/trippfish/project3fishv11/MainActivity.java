package trippfish.project3fishv11;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.InputMismatchException;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "MAIN:";
    private static final int SETTINGS_RESULT = 2;
    private SharedPreferences myPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (!checkWifiOnAndConnected()) {
            Log.i(TAG, "Network not available...");
            // Create dialog box
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No network connection available." +
                    " Please try your request again later.");
            //create an ok button
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //go to home
                    Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                    homeIntent.addCategory(Intent.CATEGORY_HOME );
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            //do the rest
            Log.i(TAG, "Network available...");
            //populate spinner
            setupSpinner();
            getMyPrefs();

        }
    }
    private void getMyPrefs() {
        myPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // THIS LINE THROWS EXCEPTION boolean hasBeenShown = myPreference.getBoolean("ServerPref", false);
    }

    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    private void doPrefChangeListener(View view) {
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                Toast.makeText(MainActivity.this, "Key=" + key, Toast.LENGTH_SHORT).show();
                if (key.equals("ServerPref")) {
                    String myString = myPreference.getString("ServerPref", "Nothing Found");
                    Toast.makeText(MainActivity.this, "From Listener ServerPref=" + myString, Toast.LENGTH_SHORT).show();
                }
            }
        };
        // register the listener
        myPreference.registerOnSharedPreferenceChangeListener(listener);

    }


    Spinner spinner;
    private void setupSpinner() {
        //create a data adapter to fill above spinner with choices
        //R.array.numbers is arraylist in strings.xml
        //R.layout.spinner_item_simple is just a textview
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.JSON_URL,R.layout.spinner_item_simple);

        //get a reference to the spinner
        spinner = (Spinner)findViewById(R.id.spinner);

        //bind the spinner to the datasource managed by adapter
        spinner.setAdapter(adapter);
        //respond when spinner clicked
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public static final int SELECTED_ITEM = 0;

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long rowid) {
                if (arg0.getChildAt(SELECTED_ITEM) != null) {
                    ((TextView) arg0.getChildAt(SELECTED_ITEM)).setTextColor(Color.WHITE);
                    Toast.makeText(MainActivity.this, (String) arg0.getItemAtPosition(pos), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            doSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void doSettings() {
        Log.i(TAG, "doSettings click...");
        Intent intent = new Intent(getApplicationContext(), PrefActivity.class);
        startActivityForResult(intent, SETTINGS_RESULT);
    }

    private boolean checkWifiOnAndConnected() {
        WifiManager wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifiMgr.isWifiEnabled()) { // WiFi adapter is ON
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            if( wifiInfo.getNetworkId() == -1 ){
                return false; // Not connected to an access-Point
            }
            return true;      // Connected to an Access Point
        } else {
            return false; // WiFi adapter is OFF
        }
    }
}
