import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class KitchenDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "kitchen.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
