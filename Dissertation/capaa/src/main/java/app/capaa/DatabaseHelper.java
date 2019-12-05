package app.capaa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Message;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.Key;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.ByteArrayOutputStream;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    String Head64String;
    String Torso64String;
    String Trousers64String;
    String Feet64String;
    String torso64Green;
    String torso64Zero;
    int steps;
    ByteArrayOutputStream objectByteArrayOutputStream;
    private byte[] imageInBytes;
    Context context;

    public DatabaseHelper(@Nullable Context context) {
        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE Table user(Email text primary key, Password text, Steps int)");
        db.execSQL("CREATE Table Avatar(Email text primary key, Head BLOB, Torso BLOB, Trousers BLOB, Feet BLOB )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP Table if exists user");
        db.execSQL("DROP Table if exists Avatar");
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

    public void insertAvatar(String Email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String sql = "INSERT INTO Avatar VALUES (?,?,?,?,?)";
        // File head = new File("C:\\Users\\PC\\Desktop\\capaa\\src\\main\\res\\drawable\\head2.png");
        // Bitmap imageToStoreBitmap = head; // doesn't work as my file isnt a bitmap yet
        objectByteArrayOutputStream = new ByteArrayOutputStream();

        // imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream);
        imageInBytes = objectByteArrayOutputStream.toByteArray();

        //this.context = context.getApplicationContext();

        String head64 = "iVBORw0KGgoAAAANSUhEUgAAArwAAAK8CAYAAAANumxDAAAgAElEQVR4Xu3d0bHsxmFF0cdY+MEs\n" +
                "mIXicCyMg1koC30oFrpUJZdJSo+46AEajd7LvzNA46yDmnvq2ZZ++OZ/CBAgQIAAAQIECGws8MPG\n" +
                "2UQjQIAAAQIECBAg8M3g9RIQIECAAAECBAhsLWDwbl2vcJsJ/LZZHnHeIeDvxDt68pQECPyFgB8y\n" +
                "rweB9wgYvO/paqcn9XdipzZlIRAV8EMWLV7sVwoYvK+s7fUP7e/E6ysUgAABP2TeAQLvETB439PV\n" +
                "Tk/q78RObcpCICrghyxavNivFDB4X1nb6x/a34nXVygAAQJ+yLwDBN4jYPC+p6udntTfiZ3alIVA\n" +
                "VMAPWbR4sV8pYPC+srbXP7S/E6+vUAACBPyQeQcIvEfA4H1PVzs9qb8TO7UpC4GogB+yaPFiv1LA\n" +
                "4H1lba9/aH8nXl+hAAQI+CHzDhB4j4DB+56udnpSfyd2alMWAlEBP2TR4sV+pcDw4P35px9fGdhD\n" +
                "XyPw93/885Mb+TvxiZ5rCRBYQsAP2RI1eIhNBYYH6tUeBu/Vou+6n8H7rr48LQEC1wsYvNebuiOB\n" +
                "/xMweL0LSwgYvEvU4CEIEHhQwOB9EN/R2wsYvNtX/I6AHw7eK0P6m3OlpnsRIPBlAT8+X6byRQKn\n" +
                "BQze02QuuEPA4L1D1T0JEHiTgMH7prY869sEDN63Nbbp8xq8mxYrFgECXxYweL9M5YsETgsYvKfJ\n" +
                "XHCHgMF7h6p7EiDwJgGD901teda3CRi8b2vM8/5B4Iah7G+Od4wAgUcE/Pg8wu7QiMClg9d/tFjk\n" +
                "rVkopsG7UBkehQCBjwQM3o/4XEzgLwUMXi/IqwUM3lfX5+EJEPidgMHrdSBwn4DBe5+tO08QMHgn\n" +
                "IDuCAIEpAgbvFGaHRAUM3mjxu8Q2eHdpUg4CBAxe7wCB+wQM3vts3XmCgME7AdkRBAhMETB4pzA7\n" +
                "JCpg8EaL3yW2wbtLk3IQIGDwegcI3Cdg8N5n684TBAzeCciOIEBgioDBO4XZIVEBgzda/C6xDd5d\n" +
                "mpSDAAGD1ztA4D4Bg/c+W3eeIGDwTkB2BAECUwQM3inMDokKGLzR4neJbfDu0qQcBAgYvN4BAvcJ\n" +
                "GLz32brzBAGDdwKyIwgQmCJg8E5hdkhUwOCNFr9LbIN3lyblIEDA4PUOELhPwOC9z9adJwgYvBOQ\n" +
                "HUGAwBQBg3cKs0OiAgZvtPhdYhu8uzQpBwECBq93gMB9AgbvfbbuPEHA4J2A7AgCBKYIGLxTmB0S\n" +
                "FTB4o8XvEtvg3aVJOQgQMHi9AwTuEzB477N15wkCBu8EZEcQIDBFwOCdwuyQqIDBGy1+l9gG7y5N\n" +
                "ykGAgMHrHSBwn4DBe5+tO08QMHgnIDuCAIEpAgbvFGaHRAUM3mjxu8Q2eHdpUg4CBAxe7wCB+wQM\n" +
                "3vts3XmCgME7AdkRBAhMETB4pzA7JCpg8EaL3yW2wbtLk3IQIGDwegcI3Cdg8N5n684TBAzeCciO\n" +
                "IEBgioDBO4XZIVEBgzda/C6xDd5dmpSDAAGD1ztA4D4Bg/c+W3eeIGDwTkB2BAECUwQM3inMDokK\n" +
                "GLzR4neJbfDu0qQcBAgYvN4BAvcJGLz32brzBAGDdwKyIwgQmCJg8E5hdkhUwOCNFr9LbIN3lybl\n" +
                "IEDA4PUOELhPwOC9z9adJwgYvBOQHUGAwBQBg3cKs0OiAgZvtPhdYhu8uzQpBwECBq93gMB9Agbv\n" +
                "fbbuPEHA4J2A7AgCBKYIGLxTmB0SFTB4o8XvEtvg3aVJOQgQMHi9AwTuE1hi8N4wWu4Tc+fLBX7+\n" +
                "6cfhe97w7vibM9yGCwkQ+ETAj88neq4l8NcCBq835HEBg/fxCjwAAQILCBi8C5TgEbYVMHi3rfY9\n" +
                "wQze93TlSQkQuE/A4L3P1p0JGLzegccFDN7HK/AABAgsIGDwLlCCR9hWwODdttr3BDN439OVJyVA\n" +
                "4D4Bg/c+W3cmYPB6Bx4XMHgfr8ADECCwgIDBu0AJHmFbAYN322rfE8zgfU9XnpQAgfsEDN77bN35\n" +
                "nQKXjtQrCUaHyw3/0VJXxnKvmwVG35t/Pdai746/Wze/M25PYEcBPxw7tirTJwIG7yd6rl1O4JPB\n" +
                "e0WYG0azv1tXFOMeBGICfjhihYt7KGDwHhL5wpsEDN43teVZCRC4S8DgvUvWfd8qcOngfXpsLPy/\n" +
                "ln7r+/G65376HfQvvK97ZTwwgS0FDN4taxXqAwGD9wM8l64nYPCu14knIkBgvoDBO9/ciWsLGLxr\n" +
                "9+PpTgoYvCfBfJ0AgS0FDN4taxXqAwGD9wM8l64nYPCu14knIkBgvoDBO9/ciWsLGLxr9+PpJgpc\n" +
                "MZb93/BOLMxRBAh8V8Dg9XIQ+KPAkoP3htGgdwKHAgbvIZEvECDwEgGD9yVFecxpAgbvNGoHrS5g\n" +
                "8K7ekOcjQOCrAgbvV6V8ryJg8FaalvNQwOA9JPIFAgReImDwvqQojzlNwOCdRu2g1QUM3tUb8nwE\n" +
                "CHxVwOD9qpTvVQQM3krTch4KGLyHRL5AgMBLBAzelxTlMacJbDV4f/v1l2lwDlpX4Ie//c/Qwxm8\n" +
                "Q2wuIkBgQQGDd8FSPNKjAgbvo/wOv0PA4L1D1T0JEHiTgMH7prY86wwBg3eGsjOmChi8U7kdRoDA\n" +
                "ggIG74KleKRHBQzeR/kdfoeAwXuHqnsSIPAmAYP3TW151hkCBu8MZWdMFTB4p3I7jACBBQUM3gVL\n" +
                "8UiPChi8j/I7/A4Bg/cOVfckQOBNAgbvm9ryrDMEDN4Zys6YKmDwTuV2GAECCwoYvAuW4pEeFTB4\n" +
                "H+V3+B0CBu8dqu5JgMCbBAzeN7XlWWcIGLwzlJ0xVcDgncrtMAIEFhQweBcsxSM9KmDwPsrv8DsE\n" +
                "DN47VN2TAIE3CRi8b2rLs84QMHhnKDtjqoDBO5XbYQQILChg8C5Yikd6VMDgfZTf4XcIGLx3qLon\n" +
                "AQJvEjB439SWZ50hYPDOUHbGVAGDdyq3wwgQWFDA4F2wFI/0qIDB+yi/w+8QMHjvUHVPAgTeJGDw\n" +
                "vqktzzpDwOCdoeyMqQIG71RuhxEgsKCAwbtgKR7pUQGD91F+h98hYPDeoeqeBAi8ScDgfVNbnnWG\n" +
                "gME7Q9kZUwUM3qncDiNAYEEBg3fBUjzSowIG7wn+0SF1dMRvv/5y9JWpn7895+jz//zTjx87//0f\n" +
                "//z4Hn+6gb9bV4u6H4GAgB+OQMkinhIweE9wjQ6poyMM3iOhc5+P9mTwnnP2bQIE1hUweNftxpM9\n" +
                "I2DwnnAfHVJHRxi8R0LnPh/tyeA95+zbBAisK2DwrtuNJ3tGwOA94T46pI6OMHiPhM59PtqTwXvO\n" +
                "2bcJEFhXwOBdtxtP9oyAwXvCfXRIHR1h8B4Jnft8tCeD95yzbxMgsK6AwbtuN57sGQGD94T76JA6\n" +
                "OsLgPRI69/loTwbvOWffJkBgXQGDd91uPNkzAgbvCffRIXV0hMF7JHTu89GeDN5zzr5NgMC6Agbv\n" +
                "ut14smcEDN4T7qND6ugIg/dI6Nznoz0ZvOecfZsAgXUFDN51u/FkzwgYvCfcR4fU0REG75HQuc9H\n" +
                "ezJ4zzn7NgEC6woYvOt248meETB4T7iPDqmjIwzeI6Fzn4/2ZPCec/ZtAgTWFTB41+3Gkz0jYPCe\n" +
                "cB8dUkdHGLxHQuc+H+3J4D3n7NsECKwrYPCu240ne0bA4D3hPjqkjo4weI+Ezn0+2pPBe87ZtwkQ\n" +
                "WFfA4F23G0/2jIDBe8J9dEgdHWHwHgmd+3y0J4P3nLNvEyCwroDBu243nuwZAYP3hPvokDo6wuA9\n" +
                "Ejr3+WhPBu85Z98mQGBdAYN33W482TMCBu8J99EhdXSEwXskdO7z0Z4M3nPOvk2AwLoCBu+63Xiy\n" +
                "ZwQM3hPuo0Pq6AiD90jo3OejPRm855x9mwCBdQUM3nW78WTPCBi8J9xHh9TREQbvkdC5z0d7MnjP\n" +
                "Ofs2AQLrChi863bjyZ4RMHhPuI8OqaMjDN4joXOfj/Zk8J5z9m0CBNYVMHjX7caTPSNg8J5wHx1S\n" +
                "R0cYvEdC5z4f7cngPefs2wQIrCtg8K7bjSd7RsDgPeE+OqSOjjB4j4TOfT7ak8F7ztm3CRBYV8Dg\n" +
                "XbcbT/aMgMF7wn10SB0dYfAeCZ37fLQng/ecs28TILCugMG7bjee7BkBg/eE++iQOjrC4D0SOvf5\n" +
                "aE8G7zln3yZAYF0Bg3fdbjzZMwIG7wn30SF1dITBeyR07vPRngzec86+TYDAugIG77rdeLJnBAze\n" +
                "E+6jQ+roCIP3SOjc56M9GbznnH2bAIF1BQzedbvxZO8XuHQ8j3DcPRxHh9RRlruf++j8P3/+9pyj\n" +
                "z2/wnn1TfJ8AgVUFDN5Vm/FcOwgYvIMtGryDcN+5zOC91tPdCBB4n4DB+77OPPF7BAzewa4M3kE4\n" +
                "g/daOHcjQGAbAYN3myoFWVDA4B0sxeAdhLt48F70FP7OXATpNgQIjAv4IRq3cyWBIwGD90joO58b\n" +
                "vINwBu+1cO5GgMA2AgbvNlUKsqCAwTtYisE7CGfwXgvnbgQIbCNg8G5TpSALChi8g6UYvINwBu+1\n" +
                "cO5GgMA2AgbvNlUKsqCAwTtYisE7CGfwXgvnbgQIbCNg8G5TpSALChi8g6UYvINwBu+1cO5GgMA2\n" +
                "AgbvNlUKsqCAwTtYisE7CGfwXgvnbgQIbCNg8G5TpSALCmw/eBc090j/RWD0v3jiIkx/Zy6CdBsC\n" +
                "BMYF/BCN27mSwJGAwXsk5PMpAgbvFGaHECCwsIDBu3A5Hu31Agbv6yvcI4DBu0ePUhAgMC5g8I7b\n" +
                "uZLAkYDBeyTk8ykCBu8UZocQILCwgMG7cDke7fUCBu/rK9wjgMG7R49SECAwLmDwjtu5ksCRgMF7\n" +
                "JOTzKQIG7xRmhxAgsLCAwbtwOR7t9QIG7+sr3COAwbtHj1IQIDAuYPCO27mSwEyBofG82n+e7Uww\n" +
                "Z/2/gMHrbSBAoC5g8NbfAPnfImDwvqWpBZ/T4F2wFI9EgMBUAYN3KrfDCAwLGLzDdC40eL0DBAjU\n" +
                "BQze+hsg/1sEDN63NLXgc34weP2NWLBPj0SAwHkBP2bnzVxB4AkBg/cJ9U3ONHg3KVIMAgSGBQze\n" +
                "YToXEpgqYPBO5d7rMIN3rz6lIUDgvIDBe97MFQSeEDB4n1Df5EyDd5MixSBAYFjA4B2mcyGBqQIG\n" +
                "71TuvQ4zePfqUxoCBM4LGLznzVxB4AkBg/cJ9U3ONHg3KVIMAgSGBQzeYToXEpgqYPBO5d7rMIN3\n" +
                "rz6lIUDgvIDBe97MFQSeEDB4n1Df5EyDd5MixSBAYFjA4B2mcyGBqQIG71TuvQ4zePfqUxoCBM4L\n" +
                "GLznzVxB4AkBg/cJ9U3ONHg3KVIMAgSGBQzeYToXEpgqYPBO5d7rMIN3rz6lIUDgvIDBe97MFQSe\n" +
                "EDB4n1Df5EyDd5MixSBAYFjA4B2mcyGBqQIG71TuvQ4zePfqUxoCBM4LGLznzVxB4AkBg/cJ9U3O\n" +
                "NHg3KVIMAgSGBQzeYToXEpgqYPBO5d7rMIN3rz6lIUDgvIDBe97MFQSeEDB4n1Df5EyDd5MixSBA\n" +
                "YFjA4B2mcyGBqQIG71TuvQ4zePfqUxoCBM4LGLznzVxB4AkBg/cJ9U3ONHg3KVIMAgSGBQzeYToX\n" +
                "EpgqYPBO5d7rMIN3rz6lIUDgvIDBe97MFQSeEDB4n1Df5EyDd5MixSBAYFjA4B2mcyGBqQIG71Tu\n" +
                "vQ4zePfqUxoCBM4LGLznzVxB4AkBg/cJ9U3ONHg3KVIMAgSGBQzeYToXEpgqYPBO5d7rMIN3rz6l\n" +
                "IUDgvIDBe97MFQSeEDB4f6f+wYD7bne//frLE71OOfMDL38jpjTkEAIE7hbwY3a3sPsTuEbA4DV4\n" +
                "h98kg3eYzoUECGwiYPBuUqQY2wsYvAbv8Etu8A7TuZAAgU0EDN5NihRjewGD1+AdfskN3mE6FxIg\n" +
                "sImAwbtJkWJsL2DwGrzDL7nBO0znQgIENhEweDcpUoztBQxeg3f4JTd4h+lcSIDAJgIG7yZFirG9\n" +
                "gMFr8A6/5AbvMJ0LCRDYRMDg3aRIMbYXMHgN3uGX3OAdpnMhAQKbCBi8mxQpxvYCBq/BO/ySG7zD\n" +
                "dC4kQGATAYN3kyLF2F7A4DV4h19yg3eYzoUECGwiYPBuUqQY2wsYvAbv8Etu8A7TuZAAgU0EDN5N\n" +
                "ihSDwL8F/jCMd/2vy/1gwH33RdnV6l+Bv+Pl99/PBgECGQE/eJmqBY0IDP1L8F/ZrDgEq4P34tx+\n" +
                "/yM/CmISIPDtmx88bwGBvQQM3sE+Vxz2f45i8A6W6zICBPICBm/+FQCwmYDBO1iowTsI5zICBAi8\n" +
                "QMDgfUFJHpHACQGD9wTW779q8A7CuYwAAQIvEDB4X1CSRyRwQsDgPYFl8A5iuYwAAQIvEzB4X1aY\n" +
                "xyVwIGDwDr4i/oV3EM5lBAgQeIGAwfuCkjwigRMCicF7wmOrr/p/WtuqTmEIEJgoYPBOxHYUgQkC\n" +
                "Bu8E5KeOMHifkncuAQJvFzB4396g5yfwRwGDd+M3wuDduFzRCBC4VcDgvZXXzQlMFzB4p5PPO9Dg\n" +
                "nWftJAIE9hIwePfqUxoCBu/G74DBu3G5ohEgcKuAwXsrr5sTmC5g8E4nn3egwTvP2kkECOwlYPDu\n" +
                "1ac0BAzejd8Bg3fjckUjQOBWAYP3Vl43JzBdwOCdTj7vQIN3nrWTCBDYS8Dg3atPaQgYvBu/Awbv\n" +
                "xuWKRoDArQIG7628bk5guoDBO5183oEG7zxrJxEgsJeAwbtXn9IQMHg3fgcM3o3LFY0AgVsFDN5b\n" +
                "ed2cwHQBg3c6+bwDDd551k4iQGAvAYN3rz6lIWDwbvwOGLwblysaAQK3Chi8t/K6OYHpAgbvdPJ5\n" +
                "Bxq886ydRIDAXgIG7159SkPA4N34HTB4Ny5XNAIEbhUweG/ldXMC0wUM3unk8w40eOdZO4kAgb0E\n" +
                "DN69+pSGgMG72Dtw8Ui9Mp3f/ys13YsAgaUF/OAtXY+HI3BawOA9TXbvBTcNXr/d99bm7gQIbCbg\n" +
                "R3OzQsXJCxi8i70CBu9ihXgcAgSSAgZvsnahNxa4fPBeZfXbr79cdatX3cfgfVVdHpYAgU0FDN5N\n" +
                "ixUrK2DwLla9wbtYIR6HAIGkgMGbrF1oAl8SuHQ8+xfeL5l/9Ut+u78q5XsECBD49u2bH02vAQEC\n" +
                "3xMweC94N/wL7wWIbkGAAIEPBQzeDwFdTmBjAYP3gnIN3gsQ3YIAAQIfChi8HwK6nMDGAgbvBeUa\n" +
                "vBcgugUBAgQ+FDB4PwR0OYGNBQzeC8o1eC9AdAsCBAh8KGDwfgjocgIbCxi8F5Rr8F6A6BYECBD4\n" +
                "UMDg/RDQ5QQ2FjB4LyjX4L0A0S0IECDwoYDB+yGgywlsLGDwXlCuwXsBolsQIEDgQwGD90NAlxPY\n" +
                "WMDgvaBcg/cCRLcgQIDAhwIG74eALiewsYDBe0G5Bu8FiG5BgACBDwUM3g8BXU5gYwGD94JyDd4L\n" +
                "EN2CAAECHwoYvB8CupzAxgIG7wXlGrwXILoFAQIEPhQweD8EdDmBjQUM3gvKNXgvQHQLAgQIfChg\n" +
                "8H4I6HICGwtcOng3dnoimt/uJ9SdSYDAawX8aL62Og9O4FUCxvN/1uX391WvsIclQODNAn5w39ye\n" +
                "ZyfwHgGD1+B9z9vqSQkQ2E7A4N2uUoEILClg8Bq8S76YHooAgYaAwdvoWUoCTwsYvAbv0++g8wkQ\n" +
                "CAsYvOHyRScwUcDgNXgnvm6OIkCAwB8FDF5vBAECMwQMXoN3xnvmDAIECPxXAYPXi0GAwAwBg9fg\n" +
                "nfGeOYMAAQIGr3eAAIHHBAxeg/exl8/BBAgQ8C+83gECBGYIGLwG74z3zBkECBDwL7zeAQIEHhMw\n" +
                "eA3ex14+BxMgQMC/8HoHCBCYIWDwGrwz3jNnECBAwL/wegcIEHhMwOA1eB97+RxMgAAB/8LrHSBA\n" +
                "YIaAwWvwznjPnEGAAAH/wusdIEDgMQGD1+B97OVzMAECBPwLr3eAAIEZAgavwTvjPXMGAQIE/Auv\n" +
                "d4AAgccEDF6D97GXz8EECBDwL7zeAQIEZggYvAbvjPfMGQQIEPAvvN4BAgQeEzB4Dd7HXj4HEyBA\n" +
                "wL/wegcIEJghYPAavDPeM2cQIEDAv/B6BwgQIECAAAECBHoC/oW317nEBAgQIECAAIGUgMGbqltY\n" +
                "AgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLyp\n" +
                "uoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSA\n" +
                "wZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQ\n" +
                "SAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBA\n" +
                "gACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAA\n" +
                "AQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nE\n" +
                "BAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7\n" +
                "nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0B\n" +
                "g7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg\n" +
                "0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECA\n" +
                "AAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgAB\n" +
                "AgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5h\n" +
                "CRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm\n" +
                "6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFIC\n" +
                "Bm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBA\n" +
                "ICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAA\n" +
                "AQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQEC\n" +
                "BAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucS\n" +
                "EyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODt\n" +
                "dS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQE\n" +
                "DN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECA\n" +
                "QE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAAB\n" +
                "AgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIE\n" +
                "CBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqF\n" +
                "JUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGb\n" +
                "qltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJ\n" +
                "GLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAA\n" +
                "gZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAEC\n" +
                "BAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQI\n" +
                "ECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51L\n" +
                "TIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO3\n" +
                "17nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINAT\n" +
                "MHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAAB\n" +
                "Aj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIE\n" +
                "CBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQ\n" +
                "IECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoW\n" +
                "lgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZv\n" +
                "qm5hCRAgQIAAAQI9AYO317nEBAgQIECAAPZBt/EAABLrSURBVIGUgMGbqltYAgQIECBAgEBPwODt\n" +
                "dS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQE\n" +
                "DN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECA\n" +
                "QE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAAB\n" +
                "AgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIE\n" +
                "CBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqF\n" +
                "JUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGb\n" +
                "qltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJ\n" +
                "GLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAA\n" +
                "gZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAEC\n" +
                "BAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQI\n" +
                "ECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51L\n" +
                "TIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO3\n" +
                "17nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINAT\n" +
                "MHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAAB\n" +
                "Aj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIE\n" +
                "CBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQ\n" +
                "IECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoW\n" +
                "lgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZv\n" +
                "qm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAl\n" +
                "YPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAEC\n" +
                "BFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQI\n" +
                "ECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMg\n" +
                "QIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUu\n" +
                "MQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAze\n" +
                "XucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBP\n" +
                "wODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIE\n" +
                "CPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQ\n" +
                "IECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVA\n" +
                "gAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pb\n" +
                "WAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8\n" +
                "qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGU\n" +
                "gMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQI\n" +
                "EEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAg\n" +
                "QIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yA\n" +
                "AAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5\n" +
                "xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4\n" +
                "e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9\n" +
                "AYO317nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQ\n" +
                "INATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBA\n" +
                "gAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYA\n" +
                "AQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6pu\n" +
                "YQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDw\n" +
                "puoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRS\n" +
                "AgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAg\n" +
                "QCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECA\n" +
                "AAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEB\n" +
                "AgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7n\n" +
                "EhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg\n" +
                "7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0\n" +
                "BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGbqltYAgQIECBA\n" +
                "gEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAA\n" +
                "AQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gC\n" +
                "BAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6\n" +
                "hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDB\n" +
                "m6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBI\n" +
                "CRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECA\n" +
                "AIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAAB\n" +
                "AgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQE\n" +
                "CBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHud\n" +
                "S0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGD\n" +
                "t9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQ\n" +
                "EzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAA\n" +
                "AQI9AYO317nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAEC\n" +
                "BAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJ\n" +
                "ECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8Kbq\n" +
                "FpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIG\n" +
                "b6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAg\n" +
                "JWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAAB\n" +
                "AgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIE\n" +
                "CBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xIT\n" +
                "IECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11\n" +
                "LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM\n" +
                "3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBA\n" +
                "T8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAEC\n" +
                "BAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGbqltYAgQI\n" +
                "ECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUl\n" +
                "QIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuq\n" +
                "W1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkY\n" +
                "vKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACB\n" +
                "lIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIE\n" +
                "CBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQ\n" +
                "IECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtM\n" +
                "gAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fX\n" +
                "ucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMw\n" +
                "eHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AT+F9P4bgjZ3yiDAAAAAElFTkSuQmCC";

        String torso64 = "iVBORw0KGgoAAAANSUhEUgAAArwAAAK8CAYAAAANumxDAAAgAElEQVR4Xu3d7bEkR24F0KE9smft\n" +
                "WFvWjrWH9lBBiiFyZ7+q5yFRSNyjv+qXVTgX1XXZQVE/ffM/BAgQIECAAAECBBYL/LR4NqMRIECA\n" +
                "AAECBAgQ+KbwWgICBAgQIECAAIHVAgrv6ngNR4AAAQIECBAgoPDaAQIECBAgQIAAgdUCCu/qeA1H\n" +
                "gAABAgQIECCg8NoBAgQIECBAgACB1QIK7+p4DUeAAAECBAgQIKDw2gECBAgQIECAAIHVAgrv6ngN\n" +
                "R4AAAQIECBAgoPDaAQIECBAgQIAAgdUCCu/qeA1HgAABAgQIECCg8NoBAgQIECBAgACB1QIK7+p4\n" +
                "DUeAAAECBAgQIKDw2gECBAgQIECAAIHVAgrv6ngNR4AAAQIECBAgoPDaAQIECBAgQIAAgdUCCu/q\n" +
                "eA1HgAABAgQIECCg8NoBAgQIECBAgACB1QIK7+p4DUeAAAECBAgQIKDw2gECBAgQIECAAIHVAgrv\n" +
                "6ngNR4AAAQIECBAgoPDaAQIECBAgQIAAgdUCCu/qeA1HgAABAgQIECCg8NoBAgQIECBAgACB1QIK\n" +
                "7+p4DUeAAAECBAgQIKDw2gECBAgQIECAAIHVAgrv6ngNR4AAAQIECBAgoPDaAQIECBAgQIAAgdUC\n" +
                "Cu/qeA1HgAABAgQIECCg8NoBAgQIECBAgACB1QIK7+p4DUeAAAECBAgQIKDw2gECBAgQIECAAIHV\n" +
                "Agrv6ngNR4AAAQIECBAgoPDaAQIECBAgQIAAgdUCCu/qeA1HgAABAgQIECCg8NoBAgQIECBAgACB\n" +
                "1QIK7+p4DUeAAAECBAgQIKDw2gECBAgQIECAAIHVAgrv6ngNR4AAAQIECBAgoPDaAQIECBAgQIAA\n" +
                "gdUCCu/qeA1HgAABAgQIECCg8NoBAgQIECBAgACB1QIK7+p4DUeAAAECBAgQIKDw2gECBAgQIECA\n" +
                "AIHVAgrv6ngNR4AAAQIECBAgoPDaAQIECBAgQIAAgdUCCu/qeA1HgAABAgQIECCg8NoBAgQIECBA\n" +
                "gACB1QIK7+p4DUeAAAECBAgQIKDw2gECBAgQIECAAIHVAgrv6ngNR4AAAQIECBAgoPDaAQIECBAg\n" +
                "QIAAgdUCCu/qeA1HgAABAgQIECCg8NoBAgQIECBAgACB1QIK7+p4DUeAAAECBAgQIKDw2gECBAgQ\n" +
                "IECAAIHVAgrv6ngNR4AAAQIECBAgoPDaAQIECBAgQIAAgdUCCu/qeA1HgAABAgQIECCg8NoBAgQI\n" +
                "ECBAgACB1QIK7+p4DUeAAAECBAgQIKDw2gECBAgQIECAAIHVAgrv6ngNR4AAAQIECBAgoPDaAQIE\n" +
                "CBAgQIAAgdUCCu/qeA1HgAABAgQIECCg8NoBAgQIECBAgACB1QIK7+p4DUeAAAECBAgQIKDw2gEC\n" +
                "BAgQIECAAIHVAgrv6ngNR4AAAQIECBAgoPDaAQIECBAgQIAAgdUCCu/qeA1HgAABAgQIECCg8NoB\n" +
                "AgQIECBAgACB1QIK7+p4DUeAAAECBAgQIKDw2gECBAgQIECAAIHVAgrv6ngNR4AAAQIECBAgoPDa\n" +
                "AQIECBAgQIAAgdUCCu/qeA1HgAABAgQIECCg8NoBAgQIECBAgACB1QIK7+p4DUeAAAECBAgQIKDw\n" +
                "2gECBAgQIECAAIHVAgrv6ngNR4AAAQIECBAgoPDaAQIECBAgQIAAgdUCCu/qeA1HgAABAgQIECCg\n" +
                "8NoBAgQIECBAgACB1QIK7+p4DUeAAAECBAgQIKDw2gECBAgQIECAAIHVAgrv6ngNR4AAAQIECBAg\n" +
                "oPDaAQIECBAgQIAAgdUCCu/qeA1HgAABAgQIECCg8NoBAgQIECBAgACB1QIK7+p4DUeAAAECBAgQ\n" +
                "IKDw2gECBAgQIECAAIHVAgrv6ngNR4AAAQIECBAgoPDaAQIECBAgQIAAgdUCCu/qeA1HgAABAgQI\n" +
                "ECCg8NoBAgQIECBAgACB1QIK7+p4DUeAAAECBAgQIKDw2gECBAgQIECAAIHVAgrv6ngNR4AAAQIE\n" +
                "CBAgoPDaAQIECBAgQIAAgdUCCu/qeA1HgAABAgQIECCg8NoBAgQIECBAgACB1QIK7+p4DUeAAAEC\n" +
                "BAgQIKDw2gECBAgQIECAAIHVAgrv6ngNR4AAAQIECBAgoPDaAQIECBAgQIAAgdUCCu/qeA1HgAAB\n" +
                "AgQIECCg8NoBAgQIECBAgACB1QIK7+p4DUeAAAECBAgQIKDw2gECBAgQIECAAIHVAgrv6ngNR4AA\n" +
                "AQIECBAgoPDaAQIECBAgQIAAgdUCCu/qeA1HgAABAgQIECCg8NoBAgQIECBAgACB1QIK7+p4DUeA\n" +
                "AAECBAgQIKDw2gECBAgQIECAAIHVAgrv6ngNR4AAAQIECBAgoPDaAQIECBAgQIAAgdUCCu/qeA1H\n" +
                "gAABAgQIECCg8NoBAgQIECBAgACB1QIK7+p4DUeAAAECBAgQIKDw2gECBAgQIECAAIHVAgrv6ngN\n" +
                "R4AAAQIECBAgoPDaAQIECBAgQIAAgdUCCu/qeA1HgAABAgQIECCg8NoBAgQIECBAgACB1QIK7+p4\n" +
                "DUeAAAECBAgQIKDw2gECBAgQIECAAIHVAgrv6ngNR4AAAQIECBAgoPDaAQIECBAgQIAAgdUCCu/q\n" +
                "eA1HgAABAgQIECCg8NoBAgQIECBAgACB1QIK7+p4DUeAAAECBAgQIKDw2gECBAgQIECAAIHVAgrv\n" +
                "6ngNR4AAAQIECBAgoPDaAQIECBAgQIAAgdUCCu/qeA1HgAABAgQIECCg8NoBAgQIECBAgACB1QIK\n" +
                "7+p4DUeAAAECBAgQIKDw2gECBAgQIECAAIHVAgrv6ngNR4AAAQIECBAgoPDaAQIECBAgQIAAgdUC\n" +
                "Cu/qeA1HgAABAgQIECCg8NoBAgQIECBAgACB1QIK7+p4DUeAAAECBAgQIKDw2gECBAgQIECAAIHV\n" +
                "Agrv6ngNR4AAAQIECBAgoPDaAQIECBAgQIAAgdUCCu/qeA1HgAABAgQIECCg8NoBAgQIECBAgACB\n" +
                "1QIK7+p4DUeAAAECBAgQIKDw2gECBAgQIECAAIHVAgrv6ngNR4AAAQIECBAgoPDaAQIECBAgQIAA\n" +
                "gdUCCu/qeA1HgAABAgQIECCg8NoBAgQIECBAgACB1QIK7+p4DUeAAAECBAgQIKDw2gECBAgQIECA\n" +
                "AIHVAgrv6ngNR4AAAQIECBAgoPDaAQIECBAgQIAAgdUCCu/qeA1HgAABAgQIECCg8NoBAgQIECBA\n" +
                "gACB1QIK7+p4DUeAAAECBAgQIKDw2gECBAgQIECAAIHVAgrv6ngNR4AAAQIECBAgoPDaAQIECBAg\n" +
                "QIAAgdUCCu/qeA1HgAABAgQIECCg8NoBAgQIECBAgACB1QIK7+p4DUeAAAECBAgQIKDw2gECBAgQ\n" +
                "IECAAIHVAgrv6ngNR4AAAQIECBAgoPDaAQIECBAgQIAAgdUCCu/qeA1HgAABAgQIECCg8NoBAgQI\n" +
                "ECBAgACB1QIK7+p4DUeAAAECBAgQIKDw2gECBAgQIECAAIHVAgrv6ngNR4AAAQIECBAgoPDaAQIE\n" +
                "CBAgQIAAgdUCCu/qeA1HgAABAgQIECCg8NoBAgQIECBAgACB1QIK7+p4DUeAAAECBAgQIKDw2gEC\n" +
                "BAgQIECAAIHVAgrv6ngNR4AAAQIECBAgoPDaAQIECBAgQIAAgdUCCu/qeA1HgAABAgQIECCg8NoB\n" +
                "AgQIECBAgACB1QIK7+p4DUeAAAECBAgQIKDw2gECBAgQIECAAIHVAgrv6ngNR4AAAQIECBAgoPDa\n" +
                "AQIECBAgQIAAgdUCCu/qeA1HgAABAgQIECCg8NoBAgQIECBAgACB1QIK7+p4DUeAAAECBAgQIKDw\n" +
                "2gECBAgQIECAAIHVAgrv6ngNR4AAAQIECBAgoPDaAQIECBAgQIAAgdUCCu/qeA1HgAABAgQIECCg\n" +
                "8NoBAgQIECBAgACB1QIK7+p4DUeAAAECBAgQIKDw2gECBAgQIECAAIHVAgrv6ngNR4AAAQIECBAg\n" +
                "oPDaAQIECBAgQIAAgdUCCu/qeA1HgAABAgQIECCg8NoBAgQIECBAgACB1QIK7+p4DUeAAAECBAgQ\n" +
                "IKDw2gECBAgQIECAAIHVAgrv6ngNR4AAAQIECBAgoPDaAQIECBAgQIAAgdUCCu/qeA1HgAABAgQI\n" +
                "ECCg8NoBAv8o8AsQAgRGC3hvjY7HzRGYKeCLY2Yu7uo9AYX3PXtXJvBEwHvriZLPECDwDwK+OCwE\n" +
                "Ab/w2gECNwl4b92UlnslMETAF8eQINzGGAG/8I6Jwo0Q+JcC3lsWgwCBjwV8cXxM5g+WCyi8ywM2\n" +
                "3vUC3lvXR2gAAv0Cvjj6zV1xtoDCOzsfd0fAe8sOECDwsYAvjo/J/MFyAYV3ecDGu17Ae+v6CA1A\n" +
                "oF/AF0e/uSvOFlB4Z+fj7gh4b9kBAgQ+FvDF8TGZP1guoPAuD9h41wt4b10foQEI9Av44ug3d8XZ\n" +
                "Agrv7HzcHQHvLTtAgMDHAr44PibzB8sFFN7lARvvegHvresjNACBfgFfHP3mrjhbQOGdnY+7I+C9\n" +
                "ZQcIEPhYwBfHx2T+YLmAwrs8YONdL+C9dX2EBiDQL+CLo9/cFWcLKLyz83F3BLy37AABAh8L+OL4\n" +
                "mMwfLBdQeJcHbLzrBby3ro/QAAT6BXxx9Ju74mwBhXd2Pu6OgPeWHSBA4GMBXxwfk/mD5QIK7/KA\n" +
                "jXe9gPfW9REagEC/gC+OfnNXnC2g8M7Ox90R8N6yAwQIfCzgi+NjMn/QLKCANoO7HAEC/1LA+9Ji\n" +
                "ELhYwAN8cXght95eeH8OgTUmgdsE/ufdG/a+fNff1Ql8ScAD/CU+f9wgoPA2ILsEgRsEFN4bUnKP\n" +
                "BGYKKLwzc3FXfwgovLaBAIHfBBRei0CAwI8KKLw/KufvugQU3i5p1yEwXEDhHR6Q2yMwWEDhHRyO\n" +
                "W/tNQOG1CAQI+IXXDhAg8CUBhfdLfP64QUDhbUB2CQI3CPiF94aU3COBmQIK78xc3NUfAgqvbSBA\n" +
                "wC+8doAAgS8JKLxf4vPHDQIKbwOySxC4QcAvvDek5B4JzBRQeGfm4q78wmsHCBD4TkDhtRIECPyo\n" +
                "gML7o3L+rkvAL7xd0q5DYLiAwjs8ILdHYLCAwjs4HLf2m4DCaxEIEPhNQOG1CAQI/KiAwvujcv6u\n" +
                "S0Dh7ZJ2HQLDBRTe4QG5PQKDBRTeweG4Nb/w2gECBP4QUHhtAwECPyqg8P6onL/rEvALb5e06xAY\n" +
                "LqDwDg/I7REYLKDwDg7HrfmF1w4QIOAXXjtAgMDXBRTerxs64ayAX3jP+jqdwDUCfuG9Jio3SmCc\n" +
                "gMI7LhI39J2AwmslCBD4TUDhtQgECPyogML7o3L+rktA4e2Sdh0CwwUU3uEBuT0CgwUU3sHhuLXf\n" +
                "BBRei0CAgF947QABAl8SUHi/xOePGwQU3gZklyBwg4BfeG9IyT0SmCmg8M7MxV39IaDw2gYCBPzC\n" +
                "awcIEPiSgML7JT5/3CCg8DYguwSBGwRe/oW3ish7t0rSOQQ+EPDgfYDlox8JtBfVj+7uP3z456qD\n" +
                "nEOAwAqB4qLtvbtiKwxxm4AH77bE7rnfssKrgN4TujslsFFA4d2YqpnSBBTetMT75lV4+6xdiQCB\n" +
                "gwIK70FcRxNoElB4m6ADL6PwBoZuZAIbBRTejamaKU1A4U1LvG9ehbfP2pUIEDgooPAexHU0gSYB\n" +
                "hbcJOvAyCm9g6EYmsFFA4d2YqpnSBBTetMT75lV4+6xdiQCBgwIK70FcRxNoElB4m6ADL6PwBoZu\n" +
                "ZAIbBRTejamaKU1A4U1LvG9ehbfP2pUIEDgooPAexHU0gSYBhbcJOvAyCm9g6EYmsFFA4d2YqpnS\n" +
                "BBTetMT75lV4+6xdiQCBgwIK70FcRxNoElB4m6ADL6PwBoZuZAIbBRTejamaKU1A4U1LvG9ehbfP\n" +
                "2pUIEDgooPAexHU0gSYBhbcJOvAyCm9g6EYmsFFA4d2YqpnSBBTetMT75lV4+6xdiQCBgwIK70Fc\n" +
                "RxNoElB4m6ADL6PwBoZuZAIbBRTejamaKU1A4U1LvG9ehbfP2pUIEDgooPAexHU0gSYBhbcJOvAy\n" +
                "Cm9g6EYmsFFA4d2YqpnSBBTetMT75lV4+6xdiQCBgwIK70FcRxNoElB4m6ADL6PwBoZuZAIbBRTe\n" +
                "jamaKU1A4U1LvG9ehbfP2pUIEDgooPAexHU0gSYBhbcJOvAyqwtv8QtwzHr8POZO3Mh/E7CD/02o\n" +
                "7n9fbO29WxeNkwg8FvDgPabywQ8FFN4PwSZ8XOGdkMKzeyguYc8u2vCpiTtYbO2927BHLkHgewEP\n" +
                "np04JaDwnpI9eO7EsnFw3KuPLi5hYywm7mCxtffumG1zI0kCHryktHtnVXh7vUuuNrFslAy28JDi\n" +
                "EjZGaOIOFlt7747ZNjeSJODBS0q7d1aFt9e75GoTy0bJYAsPKS5hY4Qm7mCxtffumG1zI0kCHryk\n" +
                "tHtnVXh7vUuuNrFslAy28JDiEjZGaOIOFlt7747ZNjeSJODBS0q7d1aFt9e75GoTy0bJYAsPKS5h\n" +
                "Y4Qm7mCxtffumG1zI0kCHryktHtnVXh7vUuuNrFslAy28JDiEjZGaOIOFlt7747ZNjeSJODBS0q7\n" +
                "d1aFt9e75GoTy0bJYAsPKS5hY4Qm7mCxtffumG1zI0kCHryktHtnVXh7vUuuNrFslAy28JDiEjZG\n" +
                "aOIOFlt7747ZNjeSJODBS0q7d1aFt9e75GoTy0bJYAsPKS5hY4Qm7mCxtffumG1zI0kCHryktHtn\n" +
                "VXh7vUuuNrFslAy28JDiEjZGaOIOFlt7747ZNjeSJODBS0q7d1aFt9e75GoTy0bJYAsPKS5hY4Qm\n" +
                "7mCxtffumG1zI0kCHryktHtnVXh7vUuuNrFslAy28JDiEjZGaOIOFlt7747ZNjeSJODBS0q7d1aF\n" +
                "t9e75GoTy0bJYAsPKS5hY4Qm7mCxtffumG1zI0kCHryktHtnVXh7vUuuNrFslAy28JDiEjZGaOIO\n" +
                "Flt7747ZNjeSJODBS0q7d1aFt9e75GoTy0bJYAsPKS5hY4Qm7mCxtffumG1zI0kCHryktHtnVXh7\n" +
                "vUuuNrFslAy28JDiEjZGaOIOFlt7747ZNjeSJODBS0q7d1aFt9e75GoTy0bJYAsPKS5hY4Qm7mCx\n" +
                "tffumG1zI0kCHryktHtnVXh7vUuuNrFslAy28JDiEjZGaOIOFlt7747ZNjeSJODBS0q7d1aFt9e7\n" +
                "5GoTy0bJYAsPKS5hY4Qm7mCxtffumG1zI0kCHryktHtnVXh7vUuuNrFslAy28JDiEjZGaOIOFlt7\n" +
                "747ZNjeSJODBS0q7d9Yxhbf4ZdWr2Hy1iWWjmeCay9nr51F9da+Lrb13n0fnkwTKBDx4ZZQO+k4g\n" +
                "ofB2Pj9lnv9pU79aDDwFfQLFJezf3Xjnjv96D0f2/Kt7XWzdbdq3lK5EYLCAB29wOJffWtmLa9jL\n" +
                "6s+xdD4/ZZ4K7+VP1u+3X1zCFN7/sBbF1p3fGzuW3RQECgQ8eAWIjviXAmUFTeH9zbfMU+Hd8cQW\n" +
                "lzCFV+Hd8WCYgsC/EVB4rcYpgbKCpvAqvKeW9OZzFd7n6Q37DvHefR6dTxIoE/DglVE66DsBhbd2\n" +
                "Jco8/cJbG8xbpym8z+UV3udWPklgq4DCuzXZ9+cqK2jDXlZ/lu18fso8Fd73H46KO1B4nysO+w7p\n" +
                "/N54juSTBJYLePCWB/zieGUFbdjLSuF9calc+g8Bhff5Ngz7DvHefR6dTxIoE/DglVE66DsBhbd2\n" +
                "Jco8/cJbG8xbpym8z+UV3udWPklgq4DCuzXZ9+cqK2jDXlZ+4X1/t9zBt2/fFN7nazDsO8R793l0\n" +
                "PkmgTMCDV0bpIL/wHt2Bsn+A8Avv0ZzaDld4n1MrvM+tfJLAVgGFd2uy789VVtCGvaz8wvv+brkD\n" +
                "v/B+tAPDvkO8dz9Kz4cJ1Ah48GocnfLPAgpv7VaUefqFtzaYt07zC+9zeYX3uZVPEtgqoPBuTfb9\n" +
                "ucoK2i9//9uXpvnpL3/90t//hz/ufH7KPBXeU+vQe67C+9x72HdI5/fGcySfJLBcwIO3POAXxysr\n" +
                "aMNeVn8m7Xx+yjwV3hefisJLK7zPMYd9h3R+bzxH8kkCywU8eMsDfnG8soI27GWl8L64VC79h4DC\n" +
                "+3wbhn2HeO8+j84nCZQJePDKKB30nYDCW7sSZZ5+4a0N5q3TFN7n8grvcyufJLBVQOHdmuz7c5UV\n" +
                "tGEvK7/wvr9b7sB/peGjHRj2HeK9+1F6PkygRsCDV+PolH8WUHhrt6LM0y+8tcG8dZpfeJ/LK7zP\n" +
                "rXySwFYBhXdrsu/PVVbQhr2s/ML7/m65A7/wfrQDw75DvHc/Ss+HCdQIePBqHJ3iF97TO1D2DxB+\n" +
                "4T0dVc/5fuF97qzwPrfySQJbBRTercm+P1dZQRv2svIL7/u75Q78wvvRDgz7DvHe/Sg9HyZQI+DB\n" +
                "q3F0il94T+9A2T9A+IX3dFQ95/uF97mzwvvcyicJbBVQeLcm+/5cZQVt2MvKL7zv75Y78AvvRzsw\n" +
                "7DvEe/ej9HyYQI2AB6/G0Sl+4T29A2X/AOEX3tNR9ZzvF97nzgrvcyufJLBVQOHdmuz7c5UVtGEv\n" +
                "K7/wvr9b7sAvvB/twLDvEO/dj9LzYQI1Ah68Gken+IX39A6U/QOEX3hPR9Vzvl94nzsrvM+tfJLA\n" +
                "VgGFd2uy789VVtCGvaz8wvv+brkDv/B+tAPDvkO8dz9Kz4cJ1Ah48GocneIX3tM7UPYPEH7hPR1V\n" +
                "z/l+4X3urPA+t/JJAlsFFN6tyb4/V1lBG/ay8gvv+7vlDvzC+9EODPsO8d79KD0fJlAj4MGrcXSK\n" +
                "X3hP70DZP0D4hfd0VD3n+4X3ubPC+9zKJwlsFVB4tyb7/lxlBW3Yy+p92YN38PPBsx1dK9BUeGtv\n" +
                "+qXThn2HeO++tAcumy3gwcvO/+T0YwrviSF/+stfTxz7+pkK7+sRPL6BrYX3q+X0MeAHHyx+3r13\n" +
                "P7D3UQJVAh68KknnfC+g8F64EwrvPaEpvH1ZKbx91q5E4JSAwntK1rkK74U7oPDeE5rC25eVwttn\n" +
                "7UoETgkovKdknavwXrgDCu89oSm8fVkpvH3WrkTglIDCe0rWuQrvhTug8N4TmsLbl5XC22ftSgRO\n" +
                "CSi8p2Sdq/BeuAMK7z2hKbx9WSm8fdauROCUgMJ7Sta5Cu+FO6Dw3hOawtuXlcLbZ+1KBE4JKLyn\n" +
                "ZJ2r8F64AwrvPaEpvH1ZKbx91q5E4JSAwntK1rkK74U7oPDeE5rC25eVwttn7UoETgkovKdknavw\n" +
                "XrgDCu89oSm8fVkpvH3WrkTglIDCe0rWuQrvhTug8N4TmsLbl5XC22ftSgROCSi8p2Sdq/BeuAMK\n" +
                "7z2hKbx9WSm8fdauROCUgMJ7Sta5Cu+FO6Dw3hOawtuXlcLbZ+1KBE4JKLynZJ2r8F64AwrvPaEp\n" +
                "vH1ZKbx91q5E4JSAwntK1rkK74U7oPDeE5rC25eVwttn7UoETgkovKdknavwXrgDCu89oSm8fVkp\n" +
                "vH3WrkTglIDCe0rWuQrvhTug8N4TmsLbl5XC22ftSgROCSi8p2Sdq/BeuAMK7z2hKbx9WSm8fdau\n" +
                "ROCUgMJ7Sta5Cu+FO6Dw3hOawtuXlcLbZ+1KBE4JKLynZJ2r8F64AwrvPaEpvH1ZKbx91q5E4JSA\n" +
                "wntK1rkK74U7oPDeE5rC25eVwttn7UoETgkovKdknavwXrgDCu89oSm8fVkpvH3WrkTglIDCe0rW\n" +
                "uQrvhTug8N4TmsLbl5XC22ftSgROCSi8p2Sdq/BeuAMK7z2hKbx9WSm8fdauROCUgMJ7Sta5Cu+F\n" +
                "O6Dw3hOawtuXlcLbZ+1KBE4JKLynZJ27uvB2x1v8wv23t6/wdif749frKLy//P1vP36Di/6y+Pnz\n" +
                "3l20G0a5R8CDd09Wt92pwluYWPELV+EtzOatoxTePvni5897ty86VyLw/wIePMtwSkDhLZQtfuEq\n" +
                "vIXZvHWUwtsnX/z8ee/2RedKBBReO3BcQOEtJC5+4Sq8hdm8dZTC2ydf/PwpvH3RuRIBhdcOHBdQ\n" +
                "eAuJi1+4Cm9hNm8dpfD2yRc/fwpvX3SuREDhtQPHBRTeQuLiF67CW5jNW0cpvH3yxc+fwtsXnSsR\n" +
                "UHjtwHEBhbeQuPiFq/AWZvPWUQpvn3zx86fw9kXnSgQUXjtwXEDhLSQufuEqvIXZvHWUwtsnX/z8\n" +
                "Kbx90bkSAYXXDhwXUHgLiYtfuApvYTZvHaXw9skXP38Kb190rkRA4bUDxwUU3kLi4heuwluYzVtH\n" +
                "Kbx98sXPn8LbF50rEVB47cBxAYW3kLj4havwFmbz1lEKb5988fOn8PZF50oEFF47cFygrPAev9Nv\n" +
                "375N/3+hWvzCVXg7lurwNRTew8B/Or74+VN4+6JzJQIKrx04LqDwFhIXv3AV3sJs3joqufB2PQ/f\n" +
                "ZauovrXsrkugQMADXIDoiKMCLcXZL7z/l+HPR6N0eKWAwlup+egs78tHTD5EYKaAB3hmLu7qDwGF\n" +
                "99u3b12/aCm89zx6Cm97Vt6X7eQuSKBOwANcZ+mkMwIKr8J7ZrMuP1XhbQ/Q+7Kd3AUJ1Al4gOss\n" +
                "nXRGQOFVeM9s1uWnKrztAXpftpO7IIE6AQ9wnaWTzggovArvmc26/FSFtz1A78t2chckUCfgAa6z\n" +
                "dNIZAYVX4T2zWZefqvC2B+h92U7uggTqBDzAdZZOOiOg8Cq8Zzbr8lMV3vYAvS/byV2QQJ2AB7jO\n" +
                "0klnBBRehffMZl1+qsLbHqD3ZTu5CxKoE/AA11k66YyAwqvwntmsy09VeNsD9L5sJ3dBAnUCHuA6\n" +
                "SyedEVB4Fd4zm3X5qQpve4Del+3kLkigTsADXGfppDMCCq/Ce2azLj9V4W0P0PuyndwFCdQJeIDr\n" +
                "LJ10RkDhVXjPbNblpyq87QF6X7aTuyCBOgEPcJ2lk84IKLwK75nNuvxUhbc9QO/LdnIXJFAn4AGu\n" +
                "s3TSGQGFV+E9s1mXn6rwtgfofdlO7oIE6vhgnH0AABY0SURBVAQ8wHWWTjojoPAqvGc26/JTFd72\n" +
                "AL0v28ldkECdgAe4ztJJZwQUXoX3zGZdfqrC2x6g92U7uQsSqBPwANdZOumMgMKr8J7ZrMtPVXjb\n" +
                "A/S+bCd3QQJ1Ah7gOksnnRFQeBXeM5t1+akKb3uA3pft5C5IoE7AA1xn6aQzAgqvwntmsy4/VeFt\n" +
                "D9D7sp3cBQnUCXiA6yyddEZA4VV4z2zW5acqvO0Bel+2k7sggToBD3CdpZPOCCi8Cu+Zzbr8VIW3\n" +
                "PUDvy3ZyFyRQJ+ABrrN00hkBhVfhPbNZl5+q8LYH6H3ZTu6CBOoEPMB1lk46I6DwKrxnNuvyUxXe\n" +
                "9gC9L9vJXZBAnYAHuM7SSWcEFF6F98xmXX6qwtseoPdlO7kLEqgT8ADXWTrpjIDCq/Ce2azLT1V4\n" +
                "2wP0vmwnd0ECdQIe4DpLJ50RUHgV3jObdfmpCm97gN6X7eQuSKBOwANcZ+mkMwIKr8J7ZrMuP1Xh\n" +
                "bQ/Q+7Kd3AUJ1Al4gOssnXRGQOFVeM9s1uWnKrztAXpftpO7IIE6AQ9wnaWTzggovArvmc26/FSF\n" +
                "tz1A78t2chckUCfgAa6zdNIZgZbCe+bW/+/UX/7+ty8f/9Nf/vrlM54c8POTD/nMCIHphbdrZxvD\n" +
                "8L5sxHYpAtUCHuBqUefdKHC0VCu8N67E/HtWeL95f81fU3dIYIyAL4wxUbiRFwUU3t/x/cL74hZ+\n" +
                "eGmFV+H9cGV8nEC0gMIbHb/hfxdQeBXe6x4GhVfhvW5p3TCBFwUU3hfxXXqMgMKr8I5Zxqc3ovAq\n" +
                "vE93xecIEPjmC8MSEPj1/67spIJ/h/ekbu7ZCq/3V+72m5zA5wJ+4f3czF/sE1B4/cJ73VYrvArv\n" +
                "dUvrhgm8KKDwvojv0mMEFF6Fd8wyPr0RhVfhfborPkeAgH+lwQ4Q+FVA4VV4r3sSFF6F97qldcME\n" +
                "XhTwC++L+C49RkDhVXjHLOPTG1F4Fd6nu+JzBAj4hdcOEPAL7592wH+H954HQuFVeO/ZVndK4H0B\n" +
                "v/C+n4E7eF/AL7x+4X1/Cz+8A4VX4f1wZXycQLSAwhsdv+F/F1B4Fd7rHgaFV+G9bmndMIEXBRTe\n" +
                "F/FdeoyAwqvwjlnGpzei8Cq8T3fF5wgQ8O/w2gECvwoovArvdU+CwqvwXre0bpjAiwJ+4X0R36XH\n" +
                "CCi8Cu+YZXx6Iwqvwvt0V3yOAAG/8NoBAn7h/dMO+K803PNAKLwK7z3b6k4JvC/gF973M3AH7wv4\n" +
                "hdcvvO9v4Yd3oPAqvB+ujI8TiBZQeKPjN/zvAgqvwnvdw6DwKrzXLa0bJvCigML7Ir5LjxFQeBXe\n" +
                "Mcv49EYUXoX36a74HAEC/h1eO0DgVwGFV+G97klQeBXe65bWDRN4UcAvvC/iu/QYAYVX4R2zjE9v\n" +
                "ROFVeJ/uis8RIOAXXjtAwC+8f9oB/5WGex4IhVfhvWdb3SmB9wX8wvt+Bu7gfQG/8PqF9/0t/PAO\n" +
                "FF6F98OV8XEC0QIKb3T8hv9dQOFVeK97GBRehfe6pXXDBF4UUHhfxHfpMQIKr8I7Zhmf3ojCq/A+\n" +
                "3RWfI0DAv8NrBwj8KqDwKrzXPQkKr8J73dK6YQIvCviF90V8lx4jcLTwjpnywY34P1p7gDTkIwqv\n" +
                "wjtkFd0GgSsEFN4rYnKTFwso0xeH59ZHC3h/jY7HzRGYJeALY1Ye7mafgMK7L1MTPRfwjnlu5ZME\n" +
                "CBwU8GV0ENfRBE7/+8GECQwX8I4ZHpDbI5Ai4MsoJWlzviXgF9635F13goB3zIQU3AMBAv6lfztA\n" +
                "4LCAwnsY2PGjBRTe0fG4OQI5Ar6McrI26TsCCu877q46Q8A7ZkYO7oJAvIAvo/gVAHBYQOE9DOz4\n" +
                "0QLeMaPjcXMEcgR8GeVkbdJ3BBTed9xddYaAd8yMHNwFgXgBX0bxKwDgsIDCexjY8aMFvGNGx+Pm\n" +
                "COQI+DLKydqk7wgovO+4u+oMAe+YGTm4CwLxAr6M4lcAwGEBhfcwsONHC3jHjI7HzRHIEfBllJO1\n" +
                "Sd8RUHjfcXfVGQLeMTNycBcE4gV8GcWvAIDDAgrvYWDHjxbwjhkdj5sjkCPgyygna5O+I6DwvuPu\n" +
                "qjMEvGNm5OAuCMQL+DKKXwEAhwUU3sPAjh8t4B0zOh43RyBHwJdRTtYmfUdA4X3H3VVnCHjHzMjB\n" +
                "XRCIF/BlFL8CAA4LKLyHgR0/WsA7ZnQ8bo5AjoAvo5ysTfqOgML7jrurzhDwjpmRg7sgEC/gyyh+\n" +
                "BQAcFlB4DwM7frSAd8zoeNwcgRwBX0Y5WZv0HQGF9x13V50h4B0zIwd3QSBewJdR/AoAOCyg8B4G\n" +
                "dvxoAe+Y0fG4OQI5Ar6McrI2KQECBAgQIEAgUkDhjYzd0AQIECBAgACBHAGFNydrkxIgQIAAAQIE\n" +
                "IgUU3sjYDU2AAAECBAgQyBFQeHOyNikBAgQIECBAIFJA4Y2M3dAECBAgQIAAgRwBhTcna5MSIECA\n" +
                "AAECBCIFFN7I2A1NgAABAgQIEMgRUHhzsjYpAQIECBAgQCBSQOGNjN3QBAgQIECAAIEcAYU3J2uT\n" +
                "EiBAgAABAgQiBRTeyNgNTYAAAQIECBDIEVB4c7I2KQECBAgQIEAgUkDhjYzd0AQIECBAgACBHAGF\n" +
                "NydrkxIgQIAAAQIEIgUU3sjYDU2AAAECBAgQyBFQeHOyNikBAgQIECBAIFJA4Y2M3dAECBAgQIAA\n" +
                "gRwBhTcna5MSIECAAAECBCIFFN7I2A1NgAABAgQIEMgRUHhzsjYpAQIECBAgQCBSQOGNjN3QBAgQ\n" +
                "IECAAIEcAYU3J2uTEiBAgAABAgQiBRTeyNgNTYAAAQIECBDIEVB4c7I2KQECBAgQIEAgUkDhjYzd\n" +
                "0AQIECBAgACBHAGFNydrkxIgQIAAAQIEIgUU3sjYDU2AAAECBAgQyBFQeHOyNikBAgQIECBAIFJA\n" +
                "4Y2M3dAECBAgQIAAgRwBhTcna5MSIECAAAECBCIFFN7I2A1NgAABAgQIEMgRUHhzsjYpAQIECBAg\n" +
                "QCBSQOGNjN3QBAgQIECAAIEcAYU3J2uTEiBAgAABAgQiBRTeyNgNTYAAAQIECBDIEVB4c7I2KQEC\n" +
                "BAgQIEAgUkDhjYzd0AQIECBAgACBHAGFNydrkxIgQIAAAQIEIgUU3sjYDU2AAAECBAgQyBFQeHOy\n" +
                "NikBAgQIECBAIFJA4Y2M3dAECBAgQIAAgRwBhTcna5MSIECAAAECBCIFFN7I2A1NgAABAgQIEMgR\n" +
                "UHhzsjYpAQIECBAgQCBSQOGNjN3QBAgQIECAAIEcAYU3J2uTEiBAgAABAgQiBRTeyNgNTYAAAQIE\n" +
                "CBDIEVB4c7I2KQECBAgQIEAgUkDhjYzd0AQIECBAgACBHAGFNydrkxIgQIAAAQIEIgUU3sjYDU2A\n" +
                "AAECBAgQyBFQeHOyNikBAgQIECBAIFJA4Y2M3dAECBAgQIAAgRwBhTcna5MSIECAAAECBCIFFN7I\n" +
                "2A1NgAABAgQIEMgRUHhzsjYpAQIECBAgQCBSQOGNjN3QBAgQIECAAIEcAYU3J2uTEiBAgAABAgQi\n" +
                "BRTeyNgNTYAAAQIECBDIEVB4c7I2KQECBAgQIEAgUkDhjYzd0AQIECBAgACBHAGFNydrkxIgQIAA\n" +
                "AQIEIgUU3sjYDU2AAAECBAgQyBFQeHOyNikBAgQIECBAIFJA4Y2M3dAECBAgQIAAgRwBhTcna5MS\n" +
                "IECAAAECBCIFFN7I2A1NgAABAgQIEMgRUHhzsjYpAQIECBAgQCBSQOGNjN3QBAgQIECAAIEcAYU3\n" +
                "J2uTEiBAgAABAgQiBRTeyNgNTYAAAQIECBDIEVB4c7I2KQECBAgQIEAgUkDhjYzd0AQIECBAgACB\n" +
                "HAGFNydrkxIgQIAAAQIEIgUU3sjYDU2AAAECBAgQyBFQeHOyNikBAgQIECBAIFJA4Y2M3dAECBAg\n" +
                "QIAAgRwBhTcna5MSIECAAAECBCIFFN7I2A1NgAABAgQIEMgRUHhzsjYpAQIECBAgQCBSQOGNjN3Q\n" +
                "BAgQIECAAIEcAYU3J2uTEiBAgAABAgQiBRTeyNgNTYAAAQIECBDIEVB4c7I2KQECBAgQIEAgUkDh\n" +
                "jYzd0AQIECBAgACBHAGFNydrkxIgQIAAAQIEIgUU3sjYDU2AAAECBAgQyBFQeHOyNikBAgQIECBA\n" +
                "IFJA4Y2M3dAECBAgQIAAgRwBhTcna5MSIECAAAECBCIFFN7I2A1NgAABAgQIEMgRUHhzsjYpAQIE\n" +
                "CBAgQCBSQOGNjN3QBAgQIECAAIEcAYU3J2uTEiBAgAABAgQiBRTeyNgNTYAAAQIECBDIEVB4c7I2\n" +
                "KQECBAgQIEAgUkDhjYzd0AQIECBAgACBHAGFNydrkxIgQIAAAQIEIgUU3sjYDU2AAAECBAgQyBFQ\n" +
                "eHOyNikBAgQIECBAIFJA4Y2M3dAECBAgQIAAgRwBhTcna5MSIECAAAECBCIFFN7I2A1NgAABAgQI\n" +
                "EMgRUHhzsjYpAQIECBAgQCBSQOGNjN3QBAgQIECAAIEcAYU3J2uTEiBAgAABAgQiBRTeyNgNTYAA\n" +
                "AQIECBDIEVB4c7I2KQECBAgQIEAgUkDhjYzd0AQIECBAgACBHAGFNydrkxIgQIAAAQIEIgUU3sjY\n" +
                "DU2AAAECBAgQyBFQeHOyNikBAgQIECBAIFJA4Y2M3dAECBAgQIAAgRwBhTcna5MSIECAAAECBCIF\n" +
                "FN7I2A1NgAABAgQIEMgRUHhzsjYpAQIECBAgQCBSQOGNjN3QBAgQIECAAIEcAYU3J2uTEiBAgAAB\n" +
                "AgQiBRTeyNgNTYAAAQIECBDIEVB4c7I2KQECBAgQIEAgUkDhjYzd0AQIECBAgACBHAGFNydrkxIg\n" +
                "QIAAAQIEIgUU3sjYDU2AAAECBAgQyBFQeHOyNikBAgQIECBAIFJA4Y2M3dAECBAgQIAAgRwBhTcn\n" +
                "a5MSIECAAAECBCIFFN7I2A1NgAABAgQIEMgRUHhzsjYpAQIECBAgQCBSQOGNjN3QBAgQIECAAIEc\n" +
                "AYU3J2uTEiBAgAABAgQiBRTeyNgNTYAAAQIECBDIEVB4c7I2KQECBAgQIEAgUkDhjYzd0AQIECBA\n" +
                "gACBHAGFNydrkxIgQIAAAQIEIgUU3sjYDU2AAAECBAgQyBFQeHOyNikBAgQIECBAIFJA4Y2M3dAE\n" +
                "CBAgQIAAgRwBhTcna5MSIECAAAECBCIFFN7I2A1NgAABAgQIEMgRUHhzsjYpAQIECBAgQCBSQOGN\n" +
                "jN3QBAgQIECAAIEcAYU3J2uTEiBAgAABAgQiBRTeyNgNTYAAAQIECBDIEVB4c7I2KQECBAgQIEAg\n" +
                "UkDhjYzd0AQIECBAgACBHAGFNydrkxIgQIAAAQIEIgUU3sjYDU2AAAECBAgQyBFQeHOyNikBAgQI\n" +
                "ECBAIFJA4Y2M3dAECBAgQIAAgRwBhTcna5MSIECAAAECBCIFFN7I2A1NgAABAgQIEMgRUHhzsjYp\n" +
                "AQIECBAgQCBSQOGNjN3QBAgQIECAAIEcAYU3J2uTEiBAgAABAgQiBRTeyNgNTYAAAQIECBDIEVB4\n" +
                "c7I2KQECBAgQIEAgUkDhjYzd0AQIECBAgACBHAGFNydrkxIgQIAAAQIEIgUU3sjYDU2AAAECBAgQ\n" +
                "yBFQeHOyNikBAgQIECBAIFJA4Y2M3dAECBAgQIAAgRwBhTcna5MSIECAAAECBCIFFN7I2A1NgAAB\n" +
                "AgQIEMgRUHhzsjYpAQIECBAgQCBSQOGNjN3QBAgQIECAAIEcAYU3J2uTEiBAgAABAgQiBRTeyNgN\n" +
                "TYAAAQIECBDIEVB4c7I2KQECBAgQIEAgUkDhjYzd0AQIECBAgACBHAGFNydrkxIgQIAAAQIEIgUU\n" +
                "3sjYDU2AAAECBAgQyBFQeHOyNikBAgQIECBAIFJA4Y2M3dAECBAgQIAAgRwBhTcna5MSIECAAAEC\n" +
                "BCIFFN7I2A1NgAABAgQIEMgRUHhzsjYpAQIECBAgQCBSQOGNjN3QBAgQIECAAIEcAYU3J2uTEiBA\n" +
                "gAABAgQiBRTeyNgNTYAAAQIECBDIEVB4c7I2KQECBAgQIEAgUkDhjYzd0AQIECBAgACBHAGFNydr\n" +
                "kxIgQIAAAQIEIgUU3sjYDU2AAAECBAgQyBFQeHOyNikBAgQIECBAIFJA4Y2M3dAECBAgQIAAgRwB\n" +
                "hTcna5MSIECAAAECBCIFFN7I2A1NgAABAgQIEMgRUHhzsjYpAQIECBAgQCBSQOGNjN3QBAgQIECA\n" +
                "AIEcAYU3J2uTEiBAgAABAgQiBRTeyNgNTYAAAQIECBDIEVB4c7I2KQECBAgQIEAgUkDhjYzd0AQI\n" +
                "ECBAgACBHAGFNydrkxIgQIAAAQIEIgUU3sjYDU2AAAECBAgQyBFQeHOyNikBAgQIECBAIFJA4Y2M\n" +
                "3dAECBAgQIAAgRwBhTcna5MSIECAAAECBCIFFN7I2A1NgAABAgQIEMgRUHhzsjYpAQIECBAgQCBS\n" +
                "QOGNjN3QBAgQIECAAIEcAYU3J2uTEiBAgAABAgQiBRTeyNgNTYAAAQIECBDIEVB4c7I2KQECBAgQ\n" +
                "IEAgUkDhjYzd0AQIECBAgACBHAGFNydrkxIgQIAAAQIEIgUU3sjYDU2AAAECBAgQyBFQeHOyNikB\n" +
                "AgQIECBAIFJA4Y2M3dAECBAgQIAAgRwBhTcna5MSIECAAAECBCIFFN7I2A1NgAABAgQIEMgRUHhz\n" +
                "sjYpAQIECBAgQCBSQOGNjN3QBAgQIECAAIEcAYU3J2uTEiBAgAABAgQiBRTeyNgNTYAAAQIECBDI\n" +
                "EVB4c7I2KQECBAgQIEAgUkDhjYzd0AQIECBAgACBHAGFNydrkxIgQIAAAQIEIgUU3sjYDU2AAAEC\n" +
                "BAgQyBFQeHOyNikBAgQIECBAIFJA4Y2M3dAECBAgQIAAgRwBhTcna5MSIECAAAECBCIFFN7I2A1N\n" +
                "gAABAgQIEMgRUHhzsjYpAQIECBAgQCBSQOGNjN3QBAgQIECAAIEcAYU3J2uTEiBAgAABAgQiBRTe\n" +
                "yNgNTYAAAQIECBDIEVB4c7I2KQECBAgQIEAgUkDhjYzd0AQIECBAgACBHAGFNydrkxIgQIAAAQIE\n" +
                "IgUU3sjYDU2AAAECBAgQyBFQeHOyNikBAgQIECBAIFJA4Y2M3dAECBAgQIAAgRwBhTcna5MSIECA\n" +
                "AAECBCIFFN7I2A1NgAABAgQIEMgRUHhzsjYpAQIECBAgQCBSQOGNjN3QBAgQIECAAIEcAYU3J2uT\n" +
                "EiBAgAABAgQiBRTeyNgNTYAAAQIECBDIEVB4c7I2KQECBAgQIEAgUkDhjYzd0AQIECBAgACBHAGF\n" +
                "NydrkxIgQIAAAQIEIgUU3sjYDU2AAAECBAgQyBFQeHOyNikBAgQIECBAIFJA4Y2M3dAECBAgQIAA\n" +
                "gRwBhTcna5MSIECAAAECBCIFFN7I2A1NgAABAgQIEMgRUHhzsjYpAQIECBAgQCBSQOGNjN3QBAgQ\n" +
                "IECAAIEcAYU3J2uTEiBAgAABAgQiBRTeyNgNTYAAAQIECBDIEVB4c7I2KQECBAgQIEAgUkDhjYzd\n" +
                "0AQIECBAgACBHAGFNydrkxIgQIAAAQIEIgUU3sjYDU2AAAECBAgQyBFQeHOyNikBAgQIECBAIFJA\n" +
                "4Y2M3dAECBAgQIAAgRyB/wVfVNX5+bJ5lQAAAABJRU5ErkJggg==";

        String trousers64 = "iVBORw0KGgoAAAANSUhEUgAAArwAAAK8CAYAAAANumxDAAAgAElEQVR4Xu3YwZEgtw2G0VUAikth\n" +
                "ODaF4bgcgH3xReVaL1kCCPLv56tb7MYDd+ar+e2H/xEgQIAAAQIECBAIFvgteDajESBAgAABAgQI\n" +
                "EPgheF0CAgQIECBAgACBaAHBG71ewxEgQIAAAQIECAhed4AAAQIECBAgQCBaQPBGr9dwBAgQIECA\n" +
                "AAECgtcdIECAAAECBAgQiBYQvNHrNRwBAgQIECBAgIDgdQcIECBAgAABAgSiBQRv9HoNR4AAAQIE\n" +
                "CBAgIHjdAQIECBAgQIAAgWgBwRu9XsMRIECAAAECBAgIXneAAAECBAgQIEAgWkDwRq/XcAQIECBA\n" +
                "gAABAoLXHSBAgAABAgQIEIgWELzR6zUcAQIECBAgQICA4HUHCBAgQIAAAQIEogUEb/R6DUeAAAEC\n" +
                "BAgQICB43QECBAgQIECAAIFoAcEbvV7DESBAgAABAgQICF53gAABAgQIECBAIFpA8Eav13AECBAg\n" +
                "QIAAAQKC1x0gQIAAAQIECBCIFhC80es1HAECBAgQIECAgOB1BwgQIECAAAECBKIFBG/0eg1HgAAB\n" +
                "AgQIECAgeN0BAgQIECBAgACBaAHBG71ewxEgQIAAAQIECAhed4AAAQIECBAgQCBaQPBGr9dwBAgQ\n" +
                "IECAAAECgtcdIECAAAECBAgQiBYQvNHrNRwBAgQIECBAgIDgdQcIECBAgAABAgSiBQRv9HoNR4AA\n" +
                "AQIECBAgIHjdAQIECBAgQIAAgWgBwRu9XsMRIECAAAECBAgIXneAAAECBAgQIEAgWkDwRq/XcAQI\n" +
                "ECBAgAABAoLXHSBAgAABAgQIEIgWELzR6zUcAQIECBAgQICA4HUHCBAgQIAAAQIEogUEb/R6DUeA\n" +
                "AAECBAgQICB43QECBAgQIECAAIFoAcEbvV7DESBAgAABAgQICF53gAABAgQIECBAIFpA8Eav13AE\n" +
                "CBAgQIAAAQKC1x0gQIAAAQIECBCIFhC80es1HAECBAgQIECAgOB1BwgQIECAAAECBKIFBG/0eg1H\n" +
                "gAABAgQIECAgeN0BAgQIECBAgACBaAHBG71ewxEgQIAAAQIECAhed4AAAQIECBAgQCBaQPBGr9dw\n" +
                "BAgQIECAAAECgtcdIECAAAECBAgQiBYQvNHrNRwBAgQIECBAgIDgdQcIECBAgAABAgSiBQRv9HoN\n" +
                "R4AAAQIECBAgIHjdAQIECBAgQIAAgWgBwRu9XsMRIECAAAECBAgIXneAAAECBAgQIEAgWkDwRq/X\n" +
                "cAQIECBAgAABAoLXHSBAgAABAgQIEIgWELzR6zUcAQIECBAgQICA4HUHCBAgQIAAAQIEogUEb/R6\n" +
                "DUeAAAECBAgQICB43QECBAgQIECAAIFoAcEbvV7DESBAgAABAgQICF53gAABAgQIECBAIFpA8Eav\n" +
                "13AECBAgQIAAAQKC1x0gQIAAAQIECBCIFhC80es1HAECBAgQIECAgOB1BwgQIECAAAECBKIFBG/0\n" +
                "eg1HgAABAgQIECAgeN0BAgQIECBAgACBaAHBG71ewxEgQIAAAQIECAhed4AAAQIECBAgQCBaQPBG\n" +
                "r9dwBAgQIECAAAECgtcdIECAAAECBAgQiBYQvNHrNRwBAgQIECBAgIDgdQcIECBAgAABAgSiBQRv\n" +
                "9HoNR4AAAQIECBAgIHjdAQIECBAgQIAAgWgBwRu9XsMRIECAAAECBAgIXneAAAECBAgQIEAgWkDw\n" +
                "Rq/XcAQIECBAgAABAoLXHSBAgAABAgQIEIgWELzR6zUcAQIECBAgQICA4HUHCBAgQIAAAQIEogUE\n" +
                "b/R6DUeAAAECBAgQICB43QECBAgQIECAAIFoAcEbvV7DESBAgAABAgQICF53gAABAgQIECBAIFpA\n" +
                "8Eav13AECBAgQIAAAQKC1x0gQIAAAQIECBCIFhC80es1HAECBAgQIECAgOB1BwgQIECAAAECBKIF\n" +
                "BG/0eg1HgAABAgQIECAgeN0BAgQIECBAgACBaAHBG71ewxEgQIAAAQIECAhed4AAAQIECBAgQCBa\n" +
                "QPBGr9dwBAgQIECAAAECgtcdIECAAAECBAgQiBYQvNHrNRwBAgQIECBAgIDgdQcIECBAgAABAgSi\n" +
                "BQRv9HoNR4AAAQIECBAgIHjdAQIECBAgQIAAgWgBwRu9XsMRIECAAAECBAgIXneAAAECBAgQIEAg\n" +
                "WkDwRq/XcAQIECBAgAABAoLXHSBAgAABAgQIEIgWELzR6zUcAQIECBAgQICA4HUHCBAgQIAAAQIE\n" +
                "ogUEb/R6DUeAAAECBAgQICB43QECBAgQIECAAIFoAcEbvV7DESBAgAABAgQICF53gAABAgQIECBA\n" +
                "IFpA8Eav13AECBAgQIAAAQKC1x0gQIAAAQIECBCIFhC80es1HAECBAgQIECAgOB1BwgQIECAAAEC\n" +
                "BKIFBG/0eg1HgAABAgQIECAgeN0BAgQIECBAgACBaAHBG71ewxEgQIAAAQIECAhed4AAAQIECBAg\n" +
                "QCBaQPBGr9dwBAgQIECAAAECgtcdIECAAAECBAgQiBYQvNHrNRwBAgQIECBAgIDgdQcIECBAgAAB\n" +
                "AgSiBQRv9HoNR4AAAQIECBAgIHjdAQIECBAgQIAAgWgBwRu9XsMRIECAAAECBAgIXneAAAECBAgQ\n" +
                "IEAgWkDwRq/XcAQIECBAgAABAoLXHSBAgAABAgQIEIgWELzR6zUcAQIECBAgQICA4HUHCBAgQIAA\n" +
                "AQIEogUEb/R6DUeAAAECBAgQICB43QECBAgQIECAAIFoAcEbvV7DESBAgAABAgQICF53gAABAgQI\n" +
                "ECBAIFpA8Eav13AECBAgQIAAAQKC1x0gQIAAAQIECBCIFhC80es1HAECBAgQIECAgOB1BwgQIECA\n" +
                "AAECBKIFBG/0eg1HgAABAgQIECAgeN0BAgQIECBAgACBaAHBG71ewxEgQIAAAQIECAhed4AAAQIE\n" +
                "CBAgQCBaQPBGr9dwBAgQIECAAAECgtcdIECAAAECBAgQiBYQvNHrNRwBAgQIECBAgIDgdQcIECBA\n" +
                "gAABAgSiBQRv9HoNR4AAAQIECBAgIHjdAQIECBAgQIAAgWgBwRu9XsMRIECAAAECBAgIXneAAAEC\n" +
                "BAgQIEAgWkDwRq/XcAQIECBAgAABAoLXHSBAgAABAgQIEIgWELzR6zUcAQIECBAgQICA4HUHCBAg\n" +
                "QIAAAQIEogUEb/R6DUeAAAECBAgQICB43QECBAgQIECAAIFoAcEbvV7DESBAgAABAgQICF53gAAB\n" +
                "AgQIECBAIFpA8Eav13AECBAgQIAAAQKC1x0gQIAAAQIECBCIFhC80es1HAECBAgQIECAgOB1BwgQ\n" +
                "IECAAAECBKIFBG/0eg1HgAABAgQIECAgeN0BAgQIECBAgACBaAHBG71ewxEgQIAAAQIECAhed4AA\n" +
                "AQIECBAgQCBaQPBGr9dwBAgQIECAAAECgtcdIECAAAECBAgQiBYQvNHrNRwBAgQIECBAgIDgdQcI\n" +
                "ECBAgAABAgSiBQRv9HoNR4AAAQIECBAgIHjdAQIECBAgQIAAgWgBwRu9XsMRIECAAAECBAgIXneA\n" +
                "AAECBAgQIEAgWkDwRq/XcAQIECBAgAABAoLXHSBAgAABAgQIEIgWELzR6zUcAQIECBAgQICA4HUH\n" +
                "CBAgQIAAAQIEogUEb/R6DUeAAAECBAgQICB43QECBAgQIECAAIFoAcEbvV7DESBAgAABAgQICF53\n" +
                "gAABAgQIECBAIFpA8Eav13AECBAgQIAAAQKC1x0gQIAAAQIECBCIFhC80es1HAECBAgQIECAgOB1\n" +
                "BwgQIECAAAECBKIFBG/0eg1HgAABAgQIECAgeN0BAgQIECBAgACBaAHBG71ewxEgQIAAAQIECAhe\n" +
                "d4AAAQIECBAgQCBaQPBGr9dwBAgQIECAAAECgtcdIECAAAECBAgQiBYQvNHrNRwBAgQIECBAgIDg\n" +
                "dQcIECBAgAABAgSiBQRv9HoNR4AAAQIECBAgIHjdAQIECBAgQIAAgWgBwRu9XsMRIECAAAECBAgI\n" +
                "XneAAAECBAgQIEAgWkDwRq/XcAQIECBAgAABAoLXHSBAgAABAgQIEIgWELzR6zUcAQIECBAgQICA\n" +
                "4HUHCBAgQIAAAQIEogUEb/R6DUeAAAECBAgQICB43QECBAgQIECAAIFoAcEbvV7DESBAgAABAgQI\n" +
                "CF53gAABAgQIECBAIFpA8Eav13AECBAgQIAAAQKC1x0gQIAAAQIECBCIFhC80es1HAECBAgQIECA\n" +
                "gOB1BwgQIECAAAECBKIFBG/0eg1HgAABAgQIECAgeN0BAgQIECBAgACBaAHBG71ewxEgQIAAAQIE\n" +
                "CAhed4AAAQIECBAgQCBaQPBGr9dwBAgQIECAAAECgtcdIECAAAECBAgQiBYQvNHrNRwBAgQIECBA\n" +
                "gIDgdQcIECBAgAABAgSiBQRv9HoNR4AAAQIECBAgIHjdAQIECBAgQIAAgWgBwRu9XsMRIECAAAEC\n" +
                "BAgIXneAAAECBAgQIEAgWkDwRq/XcAQIECBAgAABAoLXHSBAgAABAgQIEIgWELzR6zUcAQIECBAg\n" +
                "QICA4HUHCBAgQIAAAQIEogUEb/R6DUeAAAECBAgQICB43QECBAgQIECAAIFoAcEbvV7DESBAgAAB\n" +
                "AgQICF53gAABAgQIECBAIFpA8Eav13AECBAgQIAAAQKC1x0gQIAAAQIECBCIFhC80es1HAECBAgQ\n" +
                "IECAgOB1BwgQIECAAAECBKIFBG/0eg1HgAABAgQIECAgeN0BAgQIECBAgACBaAHBG71ewxEgQIAA\n" +
                "AQIECAhed4AAAQIECBAgQCBaQPBGr9dwBAgQIECAAAECgtcdIECAAAECBAgQiBYQvNHrNRwBAgQI\n" +
                "ECBAgIDgdQcIECBAgAABAgSiBQRv9HoNR4AAAQIECBAgIHjdAQIECBAgQIAAgWgBwRu9XsMRIECA\n" +
                "AAECBAgIXneAAAECBAgQIEAgWkDwRq/XcAQIECBAgAABAoLXHSBAgAABAgQIEIgWELzR6zUcAQIE\n" +
                "CBAgQICA4HUHCBAgQIAAAQIEogUEb/R6DUeAAAECBAgQICB43QECBAgQIECAAIFoAcEbvV7DESBA\n" +
                "gAABAgQICF53gAABAgQIECBAIFpA8Eav13AECBAgQIAAAQKC1x0gQIAAAQIECBCIFhC80es1HAEC\n" +
                "BAgQIECAgOB1BwgQIECAAAECBKIFBG/0eg1HgAABAgQIECAgeN0BAgQIECBAgACBaAHBG71ewxEg\n" +
                "QIAAAQIECAhed4AAAQIECBAgQCBaQPBGr9dwBAgQIECAAAECgtcdIECAAAECBAgQiBYQvNHrNRwB\n" +
                "AgQIECBAgIDgdQcIECBAgAABAgSiBQRv9HoNR4AAAQIECBAgIHjdAQIECBAgQIAAgWgBwRu9XsMR\n" +
                "IECAAAECBAgIXneAAAECBAgQIEAgWkDwRq/XcAQIECBAgAABAoLXHSBAgAABAgQIEIgWELzR6zUc\n" +
                "AQIECBAgQICA4HUHCBAgQIAAAQIEogUEb/R6DUeAAAECBAgQICB43QECBAgQIECAAIFoAcEbvV7D\n" +
                "ESBAgAABAgQICF53gAABAgQIECBAIFpA8Eav13AECBAgQIAAAQKC1x0gQIAAAQIECBCIFhC80es1\n" +
                "HAECBAgQIECAgOB1BwgQIECAAAECBKIFBG/0eg1HgAABAgQIECAgeN0BAgQIECBAgACBaAHBG71e\n" +
                "wxEgQIAAAQIECAhed4AAAQIECBAgQCBaQPBGr9dwBAgQIECAAAECgtcdIECAAAECBAgQiBYQvNHr\n" +
                "NRwBAgQIECBAgIDgdQcIECBAgAABAgSiBQRv9HoNR4AAAQIECBAgIHjdAQIECBAgQIAAgWgBwRu9\n" +
                "XsMRIECAAAECBAgIXneAAAECBAgQIEAgWkDwRq/XcAQIECBAgAABAoLXHSBAgAABAgQIEIgWELzR\n" +
                "6zUcAQIECBAgQICA4HUHCBAgQIAAAQIEogUEb/R6DUeAAAECBAgQICB43QECBAgQIECAAIFoAcEb\n" +
                "vV7DESBAgAABAgQICF53gAABAgQIECBAIFpA8Eav13AECBAgQIAAAQKC1x0gQIAAAQIECBCIFhC8\n" +
                "0es1HAECBAgQIECAgOB1BwgQIECAAAECBKIFBG/0eg1HgAABAgQIECAgeN0BAgQIECBAgACBaAHB\n" +
                "G71ewxEgQIAAAQIECAhed4AAAQIECBAgQCBaQPBGr9dwBAgQIECAAAECgtcdIECAAAECBAgQiBYQ\n" +
                "vNHrNRwBAgQIECBAgIDgdQcIECBAgAABAgSiBQRv9HoNR4AAAQIECBAgIHjdAQIECBAgQIAAgWgB\n" +
                "wRu9XsMRIECAAAECBAgIXneAAAECBAgQIEAgWkDwRq/XcAQIECBAgAABAoLXHSBAgAABAgQIEIgW\n" +
                "ELzR6zUcAQIECBAgQICA4HUHCBAgQIAAAQIEogUEb/R6DUeAAAECBAgQICB43QECBAgQIECAAIFo\n" +
                "AcEbvV7DESBAgAABAgQICF53gAABAgQIECBAIFpA8Eav13AECBAgQIAAAQKC1x0gQIAAAQIECBCI\n" +
                "FhC80es1HAECBAgQIECAgOB1BwgQIECAAAECBKIFBG/0eg1HgAABAgQIECAgeN0BAgQIECBAgACB\n" +
                "aAHBG71ewxEgQIAAAQIECAhed4AAAQIECBAgQCBaQPBGr9dwBAgQIECAAAECgtcdIECAAAECBAgQ\n" +
                "iBYQvNHrNRwBAgQIECBAgIDgdQcIECBAgAABAgSiBQRv9HoNR4AAAQIECBAgIHjdAQIECBAgQIAA\n" +
                "gWgBwRu9XsMRIECAAAECBAgIXneAAAECBAgQIEAgWkDwRq/XcAQIECBAgAABAoLXHSBAgAABAgQI\n" +
                "EIgWELzR6zUcAQIECBAgQICA4HUHCBAgQIAAAQIEogUEb/R6DUeAAAECBAgQICB43QECBAgQIECA\n" +
                "AIFoAcEbvV7DESBAgAABAgQICF53gAABAgQIECBAIFpA8Eav13AECBAgQIAAAQKC1x0gQIAAAQIE\n" +
                "CBCIFhC80es1HAECBAgQIECAgOB1BwgQIECAAAECBKIFBG/0eg1HgAABAgQIECAgeN0BAgQIECBA\n" +
                "gACBaAHBG71ewxEgQIAAAQIECAhed4AAAQIECBAgQCBaQPBGr9dwBAgQIECAAAECgtcdIECAAAEC\n" +
                "BAgQiBYQvNHrNRwBAgQIECBAgIDgdQcIECBAgAABAgSiBQRv9HoNR4AAAQIECBAgIHjdAQIECBAg\n" +
                "QIAAgWgBwRu9XsMRIECAAAECBAgIXneAAAECBAgQIEAgWkDwRq/XcAQIECBAgAABAoLXHSBAgAAB\n" +
                "AgQIEIgWELzR6zUcAQIECBAgQICA4HUHCBAgQIAAAQIEogUEb/R6DUeAAAECBAgQICB43QECBAgQ\n" +
                "IECAAIFoAcEbvV7DESBAgAABAgQICF53gAABAgQIECBAIFpA8Eav13AECBAgQIAAAQKC1x0gQIAA\n" +
                "AQIECBCIFhC80es1HAECBAgQIECAgOB1BwgQIECAAAECBKIFBG/0eg1HgAABAgQIECAgeN0BAgQI\n" +
                "ECBAgACBaAHBG71ewxEgQIAAAQIECAhed4AAAQIECBAgQCBaQPBGr9dwBAgQIECAAAECgtcdIECA\n" +
                "AAECBAgQiBYQvNHrNRwBAgQIECBAgIDgdQcIECBAgAABAgSiBQRv9HoNR4AAAQIECBAgIHjdAQIE\n" +
                "CBAgQIAAgWgBwRu9XsMRIECAAAECBAgIXneAAAECBAgQIEAgWkDwRq/XcAQIECBAgAABAoLXHSBA\n" +
                "gAABAgQIEIgWELzR6zUcAQIECBAgQICA4HUHCBAgQIAAAQIEogUEb/R6DUeAAAECBAgQICB43QEC\n" +
                "BAgQIECAAIFoAcEbvV7DESBAgAABAgQICF53gAABAgQIECBAIFpA8Eav13AECBAgQIAAAQKC1x0g\n" +
                "QIAAAQIECBCIFhC80es1HAECBAgQIECAgOB1BwgQIECAAAECBKIFBG/0eg1HgAABAgQIECAgeN0B\n" +
                "AgQIECBAgACBaAHBG71ewxEgQIAAAQIECAhed4AAAQIECBAgQCBaQPBGr9dwBAgQIECAAAECgtcd\n" +
                "IECAAAECBAgQiBYQvNHrNRwBAgQIECBAgIDgdQcIECBAgAABAgSiBQRv9HoNR4AAAQIECBAgIHjd\n" +
                "AQIECBAgQIAAgWgBwRu9XsMRIECAAAECBAgIXneAAAECBAgQIEAgWkDwRq/XcAQIECBAgAABAoLX\n" +
                "HSBAgAABAgQIEIgWELzR6zUcAQIECBAgQICA4HUHCBAgQIAAAQIEogUEb/R6DUeAAAECBAgQICB4\n" +
                "3QECBAgQIECAAIFoAcEbvV7DESBAgAABAgQICF53gAABAgQIECBAIFpA8Eav13AECBAgQIAAAQKC\n" +
                "1x0gQIAAAQIECBCIFhC80es1HAECBAgQIECAgOB1BwgQIECAAAECBKIFBG/0eg1HgAABAgQIECAg\n" +
                "eN0BAgQIECBAgACBaAHBG71ewxEgQIAAAQIECAhed4AAAQIECBAgQCBaQPBGr9dwBAgQIECAAAEC\n" +
                "gtcdIECAAAECBAgQiBYQvNHrNRwBAgQIECBAgIDgdQcIECBAgAABAgSiBQRv9HoNR4AAAQIECBAg\n" +
                "IHjdAQJvCfz7rc/1tQ8L+P3w8PJ8OgECfxXwA82NIPCWgOB9a18vf63fDy9vz7cTIPAXAT/QXAgC\n" +
                "bwkI3rf29fLX+v3w8vZ8OwECgtcdIPCwgOB9eHmPfbrgfWxhPpcAgZ8L+IHmdhB4S0DwvrWvl7/W\n" +
                "74eXt+fbCRDwF153gMDDAoL34eU99umC97GF+VwCBPyF1x0gkCIgeFM2ef8cgvf+HflCAgQWBfxA\n" +
                "W4TyGIFLBATvJYv4wGf4/fCBJRuRwFcE/ED7yqbNmSIgeFM2ef8cfj/cvyNfSIDAooAfaItQHiNw\n" +
                "iYDgvWQRH/gMvx8+sGQjEviKgB9oX9m0OVMEBG/KJu+fw++H+3fkCwkQWBTwA20RymMELhEQvJcs\n" +
                "4gOf4ffDB5ZsRAJfEfAD7SubNmeKgOBN2eT9c/j9cP+OfCEBAosCfqAtQnmMwCUCrcH7+x9/XjKm\n" +
                "z/iVwL/++Y9fPfJ3/3+/H/6uoP+eAIFrBPxAu2YVPoTAkoDgXWLKf0jw5u/YhAQI1AkI3jpLJxE4\n" +
                "ISB4Tyg/8A7B+8CSfCIBAtcICN5rVuFDCCwJCN4lpvyHBG/+jk1IgECdgOCts3QSgRMCgveE8gPv\n" +
                "ELwPLMknEiBwjYDgvWYVPoTAkoDgXWLKf0jw5u/YhAQI1AkI3jpLJxE4ISB4Tyg/8A7B+8CSfCIB\n" +
                "AtcICN5rVuFDCCwJCN4lpvyHBG/+jk1IgECdgOCts3QSgRMCgveE8gPvELwPLMknEiBwjYDgvWYV\n" +
                "PoTAkoDgXWLKf0jw5u/YhAQI1AkI3jpLJxE4ISB4Tyg/8A7B+8CSfCIBAtcICN5rVuFDCCwJCN4l\n" +
                "pvyHBG/+jk1IgECdgOCts3QSgRMCgveE8gPvELwPLMknEiBwjYDgvWYVPoTAkoDgXWLKf0jw5u/Y\n" +
                "hAQI1AkI3jpLJxE4ISB4Tyg/8A7B+8CSfCIBAtcICN5rVuFDCCwJCN4lpvyHBG/+jk1IgECdgOCt\n" +
                "s3QSgRMCgveE8gPvELwPLMknEiBwjYDgvWYVPoTAkoDgXWLKf0jw5u/YhAQI1AkI3jpLJxE4ISB4\n" +
                "Tyg/8A7B+8CSfCIBAtcICN5rVuFDCCwJCN4lpvyHBG/+jk1IgECdgOCts3QSgRMCgveE8gPvELwP\n" +
                "LMknEiBwjYDgvWYVPoTAkoDgXWLKf0jw5u/YhAQI1AkI3jpLJxE4ISB4Tyg/8A7B+8CSfCIBAtcI\n" +
                "CN5rVuFDCCwJCN4lpvyHBG/+jk1IgECdgOCts3QSgRMCgveE8gPvELwPLMknEiBwjYDgvWYVPoTA\n" +
                "koDgXWLKf0jw5u/YhAQI1AkI3jpLJxE4ISB4Tyg/8A7B+8CSfCIBAtcICN5rVuFDCCwJCN4lpvyH\n" +
                "BG/+jk1IgECdgOCts3QSgRMCgveE8gPvELwPLMknEiBwjYDgvWYVPoTAkoDgXWLKf0jw5u/YhAQI\n" +
                "1AkI3jpLJxE4ISB4Tyg/8A7B+8CSfCIBAtcICN5rVuFDCCwJCN4lpvyHBG/+jk1IgECdgOCts3QS\n" +
                "gRMCgveE8gPvELwPLMknEiBwjYDgvWYVPoTAkoDgXWLKf0jw5u/YhAQI1AkI3jpLJxE4ISB4Tyg/\n" +
                "8A7B+8CSfCIBAtcICN5rVuFDCCwJCN4lpvyHBG/+jk1IgECdgOCts3QSgRMCgveE8gPvELwPLMkn\n" +
                "EiBwjYDgvWYVPoTAkoDgXWLKf0jw5u/YhAQI1AkI3jpLJxE4ISB4Tyg/8A7B+8CSfCIBAtcICN5r\n" +
                "VuFDCCwJCN4lpvyHBG/+jk1IgECdgOCts3QSgRMCgveE8gPvELwPLMknEiBwjYDgvWYVPoTAkoDg\n" +
                "XWLKf0jw5u/YhAQI1AkI3jpLJxE4ISB4Tyg/8A7B+8CSfCIBAtcICN5rVuFDCCwJCN4lpvyHBG/+\n" +
                "jk1IgECdgOCts3QSgRMCgveE8gPvELwPLMknEiBwjYDgvWYVPoTAkoDgXWLKf0jw5u/YhAQI1AkI\n" +
                "3jpLJxE4ISB4Tyg/8A7B+8CSfCIBAtcICN5rVuFDCCwJCN4lpvyHBG/+jk1IgECdgOCts3QSgRMC\n" +
                "gveE8gPvELwPLMknEiBwjYDgvWYVPoTAkoDgXWLKf0jw5u/YhAQI1AkI3jpLJxE4ISB4Tyg/8A7B\n" +
                "+8CSfCIBAtcICN5rVuFDCCwJCN4lpvyHBG/+jk1IgECdgOCts3QSgRMCgveE8gPvELwPLMknEiBw\n" +
                "jYDgvWYVPoTAksBTwXsgypbQbnjo9z/+LP2MA7Z+P5RuzGEECEwK+IE2qe/dBPYFBO++2RX/heC9\n" +
                "Yg0+ggCBjwoI3o8u3tjPCgjeR1cneB9dnM8mQCBCQPBGrNEQHxIQvI8uW/A+ujifTYBAhIDgjVij\n" +
                "IT4kIHgfXbbgfXRxPpsAgQgBwRuxRkN8SDiNe68AAA4WSURBVEDwPrpswfvo4nw2AQIRAoI3Yo2G\n" +
                "+JCA4H102YL30cX5bAIEIgQEb8QaDfEhAcH76LIF76OL89kECEQICN6INRriQwKC99FlC95HF+ez\n" +
                "CRCIEBC8EWs0xIcEBO+jyxa8jy7OZxMgECEgeCPWaIgPCQjeR5cteB9dnM8mQCBCQPBGrNEQHxIQ\n" +
                "vI8uW/A+ujifTYBAhIDgjVijIT4kIHgfXbbgfXRxPpsAgQgBwRuxRkN8SEDwPrpswfvo4nw2AQIR\n" +
                "AoI3Yo2G+JCA4H102YL30cX5bAIEIgQEb8QaDfEhAcH76LIF76OL89kECEQICN6INRriQwKC99Fl\n" +
                "C95HF+ezCRCIEBC8EWs0xIcEBO+jyxa8jy7OZxMgECEgeCPWaIgPCQjeR5cteB9dnM8mQCBCQPBG\n" +
                "rNEQHxL4avBO/qwqMRe8H/pXalQCBK4TmPwlch2GDyLwgEBJfP1szoujbPJnVYn5xbY/uw6T5g/8\n" +
                "U/SJBAi8JOAH2kvb8q0EfvwoiS/Bu3WVSswF75a5hwkQIFAqIHhLOR1GoF2gJL4E79aeSswF75a5\n" +
                "hwkQIFAqIHhLOR1GoF2gJL4E79aeSswF75a5hwkQIFAqIHhLOR1GoF2gJL4E79aeSswF75a5hwkQ\n" +
                "IFAqIHhLOR1GoF2gJL4E79aeSswF75a5hwkQIFAqIHhLOR1GoF2gJL4E79aeSswF75a5hwkQIFAq\n" +
                "IHhLOR1GoF2gJL4E79aeSswF75a5hwkQIFAqIHhLOR1GoF2gJL4E79aeSswF75a5hwkQIFAqIHhL\n" +
                "OR1GoF2gJL4E79aeSswF75a5hwkQIFAqIHhLOR1GoF2gJL4E79aeSswF75a5hwkQIFAqIHhLOR1G\n" +
                "oF2gJL4E79aeSswF75a5hwkQIFAqIHhLOR1GoF2gJL4E79aeSswF75a5hwkQIFAqIHhLOR1GoF2g\n" +
                "JL4E79aeSswF75a5hwkQIFAqIHhLOR1GoF2gJL4E79aeSswF75a5hwkQIFAqIHhLOR1GoF2gJL4E\n" +
                "79aeSswF75a5hwkQIFAqIHhLOR1GoF2gJL4E79aeSswF75a5hwkQIFAqIHhLOR1GoF2gJL4E79ae\n" +
                "SswF75a5hwkQIFAqIHhLOR1GoF2gJL4E79aeSswF75a5hwkQIFAqIHhLOR1GoF2gJL4E79aeSswF\n" +
                "75a5hwkQIFAqIHhLOR1GoF2gJL4E79aeSswF75a5hwkQIFAqIHhLOR1GoF2gJL4E79aeSswF75a5\n" +
                "hwkQIFAqIHhLOR1GoF2gJL4E79aeSswF75a5hwkQIFAqIHhLOR1GoF2gJL4E79aeSswF75a5hwkQ\n" +
                "IFAqIHhLOR1GoF2gJL4E79aeSswF75a5hwkQIFAqIHhLOR1GoF2gJL4E79aeSswF75a5hwkQIFAq\n" +
                "IHhLOR1GoF2gJL4E79aeSswF75a5hwkQIFAqIHhLOR1GoF2gJL4E79aeSswF75a5hwkQIFAqIHhL\n" +
                "OR1GoF2gJL4E79aeSswF75a5hwkQIFAqIHhLOR1GoF2gJL7av7L+BZM/q5jX79OJBAgQOCow+Uvk\n" +
                "6KBeRiBEQHydXyTz8+beSIAAgVIBwVvK6TAC7QLiq534f17A/Ly5NxIgQKBUQPCWcjqMQLuA+Gon\n" +
                "Frz/FfD74fxd80YCBJoE/EBrgnUsgSYBwdsE+3+OZX7e3BsJECBQKiB4SzkdRqBdQHy1E/sLr7/w\n" +
                "nr9k3kiAQK+A4O31dTqBagHBWy366/OY/9rIEwQIELhaQPBevR4fR8BfGy/4a6Pg9Q+RAAECjwsI\n" +
                "3scX6PM/JyC+zq+c+XlzbyRAgECpgOAt5XQYgXYB8dVO7K/qF/xV/fyWvZEAgWgBwRu9XsMFCgje\n" +
                "80tlft7cGwkQIFAqIHhLOR1GoF1AfLUT+wuvv/Cev2TeSIBAr4Dg7fV1OoFqAcFbLfrr85j/2sgT\n" +
                "BAgQuFpA8F69Hh9HwF8bL/hro+D1D5EAAQKPCwjexxfo8z8nIL7Or5z5eXNvJECAQKmA4C3ldBiB\n" +
                "dgHx1U7sr+oX/FX9/Ja9kQCBaAHBG71ewwUKCN7zS2V+3twbCRAgUCogeEs5HUagXUB8tRP7C6+/\n" +
                "8J6/ZN5IgECvgODt9XU6AQIECBAgQIDAsIDgHV6A1xMgQIAAAQIECPQKCN5eX6cTIECAAAECBAgM\n" +
                "Cwje4QV4PQECBAgQIECAQK+A4O31dToBAgQIECBAgMCwgOAdXoDXEyBAgAABAgQI9AoI3l5fpxMg\n" +
                "QIAAAQIECAwLCN7hBXg9AQIECBAgQIBAr4Dg7fV1OgECBAgQIECAwLCA4B1egNcTIECAAAECBAj0\n" +
                "CgjeXl+nEyBAgAABAgQIDAsI3uEFeD0BAgQIECBAgECvgODt9XU6AQIECBAgQIDAsIDgHV6A1xMg\n" +
                "QIAAAQIECPQKCN5eX6cTIECAAAECBAgMCwje4QV4PQECBAgQIECAQK+A4O31dToBAgQIECBAgMCw\n" +
                "gOAdXoDXEyBAgAABAgQI9AoI3l5fpxMgQIAAAQIECAwLCN7hBXg9AQIECBAgQIBAr4Dg7fV1OgEC\n" +
                "BAgQIECAwLCA4B1egNcTIECAAAECBAj0CgjeXl+nEyBAgAABAgQIDAsI3uEFeD0BAgQIECBAgECv\n" +
                "gODt9XU6AQIECBAgQIDAsIDgHV6A1xMgQIAAAQIECPQKCN5eX6cTIECAAAECBAgMCwje4QV4PQEC\n" +
                "BAgQIECAQK+A4O31dToBAgQIECBAgMCwgOAdXoDXEyBAgAABAgQI9AoI3l5fpxMgQIAAAQIECAwL\n" +
                "CN7hBXg9AQIECBAgQIBAr4Dg7fV1OgECBAgQIECAwLCA4B1egNcTIECAAAECBAj0CgjeXl+nEyBA\n" +
                "gAABAgQIDAsI3uEFeD0BAgQIECBAgECvgODt9XU6AQIECBAgQIDAsIDgHV6A1xMgQIAAAQIECPQK\n" +
                "CN5eX6cTIECAAAECBAgMCwje4QV4PQECBAgQIECAQK+A4O31dToBAgQIECBAgMCwgOAdXoDXEyBA\n" +
                "gAABAgQI9AoI3l5fpxMgQIAAAQIECAwLCN7hBXg9AQIECBAgQIBAr4Dg7fV1OgECBAgQIECAwLCA\n" +
                "4B1egNcTIECAAAECBAj0CgjeXl+nEyBAgAABAgQIDAsI3uEFeD0BAgQIECBAgECvgODt9XU6AQIE\n" +
                "CBAgQIDAsIDgHV6A1xMgQIAAAQIECPQKCN5eX6cTIECAAAECBAgMCwje4QV4PQECBAgQIECAQK+A\n" +
                "4O31dToBAgQIECBAgMCwgOAdXoDXEyBAgAABAgQI9AoI3l5fpxMgQIAAAQIECAwLCN7hBXg9AQIE\n" +
                "CBAgQIBAr4Dg7fV1OgECBAgQIECAwLCA4B1egNcTIECAAAECBAj0CgjeXl+nEyBAgAABAgQIDAsI\n" +
                "3uEFeD0BAgQIECBAgECvgODt9XU6AQIECBAgQIDAsIDgHV6A1xMgQIAAAQIECPQKCN5eX6cTIECA\n" +
                "AAECBAgMCwje4QV4PQECBAgQIECAQK+A4O31dToBAgQIECBAgMCwgOAdXoDXEyBAgAABAgQI9AoI\n" +
                "3l5fpxMgQIAAAQIECAwLCN7hBXg9AQIECBAgQIBAr4Dg7fV1OgECBAgQIECAwLCA4B1egNcTIECA\n" +
                "AAECBAj0CgjeXl+nEyBAgAABAgQIDAsI3uEFeD0BAgQIECBAgECvgODt9XU6AQIECBAgQIDAsIDg\n" +
                "HV6A1xMgQIAAAQIECPQKCN5eX6cTIECAAAECBAgMCwje4QV4PQECBAgQIECAQK+A4O31dToBAgQI\n" +
                "ECBAgMCwgOAdXoDXEyBAgAABAgQI9AoI3l5fpxMgQIAAAQIECAwLCN7hBXg9AQIECBAgQIBAr4Dg\n" +
                "7fV1OgECBAgQIECAwLCA4B1egNcTIECAAAECBAj0CgjeXl+nEyBAgAABAgQIDAsI3uEFeD0BAgQI\n" +
                "ECBAgECvgODt9XU6AQIECBAgQIDAsIDgHV6A1xMgQIAAAQIECPQKCN5eX6cTIECAAAECBAgMCwje\n" +
                "4QV4PQECBAgQIECAQK+A4O31dToBAgQIECBAgMCwgOAdXoDXEyBAgAABAgQI9AoI3l5fpxMgQIAA\n" +
                "AQIECAwLCN7hBXg9AQIECBAgQIBAr4Dg7fV1OgECBAgQIECAwLCA4B1egNcTIECAAAECBAj0Cgje\n" +
                "Xl+nEyBAgAABAgQIDAsI3uEFeD0BAgQIECBAgECvgODt9XU6AQIECBAgQIDAsIDgHV6A1xMgQIAA\n" +
                "AQIECPQKCN5eX6cTIECAAAECBAgMCwje4QV4PQECBAgQIECAQK+A4O31dToBAgQIECBAgMCwgOAd\n" +
                "XoDXEyBAgAABAgQI9AoI3l5fpxMgQIAAAQIECAwLCN7hBXg9AQIECBAgQIBAr4Dg7fV1OgECBAgQ\n" +
                "IECAwLCA4B1egNcTIECAAAECBAj0CgjeXl+nEyBAgAABAgQIDAsI3uEFeD0BAgQIECBAgECvgODt\n" +
                "9XU6AQIECBAgQIDAsIDgHV6A1xMgQIAAAQIECPQKCN5eX6cTIECAAAECBAgMCwje4QV4PQECBAgQ\n" +
                "IECAQK+A4O31dToBAgQIECBAgMCwgOAdXoDXEyBAgAABAgQI9AoI3l5fpxMgQIAAAQIECAwLCN7h\n" +
                "BXg9AQIECBAgQIBAr4Dg7fV1OgECBAgQIECAwLCA4B1egNcTIECAAAECBAj0CgjeXl+nEyBAgAAB\n" +
                "AgQIDAsI3uEFeD0BAgQIECBAgECvgODt9XU6AQIECBAgQIDAsIDgHV6A1xMgQIAAAQIECPQKCN5e\n" +
                "X6cTIECAAAECBAgMCwje4QV4PQECBAgQIECAQK+A4O31dToBAgQIECBAgMCwwH8ANfB82+VBAI8A\n" +
                "AAAASUVORK5CYII=";

        String feet64 = "iVBORw0KGgoAAAANSUhEUgAAArwAAAK8CAYAAAANumxDAAAgAElEQVR4Xu3YQYpsNxREQXv/i7bp\n" +
                "kQdtUIkSSPf88Pjx0Y3MahL//Zf/CBAgQIAAAQIECIQF/g7f5jQCBAgQIECAAAECfxm8SkCAAAEC\n" +
                "BAgQIJAWMHjT8TqOAAECBAgQIEDA4NUBAgQIECBAgACBtIDBm47XcQQIECBAgAABAgavDhAgQIAA\n" +
                "AQIECKQFDN50vI4jQIAAAQIECBAweHWAAAECBAgQIEAgLWDwpuN1HAECBAgQIECAgMGrAwQIECBA\n" +
                "gAABAmkBgzcdr+MIECBAgAABAgQMXh0gQIAAAQIECBBICxi86XgdR4AAAQIECBAgYPDqAAECBAgQ\n" +
                "IECAQFrA4E3H6zgCBAgQIECAAAGDVwcIECBAgAABAgTSAgZvOl7HESBAgAABAgQIGLw6QIAAAQIE\n" +
                "CBAgkBYweNPxOo4AAQIECBAgQMDg1QECBAgQIECAAIG0gMGbjtdxBAgQIECAAAECBq8OECBAgAAB\n" +
                "AgQIpAUM3nS8jiNAgAABAgQIEDB4dYAAAQIECBAgQCAtYPCm43UcAQIECBAgQICAwasDBAgQIECA\n" +
                "AAECaQGDNx2v4wgQIECAAAECBAxeHSBAgAABAgQIEEgLGLzpeB1HgAABAgQIECBg8OoAAQIECBAg\n" +
                "QIBAWsDgTcfrOAIECBAgQIAAAYNXBwgQIECAAAECBNICBm86XscRIECAAAECBAgYvDpAgAABAgQI\n" +
                "ECCQFjB40/E6jgABAgQIECBAwODVAQIECBAgQIAAgbSAwZuO13EECBAgQIAAAQIGrw4QIECAAAEC\n" +
                "BAikBQzedLyOI0CAAAECBAgQMHh1gAABAgQIECBAIC1g8KbjdRwBAgQIECBAgIDBqwMECBAgQIAA\n" +
                "AQJpAYM3Ha/jCBAgQIAAAQIEDF4dIECAAAECBAgQSAsYvOl4HUeAAAECBAgQIGDw6gABAgQIECBA\n" +
                "gEBawOBNx+s4AgQIECBAgAABg1cHCBAgQIAAAQIE0gIGbzpexxEgQIAAAQIECBi8OkCAAAECBAgQ\n" +
                "IJAWMHjT8TqOAAECBAgQIEDA4NUBAgQIECBAgACBtIDBm47XcQQIECBAgAABAgavDhAgQIAAAQIE\n" +
                "CKQFDN50vI4jQIAAAQIECBAweHWAAAECBAgQIEAgLWDwpuN1HAECBAgQIECAgMGrAwQIECBAgAAB\n" +
                "AmkBgzcdr+MIECBAgAABAgQMXh0gQIAAAQIECBBICxi86XgdR4AAAQIECBAgYPDqAAECBAgQIECA\n" +
                "QFrA4E3H6zgCBAgQIECAAAGDVwcIECBAgAABAgTSAgZvOl7HESBAgAABAgQIGLw6QIAAAQIECBAg\n" +
                "kBYweNPxOo4AAQIECBAgQMDg1QECBAgQIECAAIG0gMGbjtdxBAgQIECAAAECBq8OECBAgAABAgQI\n" +
                "pAUM3nS8jiNAgAABAgQIEDB4dYAAAQIECBAgQCAtYPCm43UcAQIECBAgQICAwasDBAgQIECAAAEC\n" +
                "aQGDNx2v4wgQIECAAAECBAxeHSBAgAABAgQIEEgLGLzpeB1HgAABAgQIECBg8OoAAQIECBAgQIBA\n" +
                "WsDgTcfrOAIECBAgQIAAAYNXBwgQIECAAAECBNICBm86XscRIECAAAECBAgYvDpAgAABAgQIECCQ\n" +
                "FjB40/E6jgABAgQIECBAwODVAQIECBAgQIAAgbSAwZuO13EECBAgQIAAAQIGrw4QIECAAAECBAik\n" +
                "BQzedLyOI0CAAAECBAgQMHh1gAABAgQIECBAIC1g8KbjdRwBAgQIECBAgIDBqwMECBAgQIAAAQJp\n" +
                "AYM3Ha/jCBAgQIAAAQIEDF4dIECAAAECBAgQSAsYvOl4HUeAAAECBAgQIGDw6gABAgQIECBAgEBa\n" +
                "wOBNx+s4AgQIECBAgAABg1cHCBAgQIAAAQIE0gIGbzpexxEgQIAAAQIECBi8OkCAAAECBAgQIJAW\n" +
                "MHjT8TqOAAECBAgQIEDA4NUBAgQIECBAgACBtIDBm47XcQQIECBAgAABAgavDhAgQIAAAQIECKQF\n" +
                "DN50vI4jQIAAAQIECBAweHWAAAECBAgQIEAgLWDwpuN1HAECBAgQIECAgMGrAwQIECBAgAABAmkB\n" +
                "gzcdr+MIECBAgAABAgQMXh0gQIAAAQIECBBICxi86XgdR4AAAQIECBAgYPDqAAECBAgQIECAQFrA\n" +
                "4E3H6zgCBAgQIECAAAGDVwcIECBAgAABAgTSAgZvOl7HESBAgAABAgQIGLw6QIAAAQIECBAgkBYw\n" +
                "eNPxOo4AAQIECBAgQMDg1QECBAgQIECAAIG0gMGbjtdxBAgQIECAAAECBq8OECBAgAABAgQIpAUM\n" +
                "3nS8jiNAgAABAgQIEDB4dYAAAQIECBAgQCAtYPCm43UcAQIECBAgQICAwasDBAgQIECAAAECaQGD\n" +
                "Nx2v4wgQIECAAAECBAxeHSBAgAABAgQIEEgLGLzpeB1HgAABAgQIECBg8OoAAQIECBAgQIBAWsDg\n" +
                "TcfrOAIECBAgQIAAAYNXBwgQIECAAAECBNICBm86XscRIECAAAECBAgYvDpAgAABAgQIECCQFjB4\n" +
                "0/E6jgABAgQIECBAwODVAQIECBAgQIAAgbSAwZuO13EECBAgQIAAAQIGrw4QIECAAAECBAikBQze\n" +
                "dLyOI0CAAAECBAgQMHh1gAABAgQIECBAIC1g8KbjdRwBAgQIECBAgIDBqwMECBAgQIAAAQJpAYM3\n" +
                "Ha/jCBAgQIAAAQIEDF4dIECAAAECBAgQSAsYvOl4HUeAAAECBAgQIGDw6gABAgQIECBAgEBawOBN\n" +
                "x+s4AgQIECBAgAABg1cHCBAgQIAAAQIE0gIGbzpexxEgQIAAAQIECBi8OkCAAAECBAgQIJAWMHjT\n" +
                "8TqOAAECBAgQIEDA4NUBAgQIECBAgACBtIDBm47XcQQIECBAgAABAgavDhAgQIAAAQIECKQFDN50\n" +
                "vI4jQIAAAQIECBAweHWAAAECBAgQIEAgLWDwpuN1HAECBAgQIECAgMGrAwQIECBAgAABAmkBgzcd\n" +
                "r+MIECBAgAABAgQMXh0gQIAAAQIECBBICxi86XgdR4AAAQIECBAgYPDqAAECBAgQIECAQFrA4E3H\n" +
                "6zgCBAgQIECAAAGDVwcIECBAgAABAgTSAgZvOl7HESBAgAABAgQIGLw6QIAAAQIECBAgkBYweNPx\n" +
                "Oo4AAQIECBAgQMDg1QECBAgQIECAAIG0gMGbjtdxBAgQIECAAAECBq8OECBAgAABAgQIpAUM3nS8\n" +
                "jiNAgAABAgQIEDB4dYAAAQIECBAgQCAtYPCm43UcAQIECBAgQICAwasDBAgQIECAAAECaQGDNx2v\n" +
                "4wgQIECAAAECBAxeHSBAgAABAgQIEEgLGLzpeB1HgAABAgQIECBg8OoAAQIECBAgQIBAWsDgTcfr\n" +
                "OAIECBAgQIAAAYNXBwgQIECAAAECBNICBm86XscRIECAAAECBAgYvDpAgAABAgQIECCQFjB40/E6\n" +
                "jgABAgQIECBAwODVAQIECBAgQIAAgbSAwZuO13EECBAgQIAAAQIGrw4QIECAAAECBAikBQzedLyO\n" +
                "I0CAAAECBAgQMHh1gAABAgQIECBAIC1g8KbjdRwBAgQIECBAgIDBqwMECBAgQIAAAQJpAYM3Ha/j\n" +
                "CBAgQIAAAQIEDF4dIECAAAECBAgQSAsYvOl4HUeAAAECBAgQIGDw6gABAgQIECBAgEBawOBNx+s4\n" +
                "AgQIECBAgAABg1cHCBAgQIAAAQIE0gIGbzpexxEgQIAAAQIECBi8OkCAAAECBAgQIJAWMHjT8TqO\n" +
                "AAECBAgQIEDA4NUBAgQIECBAgACBtIDBm47XcQQIECBAgAABAgavDhAgQIAAAQIECKQFDN50vI4j\n" +
                "QIAAAQIECBAweHWAAAECBAgQIEAgLWDwpuN1HAECBAgQIECAgMGrAwQIECBAgAABAmkBgzcdr+MI\n" +
                "ECBAgAABAgQMXh0gQIAAAQIECBBICxi86XgdR4AAAQIECBAgYPDqAAECBAgQIECAQFrA4E3H6zgC\n" +
                "BAgQIECAAAGDVwcIECBAgAABAgTSAgZvOl7HESBAgAABAgQIGLw6QIAAAQIECBAgkBYweNPxOo4A\n" +
                "AQIECBAgQMDg1QECBAgQIECAAIG0gMGbjtdxBAgQIECAAAECBq8OECBAgAABAgQIpAUM3nS8jiNA\n" +
                "gAABAgQIEDB4dYAAAQIECBAgQCAtYPCm43UcAQIECBAgQICAwasDBAgQIECAAAECaQGDNx2v4wgQ\n" +
                "IECAAAECBAxeHSBAgAABAgQIEEgLGLzpeB1HgAABAgQIECBg8OoAAQIECBAgQIBAWsDgTcfrOAIE\n" +
                "CBAgQIAAAYNXBwgQIECAAAECBNICBm86XscRIECAAAECBAgYvDpAgAABAgQIECCQFjB40/E6jgAB\n" +
                "AgQIECBAwODVAQIECBAgQIAAgbSAwZuO13EECBAgQIAAAQIGrw4QIECAAAECBAikBQzedLyOI0CA\n" +
                "AAECBAgQMHh1gAABAgQIECBAIC1g8KbjdRwBAgQIECBAgIDBqwMECBAgQIAAAQJpAYM3Ha/jCBAg\n" +
                "QIAAAQIEDF4dIECAAAECBAgQSAsYvOl4HUeAAAECBAgQIGDw6gABAgQIECBAgEBawOBNx+s4AgQI\n" +
                "ECBAgAABg1cHCBAgQIAAAQIE0gIGbzpexxEgQIAAAQIECBi8OkCAAAECBAgQIJAWMHjT8TqOAAEC\n" +
                "BAgQIEDA4NUBAgQIECBAgACBtIDBm47XcQQIECBAgAABAgavDhAgQIAAAQIECKQFDN50vI4jQIAA\n" +
                "AQIECBAweHWAAAECBAgQIEAgLWDwpuN1HAECBAgQIECAgMGrAwQIECBAgAABAmkBgzcdr+MIECBA\n" +
                "gAABAgQMXh0gQIAAAQIECBBICxi86XgdR4AAAQIECBAgYPDqAAECBAgQIECAQFrA4E3H6zgCBAgQ\n" +
                "IECAAAGDVwcIECBAgAABAgTSAgZvOl7HESBAgAABAgQIGLw6QIAAAQIECBAgkBYweNPxOo4AAQIE\n" +
                "CBAgQMDg1QECBAgQIECAAIG0gMGbjtdxBAgQIECAAAECBq8OECBAgAABAgQIpAUM3nS8jiNAgAAB\n" +
                "AgQIEDB4dYAAAQIECBAgQCAtYPCm43UcAQIECBAgQICAwasDBAgQIECAAAECaQGDNx2v4wgQIECA\n" +
                "AAECBAxeHSBAgAABAgQIEEgLGLzpeB1HgAABAgQIECBg8OoAAQIECBAgQIBAWsDgTcfrOAIECBAg\n" +
                "QIAAAYNXBwgQIECAAAECBNICBm86XscRIECAAAECBAgYvDpAgAABAgQIECCQFjB40/E6jgABAgQI\n" +
                "ECBAwODVAQIECBAgQIAAgbSAwZuO13EECBAgQIAAAQIGrw4QIECAAAECBAikBQzedLyOI0CAAAEC\n" +
                "BAgQMHh1gAABAgQIECBAIC1g8KbjdRwBAgQIECBAgIDBqwMECBAgQIAAAQJpAYM3Ha/jCBAgQIAA\n" +
                "AQIEDF4dIECAAAECBAgQSAsYvOl4HUeAAAECBAgQIGDw6gABAgQIECBAgEBawOBNx+s4AgQIECBA\n" +
                "gAABg1cHCBAgQIAAAQIE0gIGbzpexxEgQIAAAQIECBi8OkCAAAECBAgQIJAWMHjT8TqOAAECBAgQ\n" +
                "IEDA4NUBAgQIECBAgACBtIDBm47XcQQIECBAgAABAgavDhAgQIAAAQIECKQFDN50vI4jQIAAAQIE\n" +
                "CBAweHWAAAECBAgQIEAgLWDwpuN1HAECBAgQIECAgMGrAwQIECBAgAABAmkBgzcdr+MIECBAgAAB\n" +
                "AgQMXh0gQIAAAQIECBBICxi86XgdR4AAAQIECBAgYPDqAAECBAgQIECAQFrA4E3H6zgCBAgQIECA\n" +
                "AAGDVwcIECBAgAABAgTSAgZvOl7HESBAgAABAgQIGLw6QIAAAQIECBAgkBYweNPxOo4AAQIECBAg\n" +
                "QMDg1QECBAgQIECAAIG0gMGbjtdxBAgQIECAAAECBq8OECBAgAABAgQIpAUM3nS8jiNAgAABAgQI\n" +
                "EDB4dYAAAQIECBAgQCAtYPCm43UcAQIECBAgQICAwasDBAgQIECAAAECaQGDNx2v4wgQIECAAAEC\n" +
                "BAxeHSBAgAABAgQIEEgLGLzpeB1HgAABAgQIECBg8OoAAQIECBAgQIBAWsDgTcfrOAIECBAgQIAA\n" +
                "AYNXBwgQIECAAAECBNICBm86XscRIECAAAECBAgYvDpAgAABAgQIECCQFjB40/E6jgABAgQIECBA\n" +
                "wODVAQIECBAgQIAAgbSAwZuO13EECBAgQIAAAQIGrw4QIECAAAECBAikBQzedLyOI0CAAAECBAgQ\n" +
                "MHh1gAABAgQIECBAIC1g8KbjdRwBAgQIECBAgIDBqwMECBAgQIAAAQJpAYM3Ha/jCBAgQIAAAQIE\n" +
                "DF4dIECAAAECBAgQSAsYvOl4HUeAAAECBAgQIGDw6gABAgQIECBAgEBawOBNx+s4AgQIECBAgAAB\n" +
                "g1cHCBAgQIAAAQIE0gIGbzpexxEgQIAAAQIECBi8OkCAAAECBAgQIJAWMHjT8TqOAAECBAgQIEDA\n" +
                "4NUBAgQIECBAgACBtIDBm47XcQQIECBAgAABAgavDhAgQIAAAQIECKQFDN50vI4jQIAAAQIECBAw\n" +
                "eHWAAAECBAgQIEAgLWDwpuN1HAECBAgQIECAgMGrAwQIECBAgAABAmkBgzcdr+MIECBAgAABAgQM\n" +
                "Xh0gQIAAAQIECBBICxi86XgdR4AAAQIECBAgYPDqAAECBAgQIECAQFrA4E3H6zgCBAgQIECAAAGD\n" +
                "VwcIECBAgAABAgTSAgZvOl7HESBAgAABAgQIGLw6QIAAAQIECBAgkBYweNPxOo4AAQIECBAgQMDg\n" +
                "1QECBAgQIECAAIG0gMGbjtdxBAgQIECAAAECBq8OECBAgAABAgQIpAUM3nS8jiNAgAABAgQIEDB4\n" +
                "dYAAAQIECBAgQCAtYPCm43UcAQIECBAgQICAwasDBAgQIECAAAECaQGDNx2v4wgQIECAAAECBAxe\n" +
                "HSBAgAABAgQIEEgLGLzpeB1HgAABAgQIECBg8OoAAQIECBAgQIBAWsDgTcfrOAIECBAgQIAAAYNX\n" +
                "BwgQIECAAAECBNICBm86XscRIECAAAECBAgYvDpAgAABAgQIECCQFjB40/E6jgABAgQIECBAwODV\n" +
                "AQIECBAgQIAAgbSAwZuO13EECBAgQIAAAQIGrw4QIECAAAECBAikBQzedLyOI0CAAAECBAgQMHh1\n" +
                "gAABAgQIECBAIC1g8KbjdRwBAgQIECBAgIDBqwMECBAgQIAAAQJpAYM3Ha/jCBAgQIAAAQIEDF4d\n" +
                "IECAAAECBAgQSAsYvOl4HUeAAAECBAgQIGDw6gABAgQIECBAgEBawOBNx+s4AgQIECBAgAABg1cH\n" +
                "CBAgQIAAAQIE0gIGbzpexxEgQIAAAQIECBi8OkCAAAECBAgQIJAWMHjT8TqOAAECBAgQIEDA4NUB\n" +
                "AgQIECBAgACBtIDBm47XcQQIECBAgAABAgavDhAgQIAAAQIECKQFDN50vI4jQIAAAQIECBAweHWA\n" +
                "AAECBAgQIEAgLWDwpuN1HAECBAgQIECAgMGrAwQIECBAgAABAmkBgzcdr+MIECBAgAABAgQMXh0g\n" +
                "QIAAAQIECBBICxi86XgdR4AAAQIECBAgYPDqAAECBAgQIECAQFrA4E3H6zgCBAgQIECAAAGDVwcI\n" +
                "ECBAgAABAgTSAgZvOl7HESBAgAABAgQIGLw6QIAAAQIECBAgkBYweNPxOo4AAQIECBAgQMDg1QEC\n" +
                "BAgQIECAAIG0gMGbjtdxBAgQIECAAAECBq8OECBAgAABAgQIpAUM3nS8jiNAgAABAgQIEDB4dYAA\n" +
                "AQIECBAgQCAtYPCm43UcAQIECBAgQICAwasDBAgQIECAAAECaQGDNx2v4wgQIECAAAECBAxeHSBA\n" +
                "gAABAgQIEEgLGLzpeB1HgAABAgQIECBg8OoAAQIECBAgQIBAWsDgTcfrOAIECBAgQIAAAYNXBwgQ\n" +
                "IECAAAECBNICBm86XscRIECAAAECBAgYvDpAgAABAgQIECCQFjB40/E6jgABAgQIECBAwODVAQIE\n" +
                "CBAgQIAAgbSAwZuO13EECBAgQIAAAQIGrw4QIECAAAECBAikBQzedLyOI0CAAAECBAgQMHh1gAAB\n" +
                "AgQIECBAIC1g8KbjdRwBAgQIECBAgIDBqwMECBAgQIAAAQJpAYM3Ha/jCBAgQIAAAQIEDF4dIECA\n" +
                "AAECBAgQSAsYvOl4HUeAAAECBAgQIGDw6gABAgQIECBAgEBawOBNx+s4AgQIECBAgAABg1cHCBAg\n" +
                "QIAAAQIE0gIGbzpexxEgQIAAAQIECBi8OkCAAAECBAgQIJAWMHjT8TqOAAECBAgQIEDA4NUBAgQI\n" +
                "ECBAgACBtIDBm47XcQQIECBAgAABAgavDhAgQIAAAQIECKQFDN50vI4jQIAAAQIECBAweHWAAAEC\n" +
                "BAgQIEAgLWDwpuN1HAECBAgQIECAgMGrAwQIECBAgAABAmkBgzcdr+MIECBAgAABAgQMXh0gQIAA\n" +
                "AQIECBBICxi86XgdR4AAAQIECBAgYPDqAAECBAgQIECAQFrA4E3H6zgCBAgQIECAAAGDVwcIECBA\n" +
                "gAABAgTSAgZvOl7HESBAgAABAgQIGLw6QIAAAQIECBAgkBYweNPxOo4AAQIECBAgQMDg1QECBAgQ\n" +
                "IECAAIG0gMGbjtdxBAgQIECAAAECBq8OECBAgAABAgQIpAUM3nS8jiNAgAABAgQIEDB4dYAAAQIE\n" +
                "CBAgQCAtYPCm43UcAQIECBAgQICAwasDBAgQIECAAAECaQGDNx2v4wgQIECAAAECBAxeHSBAgAAB\n" +
                "AgQIEEgLGLzpeB1HgAABAgQIECBg8OoAAQIECBAgQIBAWsDgTcfrOAIECBAgQIAAAYNXBwgQIECA\n" +
                "AAECBNICBm86XscRIECAAAECBAgYvDpAgAABAgQIECCQFjB40/E6jgABAgQIECBAwODVAQIECBAg\n" +
                "QIAAgbSAwZuO13EECBAgQIAAAQIGrw4QIECAAAECBAikBQzedLyOI0CAAAECBAgQMHh1gAABAgQI\n" +
                "ECBAIC1g8KbjdRwBAgQIECBAgIDBqwMECBAgQIAAAQJpAYM3Ha/jCBAgQIAAAQIEDF4dIECAAAEC\n" +
                "BAgQSAsYvOl4HUeAAAECBAgQIGDw6gABAgQIECBAgEBawOBNx+s4AgQIECBAgAABg1cHCBAgQIAA\n" +
                "AQIE0gIGbzpexxEgQIAAAQIECBi8OkCAAAECBAgQIJAWMHjT8TqOAAECBAgQIEDA4NUBAgQIECBA\n" +
                "gACBtIDBm47XcQQIECBAgAABAgavDhAgQIAAAQIECKQFDN50vI4jQIAAAQIECBAweHWAAAECBAgQ\n" +
                "IEAgLWDwpuN1HAECBAgQIECAgMGrAwQIECBAgAABAmkBgzcdr+MIECBAgAABAgQMXh0gQIAAAQIE\n" +
                "CBBICxi86XgdR4AAAQIECBAgYPDqAAECBAgQIECAQFrA4E3H6zgCBAgQIECAAAGDVwcIECBAgAAB\n" +
                "AgTSAgZvOl7HESBAgAABAgQIGLw6QIAAAQIECBAgkBYweNPxOo4AAQIECBAgQMDg1QECBAgQIECA\n" +
                "AIG0gMGbjtdxBAgQIECAAAECBq8OECBAgAABAgQIpAUM3nS8jiNAgAABAgQIEDB4dYAAAQIECBAg\n" +
                "QCAtYPCm43UcAQIECBAgQICAwasDBAgQIECAAAECaQGDNx2v4wgQIECAAAECBAxeHSBAgAABAgQI\n" +
                "EEgLGLzpeB1HgAABAgQIECBg8OoAAQIECBAgQIBAWsDgTcfrOAIECBAgQIAAAYNXBwgQIECAAAEC\n" +
                "BNICBm86XscRIECAAAECBAgYvDpAgAABAgQIECCQFjB40/E6jgABAgQIECBAwODVAQIECBAgQIAA\n" +
                "gbSAwZuO13EECBAgQIAAAQIGrw4QIECAAAECBAikBQzedLyOI0CAAAECBAgQMHh1gAABAgQIECBA\n" +
                "IC1g8KbjdRwBAgQIECBAgIDBqwMECBAgQIAAAQJpAYM3Ha/jCBAgQIAAAQIEDF4dIECAAAECBAgQ\n" +
                "SAsYvOl4HUeAAAECBAgQIGDw6gABAgQIECBAgEBawOBNx+s4AgQIECBAgAABg1cHCBAgQIAAAQIE\n" +
                "0gIGbzpexxEgQIAAAQIECBi8OkCAAAECBAgQIJAWMHjT8TqOAAECBAgQIEDA4NUBAgQIECBAgACB\n" +
                "tIDBm47XcQQIECBAgAABAgavDhAgQIAAAQIECKQFDN50vI4jQIAAAQIECBAweHWAAAECBAgQIEAg\n" +
                "LWDwpuN1HAECBAgQIECAgMGrAwQIECBAgAABAmkBgzcdr+MIECBAgAABAgQMXh0gQIAAAQIECBBI\n" +
                "Cxi86XgdR4AAAQIECBAgYPDqAAECBAgQIECAQFrA4E3H6zgCBAgQIECAAAGDVwcIECBAgAABAgTS\n" +
                "AgZvOl7HESBAgAABAgQIGLw6QIAAAQIECBAgkBYweNPxOo4AAQIECBAgQMDg1QECBAgQIECAAIG0\n" +
                "gMGbjtdxBAgQIECAAAECBq8OECBAgAABAgQIpAUM3nS8jiNAgAABAgQIEDB4dYAAAQIECBAgQCAt\n" +
                "YPCm43UcAQIECBAgQICAwasDBAgQIECAAAECaQGDNx2v4wgQIECAAAECBAxeHSBAgAABAgQIEEgL\n" +
                "GLzpeB1HgAABAgQIECBg8OoAAQIECBAgQIBAWsDgTcfrOAIECBAgQIAAAYNXBwgQIECAAAECBNIC\n" +
                "Bm86XscRIECAAAECBAgYvDpAgAABAgQIECCQFjB40/E6jgABAgQIECBAwODVAQIECBAgQIAAgbSA\n" +
                "wZuO13EECBAgQIAAAQIGrw4QIECAAAECBAikBQzedLyOI0CAAAECBAgQMHh1gAABAgQIECBAIC1g\n" +
                "8KbjdRwBAgQIECBAgIDBqwMECBAgQIAAAQJpAYM3Ha/jCBAgQIAAAQIEDF4dIECAAAECBAgQSAsY\n" +
                "vOl4HUeAAAECBAgQIGDw6gABAgQIECBAgDep3+AAAArnSURBVEBawOBNx/vHHffPH3fxn3Pwzb9V\n" +
                "etXu2c1utWVdR+AhAT/0h8LwlK8FDJOvCZ/9B27+rdKrZ2tx5GE3u3XkAP8IAQJrAT/0tZEv5ggY\n" +
                "JnOy2n3pzb9VerWb1qzvb3ZrlpTXEhgs4Ic+ODxP/yVgmHRLcfNvlV51e/Vz2c1utWVdR+AhAT/0\n" +
                "h8LwlK8FDJOvCZ/9B27+rdKrZ2tx5GE3u3XkAP8IAQJrAT/0tZEv5ggYJnOy2n3pzb9VerWb1qzv\n" +
                "b3ZrlpTXEhgs4Ic+ODxP/yVgmHRLcfNvlV51e/Vz2c1utWVdR+AhAT/0h8LwlK8FDJOvCZ/9B27+\n" +
                "rdKrZ2tx5GE3u3XkAP8IAQJrAT/0tZEv5ggYJnOy2n3pzb9VerWb1qzvb3ZrlpTXEhgs4Ic+ODxP\n" +
                "/yVgmHRLcfNvlV51e/Vz2c1utWVdR+AhAT/0h8LwlK8FDJOvCZ/9B27+rdKrZ2tx5GE3u3XkAP8I\n" +
                "AQJrAT/0tZEv5ggYJnOy2n3pzb9VerWb1qzvb3ZrlpTXEhgs4Ic+ODxP/yVgmHxfipN/E07mcfJd\n" +
                "u0qv3nHyXbsmpe9vdqvk6BYCTwv4oT8dj8dtChgAm2D/8/nJvwkn8zj5rl2lV+84+a5dk9L3N7tV\n" +
                "cnQLgacF/NCfjsfjNgUMgE0wg/cjsJO9Ovk39+S7PoKIfnQykyiRswjMF/BDn5+hC/4TMAC+b8PJ\n" +
                "vwkn8zj5rl2lV+84+a5dk9L3N7tVcnQLgacF/NCfjsfjNgUMgE0w/4f3I7CTvTr5N/fkuz6CiH50\n" +
                "MpMokbMIzBfwQ5+foQv8H96THTj5N+HkIDv5rl2vV+84+a5dk9L3N7tVcnQLgacF/NCfjsfjNgUM\n" +
                "gE0w/4f3I7CTvTr5N/fkuz6CiH50MpMokbMIzBfwQ5+foQv8H96THTj5N+HkIDv5rl2vV+84+a5d\n" +
                "k9L3N7tVcnQLgacF/NCfjsfjNgUMgE0w/4f3I7CTvTr5N/fkuz6CiH50MpMokbMIzBfwQ5+foQv8\n" +
                "H96THTj5N+HkIDv5rl2vV+84+a5dk9L3N7tVcnQLgacF/NCfjsfjNgUMgE0w/4f3I7CTvTr5N/fk\n" +
                "uz6CiH50MpMokbMIzBfwQ5+foQv8H96THTj5N+HkIDv5rl2vV+84+a5dk9L3N7tVcnQLgacF/NCf\n" +
                "jsfjNgUMgE0w/4f3I7CTvTr5N/fkuz6CiH50MpMokbMIzBfwQ5+foQv8H96THTj5N+HkIDv5rl2v\n" +
                "V+84+a5dk9L3N7tVcnQLgacF/NCfjsfjNgUMgE0w/4f3I7CTvTr5N/fkuz6CiH50MpMokbMIzBfw\n" +
                "Q5+foQv8H94/oQM3/1YZlu2G3exWW9Z1BB4S8EN/KAxP+VrAMPma8Nl/4ObfKr16thZHHnazW0cO\n" +
                "8I8QILAW8ENfG/lijoBhMier3Zfe/FulV7tpzfr+ZrdmSXktgcECfuiDw/P0XwKGSbcUN/9W6VW3\n" +
                "Vz+X3exWW9Z1BB4S8EN/KAxP+VrAMPma8Nl/4ObfKr16thZHHnazW0cO8I8QILAW8ENfG/lijoBh\n" +
                "Mier3Zfe/FulV7tpzfr+ZrdmSXktgcECfuiDw/P0XwKGSbcUN/9W6VW3Vz+X3exWW9Z1BB4S8EN/\n" +
                "KAxP+VrAMPma8Nl/4ObfKr16thZHHnazW0cO8I8QILAW8ENfG/lijoBhMier3Zfe/FulV7tpzfr+\n" +
                "ZrdmSXktgcECfuiDw/P0XwKGSbcUN/9W6VW3Vz+X3exWW9Z1BB4S8EN/KAxP+VrAMPma8Nl/4Obf\n" +
                "Kr16thZHHnazW0cO8I8QILAW8ENfG/lijoBhMier3Zfe/FulV7tpzfr+ZrdmSXktgcECfuiDw/N0\n" +
                "AgQIECBAgACBtYDBuzbyBQECBAgQIECAwGABg3dweJ5OgAABAgQIECCwFjB410a+IECAAAECBAgQ\n" +
                "GCxg8A4Oz9MJECBAgAABAgTWAgbv2sgXBAgQIECAAAECgwUM3sHheToBAgQIECBAgMBawOBdG/mC\n" +
                "AAECBAgQIEBgsIDBOzg8TydAgAABAgQIEFgLGLxrI18QIECAAAECBAgMFjB4B4fn6QQIECBAgAAB\n" +
                "AmsBg3dt5AsCBAgQIECAAIHBAgbv4PA8nQABAgQIECBAYC1g8K6NfEGAAAECBAgQIDBYwOAdHJ6n\n" +
                "EyBAgAABAgQIrAUM3rWRLwgQIECAAAECBAYLGLyDw/N0AgQIECBAgACBtYDBuzbyBQECBAgQIECA\n" +
                "wGABg3dweJ5OgAABAgQIECCwFjB410a+IECAAAECBAgQGCxg8A4Oz9MJECBAgAABAgTWAgbv2sgX\n" +
                "BAgQIECAAAECgwUM3sHheToBAgQIECBAgMBawOBdG/mCAAECBAgQIEBgsIDBOzg8TydAgAABAgQI\n" +
                "EFgLGLxrI18QIECAAAECBAgMFjB4B4fn6QQIECBAgAABAmsBg3dt5AsCBAgQIECAAIHBAgbv4PA8\n" +
                "nQABAgQIECBAYC1g8K6NfEGAAAECBAgQIDBYwOAdHJ6nEyBAgAABAgQIrAUM3rWRLwgQIECAAAEC\n" +
                "BAYLGLyDw/N0AgQIECBAgACBtYDBuzbyBQECBAgQIECAwGABg3dweJ5OgAABAgQIECCwFjB410a+\n" +
                "IECAAAECBAgQGCxg8A4Oz9MJECBAgAABAgTWAgbv2sgXBAgQIECAAAECgwUM3sHheToBAgQIECBA\n" +
                "gMBawOBdG/mCAAECBAgQIEBgsIDBOzg8TydAgAABAgQIEFgLGLxrI18QIECAAAECBAgMFjB4B4fn\n" +
                "6QQIECBAgAABAmsBg3dt5AsCBAgQIECAAIHBAgbv4PA8nQABAgQIECBAYC1g8K6NfEGAAAECBAgQ\n" +
                "IDBYwOAdHJ6nEyBAgAABAgQIrAUM3rWRLwgQIECAAAECBAYLGLyDw/N0AgQIECBAgACBtYDBuzby\n" +
                "BQECBAgQIECAwGABg3dweJ5OgAABAgQIECCwFjB410a+IECAAAECBAgQGCxg8A4Oz9MJECBAgAAB\n" +
                "AgTWAgbv2sgXBAgQIECAAAECgwUM3sHheToBAgQIECBAgMBawOBdG/mCAAECBAgQIEBgsIDBOzg8\n" +
                "TydAgAABAgQIEFgLGLxrI18QIECAAAECBAgMFjB4B4fn6QQIECBAgAABAmsBg3dt5AsCBAgQIECA\n" +
                "AIHBAgbv4PA8nQABAgQIECBAYC1g8K6NfEGAAAECBAgQIDBYwOAdHJ6nEyBAgAABAgQIrAUM3rWR\n" +
                "LwgQIECAAAECBAYLGLyDw/N0AgQIECBAgACBtYDBuzbyBQECBAgQIECAwGABg3dweJ5OgAABAgQI\n" +
                "ECCwFjB410a+IECAAAECBAgQGCxg8A4Oz9MJECBAgAABAgTWAgbv2sgXBAgQIECAAAECgwUM3sHh\n" +
                "eToBAgQIECBAgMBawOBdG/mCAAECBAgQIEBgsIDBOzg8TydAgAABAgQIEFgLGLxrI18QIECAAAEC\n" +
                "BAgMFjB4B4fn6QQIECBAgAABAmsBg3dt5AsCBAgQIECAAIHBAgbv4PA8nQABAgQIECBAYC1g8K6N\n" +
                "fEGAAAECBAgQIDBYwOAdHJ6nEyBAgAABAgQIrAUM3rWRLwgQIECAAAECBAYLGLyDw/N0AgQIECBA\n" +
                "gACBtYDBuzbyBQECBAgQIECAwGABg3dweJ5OgAABAgQIECCwFjB410a+IECAAAECBAgQGCxg8A4O\n" +
                "z9MJECBAgAABAgTWAgbv2sgXBAgQIECAAAECgwX+BSZpbr01gtToAAAAAElFTkSuQmCC";

        contentValues.put("Email", Email);
        contentValues.put("Head", head64);
        contentValues.put("torso", torso64);
        contentValues.put("trousers", trousers64);
        contentValues.put("feet", feet64);

        long checkIfQueryRuns = db.insert("Avatar", null, contentValues);
//        if (checkIfQueryRuns != 0){
//           // Toast.makeText(context)
//        }
        //        SQLiteStatement statement = db.compileStatement(sql);S
//        statement.clearBindings();
//
//        statement.bindString(1,Email);
//        statement.bindBlob(2,head);
//
//        statement.executeInsert();

        //File head = new File("C:capaa\\src\\main\\res\\drawable\\head2.png");
        //"drawable/head2.png"
//        String HeadUri = "drawable://" + R.drawable.head2;
//        //File head = new File("C:\\Users\\Greg\\Desktop\\CAPAA\\CAPAA\\Dissertation\\capaa\\src\\main\\res\\drawable\\head2.png");
//        File head = new File(HeadUri);
//        try {
//            contentValues.put("Email", Email);
//            contentValues.put("Head",read(head));
//        }catch (IOException e){ }
//
//        long ins = db.insert("Avatar", null, contentValues);
//        return true;
    }

    public String getHeadFromDb(String Email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Head FROM Avatar WHERE Email=?", new String[]{Email});
        if (cursor.moveToLast()) {
            Head64String = cursor.getString(0);
        }
        // String imageDataBytes = Head64String.substring(Head64String.indexOf(",")+1);
        //InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));
        //Bitmap bitmap = BitmapFactory.decodeStream(stream);
        // ImageView IV
        return Head64String;
    }

    public String getTorsoFromDb(String Email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Torso FROM Avatar WHERE Email=?", new String[]{Email});
        if (cursor.moveToLast()) {
            Torso64String = cursor.getString(0);
        }
        return Torso64String;
    }

    public String getTrousersFromDb(String Email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Trousers FROM Avatar WHERE Email=?", new String[]{Email});
        if (cursor.moveToLast()) {
            Trousers64String = cursor.getString(0);
        }
        return Trousers64String;
    }

    public String getFeetFromDb(String Email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Feet FROM Avatar WHERE Email=?", new String[]{Email});
        if (cursor.moveToLast()) {
            Feet64String = cursor.getString(0);
        }
        return Feet64String;
    }

    public void updateTorso(String Email, int TorsoNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put("Email", Email);

        if (TorsoNumber == 2) {
            String torsoGreen64 = "iVBORw0KGgoAAAANSUhEUgAAArwAAAK8CAYAAAANumxDAAAgAElEQVR4Xu3dQZYlx3Es0Mb+MIAG\n" +
                    "XAfXwnVwAA20P+gAxAGaDbGVWeXhEeF2//hVZPo1z5eGd1r8P3zx/wgQIECAAAECBAgMFvhh8GxG\n" +
                    "I0CAAAECBAgQIPBF4bUEBAgQIECAAAECowUU3tHxGo4AAQIECBAgQEDhtQMECBAgQIAAAQKjBRTe\n" +
                    "0fEajgABAgQIECBAQOG1AwQIECBAgAABAqMFFN7R8RqOAAECBAgQIEBA4bUDBAgQIECAAAECowUU\n" +
                    "3tHxGo4AAQIECBAgQEDhtQMECBAgQIAAAQKjBRTe0fEajgABAgQIECBAQOG1AwQIECBAgAABAqMF\n" +
                    "FN7R8RqOAAECBAgQIEBA4bUDBAgQIECAAAECowUU3tHxGo4AAQIECBAgQEDhtQMECBAgQIAAAQKj\n" +
                    "BRTe0fEajgABAgQIECBAQOG1AwQIECBAgAABAqMFFN7R8RqOAAECBAgQIEBA4bUDBAgQIECAAAEC\n" +
                    "owUU3tHxGo4AAQIECBAgQEDhtQMECBAgQIAAAQKjBRTe0fEajgABAgQIECBAQOG1AwQIECBAgAAB\n" +
                    "AqMFFN7R8RqOAAECBAgQIEBA4bUDBAgQIECAAAECowUU3tHxGo4AAQIECBAgQEDhtQMECBAgQIAA\n" +
                    "AQKjBRTe0fEajgABAgQIECBAQOG1AwQIECBAgAABAqMFFN7R8RqOAAECBAgQIEBA4bUDBAgQIECA\n" +
                    "AAECowUU3tHxGo4AAQIECBAgQEDhtQMECBAgQIAAAQKjBRTe0fEajgABAgQIECBAQOG1AwQIECBA\n" +
                    "gAABAqMFFN7R8RqOAAECBAgQIEBA4bUDBAgQIECAAAECowUU3tHxGo4AAQIECBAgQEDhtQMECBAg\n" +
                    "QIAAAQKjBRTe0fEajgABAgQIECBAQOG1AwQIECBAgAABAqMFFN7R8RqOAAECBAgQIEBA4bUDBAgQ\n" +
                    "IECAAAECowUU3tHxGo4AAQIECBAgQEDhtQMECBAgQIAAAQKjBRTe0fEajgABAgQIECBAQOG1AwQI\n" +
                    "ECBAgAABAqMFFN7R8RqOAAECBAgQIEBA4bUDBAgQIECAAAECowUU3tHxGo4AAQIECBAgQEDhtQME\n" +
                    "CBAgQIAAAQKjBRTe0fEajgABAgQIECBAQOG1AwQIECBAgAABAqMFFN7R8RqOAAECBAgQIEBA4bUD\n" +
                    "BAgQIECAAAECowUU3tHxGo4AAQIECBAgQEDhtQMECBAgQIAAAQKjBRTe0fEajgABAgQIECBAQOG1\n" +
                    "AwQIECBAgAABAqMFFN7R8RqOAAECBAgQIEBA4bUDBAgQIECAAAECowUU3tHxGo4AAQIECBAgQEDh\n" +
                    "tQMECBAgQIAAAQKjBRTe0fEajgABAgQIECBAQOG1AwQIECBAgAABAqMFFN7R8RqOAAECBAgQIEBA\n" +
                    "4bUDBAgQIECAAAECowUU3tHxGo4AAQIECBAgQEDhtQMECBAgQIAAAQKjBRTe0fEajgABAgQIECBA\n" +
                    "QOG1AwQIECBAgAABAqMFFN7R8RqOAAECBAgQIEBA4bUDBAgQIECAAAECowUU3tHxGo4AAQIECBAg\n" +
                    "QEDhtQMECBAgQIAAAQKjBRTe0fEajgABAgQIECBAQOG1AwQIECBAgAABAqMFFN7R8RqOAAECBAgQ\n" +
                    "IEBA4bUDBAgQIECAAAECowUU3tHxGo4AAQIECBAgQEDhtQMECBAgQIAAAQKjBRTe0fEajgABAgQI\n" +
                    "ECBAQOG1AwQIECBAgAABAqMFFN7R8RqOAAECBAgQIEBA4bUDBAgQIECAAAECowUU3tHxGo4AAQIE\n" +
                    "CBAgQEDhtQMECBAgQIAAAQKjBRTe0fEajgABAgQIECBAQOG1AwQIECBAgAABAqMFFN7R8RqOAAEC\n" +
                    "BAgQIEBA4bUDBAgQIECAAAECowUU3tHxGo4AAQIECBAgQEDhtQMECBAgQIAAAQKjBRTe0fEajgAB\n" +
                    "AgQIECBAQOG1AwQIECBAgAABAqMFFN7R8RqOAAECBAgQIEBA4bUDBAgQIECAAAECowUU3tHxGo4A\n" +
                    "AQIECBAgQEDhtQMECBAgQIAAAQKjBRTe0fEajgABAgQIECBAQOG1AwQIECBAgAABAqMFFN7R8RqO\n" +
                    "AAECBAgQIEBA4bUDBAgQIECAAAECowUU3tHxGo4AAQIECBAgQEDhtQMECBAgQIAAAQKjBRTe0fEa\n" +
                    "jgABAgQIECBAQOG1AwQIECBAgAABAqMFFN7R8RqOAAECBAgQIEBA4bUDBAgQIECAAAECowUU3tHx\n" +
                    "Go4AAQIECBAgQEDhtQMECBAgQIAAAQKjBRTe0fEajgABAgQIECBAQOG1AwQIECBAgAABAqMFFN7R\n" +
                    "8RqOAAECBAgQIEBA4bUDBAgQIECAAAECowUU3tHxGo4AAQIECBAgQEDhtQMECBAgQIAAAQKjBRTe\n" +
                    "0fEajgABAgQIECBAQOG1AwQIECBAgAABAqMFFN7R8RqOAAECBAgQIEBA4bUDBAgQIECAAAECowUU\n" +
                    "3tHxGo4AAQIECBAgQEDhtQMECBAgQIAAAQKjBRTe0fEajgABAgQIECBAQOG1AwQIECBAgAABAqMF\n" +
                    "FN7R8RqOAAECBAgQIEBA4bUDBAgQIECAAAECowUU3tHxGo4AAQIECBAgQEDhtQMECBAgQIAAAQKj\n" +
                    "BRTe0fEajgABAgQIECBAQOG1AwQIECBAgAABAqMFFN7R8RqOAAECBAgQIEBA4bUDBAgQIECAAAEC\n" +
                    "owUU3tHxGo4AAQIECBAgQEDhtQMECBAgQIAAAQKjBRTe0fEajgABAgQIECBAQOG1AwQIECBAgAAB\n" +
                    "AqMFFN7R8RqOAAECBAgQIEBA4bUDBAgQIECAAAECowUU3tHxGo4AAQIECBAgQEDhtQMECBAgQIAA\n" +
                    "AQKjBRTe0fEajgABAgQIECBAQOG1AwQIECBAgAABAqMFFN7R8RqOAAECBAgQIEBA4bUDBAgQIECA\n" +
                    "AAECowUU3tHxGo4AAQIECBAgQEDhtQMECBAgQIAAAQKjBRTe0fEajgABAgQIECBAQOG1AwQIECBA\n" +
                    "gAABAqMFFN7R8RqOAAECBAgQIEBA4bUDBAgQIECAAAECowUU3tHxGo4AAQIECBAgQEDhtQMECBAg\n" +
                    "QIAAAQKjBRTe0fEajgABAgQIECBAQOG1AwQIECBAgAABAqMFFN7R8RqOAAECBAgQIEBA4bUDBAgQ\n" +
                    "IECAAAECowUU3tHxGo4AAQIECBAgQEDhtQMECBAgQIAAAQKjBRTe0fEajgABAgQIECBAQOG1AwQI\n" +
                    "ECBAgAABAqMFFN7R8RqOAAECBAgQIEBA4bUDBAgQIECAAAECowUU3tHxGo4AAQIECBAgQEDhtQME\n" +
                    "CBAgQIAAAQKjBRTe0fEajgABAgQIECBAQOG1AwQIECBAgAABAqMFFN7R8RqOAAECBAgQIEBA4bUD\n" +
                    "BAgQIECAAAECowUU3tHxGo4AAQIECBAgQEDhtQMECBAgQIAAAQKjBRTe0fEajgABAgQIECBAQOG1\n" +
                    "AwQIECBAgAABAqMFFN7R8RqOAAECBAgQIEBA4bUDBAgQIECAAAECowUU3tHxGo4AAQIECBAgQEDh\n" +
                    "tQMECBAgQIAAAQKjBRTe0fEajgABAgQIECBAQOG1AwQIECBAgAABAqMFFN7R8RqOAAECBAgQIEBA\n" +
                    "4bUDBAgQIECAAAECowUU3tHxGo4AAQIECBAgQEDhtQMECBAgQIAAAQKjBRTe0fEajgABAgQIECBA\n" +
                    "QOG1AwQIECBAgAABAqMFFN7R8RqOAAECBAgQIEBA4bUDBAgQIECAAAECowUU3tHxGo4AAQIECBAg\n" +
                    "QEDhtQMECBAgQIAAAQKjBRTe0fEajgABAgQIECBAQOG1AwQIECBAgAABAqMFFN7R8RqOAAECBAgQ\n" +
                    "IEBA4bUDBAgQIECAAAECowUU3tHxGo4AAQIECBAgQEDhtQMECBAgQIAAAQKjBRTe0fEajgABAgQI\n" +
                    "ECBAQOG1AwQIECBAgAABAqMFFN7R8RqOAAECBAgQIEBA4bUDBAgQIECAAAECowUU3tHxGo4AAQIE\n" +
                    "CBAgQEDhtQMECBAgQIAAAQKjBRTe0fEajgABAgQIECBAQOG1AwQIECBAgAABAqMFFN7R8RqOAAEC\n" +
                    "BAgQIEBA4bUDBAgQIECAAAECowUU3tHxGo4AAQIECBAgQEDhtQMECBAgQIAAAQKjBRTe0fEajgAB\n" +
                    "AgQIECBAQOG1AwT+XeAXIAQIHC3gvXV0PG6OwJkCvjjOzMVd7RNQePfZuzKBJwLeW0+UfIYAgX8T\n" +
                    "8MVhIQj4hdcOELhJwHvrprTcK4FDBHxxHBKE2zhGwC+8x0ThRgj8nwLeWxaDAIHXAr44XpP5g+EC\n" +
                    "Cu/wgI13vYD31vURGoBAv4Avjn5zVzxbQOE9Ox93R8B7yw4QIPBawBfHazJ/MFxA4R0esPGuF/De\n" +
                    "uj5CAxDoF/DF0W/uimcLKLxn5+PuCHhv2QECBF4L+OJ4TeYPhgsovMMDNt71At5b10doAAL9Ar44\n" +
                    "+s1d8WwBhffsfNwdAe8tO0CAwGsBXxyvyfzBcAGFd3jAxrtewHvr+ggNQKBfwBdHv7krni2g8J6d\n" +
                    "j7sj4L1lBwgQeC3gi+M1mT8YLqDwDg/YeNcLeG9dH6EBCPQL+OLoN3fFswUU3rPzcXcEvLfsAAEC\n" +
                    "rwV8cbwm8wfDBRTe4QEb73oB763rIzQAgX4BXxz95q54toDCe3Y+7o6A95YdIEDgtYAvjtdk/mC4\n" +
                    "gMI7PGDjXS/gvXV9hAYg0C/gi6Pf3BXPFlB4z87H3RHw3rIDBAi8FvDF8ZrMHzQLKKDN4C5HgMD/\n" +
                    "KeB9aTEIXCzgAb44vJBbby+8P/78UwitMQncJfA///XfO2/Y+3KnvmsT+KSAB/iTgP58uYDCu5zY\n" +
                    "BQjcIaDw3pGTuyRwooDCe2Iq7ulrAYXXPhAg8JuAwmsRCBD4qIDC+1E5f9cloPB2SbsOgcMFFN7D\n" +
                    "A3J7BA4WUHgPDset/Sag8FoEAgT8wmsHCBD4lIDC+yk+f9wgoPA2ILsEgRsE/MJ7Q0rukcCZAgrv\n" +
                    "mbm4qz8FFF7bQICAX3jtAAECnxJQeD/F548bBBTeBmSXIHCDgF94b0jJPRI4U0DhPTMXd+UXXjtA\n" +
                    "gMA3AgqvlSBA4KMCCu9H5fxdl4BfeLukXYfA4QIK7+EBuT0CBwsovAeH49Z+E1B4LQIBAr8JKLwW\n" +
                    "gQCBjwoovB+V83ddAgpvl7TrEDhcQOE9PCC3R+BgAYX34HDcml947QABAn8KKLy2gQCBjwoovB+V\n" +
                    "83ddAn7h7ZJ2HQKHCyi8hwfk9ggcLKDwHhyOW/MLrx0gQMAvvHaAAIHPCyi8nzd0wloBv/Cu9XU6\n" +
                    "gWsE/MJ7TVRulMBxAgrvcZG4oW8EFF4rQYDAbwIKr0UgQOCjAgrvR+X8XZeAwtsl7ToEDhdQeA8P\n" +
                    "yO0ROFhA4T04HLf2m4DCaxEIEPALrx0gQOBTAgrvp/j8cYOAwtuA7BIEbhDwC+8NKblHAmcKKLxn\n" +
                    "5uKu/hRQeG0DAQJ+4bUDBAh8SkDh/RSfP24QUHgbkF2CwA0Cm3/hrSLy3q2SdA6BFwIevBdYPvpK\n" +
                    "oL2ovrq773z4x59/qjrKOQQIDBAoLtreuwN2wgj3CXjw7svsljsuK7wK6C2Ru08CMwUU3pm5mipL\n" +
                    "QOHNyrtzWoW3U9u1CBBYJqDwLqN1MIE2AYW3jTruQgpvXOQGJjBTQOGdmaupsgQU3qy8O6dVeDu1\n" +
                    "XYsAgWUCCu8yWgcTaBNQeNuo4y6k8MZFbmACMwUU3pm5mipLQOHNyrtzWoW3U9u1CBBYJqDwLqN1\n" +
                    "MIE2AYW3jTruQgpvXOQGJjBTQOGdmaupsgQU3qy8O6dVeDu1XYsAgWUCCu8yWgcTaBNQeNuo4y6k\n" +
                    "8MZFbmACMwUU3pm5mipLQOHNyrtzWoW3U9u1CBBYJqDwLqN1MIE2AYW3jTruQgpvXOQGJjBTQOGd\n" +
                    "maupsgQU3qy8O6dVeDu1XYsAgWUCCu8yWgcTaBNQeNuo4y6k8MZFbmACMwUU3pm5mipLQOHNyrtz\n" +
                    "WoW3U9u1CBBYJqDwLqN1MIE2AYW3jTruQgpvXOQGJjBTQOGdmaupsgQU3qy8O6dVeDu1XYsAgWUC\n" +
                    "Cu8yWgcTaBNQeNuo4y6k8MZFbmACMwUU3pm5mipLQOHNyrtzWoW3U9u1CBBYJqDwLqN1MIE2AYW3\n" +
                    "jTruQgpvXOQGJjBTQOGdmaupsgQU3qy8O6dVeDu1XYsAgWUCCu8yWgcTaBNQeNuo4y40uvAWvwCP\n" +
                    "WY4ff/7pmHtxI98XsIN9G1Js7b3bF50rEfhDwINnGVYJKLyrZBeeq/AuxC0+uriEFd/dx487cQeL\n" +
                    "rb13P74e/pLAhwU8eB+m84f/j4DCe+GKnFg2LmRsueXiEtZyz08ucuIOFlt77z5ZBJ8hUCzgwSsG\n" +
                    "ddwfAgrvhctwYtm4kLHllotLWMs9P7nIiTtYbO29+2QRfIZAsYAHrxjUcQrvzTtwYtm42XPlvReX\n" +
                    "sJW3+ursE3ew2Np799VG+DCBGgEPXo2jU/4q4BfeC7fixLJxIWPLLReXsJZ7fnKRE3ew2Np798ki\n" +
                    "+AyBYgEPXjGo4/zCe/MOnFg2bvZcee/FJWzlrb46+8QdLLb23n21ET5MoEbAg1fj6BS/8I7YgRPL\n" +
                    "xgjYBUMUl7AFd/ixI0/cwWJr792PrYa/IvApAQ/ep/j88XcE/JOGC9fjxLJxIWPLLReXsJZ7fnKR\n" +
                    "E3ew2Np798ki+AyBYgEPXjGo4/4QUHgvXIYTy8aFjC23XFzCWu75yUVO3MFia+/dJ4vgMwSKBTx4\n" +
                    "xaCOU3hv3oETy8bNnivvvbiErbzVV2efuIPF1t67rzbChwnUCHjwahyd8lcBv/BeuBUnlo0LGVtu\n" +
                    "ubiEtdzzk4ucuIPF1t67TxbBZwgUC3jwikEd5xfem3fgxLJxs+fKey8uYStv9dXZJ+5gsbX37quN\n" +
                    "8GECNQIevBpHp/iFd8QOnFg2RsAuGKK4hC24w48deeIOFlt7735sNfwVgU8JePA+xeePvyPgnzRc\n" +
                    "uB4nlo0LGVtuubiEtdzzk4ucuIPF1t67TxbBZwgUC3jwikEd94eAwnvhMpxYNi5kbLnl4hLWcs9P\n" +
                    "LnLiDhZbe+8+WQSfIVAs4MErBnWcwnvzDpxYNm72XHnvxSVs5a2+OvvEHSy29t59tRE+TKBGwINX\n" +
                    "4+iUvwr4hffCrTixbFzI2HLLxSWs5Z6fXOTEHSy29t59sgg+Q6BYwINXDOo4v/DevAMnlo2bPVfe\n" +
                    "e3EJW3mrr84+cQeLrb13X22EDxOoEfDg1Tg6xS+8I3bgxLIxAnbBEMUlbMEdfuzIE3ew2Np792Or\n" +
                    "4a8IfErAg/cpPn/8HQH/pOHC9TixbFzI2HLLxSWs5Z6fXOTEHSy29t59sgg+Q6BYwINXDOq4PwQU\n" +
                    "3guX4cSycSFjyy0Xl7CWe35ykRN3sNjae/fJIvgMgWIBD14xqOPOK7zFL6vREZ9YNkaDf2I4e/0c\n" +
                    "77N7XWztvfs8Op8kUCbgwSujdNA3Asf8wlv8svp6zM7np8zze5v62WLgKegTWLjXu3b81+su2fPP\n" +
                    "7nWxdef3Rt9CuhKBwwU8eIcHdPHtlb24DntZ7SoDZZ4K78VP1Ve3XlzC/hNK9ztiyZ4f9h3SbTpj\n" +
                    "4U1B4JMCHrxPAvrz/yhQ9uI67GWl8Fr6IwQU3ucxHPYd4r37PDqfJFAm4MEro3TQNwIKb+1KlHn6\n" +
                    "hbc2mF2nKbzP5RXe51Y+SWCqgMI7Ndn9c5UVtMNeVn7h3b9b7uDLly8K7/M1OOw7xHv3eXQ+SaBM\n" +
                    "wINXRukgv/Au3YGy/4DwC+/SnNoOV3ifUyu8z618ksBUAYV3arL75yoraIe9rPzCu3+33IFfeF/t\n" +
                    "wGHfId67r9LzYQI1Ah68Gken/FVA4a3dijJPv/DWBrPrNL/wPpdXeJ9b+SSBqQIK79Rk989VVtAO\n" +
                    "e1n5hXf/brkDv/C+2oHDvkO8d1+l58MEagQ8eDWOTvEL7+odKPsPCL/wro6q53y/8D53VnifW/kk\n" +
                    "gakCCu/UZPfPVVbQDntZ+YV3/265A7/wvtqBw75DvHdfpefDBGoEPHg1jk7xC+/qHSj7Dwi/8K6O\n" +
                    "qud8v/A+d1Z4n1v5JIGpAgrv1GT3z1VW0H755z8+Nc0Pf/v7p/7+O3/c+fyUeSq8q9ah91yF97n3\n" +
                    "Yd8hnd8bz5F8ksBwAQ/e8IA3jldW0A57WX1N2vn8lHkqvBufisJLK7zPMQ/7Dun83niO5JMEhgt4\n" +
                    "8IYHvHG8soJ22MtK4d24VC79p4DC+3wbDvsO8d59Hp1PEigT8OCVUTroGwGFt3Ylyjz9wlsbzK7T\n" +
                    "FN7n8grvcyufJDBVQOGdmuz+ucoK2mEvK7/w7t8td+B/peHVDhz2HeK9+yo9HyZQI+DBq3F0yl8F\n" +
                    "FN7arSjz9AtvbTC7TvML73N5hfe5lU8SmCqg8E5Ndv9cZQXtsJeVX3j375Y78Avvqx047DvEe/dV\n" +
                    "ej5MoEbAg1fj6BS/8K7egbL/gPAL7+qoes73C+9zZ4X3uZVPEpgqoPBOTXb/XGUF7bCXlV949++W\n" +
                    "O/AL76sdOOw7xHv3VXo+TKBGwINX4+gUv/Cu3oGy/4DwC+/qqHrO9wvvc2eF97mVTxKYKqDwTk12\n" +
                    "/1xlBe2wl5VfePfvljvwC++rHTjsO8R791V6PkygRsCDV+PoFL/wrt6Bsv+A8Avv6qh6zvcL73Nn\n" +
                    "hfe5lU8SmCqg8E5Ndv9cZQXtsJeVX3j375Y78Avvqx047DvEe/dVej5MoEbAg1fj6BS/8K7egbL/\n" +
                    "gPAL7+qoes73C+9zZ4X3uZVPEpgqoPBOTXb/XGUF7bCXlV949++WO/AL76sdOOw7xHv3VXo+TKBG\n" +
                    "wINX4+gUv/Cu3oGy/4DwC+/qqHrO9wvvc2eF97mVTxKYKqDwTk12/1xlBe2wl5VfePfvljvwC++r\n" +
                    "HTjsO8R791V6PkygRsCDV+PoFL/wrt6Bsv+A8Avv6qh6zvcL73Nnhfe5lU8SmCqg8E5Ndv9cZQXt\n" +
                    "sJfVftmFd/Djzz8tPN3RlQJNhbfylredddh3iPfutk1w4WQBD15y+mtnP6bwrhjzh7/9fcWx289U\n" +
                    "eLdH8PgGphbez5bTx4AvPlj8vHvvvrD3UQJVAh68KknnfCug8F64EwrvPaEpvH1ZKbx91q5EYJWA\n" +
                    "wrtK1rkK74U7oPDeE5rC25eVwttn7UoEVgkovKtknavwXrgDCu89oSm8fVkpvH3WrkRglYDCu0rW\n" +
                    "uQrvhTug8N4TmsLbl5XC22ftSgRWCSi8q2Sdq/BeuAMK7z2hKbx9WSm8fdauRGCVgMK7Sta5Cu+F\n" +
                    "O6Dw3hOawtuXlcLbZ+1KBFYJKLyrZJ2r8F64AwrvPaEpvH1ZKbx91q5EYJWAwrtK1rkK74U7oPDe\n" +
                    "E5rC25eVwttn7UoEVgkovKtknavwXrgDCu89oSm8fVkpvH3WrkRglYDCu0rWuQrvhTug8N4TmsLb\n" +
                    "l5XC22ftSgRWCSi8q2Sdq/BeuAMK7z2hKbx9WSm8fdauRGCVgMK7Sta5Cu+FO6Dw3hOawtuXlcLb\n" +
                    "Z+1KBFYJKLyrZJ2r8F64AwrvPaEpvH1ZKbx91q5EYJWAwrtK1rkK74U7oPDeE5rC25eVwttn7UoE\n" +
                    "VgkovKtknavwXrgDCu89oSm8fVkpvH3WrkRglYDCu0rWuQrvhTug8N4TmsLbl5XC22ftSgRWCSi8\n" +
                    "q2Sdq/BeuAMK7z2hKbx9WSm8fdauRGCVgMK7Sta5Cu+FO6Dw3hOawtuXlcLbZ+1KBFYJKLyrZJ2r\n" +
                    "8F64AwrvPaEpvH1ZKbx91q5EYJWAwrtK1rkK74U7oPDeE5rC25eVwttn7UoEVgkovKtknavwXrgD\n" +
                    "Cu89oSm8fVkpvH3WrkRglYDCu0rWuQrvhTug8N4TmsLbl5XC22ftSgRWCSi8q2Sdq/BeuAMK7z2h\n" +
                    "Kbx9WSm8fdauRGCVgMK7Sta5Cu+FO6Dw3hOawtuXlcLbZ+1KBFYJKLyrZJ07uvB2x1v8wv2Pt6/w\n" +
                    "dif78et1FN5f/vmPj9/goL8sfv68dwfthlHuEfDg3ZPVbXeq8BYmVvzCVXgLs9l1lMLbJ1/8/Hnv\n" +
                    "9kXnSgT+EPDgWYZVAgpvoWzxC1fhLcxm11EKb5988fPnvdsXnSsRUHjtwHIBhbeQuPiFq/AWZrPr\n" +
                    "KIW3T774+VN4+6JzJQIKrx1YLqDwFhIXv3AV3sJsdh2l8PbJFz9/Cm9fdK5EQOG1A8sFFN5C4uIX\n" +
                    "rsJbmM2uoxTePvni50/h7YvOlQgovHZguYDCW0hc/MJVeAuz2XWUwtsnX/z8Kbx90bkSAYXXDiwX\n" +
                    "UHgLiYtfuApvYTa7jlJ4++SLnz+Fty86VyKg8NqB5QIKbyFx8QtX4S3MZtdRCm+ffPHzp/D2RedK\n" +
                    "BBReO7BcQOEtJC5+4Sq8hdnsOkrh7ZMvfv4U3r7oXImAwmsHlgsovIXExS9chbcwm11HKbx98sXP\n" +
                    "n8LbF50rEVB47cBygbLCu/xOv3z5cvr/F6rFL1yFt2OpFl9D4V0M/NXxxc+fwtsXnSsRUHjtwHIB\n" +
                    "hbeQuPiFq/AWZrPrqOTC2/U8fJOtorpr2V2XQIGAB7gA0RFLBVqKs194/5Xhjz//tDRMh9cJKLx1\n" +
                    "lg9P8r58COVjBE4U8ACfmIp7+lpA4f3y5UvXL1oK7z0Pn8LbnpX3ZTu5CxKoE/AA11k6aY2Awqvw\n" +
                    "rtmsy09VeNsD9L5sJ3dBAnUCHuA6SyetEVB4Fd41m3X5qQpve4Del+3kLkigTsADXGfppDUCCq/C\n" +
                    "u2azLj9V4W0P0PuyndwFCdQJeIDrLJ20RkDhVXjXbNblpyq87QF6X7aTuyCBOgEPcJ2lk9YIKLwK\n" +
                    "75rNuvxUhbc9QO/LdnIXJFAn4AGus3TSGgGFV+Fds1mXn6rwtgfofdlO7oIE6gQ8wHWWTlojoPAq\n" +
                    "vGs26/JTFd72AL0v28ldkECdgAe4ztJJawQUXoV3zWZdfqrC2x6g92U7uQsSqBPwANdZOmmNgMKr\n" +
                    "8K7ZrMtPVXjbA/S+bCd3QQJ1Ah7gOksnrSNka9YAABaFSURBVBFQeBXeNZt1+akKb3uA3pft5C5I\n" +
                    "oE7AA1xn6aQ1Agqvwrtmsy4/VeFtD9D7sp3cBQnUCXiA6yydtEZA4VV412zW5acqvO0Bel+2k7sg\n" +
                    "gToBD3CdpZPWCCi8Cu+azbr8VIW3PUDvy3ZyFyRQJ+ABrrN00hoBhVfhXbNZl5+q8LYH6H3ZTu6C\n" +
                    "BOoEPMB1lk5aI6DwKrxrNuvyUxXe9gC9L9vJXZBAnYAHuM7SSWsEFF6Fd81mXX6qwtseoPdlO7kL\n" +
                    "EqgT8ADXWTppjYDCq/Cu2azLT1V42wP0vmwnd0ECdQIe4DpLJ60RUHgV3jWbdfmpCm97gN6X7eQu\n" +
                    "SKBOwANcZ+mkNQIKr8K7ZrMuP1XhbQ/Q+7Kd3AUJ1Al4gOssnbRGQOFVeNds1uWnKrztAXpftpO7\n" +
                    "IIE6AQ9wnaWT1ggovArvms26/FSFtz1A78t2chckUCfgAa6zdNIaAYVX4V2zWZefqvC2B+h92U7u\n" +
                    "ggTqBDzAdZZOWiOg8Cq8azbr8lMV3vYAvS/byV2QQJ2AB7jO0klrBBRehXfNZl1+qsLbHqD3ZTu5\n" +
                    "CxKoE/AA11k6aY2Awqvwrtmsy09VeNsD9L5sJ3dBAnUCHuA6SyetEVB4Fd41m3X5qQpve4Del+3k\n" +
                    "LkigTsADXGfppDUCCq/Cu2azLj9V4W0P0PuyndwFCdQJeIDrLJ20RqCl8K659X+d+ss///Hp43/4\n" +
                    "298/fcaTA378+acnH/OZAwROL7xdO9sYhfdlI7ZLEagW8ABXizrvRoGlpVrhvXElzr9nhfeL99f5\n" +
                    "a+oOCRwj4AvjmCjcyEYBhfd3fL/wbtzCl5dWeBXelyvj4wSiBRTe6PgN/7uAwqvwXvcwKLwK73VL\n" +
                    "64YJbBRQeDfiu/QxAgqvwnvMMj69EYVX4X26Kz5HgMAXXxiWgMCv/3dlKxX8G96VurlnK7zeX7nb\n" +
                    "b3IC7wX8wvvezF/ME1B4/cJ73VYrvArvdUvrhglsFFB4N+K79DECCq/Ce8wyPr0RhVfhfborPkeA\n" +
                    "gH/SYAcI/Cqg8Cq81z0JCq/Ce93SumECGwX8wrsR36WPEVB4Fd5jlvHpjSi8Cu/TXfE5AgT8wmsH\n" +
                    "CPiF96sd8L/De88DofAqvPdsqzslsF/AL7z7M3AH+wX8wusX3v1b+PIOFF6F9+XK+DiBaAGFNzp+\n" +
                    "w/8uoPAqvNc9DAqvwnvd0rphAhsFFN6N+C59jIDCq/Aes4xPb0ThVXif7orPESDg3/DaAQK/Cii8\n" +
                    "Cu91T4LCq/Bet7RumMBGAb/wbsR36WMEFF6F95hlfHojCq/C+3RXfI4AAb/w2gECfuH9agf8rzTc\n" +
                    "80AovArvPdvqTgnsF/AL7/4M3MF+Ab/w+oV3/xa+vAOFV+F9uTI+TiBaQOGNjt/wvwsovArvdQ+D\n" +
                    "wqvwXre0bpjARgGFdyO+Sx8joPAqvMcs49MbUXgV3qe74nMECPg3vHaAwK8CCq/Ce92ToPAqvNct\n" +
                    "rRsmsFHAL7wb8V36GAGFV+E9Zhmf3ojCq/A+3RWfI0DAL7x2gIBfeL/aAf8rDfc8EAqvwnvPtrpT\n" +
                    "AvsF/MK7PwN3sF/AL7x+4d2/hS/vQOFVeF+ujI8TiBZQeKPjN/zvAgqvwnvdw6DwKrzXLa0bJrBR\n" +
                    "QOHdiO/SxwgovArvMcv49EYUXoX36a74HAEC/g2vHSDwq4DCq/Be9yQovArvdUvrhglsFPAL70Z8\n" +
                    "lz5GYGnhPWbKBzfi/2jtAdIhH1F4Fd5DVtFtELhCQOG9IiY3ebGAMn1xeG79aAHvr6PjcXMEzhLw\n" +
                    "hXFWHu5mnoDCOy9TEz0X8I55buWTBAgsFPBltBDX0QRW//tgwgQOF/COOTwgt0cgRcCXUUrS5twl\n" +
                    "4BfeXfKue4KAd8wJKbgHAgT8o387QGCxgMK7GNjxRwsovEfH4+YI5Aj4MsrJ2qR7BBTePe6ueoaA\n" +
                    "d8wZObgLAvECvoziVwDAYgGFdzGw448W8I45Oh43RyBHwJdRTtYm3SOg8O5xd9UzBLxjzsjBXRCI\n" +
                    "F/BlFL8CABYLKLyLgR1/tIB3zNHxuDkCOQK+jHKyNukeAYV3j7urniHgHXNGDu6CQLyAL6P4FQCw\n" +
                    "WEDhXQzs+KMFvGOOjsfNEcgR8GWUk7VJ9wgovHvcXfUMAe+YM3JwFwTiBXwZxa8AgMUCCu9iYMcf\n" +
                    "LeAdc3Q8bo5AjoAvo5ysTbpHQOHd4+6qZwh4x5yRg7sgEC/gyyh+BQAsFlB4FwM7/mgB75ij43Fz\n" +
                    "BHIEfBnlZG3SPQIK7x53Vz1DwDvmjBzcBYF4AV9G8SsAYLGAwrsY2PFHC3jHHB2PmyOQI+DLKCdr\n" +
                    "k+4RUHj3uLvqGQLeMWfk4C4IxAv4MopfAQCLBRTexcCOP1rAO+boeNwcgRwBX0Y5WZt0j4DCu8fd\n" +
                    "Vc8Q8I45Iwd3QSBewJdR/AoAWCyg8C4GdvzRAt4xR8fj5gjkCPgyysnapAQIECBAgACBSAGFNzJ2\n" +
                    "QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J2qQECBAgQIAAgUgB\n" +
                    "hTcydkMTIECAAAECBHIEFN6crE1KgAABAgQIEIgUUHgjYzc0AQIECBAgQCBHQOHNydqkBAgQIECA\n" +
                    "AIFIAYU3MnZDEyBAgAABAgRyBBTenKxNSoAAAQIECBCIFFB4I2M3NAECBAgQIEAgR0DhzcnapAQI\n" +
                    "ECBAgACBSAGFNzJ2QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J\n" +
                    "2qQECBAgQIAAgUgBhTcydkMTIECAAAECBHIEFN6crE1KgAABAgQIEIgUUHgjYzc0AQIECBAgQCBH\n" +
                    "QOHNydqkBAgQIECAAIFIAYU3MnZDEyBAgAABAgRyBBTenKxNSoAAAQIECBCIFFB4I2M3NAECBAgQ\n" +
                    "IEAgR0DhzcnapAQIECBAgACBSAGFNzJ2QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQiBRQeCNjNzQB\n" +
                    "AgQIECBAIEdA4c3J2qQECBAgQIAAgUgBhTcydkMTIECAAAECBHIEFN6crE1KgAABAgQIEIgUUHgj\n" +
                    "Yzc0AQIECBAgQCBHQOHNydqkBAgQIECAAIFIAYU3MnZDEyBAgAABAgRyBBTenKxNSoAAAQIECBCI\n" +
                    "FFB4I2M3NAECBAgQIEAgR0DhzcnapAQIECBAgACBSAGFNzJ2QxMgQIAAAQIEcgQU3pysTUqAAAEC\n" +
                    "BAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J2qQECBAgQIAAgUgBhTcydkMTIECAAAECBHIEFN6crE1K\n" +
                    "gAABAgQIEIgUUHgjYzc0AQIECBAgQCBHQOHNydqkBAgQIECAAIFIAYU3MnZDEyBAgAABAgRyBBTe\n" +
                    "nKxNSoAAAQIECBCIFFB4I2M3NAECBAgQIEAgR0DhzcnapAQIECBAgACBSAGFNzJ2QxMgQIAAAQIE\n" +
                    "cgQU3pysTUqAAAECBAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J2qQECBAgQIAAgUgBhTcydkMTIECA\n" +
                    "AAECBHIEFN6crE1KgAABAgQIEIgUUHgjYzc0AQIECBAgQCBHQOHNydqkBAgQIECAAIFIAYU3MnZD\n" +
                    "EyBAgAABAgRyBBTenKxNSoAAAQIECBCIFFB4I2M3NAECBAgQIEAgR0DhzcnapAQIECBAgACBSAGF\n" +
                    "NzJ2QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J2qQECBAgQIAA\n" +
                    "gUgBhTcydkMTIECAAAECBHIEFN6crE1KgAABAgQIEIgUUHgjYzc0AQIECBAgQCBHQOHNydqkBAgQ\n" +
                    "IECAAIFIAYU3MnZDEyBAgAABAgRyBBTenKxNSoAAAQIECBCIFFB4I2M3NAECBAgQIEAgR0Dhzcna\n" +
                    "pAQIECBAgACBSAGFNzJ2QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQiBRQeCNjNzQBAgQIECBAIEdA\n" +
                    "4c3J2qQECBAgQIAAgUgBhTcydkMTIECAAAECBHIEFN6crE1KgAABAgQIEIgUUHgjYzc0AQIECBAg\n" +
                    "QCBHQOHNydqkBAgQIECAAIFIAYU3MnZDEyBAgAABAgRyBBTenKxNSoAAAQIECBCIFFB4I2M3NAEC\n" +
                    "BAgQIEAgR0DhzcnapAQIECBAgACBSAGFNzJ2QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQiBRQeCNj\n" +
                    "NzQBAgQIECBAIEdA4c3J2qQECBAgQIAAgUgBhTcydkMTIECAAAECBHIEFN6crE1KgAABAgQIEIgU\n" +
                    "UHgjYzc0AQIECBAgQCBHQOHNydqkBAgQIECAAIFIAYU3MnZDEyBAgAABAgRyBBTenKxNSoAAAQIE\n" +
                    "CBCIFFB4I2M3NAECBAgQIEAgR0DhzcnapAQIECBAgACBSAGFNzJ2QxMgQIAAAQIEcgQU3pysTUqA\n" +
                    "AAECBAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J2qQECBAgQIAAgUgBhTcydkMTIECAAAECBHIEFN6c\n" +
                    "rE1KgAABAgQIEIgUUHgjYzc0AQIECBAgQCBHQOHNydqkBAgQIECAAIFIAYU3MnZDEyBAgAABAgRy\n" +
                    "BBTenKxNSoAAAQIECBCIFFB4I2M3NAECBAgQIEAgR0DhzcnapAQIECBAgACBSAGFNzJ2QxMgQIAA\n" +
                    "AQIEcgQU3pysTUqAAAECBAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J2qQECBAgQIAAgUgBhTcydkMT\n" +
                    "IECAAAECBHIEFN6crE1KgAABAgQIEIgUUHgjYzc0AQIECBAgQCBHQOHNydqkBAgQIECAAIFIAYU3\n" +
                    "MnZDEyBAgAABAgRyBBTenKxNSoAAAQIECBCIFFB4I2M3NAECBAgQIEAgR0DhzcnapAQIECBAgACB\n" +
                    "SAGFNzJ2QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J2qQECBAg\n" +
                    "QIAAgUgBhTcydkMTIECAAAECBHIEFN6crE1KgAABAgQIEIgUUHgjYzc0AQIECBAgQCBHQOHNydqk\n" +
                    "BAgQIECAAIFIAYU3MnZDEyBAgAABAgRyBBTenKxNSoAAAQIECBCIFFB4I2M3NAECBAgQIEAgR0Dh\n" +
                    "zcnapAQIECBAgACBSAGFNzJ2QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQiBRQeCNjNzQBAgQIECBA\n" +
                    "IEdA4c3J2qQECBAgQIAAgUgBhTcydkMTIECAAAECBHIEFN6crE1KgAABAgQIEIgUUHgjYzc0AQIE\n" +
                    "CBAgQCBHQOHNydqkBAgQIECAAIFIAYU3MnZDEyBAgAABAgRyBBTenKxNSoAAAQIECBCIFFB4I2M3\n" +
                    "NAECBAgQIEAgR0DhzcnapAQIECBAgACBSAGFNzJ2QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQiBRQ\n" +
                    "eCNjNzQBAgQIECBAIEdA4c3J2qQECBAgQIAAgUgBhTcydkMTIECAAAECBHIEFN6crE1KgAABAgQI\n" +
                    "EIgUUHgjYzc0AQIECBAgQCBHQOHNydqkBAgQIECAAIFIAYU3MnZDEyBAgAABAgRyBBTenKxNSoAA\n" +
                    "AQIECBCIFFB4I2M3NAECBAgQIEAgR0DhzcnapAQIECBAgACBSAGFNzJ2QxMgQIAAAQIEcgQU3pys\n" +
                    "TUqAAAECBAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J2qQECBAgQIAAgUgBhTcydkMTIECAAAECBHIE\n" +
                    "FN6crE1KgAABAgQIEIgUUHgjYzc0AQIECBAgQCBHQOHNydqkBAgQIECAAIFIAYU3MnZDEyBAgAAB\n" +
                    "AgRyBBTenKxNSoAAAQIECBCIFFB4I2M3NAECBAgQIEAgR0DhzcnapAQIECBAgACBSAGFNzJ2QxMg\n" +
                    "QIAAAQIEcgQU3pysTUqAAAECBAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J2qQECBAgQIAAgUgBhTcy\n" +
                    "dkMTIECAAAECBHIEFN6crE1KgAABAgQIEIgUUHgjYzc0AQIECBAgQCBHQOHNydqkBAgQIECAAIFI\n" +
                    "AYU3MnZDEyBAgAABAgRyBBTenKxNSoAAAQIECBCIFFB4I2M3NAECBAgQIEAgR0DhzcnapAQIECBA\n" +
                    "gACBSAGFNzJ2QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J2qQE\n" +
                    "CBAgQIAAgUgBhTcydkMTIECAAAECBHIEFN6crE1KgAABAgQIEIgUUHgjYzc0AQIECBAgQCBHQOHN\n" +
                    "ydqkBAgQIECAAIFIAYU3MnZDEyBAgAABAgRyBBTenKxNSoAAAQIECBCIFFB4I2M3NAECBAgQIEAg\n" +
                    "R0DhzcnapAQIECBAgACBSAGFNzJ2QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQiBRQeCNjNzQBAgQI\n" +
                    "ECBAIEdA4c3J2qQECBAgQIAAgUgBhTcydkMTIECAAAECBHIEFN6crE1KgAABAgQIEIgUUHgjYzc0\n" +
                    "AQIECBAgQCBHQOHNydqkBAgQIECAAIFIAYU3MnZDEyBAgAABAgRyBBTenKxNSoAAAQIECBCIFFB4\n" +
                    "I2M3NAECBAgQIEAgR0DhzcnapAQIECBAgACBSAGFNzJ2QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQ\n" +
                    "iBRQeCNjNzQBAgQIECBAIEdA4c3J2qQECBAgQIAAgUgBhTcydkMTIECAAAECBHIEFN6crE1KgAAB\n" +
                    "AgQIEIgUUHgjYzc0AQIECBAgQCBHQOHNydqkBAgQIECAAIFIAYU3MnZDEyBAgAABAgRyBBTenKxN\n" +
                    "SoAAAQIECBCIFFB4I2M3NAECBAgQIEAgR0DhzcnapAQIECBAgACBSAGFNzJ2QxMgQIAAAQIEcgQU\n" +
                    "3pysTUqAAAECBAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J2qQECBAgQIAAgUgBhTcydkMTIECAAAEC\n" +
                    "BHIEFN6crE1KgAABAgQIEIgUUHgjYzc0AQIECBAgQCBHQOHNydqkBAgQIECAAIFIAYU3MnZDEyBA\n" +
                    "gAABAgRyBBTenKxNSoAAAQIECBCIFFB4I2M3NAECBAgQIEAgR0DhzcnapAQIECBAgACBSAGFNzJ2\n" +
                    "QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J2qQECBAgQIAAgUgB\n" +
                    "hTcydkMTIECAAAECBHIEFN6crE1KgAABAgQIEIgUUHgjYzc0AQIECBAgQCBHQOHNydqkBAgQIECA\n" +
                    "AIFIAYU3MnZDEyBAgAABAgRyBBTenKxNSoAAAQIECBCIFFB4I2M3NAECBAgQIEAgR0DhzcnapAQI\n" +
                    "ECBAgACBSAGFNzJ2QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J\n" +
                    "2qQECBAgQIAAgUgBhTcydkMTIECAAAECBHIEFN6crE1KgAABAgQIEIgUUHgjYzc0AQIECBAgQCBH\n" +
                    "QOHNydqkBAgQIECAAIFIAYU3MnZDEyBAgAABAgRyBBTenKxNSoAAAQIECBCIFFB4I2M3NAECBAgQ\n" +
                    "IEAgR0DhzcnapAQIECBAgACBSAGFNzJ2QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQiBRQeCNjNzQB\n" +
                    "AgQIECBAIEdA4c3J2qQECBAgQIAAgUgBhTcydkMTIECAAAECBHIE/hf3XeIXDYMQqgAAAABJRU5E\n" +
                    "rkJggg==";
            contentValues.put("Torso", torsoGreen64);
            db.update("Avatar", contentValues, "email=?", new String[]{Email});
        }
        if (TorsoNumber == 3) {
            String torso64Zero = "iVBORw0KGgoAAAANSUhEUgAAArwAAAK8CAYAAAANumxDAAAgAElEQVR4Xu3d4ZUcx9EsUNC19UEO\n" +
                    "wAHZIgfogHyQa9IBSUgghAW7d7KqszLu+/tmqztvZE+H5uCTfvnk/xEgQIAAAQIECBAYLPDL4NmM\n" +
                    "RoAAAQIECBAgQOCTwmsJCBAgQIAAAQIERgsovKPjNRwBAgQIECBAgIDCawcIECBAgAABAgRGCyi8\n" +
                    "o+M1HAECBAgQIECAgMJrBwgQIECAAAECBEYLKLyj4zUcAQIECBAgQICAwmsHCBAgQIAAAQIERgso\n" +
                    "vKPjNRwBAgQIECBAgIDCawcIECBAgAABAgRGCyi8o+M1HAECBAgQIECAgMJrBwgQIECAAAECBEYL\n" +
                    "KLyj4zUcAQIECBAgQICAwmsHCBAgQIAAAQIERgsovKPjNRwBAgQIECBAgIDCawcIECBAgAABAgRG\n" +
                    "Cyi8o+M1HAECBAgQIECAgMJrBwgQIECAAAECBEYLKLyj4zUcAQIECBAgQICAwmsHCBAgQIAAAQIE\n" +
                    "RgsovKPjNRwBAgQIECBAgIDCawcIECBAgAABAgRGCyi8o+M1HAECBAgQIECAgMJrBwgQIECAAAEC\n" +
                    "BEYLKLyj4zUcAQIECBAgQICAwmsHCBAgQIAAAQIERgsovKPjNRwBAgQIECBAgIDCawcIECBAgAAB\n" +
                    "AgRGCyi8o+M1HAECBAgQIECAgMJrBwgQIECAAAECBEYLKLyj4zUcAQIECBAgQICAwmsHCBAgQIAA\n" +
                    "AQIERgsovKPjNRwBAgQIECBAgIDCawcIECBAgAABAgRGCyi8o+M1HAECBAgQIECAgMJrBwgQIECA\n" +
                    "AAECBEYLKLyj4zUcAQIECBAgQICAwmsHCBAgQIAAAQIERgsovKPjNRwBAgQIECBAgIDCawcIECBA\n" +
                    "gAABAgRGCyi8o+M1HAECBAgQIECAgMJrBwgQIECAAAECBEYLKLyj4zUcAQIECBAgQICAwmsHCBAg\n" +
                    "QIAAAQIERgsovKPjNRwBAgQIECBAgIDCawcIECBAgAABAgRGCyi8o+M1HAECBAgQIECAgMJrBwgQ\n" +
                    "IECAAAECBEYLKLyj4zUcAQIECBAgQICAwmsHCBAgQIAAAQIERgsovKPjNRwBAgQIECBAgIDCawcI\n" +
                    "ECBAgAABAgRGCyi8o+M1HAECBAgQIECAgMJrBwgQIECAAAECBEYLKLyj4zUcAQIECBAgQICAwmsH\n" +
                    "CBAgQIAAAQIERgsovKPjNRwBAgQIECBAgIDCawcIECBAgAABAgRGCyi8o+M1HAECBAgQIECAgMJr\n" +
                    "BwgQIECAAAECBEYLKLyj4zUcAQIECBAgQICAwmsHCBAgQIAAAQIERgsovKPjNRwBAgQIECBAgIDC\n" +
                    "awcIECBAgAABAgRGCyi8o+M1HAECBAgQIECAgMJrBwgQIECAAAECBEYLKLyj4zUcAQIECBAgQICA\n" +
                    "wmsHCBAgQIAAAQIERgsovKPjNRwBAgQIECBAgIDCawcIECBAgAABAgRGCyi8o+M1HAECBAgQIECA\n" +
                    "gMJrBwgQIECAAAECBEYLKLyj4zUcAQIECBAgQICAwmsHCBAgQIAAAQIERgsovKPjNRwBAgQIECBA\n" +
                    "gIDCawcIECBAgAABAgRGCyi8o+M1HAECBAgQIECAgMJrBwgQIECAAAECBEYLKLyj4zUcAQIECBAg\n" +
                    "QICAwmsHCBAgQIAAAQIERgsovKPjNRwBAgQIECBAgIDCawcIECBAgAABAgRGCyi8o+M1HAECBAgQ\n" +
                    "IECAgMJrBwgQIECAAAECBEYLKLyj4zUcAQIECBAgQICAwmsHCBAgQIAAAQIERgsovKPjNRwBAgQI\n" +
                    "ECBAgIDCawcIECBAgAABAgRGCyi8o+M1HAECBAgQIECAgMJrBwgQIECAAAECBEYLKLyj4zUcAQIE\n" +
                    "CBAgQICAwmsHCBAgQIAAAQIERgsovKPjNRwBAgQIECBAgIDCawcIECBAgAABAgRGCyi8o+M1HAEC\n" +
                    "BAgQIECAgMJrBwgQIECAAAECBEYLKLyj4zUcAQIECBAgQICAwmsHCBAgQIAAAQIERgsovKPjNRwB\n" +
                    "AgQIECBAgIDCawcIECBAgAABAgRGCyi8o+M1HAECBAgQIECAgMJrBwgQIECAAAECBEYLKLyj4zUc\n" +
                    "AQIECBAgQICAwmsHCBAgQIAAAQIERgsovKPjNRwBAgQIECBAgIDCawcIECBAgAABAgRGCyi8o+M1\n" +
                    "HAECBAgQIECAgMJrBwgQIECAAAECBEYLKLyj4zUcAQIECBAgQICAwmsHCBAgQIAAAQIERgsovKPj\n" +
                    "NRwBAgQIECBAgIDCawcIECBAgAABAgRGCyi8o+M1HAECBAgQIECAgMJrBwgQIECAAAECBEYLKLyj\n" +
                    "4zUcAQIECBAgQICAwmsHCBAgQIAAAQIERgsovKPjNRwBAgQIECBAgIDCawcIECBAgAABAgRGCyi8\n" +
                    "o+M1HAECBAgQIECAgMJrBwgQIECAAAECBEYLKLyj4zUcAQIECBAgQICAwmsHCBAgQIAAAQIERgso\n" +
                    "vKPjNRwBAgQIECBAgIDCawcIECBAgAABAgRGCyi8o+M1HAECBAgQIECAgMJrBwgQIECAAAECBEYL\n" +
                    "KLyj4zUcAQIECBAgQICAwmsHCBAgQIAAAQIERgsovKPjNRwBAgQIECBAgIDCawcIECBAgAABAgRG\n" +
                    "Cyi8o+M1HAECBAgQIECAgMJrBwgQIECAAAECBEYLKLyj4zUcAQIECBAgQICAwmsHCBAgQIAAAQIE\n" +
                    "RgsovKPjNRwBAgQIECBAgIDCawcIECBAgAABAgRGCyi8o+M1HAECBAgQIECAgMJrBwgQIECAAAEC\n" +
                    "BEYLKLyj4zUcAQIECBAgQICAwmsHCBAgQIAAAQIERgsovKPjNRwBAgQIECBAgIDCawcIECBAgAAB\n" +
                    "AgRGCyi8o+M1HAECBAgQIECAgMJrBwgQIECAAAECBEYLKLyj4zUcAQIECBAgQICAwmsHCBAgQIAA\n" +
                    "AQIERgsovKPjNRwBAgQIECBAgIDCawcIECBAgAABAgRGCyi8o+M1HAECBAgQIECAgMJrBwgQIECA\n" +
                    "AAECBEYLKLyj4zUcAQIECBAgQICAwmsHCBAgQIAAAQIERgsovKPjNRwBAgQIECBAgIDCawcIECBA\n" +
                    "gAABAgRGCyi8o+M1HAECBAgQIECAgMJrBwgQIECAAAECBEYLKLyj4zUcAQIECBAgQICAwmsHCBAg\n" +
                    "QIAAAQIERgsovKPjNRwBAgQIECBAgIDCawcIECBAgAABAgRGCyi8o+M1HAECBAgQIECAgMJrBwgQ\n" +
                    "IECAAAECBEYLKLyj4zUcAQIECBAgQICAwmsHCBAgQIAAAQIERgsovKPjNRwBAgQIECBAgIDCawcI\n" +
                    "ECBAgAABAgRGCyi8o+M1HAECBAgQIECAgMJrBwgQIECAAAECBEYLKLyj4zUcAQIECBAgQICAwmsH\n" +
                    "CBAgQIAAAQIERgsovKPjNRwBAgQIECBAgIDCawcIECBAgAABAgRGCyi8o+M1HAECBAgQIECAgMJr\n" +
                    "BwgQIECAAAECBEYLKLyj4zUcAQIECBAgQICAwmsHCBAgQIAAAQIERgsovKPjNRwBAgQIECBAgIDC\n" +
                    "awcIECBAgAABAgRGCyi8o+M1HAECBAgQIECAgMJrBwgQIECAAAECBEYLKLyj4zUcAQIECBAgQICA\n" +
                    "wmsHCBAgQIAAAQIERgsovKPjNRwBAgQIECBAgIDCawcIECBAgAABAgRGCyi8o+M1HAECBAgQIECA\n" +
                    "gMJrBwgQIECAAAECBEYLKLyj4zUcAQIECBAgQICAwmsHCBAgQIAAAQIERgsovKPjNRwBAgQIECBA\n" +
                    "gIDCawcIECBAgAABAgRGCyi8o+M1HAECBAgQIECAgMJrBwgQIECAAAECBEYLKLyj4zUcAQIECBAg\n" +
                    "QICAwmsHCBAgQIAAAQIERgsovKPjNRwBAgQIECBAgIDCawcIECBAgAABAgRGCyi8o+M1HAECBAgQ\n" +
                    "IECAgMJrBwgQIECAAAECBEYLKLyj4zUcAQIECBAgQICAwmsHCBAgQIAAAQIERgsovKPjNRwBAgQI\n" +
                    "ECBAgIDCawcIECBAgAABAgRGCyi8o+M1HAECBAgQIECAgMJrBwgQIECAAAECBEYLKLyj4zUcAQIE\n" +
                    "CBAgQICAwmsHCBAgQIAAAQIERgsovKPjNRwBAgQIECBAgIDCawcIECBAgAABAgRGCyi8o+M1HAEC\n" +
                    "BAgQIECAgMJrBwj8WeDfQAgQaC3gvdU6HjdHoKeAL46eubir5wQU3ufsXZnAFQHvrStKPkOAwJ8E\n" +
                    "fHFYCAJ+4bUDBE4S8N46KS33SqCJgC+OJkG4jTYCfuFtE4UbIfBDAe8ti0GAwG0BXxy3yfzBcAGF\n" +
                    "d3jAxjtewHvr+AgNQGC/gC+O/eau2FtA4e2dj7sj4L1lBwgQuC3gi+M2mT8YLqDwDg/YeMcLeG8d\n" +
                    "H6EBCOwX8MWx39wVewsovL3zcXcEvLfsAAECtwV8cdwm8wfDBRTe4QEb73gB763jIzQAgf0Cvjj2\n" +
                    "m7tibwGFt3c+7o6A95YdIEDgtoAvjttk/mC4gMI7PGDjHS/gvXV8hAYgsF/AF8d+c1fsLaDw9s7H\n" +
                    "3RHw3rIDBAjcFvDFcZvMHwwXUHiHB2y84wW8t46P0AAE9gv44thv7oq9BRTe3vm4OwLeW3aAAIHb\n" +
                    "Ar44bpP5g+ECCu/wgI13vID31vERGoDAfgFfHPvNXbG3gMLbOx93R8B7yw4QIHBbwBfHbTJ/MFxA\n" +
                    "4R0esPGOF/DeOj5CAxDYL+CLY7+5K/YWUHh75+PuCHhv2QECBG4L+OK4TeYPNgsooJvBXY4AgR8K\n" +
                    "eF9aDAIHC3iADw4v5Na3F963t7cQWmMSOEvgX//615M37H35pL5rE3hRwAP8IqA/Xy6g8C4ndgEC\n" +
                    "ZwgovGfk5C4JdBRQeDum4p6+FVB47QMBAr8JKLwWgQCBjwoovB+V83e7BBTeXdKuQ6C5gMLbPCC3\n" +
                    "R6CxgMLbOBy39puAwmsRCBDwC68dIEDgJQGF9yU+f7xBQOHdgOwSBE4Q8AvvCSm5RwI9BRTenrm4\n" +
                    "q/8JKLy2gQABv/DaAQIEXhJQeF/i88cbBBTeDcguQeAEAb/wnpCSeyTQU0Dh7ZmLu/ILrx0gQOA7\n" +
                    "AYXXShAg8FEBhfejcv5ul4BfeHdJuw6B5gIKb/OA3B6BxgIKb+Nw3NpvAgrvwkX49ddfF57+6dPn\n" +
                    "z59/eP7p131vrruYdx2qrnv3Prt8XuHtkoT7IHCegMJ7XmZpd6zwLkz8buG6eysK78/F7vorvP6n\n" +
                    "he8+gz5PgMDvAgqvTeguoPAuTOhu4bp7Kwqvwnt3Z372eb/wVmo6i0CWgMKblfeJ0yq8C1NTeD+G\n" +
                    "W/VL613/qut+bOrn/0rhfT4Dd0DgVAGF99Tkcu5b4V2Y9d3CdfdW/MLrF967O+MX3koxZxEg8FVA\n" +
                    "4bUL3QUU3oUJKbwfw636pfWuf9V1Pzb183/lF97nM3AHBE4VUHhPTS7nvhXeRlm/V9DuFrGqc+7S\n" +
                    "VF337jl3P//eXFXn3HXr8nmFt0sS7oPAeQIK73mZpd2xwtso8arCVXXOXZqq69495+7nFd4fCyi8\n" +
                    "dzfe5wkQ8E8a7MApAgpvo6ROL25P3f9T1220OiW3ovCWMDqEQKSAX3gjYz9qaIW3UVynF7en7v+p\n" +
                    "6zZanZJbUXhLGB1CIFJA4Y2M/aihFd5GcZ1e3J66/6eu22h1Sm5F4S1hdAiBSAGFNzL2o4ZWeBvF\n" +
                    "dXpxe+r+n7puo9UpuZWHC2/JDP4Hn6oYnUPgnoDCe8/Lp68LbC+q12/t5598e3urOurYc6oK2nsA\n" +
                    "d//ruN47p+q/HeJuUFXXfeqcu/Omf764aHvvpi+U+R8R8OA9wh5x0bLCq4Du3xeF9+fmTxXV1bns\n" +
                    "37QzrqjwnpGTuyTwMwGF136sElB4V8luOHd1sfIL7+8hPlWcN6zQqEsovKPiNEyogMIbGvyGsRXe\n" +
                    "DcirLqHw+oV31W6deK7Ce2Jq7pnAnwUUXhuxSkDhXSW74VyFV+HdsGbHXELhPSYqN0rgXQGF13Ks\n" +
                    "ElB4V8luOFfhVXg3rNkxl1B4j4nKjRJQeO3AdgGFdzv5/QuuLrbv3dEp1626z27n3N+U7L9QeLPz\n" +
                    "N/0MAb/wzsix4xQKb8dUvrunqiJ2d9RTrlt1n93OuZtX+ucV3vQNMP8EAYV3Qoo9Z1B4e+byp7uq\n" +
                    "KmJ3Rz3lulX32e2cu3mlf17hTd8A808QUHgnpNhzBoW3Zy4K7w9yee+/HqxbUa26nwNWs9UtKryt\n" +
                    "4nAzBD4koPB+iM0fXRBQeC8gPf2RpwrUKdetus9u5zy9d6ddX+E9LTH3S+D/BRReW7FKQOFdJVt4\n" +
                    "blURu3tLp1y36j67nXM3r/TPK7zpG2D+CQIK74QUe86g8PbM5dI/aai69bv/VOCp6z71v3h2939x\n" +
                    "7u59Vnmmn6Pwpm+A+ScIKLwTUuw5g8LbMxeF9we53C2Sq3+xfW917t7nASt4xC0qvEfE5CYJ/FRA\n" +
                    "4bUgqwQU3lWyhefe/YXx7qX9wvtzsbv+Cu/dDaz5vMJb4+gUAk8KKLxP6s++tsJ7QL53C9fdkRRe\n" +
                    "hffuznT8vMLbMRX3ROCegMJ7z8unrwsovNetHvukwvs7/d1fTv2ThsdW9pELK7yPsLsogVIBhbeU\n" +
                    "02HfCCi8B6yDwqvwHrCmj9+iwvt4BG6AwMsCCu/LhA54R0DhtRoECIwQUHhHxGiIcAGFN3wBFo6v\n" +
                    "8C7EdTQBAvsEFN591q5EYJWAwrtK1rkKrx0gQGCEgMI7IkZDhAsovOELsHD80YW3+AW4MIZ7R7+9\n" +
                    "vd37A59+TMAO7qMvtvbe3RedKxH4r4AHzzKsElB4V8kuPFfhXYhbfHRxCSu+u48f13EHi629dz++\n" +
                    "Hv6SwIcFPHgfpvOHfyGg8B64Ih3LxoGMW265uIRtuecrF+m4g8XW3rtXFsFnCBQLePCKQR33XwGF\n" +
                    "98Bl6Fg2DmTccsvFJWzLPV+5SMcdLLb23r2yCD5DoFjAg1cM6jiF9+Qd6Fg2TvZcee/FJWzlrd46\n" +
                    "u+MOFlt7797aCB8mUCPgwatxdMr/C/iF98Ct6Fg2DmTccsvFJWzLPV+5SMcdLLb23r2yCD5DoFjA\n" +
                    "g1cM6ji/8J68Ax3LxsmeK++9uIStvNVbZ3fcwWJr791bG+HDBGoEPHg1jk7xC++IHehYNkbALhii\n" +
                    "uIQtuMOPHdlxB4utvXc/thr+isBLAh68l/j88U8E/JOGA9ejY9k4kHHLLReXsC33fOUiHXew2Np7\n" +
                    "98oi+AyBYgEPXjGo4/4roPAeuAwdy8aBjFtuubiEbbnnKxfpuIPF1t67VxbBZwgUC3jwikEdp/Ce\n" +
                    "vAMdy8bJnivvvbiErbzVW2d33MFia+/dWxvhwwRqBDx4NY5O+X8Bv/AeuBUdy8aBjFtuubiEbbnn\n" +
                    "KxfpuIPF1t67VxbBZwgUC3jwikEd5xfek3egY9k42XPlvReXsJW3euvsjjtYbO29e2sjfJhAjYAH\n" +
                    "r8bRKX7hHbEDHcvGCNgFQxSXsAV3+LEjO+5gsbX37sdWw18ReEnAg/cSnz/+iYB/0nDgenQsG1cZ\n" +
                    "f/31198++vnz56t/cvTniktYG4uOO1hs7b3bZtvcSJKABy8p7b2zKrx7vUuu1rFsXBnsa9n9+tmE\n" +
                    "0ltcwq4wb/lMxx0stvbe3bJJLkLgzwIePBuxSkDhXSW78NyOZePKuArvFaUzPtNxBxXeM3bHXRL4\n" +
                    "mYDCaz9WCSi8q2QXntuxbPzVuN+X3ZRfeYtL2F8xb/v/77iDxdbeu9u2yYUI/E/Ag2cbVgkovKtk\n" +
                    "F57bsWz8bNz3ym5C6S0uYQu36t7RHXew2Np7995K+DSBEgEPXgmjQ34goPAeuBYdy4bC+2OB4hLW\n" +
                    "Zls77mCxtfdum21zI0kCHryktPfOqvDu9S65Wsey8d5gf/Xr7vRfeYtLWMn+VBzScQeLrb13KxbF\n" +
                    "GQRuCnjwboL5+GUBhfcyVZ8PdiwbCq9feJ9+QhTepxNwfQKvCyi8rxs64ccCbQpv8ctqdN6nFN6r\n" +
                    "v+5O/pXXXl9/FF/d62Jr793r0fkkgTIBD14ZpYO+E0govDufnzLPn23qq8Vgx1Nwt+xOLb3FJey9\n" +
                    "6Hbu+Jd7WLLnr+51sfVu0x2PpWsQaC/gwWsf0bE3WPbiavay+jaQnc9PmafCe+wz9acbLy5hCu9P\n" +
                    "1qLYeuf3xoxlNwWBAgEPXgGiI34oUFbQFN7ffMs8Ty68H/11d+KvvMUlTOFVeL3KCIwWUHhHx/vo\n" +
                    "cGUFTeFVeL8IvFp2p5Vehff691uz7xDv3evR+SSBMgEPXhmlg74TUHhrV6LM89RfeBXePyen8F5/\n" +
                    "wBTe61Y+SWCqgMI7Ndnn5yoraM1eVt/K7nx+yjx3Ft6qkvr8Ov/4Dj5//vzYrSm81+mbfYfs/N64\n" +
                    "juSTBIYLePCGB/zgeGUFrdnLSuH9wFJNK75PFt2v/Arv9UVs9h3ivXs9Op8kUCbgwSujdNB3Agpv\n" +
                    "7UqUee78hff7a51efDsUXYX3/oOl8N438xcEpgkovNMS7TNPWUFr9rLyC++LO3Zq6e1Udr9E4Bfe\n" +
                    "64vY7DvEe/d6dD5JoEzAg1dG6SC/8C7dgbL/APHkL7xfr31a6e1WdhXee8+awnvPy6cJTBRQeCem\n" +
                    "2mOmsoLW7GXlF97C/epefDsWXf+k4f4CNvsO8d69H6G/IPCygAfvZUIHvCOg8NauRplnh194v72H\n" +
                    "rqW3c9n1C++9h0vhvefl0wQmCii8E1PtMVNZQfv3P//x0kS//O3vL/39T/545/NT5tmt8H65n26l\n" +
                    "t3vZVXjvPdLNvkN2fm/cg/JpAoMFPHiDw314tLKC1uxl9S3rzuenzLNj4f16T08X3xOK7lcr/0dr\n" +
                    "17/hmn2H7PzeuI7kkwSGC3jwhgf84HhlBa3Zy0rhXbxUT5Xek8quX3jvLWGz7xDv3Xvx+TSBEgEP\n" +
                    "XgmjQ34goPDWrkWZZ+dfeJ/6pfe0sqvw3nu4FN57Xj5NYKKAwjsx1R4zlRW0Zi8rv/Bu3K/Vv/ae\n" +
                    "WHT9k4b7C9jsO8R7936E/oLAywIevJcJHfCOgMJbuxplnif8wvvlHu+U3e+L6yt/WxvbutP8G97r\n" +
                    "tgrvdSufJDBVQOGdmuzzc5UVtGYvK7/wbtqtq6X1vV9pX/37TWN++DIK73W6Zt8h3rvXo/NJAmUC\n" +
                    "HrwySgd9J6Dw1q5EmecJv/BeKatX/jnClXO+eFw5qzbO109TeK8bKrzXrXySwFQBhXdqss/PVVbQ\n" +
                    "mr2s/MK7eLeulNSPFNS/OvcjZy6m+OnxCu91/WbfId6716PzSQJlAh68MkoH+YV36Q6U/QeI7r/w\n" +
                    "/qyYvlpKV569NP0fHK7wXhdXeK9b+SSBqQIK79Rkn5+rrKA1e1n5hXfhbu0qpO9d59VCvZDm/45W\n" +
                    "eK9rN/sO8d69Hp1PEigT8OCVUTrIL7xLd6DsP0B0/YV3V9n9Ov/ppVfhvf68KbzXrXySwFQBhXdq\n" +
                    "ss/PVVbQmr2s/MK7aLd+VEB3/OL6/XV3XLOCUOG9rtjsO8R793p0PkmgTMCDV0bpIL/wLt2Bsv8A\n" +
                    "0fEX3qdL59PX/8jmKLzX1RTe61Y+SWCqgMI7Ndnn5yoraM1eVn7hLd6tb8vm07+udrqXv2JWeP9K\n" +
                    "6H///82+Q7x3r0fnkwTKBDx4ZZQO8gvv0h0o+w8Q3X7h/Voyny67X1263c97eSm81583hfe6lU8S\n" +
                    "mCqg8E5N9vm5ygpas5eVX3gLd+tLuexSdL8fq/O9fblXhff6Ijb7DvHevR6dTxIoE/DglVE6yC+8\n" +
                    "S3eg7D9AdPuFd6na4MMV3uvhKrzXrXySwFQBhXdqss/PVVbQmr2snpddeAdvb28LT3d0pcCmwlt5\n" +
                    "y4+d1ew7xHv3sU1w4WQBD15y+mtnb1N4V4z5y9/+vuLYx89UeB+P4PINTC28r5bTy4A3Plj8vHvv\n" +
                    "3rD3UQJVAh68KknnfC+g8B64EwrvOaEpvPuyUnj3WbsSgVUCCu8qWecqvAfugMJ7TmgK776sFN59\n" +
                    "1q5EYJWAwrtK1rkK74E7oPCeE5rCuy8rhXeftSsRWCWg8K6Sda7Ce+AOKLznhKbw7stK4d1n7UoE\n" +
                    "VgkovKtknavwHrgDCu85oSm8+7JSePdZuxKBVQIK7ypZ5yq8B+6AwntOaArvvqwU3n3WrkRglYDC\n" +
                    "u0rWuQrvgTug8J4TmsK7LyuFd5+1KxFYJaDwrpJ1rsJ74A4ovOeEpvDuy0rh3WftSgRWCSi8q2Sd\n" +
                    "q/AeuAMK7zmhKbz7slJ491m7EoFVAgrvKlnnKrwH7oDCe05oCu++rBTefdauRGCVgMK7Sta5Cu+B\n" +
                    "O6DwnhOawrsvK4V3n7UrEVgloPCuknWuwnvgDii854Sm8O7LSuHdZ+1KBFYJKLyrZJ2r8B64Awrv\n" +
                    "OaEpvPuyUnj3WbsSgVUCCu8qWecqvAfugMJ7TmgK776sFN591q5EYJWAwrtK1rkK74E7oPCeE5rC\n" +
                    "uy8rhXeftSsRWCWg8K6Sda7Ce+AOKLznhKbw7stK4d1n7UoEVgkovKtknavwHrgDCu85oSm8+7JS\n" +
                    "ePdZuxKBVQIK7ypZ5yq8B+6AwntOaArvvqwU3n3WrkRglYDCu0rWuQrvgTug8J4TmsK7LyuFd5+1\n" +
                    "KxFYJaDwrpJ1rsJ74A4ovOeEpvDuy0rh3WftSgRWCSi8q2Sdq/AeuAMK7zmhKbz7slJ491m7EoFV\n" +
                    "AgrvKlnnKrwH7oDCe05oCu++rBTefdauRGCVgMK7Sta5Cu+BO6DwnhOawrsvK4V3n7UrEVgloPCu\n" +
                    "knWuwnvgDii854Sm8O7LSuHdZ+1KBFYJKLyrZJ07uvDujrf4hfvu7Su8u5P9+PV2FN5///MfH7/B\n" +
                    "QX9Z/Px57w7aDaOcI+DBOyer0+5U4S1MrPiFq/AWZvPUUQrvPvni5897d190rkTgvwIePMuwSkDh\n" +
                    "LZQtfuEqvIXZPHWUwrtPvvj5897dF50rEVB47cByAYW3kLj4havwFmbz1FEK7z754udP4d0XnSsR\n" +
                    "UHjtwHIBhbeQuPiFq/AWZvPUUQrvPvni557EEN0AABjwSURBVE/h3RedKxFQeO3AcgGFt5C4+IWr\n" +
                    "8BZm89RRCu8++eLnT+HdF50rEVB47cByAYW3kLj4havwFmbz1FEK7z754udP4d0XnSsRUHjtwHIB\n" +
                    "hbeQuPiFq/AWZvPUUQrvPvni50/h3RedKxFQeO3AcgGFt5C4+IWr8BZm89RRCu8++eLnT+HdF50r\n" +
                    "EVB47cByAYW3kLj4havwFmbz1FEK7z754udP4d0XnSsRUHjtwHIBhbeQuPiFq/AWZvPUUQrvPvni\n" +
                    "50/h3RedKxFQeO3AcoGywrv8Tj99+tT9f0K1+IWr8O5YqsXXUHgXA39zfPHzp/Dui86VCCi8dmC5\n" +
                    "gMJbSFz8wlV4C7N56qjkwrvrefguW0X1qWV3XQIFAh7gAkRHLBXYUpz9wvt7hm9vb0vDdHidgMJb\n" +
                    "Z3nxJO/Li1A+RqCjgAe4Yyru6VsBhffTp0+7ftFSeM95+BTe7Vl5X24nd0ECdQIe4DpLJ60RUHgV\n" +
                    "3jWbdfipCu/2AL0vt5O7IIE6AQ9wnaWT1ggovArvms06/FSFd3uA3pfbyV2QQJ2AB7jO0klrBBRe\n" +
                    "hXfNZh1+qsK7PUDvy+3kLkigTsADXGfppDUCCq/Cu2azDj9V4d0eoPfldnIXJFAn4AGus3TSGgGF\n" +
                    "V+Fds1mHn6rwbg/Q+3I7uQsSqBPwANdZOmmNgMKr8K7ZrMNPVXi3B+h9uZ3cBQnUCXiA6yydtEZA\n" +
                    "4VV412zW4acqvNsD9L7cTu6CBOoEPMB1lk5aI6DwKrxrNuvwUxXe7QF6X24nd0ECdQIe4DpLJ60R\n" +
                    "UHgV3jWbdfipCu/2AL0vt5O7IIE6AQ9wnaWT1ggovArvms06/FSFd3uA3pfbyV2QQJ2AB7jO0klr\n" +
                    "BBRehXfNZh1+qsK7PUDvy+3kLkigTsADXGfppDUCCq/Cu2azDj9V4d0eoPfldnIXJFAn4AGus3TS\n" +
                    "GgGFV+Fds1mHn6rwbg/Q+3I7uQsSqBPwANdZOmmNgMKr8K7ZrMNPVXi3B+h9uZ3cBQnUCXiA6yyd\n" +
                    "tEZA4VV412zW4acqvNsD9L7cTu6CBOoEPMB1lk5aI6DwKrxrNuvwUxXe7QF6X24nd0ECdQIe4DpL\n" +
                    "J60RUHgV3jWbdfipCu/2AL0vt5O7IIE6AQ9wnaWT1ggovArvms06/FSFd3uA3pfbyV2QQJ2AB7jO\n" +
                    "0klrBBRehXfNZh1+qsK7PUDvy+3kLkigTsADXGfppDUCCq/Cu2azDj9V4d0eoPfldnIXJFAn4AGu\n" +
                    "s3TSGgGFV+Fds1mHn6rwbg/Q+3I7uQsSqBPwANdZOmmNgMKr8K7ZrMNPVXi3B+h9uZ3cBQnUCXiA\n" +
                    "6yydtEZA4VV412zW4acqvNsD9L7cTu6CBOoEPMB1lk5aI6DwKrxrNuvwUxXe7QF6X24nd0ECdQIe\n" +
                    "4DpLJ60RUHgV3jWbdfipCu/2AL0vt5O7IIE6AQ9wnaWT1ggovArvms06/FSFd3uA3pfbyV2QQJ2A\n" +
                    "B7jO0klrBBRehXfNZh1+qsK7PUDvy+3kLkigTsADXGfppDUCWwrvmlv//dR///MfLx//y9/+/vIZ\n" +
                    "Vw54e3u78jGfaSDQvfDu2tmNUXhfbsR2KQLVAh7galHnnSiwtFQrvCeuRP97Vng/eX/1X1N3SKCN\n" +
                    "gC+MNlG4kQcFFN4/8P3C++AW3ry0wqvw3lwZHycQLaDwRsdv+D8EFF6F97iHQeFVeI9bWjdM4EEB\n" +
                    "hfdBfJduI6DwKrxtlvHqjSi8Cu/VXfE5AgQ++cKwBAS+/N+VrVTwb3hX6uaerfB6f+Vuv8kJ3Bfw\n" +
                    "C+99M38xT0Dh9QvvcVut8Cq8xy2tGybwoIDC+yC+S7cRUHgV3jbLePVGFF6F9+qu+BwBAv5Jgx0g\n" +
                    "8EVA4VV4j3sSFF6F97ildcMEHhTwC++D+C7dRkDhVXjbLOPVG1F4Fd6ru+JzBAj4hdcOEPAL7zc7\n" +
                    "4L+H95wHQuFVeM/ZVndK4HkBv/A+n4E7eF7AL7x+4X1+C2/egcKr8N5cGR8nEC2g8EbHb/g/BBRe\n" +
                    "hfe4h0HhVXiPW1o3TOBBAYX3QXyXbiOg8Cq8bZbx6o0ovArv1V3xOQIE/BteO0Dgi4DCq/Ae9yQo\n" +
                    "vArvcUvrhgk8KOAX3gfxXbqNgMKr8LZZxqs3ovAqvFd3xecIEPALrx0g4Bfeb3bAf0vDOQ+Ewqvw\n" +
                    "nrOt7pTA8wJ+4X0+A3fwvIBfeP3C+/wW3rwDhVfhvbkyPk4gWkDhjY7f8H8IKLwK73EPg8Kr8B63\n" +
                    "tG6YwIMCCu+D+C7dRkDhVXjbLOPVG1F4Fd6ru+JzBAj4N7x2gMAXAYVX4T3uSVB4Fd7jltYNE3hQ\n" +
                    "wC+8D+K7dBsBhVfhbbOMV29E4VV4r+6KzxEg4BdeO0DAL7zf7ID/loZzHgiFV+E9Z1vdKYHnBfzC\n" +
                    "+3wG7uB5Ab/w+oX3+S28eQcKr8J7c2V8nEC0gMIbHb/h/xBQeBXe4x4GhVfhPW5p3TCBBwUU3gfx\n" +
                    "XbqNgMKr8LZZxqs3ovAqvFd3xecIEPBveO0AgS8CCq/Ce9yToPAqvMctrRsm8KCAX3gfxHfpNgJL\n" +
                    "C2+bKS/ciP+jtQtITT6i8Cq8TVbRbRA4QkDhPSImN3mwgDJ9cHhuvbWA91freNwcgV4CvjB65eFu\n" +
                    "5gkovPMyNdF1Ae+Y61Y+SYDAQgFfRgtxHU1g9b8PJkyguYB3TPOA3B6BFAFfRilJm/MpAb/wPiXv\n" +
                    "uh0EvGM6pOAeCBDwj/7tAIHFAgrvYmDHtxZQeFvH4+YI5Aj4MsrJ2qTPCCi8z7i7ag8B75geObgL\n" +
                    "AvECvoziVwDAYgGFdzGw41sLeMe0jsfNEcgR8GWUk7VJnxFQeJ9xd9UeAt4xPXJwFwTiBXwZxa8A\n" +
                    "gMUCCu9iYMe3FvCOaR2PmyOQI+DLKCdrkz4joPA+4+6qPQS8Y3rk4C4IxAv4MopfAQCLBRTexcCO\n" +
                    "by3gHdM6HjdHIEfAl1FO1iZ9RkDhfcbdVXsIeMf0yMFdEIgX8GUUvwIAFgsovIuBHd9awDumdTxu\n" +
                    "jkCOgC+jnKxN+oyAwvuMu6v2EPCO6ZGDuyAQL+DLKH4FACwWUHgXAzu+tYB3TOt43ByBHAFfRjlZ\n" +
                    "m/QZAYX3GXdX7SHgHdMjB3dBIF7Al1H8CgBYLKDwLgZ2fGsB75jW8bg5AjkCvoxysjbpMwIK7zPu\n" +
                    "rtpDwDumRw7ugkC8gC+j+BUAsFhA4V0M7PjWAt4xreNxcwRyBHwZ5WRt0mcEFN5n3F21h4B3TI8c\n" +
                    "3AWBeAFfRvErAGCxgMK7GNjxrQW8Y1rH4+YI5Aj4MsrJ2qQECBAgQIAAgUgBhTcydkMTIECAAAEC\n" +
                    "BHIEFN6crE1KgAABAgQIEIgUUHgjYzc0AQIECBAgQCBHQOHNydqkBAgQIECAAIFIAYU3MnZDEyBA\n" +
                    "gAABAgRyBBTenKxNSoAAAQIECBCIFFB4I2M3NAECBAgQIEAgR0DhzcnapAQIECBAgACBSAGFNzJ2\n" +
                    "QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J2qQECBAgQIAAgUgB\n" +
                    "hTcydkMTIECAAAECBHIEFN6crE1KgAABAgQIEIgUUHgjYzc0AQIECBAgQCBHQOHNydqkBAgQIECA\n" +
                    "AIFIAYU3MnZDEyBAgAABAgRyBBTenKxNSoAAAQIECBCIFFB4I2M3NAECBAgQIEAgR0DhzcnapAQI\n" +
                    "ECBAgACBSAGFNzJ2QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J\n" +
                    "2qQECBAgQIAAgUgBhTcydkMTIECAAAECBHIEFN6crE1KgAABAgQIEIgUUHgjYzc0AQIECBAgQCBH\n" +
                    "QOHNydqkBAgQIECAAIFIAYU3MnZDEyBAgAABAgRyBBTenKxNSoAAAQIECBCIFFB4I2M3NAECBAgQ\n" +
                    "IEAgR0DhzcnapAQIECBAgACBSAGFNzJ2QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQiBRQeCNjNzQB\n" +
                    "AgQIECBAIEdA4c3J2qQECBAgQIAAgUgBhTcydkMTIECAAAECBHIEFN6crE1KgAABAgQIEIgUUHgj\n" +
                    "Yzc0AQIECBAgQCBHQOHNydqkBAgQIECAAIFIAYU3MnZDEyBAgAABAgRyBBTenKxNSoAAAQIECBCI\n" +
                    "FFB4I2M3NAECBAgQIEAgR0DhzcnapAQIECBAgACBSAGFNzJ2QxMgQIAAAQIEcgQU3pysTUqAAAEC\n" +
                    "BAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J2qQECBAgQIAAgUgBhTcydkMTIECAAAECBHIEFN6crE1K\n" +
                    "gAABAgQIEIgUUHgjYzc0AQIECBAgQCBHQOHNydqkBAgQIECAAIFIAYU3MnZDEyBAgAABAgRyBBTe\n" +
                    "nKxNSoAAAQIECBCIFFB4I2M3NAECBAgQIEAgR0DhzcnapAQIECBAgACBSAGFNzJ2QxMgQIAAAQIE\n" +
                    "cgQU3pysTUqAAAECBAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J2qQECBAgQIAAgUgBhTcydkMTIECA\n" +
                    "AAECBHIEFN6crE1KgAABAgQIEIgUUHgjYzc0AQIECBAgQCBHQOHNydqkBAgQIECAAIFIAYU3MnZD\n" +
                    "EyBAgAABAgRyBBTenKxNSoAAAQIECBCIFFB4I2M3NAECBAgQIEAgR0DhzcnapAQIECBAgACBSAGF\n" +
                    "NzJ2QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J2qQECBAgQIAA\n" +
                    "gUgBhTcydkMTIECAAAECBHIEFN6crE1KgAABAgQIEIgUUHgjYzc0AQIECBAgQCBHQOHNydqkBAgQ\n" +
                    "IECAAIFIAYU3MnZDEyBAgAABAgRyBBTenKxNSoAAAQIECBCIFFB4I2M3NAECBAgQIEAgR0Dhzcna\n" +
                    "pAQIECBAgACBSAGFNzJ2QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQiBRQeCNjNzQBAgQIECBAIEdA\n" +
                    "4c3J2qQECBAgQIAAgUgBhTcydkMTIECAAAECBHIEFN6crE1KgAABAgQIEIgUUHgjYzc0AQIECBAg\n" +
                    "QCBHQOHNydqkBAgQIECAAIFIAYU3MnZDEyBAgAABAgRyBBTenKxNSoAAAQIECBCIFFB4I2M3NAEC\n" +
                    "BAgQIEAgR0DhzcnapAQIECBAgACBSAGFNzJ2QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQiBRQeCNj\n" +
                    "NzQBAgQIECBAIEdA4c3J2qQECBAgQIAAgUgBhTcydkMTIECAAAECBHIEFN6crE1KgAABAgQIEIgU\n" +
                    "UHgjYzc0AQIECBAgQCBHQOHNydqkBAgQIECAAIFIAYU3MnZDEyBAgAABAgRyBBTenKxNSoAAAQIE\n" +
                    "CBCIFFB4I2M3NAECBAgQIEAgR0DhzcnapAQIECBAgACBSAGFNzJ2QxMgQIAAAQIEcgQU3pysTUqA\n" +
                    "AAECBAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J2qQECBAgQIAAgUgBhTcydkMTIECAAAECBHIEFN6c\n" +
                    "rE1KgAABAgQIEIgUUHgjYzc0AQIECBAgQCBHQOHNydqkBAgQIECAAIFIAYU3MnZDEyBAgAABAgRy\n" +
                    "BBTenKxNSoAAAQIECBCIFFB4I2M3NAECBAgQIEAgR0DhzcnapAQIECBAgACBSAGFNzJ2QxMgQIAA\n" +
                    "AQIEcgQU3pysTUqAAAECBAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J2qQECBAgQIAAgUgBhTcydkMT\n" +
                    "IECAAAECBHIEFN6crE1KgAABAgQIEIgUUHgjYzc0AQIECBAgQCBHQOHNydqkBAgQIECAAIFIAYU3\n" +
                    "MnZDEyBAgAABAgRyBBTenKxNSoAAAQIECBCIFFB4I2M3NAECBAgQIEAgR0DhzcnapAQIECBAgACB\n" +
                    "SAGFNzJ2QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J2qQECBAg\n" +
                    "QIAAgUgBhTcydkMTIECAAAECBHIEFN6crE1KgAABAgQIEIgUUHgjYzc0AQIECBAgQCBHQOHNydqk\n" +
                    "BAgQIECAAIFIAYU3MnZDEyBAgAABAgRyBBTenKxNSoAAAQIECBCIFFB4I2M3NAECBAgQIEAgR0Dh\n" +
                    "zcnapAQIECBAgACBSAGFNzJ2QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQiBRQeCNjNzQBAgQIECBA\n" +
                    "IEdA4c3J2qQECBAgQIAAgUgBhTcydkMTIECAAAECBHIEFN6crE1KgAABAgQIEIgUUHgjYzc0AQIE\n" +
                    "CBAgQCBHQOHNydqkBAgQIECAAIFIAYU3MnZDEyBAgAABAgRyBBTenKxNSoAAAQIECBCIFFB4I2M3\n" +
                    "NAECBAgQIEAgR0DhzcnapAQIECBAgACBSAGFNzJ2QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQiBRQ\n" +
                    "eCNjNzQBAgQIECBAIEdA4c3J2qQECBAgQIAAgUgBhTcydkMTIECAAAECBHIEFN6crE1KgAABAgQI\n" +
                    "EIgUUHgjYzc0AQIECBAgQCBHQOHNydqkBAgQIECAAIFIAYU3MnZDEyBAgAABAgRyBBTenKxNSoAA\n" +
                    "AQIECBCIFFB4I2M3NAECBAgQIEAgR0DhzcnapAQIECBAgACBSAGFNzJ2QxMgQIAAAQIEcgQU3pys\n" +
                    "TUqAAAECBAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J2qQECBAgQIAAgUgBhTcydkMTIECAAAECBHIE\n" +
                    "FN6crE1KgAABAgQIEIgUUHgjYzc0AQIECBAgQCBHQOHNydqkBAgQIECAAIFIAYU3MnZDEyBAgAAB\n" +
                    "AgRyBBTenKxNSoAAAQIECBCIFFB4I2M3NAECBAgQIEAgR0DhzcnapAQIECBAgACBSAGFNzJ2QxMg\n" +
                    "QIAAAQIEcgQU3pysTUqAAAECBAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J2qQECBAgQIAAgUgBhTcy\n" +
                    "dkMTIECAAAECBHIEFN6crE1KgAABAgQIEIgUUHgjYzc0AQIECBAgQCBHQOHNydqkBAgQIECAAIFI\n" +
                    "AYU3MnZDEyBAgAABAgRyBBTenKxNSoAAAQIECBCIFFB4I2M3NAECBAgQIEAgR0DhzcnapAQIECBA\n" +
                    "gACBSAGFNzJ2QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J2qQE\n" +
                    "CBAgQIAAgUgBhTcydkMTIECAAAECBHIEFN6crE1KgAABAgQIEIgUUHgjYzc0AQIECBAgQCBHQOHN\n" +
                    "ydqkBAgQIECAAIFIAYU3MnZDEyBAgAABAgRyBBTenKxNSoAAAQIECBCIFFB4I2M3NAECBAgQIEAg\n" +
                    "R0DhzcnapAQIECBAgACBSAGFNzJ2QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQiBRQeCNjNzQBAgQI\n" +
                    "ECBAIEdA4c3J2qQECBAgQIAAgUgBhTcydkMTIECAAAECBHIEFN6crE1KgAABAgQIEIgUUHgjYzc0\n" +
                    "AQIECBAgQCBHQOHNydqkBAgQIECAAIFIAYU3MnZDEyBAgAABAgRyBBTenKxNSoAAAQIECBCIFFB4\n" +
                    "I2M3NAECBAgQIEAgR0DhzcnapAQIECBAgACBSAGFNzJ2QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQ\n" +
                    "iBRQeCNjNzQBAgQIECBAIEdA4c3J2qQECBAgQIAAgUgBhTcydkMTIECAAAECBHIEFN6crE1KgAAB\n" +
                    "AgQIEIgUUHgjYzc0AQIECBAgQCBHQOHNydqkBAgQIECAAIFIAYU3MnZDEyBAgAABAgRyBBTenKxN\n" +
                    "SoAAAQIECBCIFFB4I2M3NAECBAgQIEAgR0DhzcnapAQIECBAgACBSAGFNzJ2QxMgQIAAAQIEcgQU\n" +
                    "3pysTUqAAAECBAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J2qQECBAgQIAAgUgBhTcydkMTIECAAAEC\n" +
                    "BHIEFN6crE1KgAABAgQIEIgUUHgjYzc0AQIECBAgQCBHQOHNydqkBAgQIECAAIFIAYU3MnZDEyBA\n" +
                    "gAABAgRyBBTenKxNSoAAAQIECBCIFFB4I2M3NAECBAgQIEAgR0DhzcnapAQIECBAgACBSAGFNzJ2\n" +
                    "QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J2qQECBAgQIAAgUgB\n" +
                    "hTcydkMTIECAAAECBHIEFN6crE1KgAABAgQIEIgUUHgjYzc0AQIECBAgQCBHQOHNydqkBAgQIECA\n" +
                    "AIFIAYU3MnZDEyBAgAABAgRyBBTenKxNSoAAAQIECBCIFFB4I2M3NAECBAgQIEAgR0DhzcnapAQI\n" +
                    "ECBAgACBSAGFNzJ2QxMgQIAAAQIEcgQU3pysTUqAAAECBAgQiBRQeCNjNzQBAgQIECBAIEdA4c3J\n" +
                    "2qQECBAgQIAAgUgBhTcydkMTIECAAAECBHIEFN6crE1KgAABAgQIEIgUUHgjYzc0AQIECBAgQCBH\n" +
                    "QOHNydqkBAgQIECAAIFIAYU3MnZDEyBAgAABAgRyBP4DQatMRMd4vtAAAAAASUVORK5CYII=";

            contentValues.put("Torso", torso64Zero);
            db.update("Avatar", contentValues, "email=?", new String[]{Email});
        }

    }


    public Cursor getData(String sql) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql, null);
    }


    //converts to bytes
    public byte[] read(File file) throws IOException {
        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        } finally {
            try {
                if (ous != null)
                    ous.close();
            } catch (IOException e) {
            }

            try {
                if (ios != null)
                    ios.close();
            } catch (IOException e) {
            }
        }
        return ous.toByteArray();
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
        if (cursor.moveToLast()) {
            steps = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return steps;
    }


    public void updateHead(String Email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

            String yellowhead64 = "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qltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8FjtndIAABLnSURBVKbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9AYO317nEBAgQIECAAIGUgMGbqltYAgQIECBAgEBPwODtdS4xAQIECBAgQCAlYPCm6haWAAECBAgQINATMHh7nUtMgAABAgQIEEgJGLypuoUlQIAAAQIECPQEDN5e5xITIECAAAECBFICBm+qbmEJECBAgAABAj0Bg7fXucQECBAgQIAAgZSAwZuqW1gCBAgQIECAQE/A4O11LjEBAgQIECBAICVg8KbqFpYAAQIECBAg0BMweHudS0yAAAECBAgQSAkYvKm6hSVAgAABAgQI9AQM3l7nEhMgQIAAAQIEUgIGb6puYQkQIECAAAECPQGDt9e5xAQIECBAgACBlIDBm6pbWAIECBAgQIBAT8Dg7XUuMQECBAgQIEAgJWDwpuoWlgABAgQIECDQEzB4e51LTIAAAQIECBBICRi8qbqFJUCAAAECBAj0BAzeXucSEyBAgAABAgRSAgZvqm5hCRAgQIAAAQI9gf8BiRluCJoWNxYAAAAASUVORK5CYII=";
            contentValues.put("Head", yellowhead64);
            db.update("Avatar", contentValues, "email=?", new String[]{Email});

    }
}
