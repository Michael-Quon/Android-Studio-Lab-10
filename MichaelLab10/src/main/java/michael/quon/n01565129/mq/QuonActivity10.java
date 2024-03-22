// Michael Quon N01565129
package michael.quon.n01565129.mq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class QuonActivity10 extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.MicBottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.Mic_Michael);

    }

    Mi1chaelFragment mi1chaelFragment = new Mi1chaelFragment();
    Qu2onFragment qu2onFragment = new Qu2onFragment();
  //  SettingsFragment settingsFragment = new SettingsFragment();
    //MichaelFragment michaelFragment = new MichaelFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.Mic_Michael) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.MicFlFragment, mi1chaelFragment)
                    .commit();
            return true;
        }

        if (itemId == R.id.Mic_Quon) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.MicFlFragment, qu2onFragment)
                    .commit();
            return true;
        }
//
//        if (itemId == R.id.Mic_N01565129) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                //    .replace(R.id.MicFlFragment, n013565129Fragment)
//                    .commit();
//            return true;
//        }
//
//        if (itemId == R.id.Mic_MQ) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//              //      .replace(R.id.MicFlFragment, m4qFragment)
//                    .commit();
//            return true;
//        }

        return false;
    }

}