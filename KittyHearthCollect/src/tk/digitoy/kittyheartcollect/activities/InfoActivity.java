package tk.digitoy.kittyheartcollect.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class InfoActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_activity, menu);
        return true;
    }

    
}
