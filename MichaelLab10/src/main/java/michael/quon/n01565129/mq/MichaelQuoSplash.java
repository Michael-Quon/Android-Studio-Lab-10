// Michael Quon N01565129
package michael.quon.n01565129.mq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MichaelQuoSplash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_michael_quo_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //This method will be executed once the timer is over
                // Start your app main activity
                Intent intent = new Intent(MichaelQuoSplash.this, QuonActivity10.class);
                startActivity(intent);
                // close this activity
                finish();
            }
        }, 3000);
    }
}