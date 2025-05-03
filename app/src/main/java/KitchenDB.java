import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class KitchenDB extends SQLiteOpenHelper {
    //Create the database
    private static final String DATABASE_NAME = "kitchen.db";
    private static final int DATABASE_VERSION = 1;

    //Initialize the recipes table
    public static final String RECIPE_TABLE_NAME = "recipes";
    public static final String RECIPE_COLUMN_ID = "id_recipe";
    public static final String RECIPE_COLUMN_PREP_TIME = "prep_time";
    public static final String RECIPE_COLUMN_PRICE_LEVEL = "price_level";
    public static final String RECIPE_COLUMN_SERVINGS = "servings";
    public static final String RECIPE_COLUMN_ISFAV = "isfav";

    //Initialize the steps table
    public static final String STEP_TABLE_NAME = "steps";
    public static final String STEP_COLUMN_ID = "id_step";
    public static final String STEP_COLUMN_TITLE = "title";
    public static final String STEP_COLUMN_NUMBER = "step_number";
    public static final String STEP_COLUMN_DESCRIPTION = "description";

    //Initialize the ingredients table
    public static final String INGREDIENT_TABLE_NAME = "ingredients";
    public static final String INGREDIENT_COLUMN_ID = "id_ingredient";
    public static final String INGREDIENT_COLUMN_NAME = "name";
    public static final String INGREDIENT_COLUMN_UNIT = "unit";

    //Initialize the utensil table
    public static final String UTENSIL_TABLE_NAME = "utensils";
    public static final String UTENSIL_COLUMN_ID = "id_utensil";
    public static final String UTENSIL_COLUMN_NAME = "name";

    //Initialize the tag table
    public static final String TAG_TABLE_NAME = "tags";
    public static final String TAG_COLUMN_ID = "id_tag";
    public static final String TAG_COLUMN_NAME = "name";
    public static final String TAG_COLUMN_COLOR = "color";

    //Initialize the group table
    public static final String GROUP_TABLE_NAME = "groups";
    public static final String GROUP_COLUMN_ID = "id_group";
    public static final String GROUP_COLUMN_NAME = "name";
    public static final String GROUP_COLUMN_COLOR = "color";

    //Initialize the rating table
    public static final String RATING_TABLE_NAME = "ratings";
    public static final String RATING_COLUMN_ID = "id_rating";
    public static final String RATING_COLUMN_VALUE = "value";
    public static final String RATING_COLUMN_COMMENT = "comment";

    //Create the tables
    public static final String RECIPE_TABLE_CREATE =
            "CREATE TABLE " + RECIPE_TABLE_NAME + " (" +
                    RECIPE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RECIPE_COLUMN_PREP_TIME + " INTEGER, " +
                    RECIPE_COLUMN_PRICE_LEVEL + " TEXT, " +
                    RECIPE_COLUMN_SERVINGS + " INTEGER, " +
                    RECIPE_COLUMN_ISFAV + " INTEGER" +
                    ");";

    public static final String STEP_TABLE_CREATE =
            "CREATE TABLE " + STEP_TABLE_NAME + " (" +
                    STEP_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    STEP_COLUMN_TITLE + " TEXT, " +
                    STEP_COLUMN_DESCRIPTION + " TEXT, " +
                    STEP_COLUMN_NUMBER + " INTEGER" +
                    ");";

    public static final String INGREDIENT_TABLE_CREATE =
            "CREATE TABLE " + INGREDIENT_TABLE_NAME + " (" +
                    INGREDIENT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    INGREDIENT_COLUMN_NAME + " TEXT, " +
                    INGREDIENT_COLUMN_UNIT + " TEXT" +
                    ");";

    public static final String UTENSIL_TABLE_CREATE =
            "CREATE TABLE " + UTENSIL_TABLE_NAME + " (" +
                    UTENSIL_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    UTENSIL_COLUMN_NAME + " TEXT" +
                    ");";

    public static final String TAG_TABLE_CREATE =
            "CREATE TABLE " + TAG_TABLE_NAME + " (" +
                    TAG_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TAG_COLUMN_NAME + " TEXT, " +
                    TAG_COLUMN_COLOR + " TEXT" +
                    ");";

    public static final String GROUP_TABLE_CREATE =
            "CREATE TABLE " + GROUP_TABLE_NAME + " (" +
                    GROUP_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    GROUP_COLUMN_NAME + " TEXT, " +
                    GROUP_COLUMN_COLOR + " TEXT" +
                    ");";

    public static final String RATING_TABLE_CREATE =
            "CREATE TABLE " + RATING_TABLE_NAME + " (" +
                    RATING_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RATING_COLUMN_VALUE + " INTEGER," +
                    RATING_COLUMN_COMMENT + " TEXT" +
                    ");";

    //Constructor
    public KitchenDB ( Context context ) {
        super ( context , DATABASE_NAME , null , DATABASE_VERSION );
    }

    public KitchenDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(RECIPE_TABLE_CREATE);
        sqLiteDatabase.execSQL(STEP_TABLE_CREATE);
        sqLiteDatabase.execSQL(INGREDIENT_TABLE_CREATE);
        sqLiteDatabase.execSQL(UTENSIL_TABLE_CREATE);
        sqLiteDatabase.execSQL(TAG_TABLE_CREATE);
        sqLiteDatabase.execSQL(GROUP_TABLE_CREATE);
        sqLiteDatabase.execSQL(RATING_TABLE_CREATE);
    }

    @Override

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RECIPE_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + STEP_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + INGREDIENT_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UTENSIL_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TAG_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GROUP_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RATING_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
