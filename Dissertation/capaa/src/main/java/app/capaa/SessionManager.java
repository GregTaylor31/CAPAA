package app.capaa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.FileInputStream;
import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;

    public SharedPreferences.Editor editor;
    public Context _context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME ="LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";

    private static final String IS_USER_LOGIN ="IsUserLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL ="Email";

    public void SessionManager(Context context){
        this._context = context;
        //sharedPreferences = _context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        editor =sharedPreferences.edit();
    }

    //creates login session
    public void createSession(String name, String email){
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.apply();
        //editor.commit();
    }

//    public boolean IsUserLoggedIn() {
//        return sharedPreferences.getBoolean(LOGIN, false);
//    }
        public boolean isUserLoggedIn(){
            return sharedPreferences.getBoolean(IS_USER_LOGIN, false);
            }

    public void checkLogin() {
        //check login status
        if (!this.isUserLoggedIn()) {
            // user is not logged in, redirect to login page
            Intent i = new Intent(_context, Login.class);
            _context.startActivity(i);
            ((Home) _context).finish();
        }
    }

//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> user = new HashMap<>();
        user.put(NAME,sharedPreferences.getString(NAME, null));
        user.put(EMAIL,sharedPreferences.getString(EMAIL, null));
        return user;
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context,Login.class);
        _context.startActivity(i);
        ((Home)_context).finish();

    }
}
