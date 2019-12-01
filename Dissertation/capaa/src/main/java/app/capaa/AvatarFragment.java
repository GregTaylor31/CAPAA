package app.capaa;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.text.style.BackgroundColorSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AvatarFragment extends Fragment {
    public ImageView IV;
    public ImageView IV2;
    public ImageView IV3;
    public ImageView IV4;
    public Boolean GreenTorso = false;
    UserSessionManager sessionManager;
    public String Email;
    public int steps;
    public int TorsoColour;
    DatabaseHelper db;
    //public static SQLiteOpenHelper sqLiteHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_avatar, container, false);
        Email= ((Home)getActivity()).getEmail();
        steps = ((Home)getActivity()).getStepsFromDB();
        Toast.makeText(getActivity(),Email , Toast.LENGTH_SHORT).show();
        db = new DatabaseHelper(getActivity());

        String Head64String = db.getHeadFromDb(Email);
        String Torso64String = db.getTorsoFromDb(Email);
        String Trousers64String = db.getTrousersFromDb(Email);
        String Feet64String = db.getFeetFromDb(Email);

        String imageDataBytes1 = Head64String.substring(Head64String.indexOf(",")+1);
        String imageDataBytes2 = Torso64String.substring(Torso64String.indexOf(",")+1);
        String imageDataBytes3 = Trousers64String.substring(Trousers64String.indexOf(",")+1);
        String imageDataBytes4 = Feet64String.substring(Feet64String.indexOf(",")+1);

        InputStream stream1 = new ByteArrayInputStream(Base64.decode(imageDataBytes1.getBytes(), Base64.DEFAULT));
        InputStream stream2 = new ByteArrayInputStream(Base64.decode(imageDataBytes2.getBytes(), Base64.DEFAULT));
        InputStream stream3 = new ByteArrayInputStream(Base64.decode(imageDataBytes3.getBytes(), Base64.DEFAULT));
        InputStream stream4 = new ByteArrayInputStream(Base64.decode(imageDataBytes4.getBytes(), Base64.DEFAULT));

        Bitmap bitmap1 = BitmapFactory.decodeStream(stream1);
        Bitmap bitmap2 = BitmapFactory.decodeStream(stream2);
        Bitmap bitmap3 = BitmapFactory.decodeStream(stream3);
        Bitmap bitmap4 = BitmapFactory.decodeStream(stream4);

        IV = (ImageView) rootView.findViewById(R.id.Head);
        IV2 = (ImageView) rootView.findViewById(R.id.Torso);
        IV3 = (ImageView) rootView.findViewById(R.id.Trousers);
        IV4 = (ImageView) rootView.findViewById(R.id.Feet);

        IV.setImageBitmap(bitmap1);
        IV2.setImageBitmap(bitmap2);
        IV3.setImageBitmap(bitmap3);
        IV4.setImageBitmap(bitmap4);
        //IV.setImageResource();
        //String imageDataBytes = Head64String.substring(Head64String.indexOf(",")+1);
        //InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));

        if (GreenTorso == true) {
           IV = (ImageView) rootView.findViewById(R.id.Torso);
            IV.setImageResource(R.drawable.green_torso);
          // IV.setImageResource(R.drawable.green_torso);
          // db.updateTorso(Email, 2);
            db.updateTorso(Email, 2);

        }
        return rootView;
    }

    public void greenTorso()
    {
        GreenTorso = true;
    }
}
