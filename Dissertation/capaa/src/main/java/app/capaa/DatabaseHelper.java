package app.capaa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.Key;
import java.sql.ResultSet;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    int steps;
    public DatabaseHelper(@Nullable Context context) {
        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE Table user(Email text primary key, Password text, Steps int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP Table if exists user");
        //onCreate(db);
    }

    //insert to database
    public boolean insert(String Email, String Password, int Steps) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Email", Email);
        contentValues.put("Password", Password);
        contentValues.put("Steps", Steps);
        long ins = db.insert("user", null, contentValues);
        if (ins == -1) return false;
        else return true;
    }

    public boolean checkEmail(String Email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM User WHERE Email=?", new String[]{Email});
        if (cursor.getCount() > 0) return false;
        else return true;
    }

    //check email and password

    public boolean emailAndPasswordCheckInDB(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM user WHERE email=? AND password=?", new String[]{email, password});
        if (cursor.getCount() > 0) return true;
        else return false;
    }

    public boolean updateSteps(String Email, int Steps) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Email", Email);
        //contentValues.put("Password", Password);
        contentValues.put("Steps", Steps);
        db.update("user", contentValues, "email=?", new String[]{Email});
        return true;
    }

    public int getStepsFromDataBase(String Email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Steps FROM user WHERE Email=?", new String[]{Email});
        //Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToLast()){
            steps = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return steps;

    }
}

