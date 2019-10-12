package app.capaa;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class Home extends AppCompatActivity {

    private TextView name, email;
    Button LogoutButton;
    UserSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        getWindow().setBackgroundDrawableResource(R.drawable.background2); // sets background to background2.png
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); // hides keyboard on boot

        sessionManager = new UserSessionManager(getApplicationContext());

        TextView lblName = (TextView) findViewById(R.id.lblName);
        TextView lblEmail = (TextView) findViewById(R.id.lblEmail);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();


        if(sessionManager.checkLogin())
            finish();

        // get user data from session
        HashMap<String, String> user = sessionManager.getUserDetails();

        // get name
        String name = user.get(UserSessionManager.KEY_NAME);

        // get email
        String email = user.get(UserSessionManager.KEY_EMAIL);


        //lblName.setText(Html.fromHtml("Name: <b>" + name + "</b>"));
        lblEmail.setText(Html.fromHtml("Welcome: <b>" + email + "</b>"));




//        sessionManager.checkLogin();
//        HashMap<String, String> user = sessionManager.getUserDetail();
//        String mName = user.get(sessionManager.NAME);
//        String mEmail = user.get(sessionManager.EMAIL);

//        name.setText(mName);
//        email.setText(mEmail);

        LogoutButton=(Button)findViewById(R.id.LogoutButton);
        Toast.makeText(getApplicationContext(),
                "User Login Status: " + sessionManager.isUserLoggedIn(),
                Toast.LENGTH_LONG).show();

        LogoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Clear the User session data
                // and redirect user to LoginActivity
                sessionManager.logoutUser();
            }
        });
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    switch (menuItem.getItemId()) {
                        case R.id.nav_steps:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_avatar:
                            selectedFragment = new AvatarFragment();
                            break;
                        case R.id.nav_settings:
                            selectedFragment = new SettingsFragment();
                            break;

                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };
}
