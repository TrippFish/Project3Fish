package trippfish.project3fishv11;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ListAdapter;
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
    private Object listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myPreference=PreferenceManager.getDefaultSharedPreferences(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
            listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                   if (key.equals("ServerPref")) {
                    //THIS LINE DOESN"T PRINT ANYTHING WHEN I CHANGE SERVER
                    Log.i(TAG, "onSharedPreferenceChanged: " + prefs + " " + key);
                   }
                }
            };
        }
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
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
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
