package com.example.cocolocokitchen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
    public static final String RECIPE_COLUMN_NAME = "name";
    public static final String RECIPE_COLUMN_DESCRIPTION = "description";
    public static final String RECIPE_COLUMN_PREP_TIME = "prep_time";
    public static final String RECIPE_COLUMN_PRICE_LEVEL = "price_level";
    public static final String RECIPE_COLUMN_SERVINGS = "servings";
    public static final String RECIPE_COLUMN_SOURCE = "source";

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

    //Initialize image table
    public static final String IMAGE_TABLE_NAME = "images";
    public static final String IMAGE_COLUMN_ID = "id_image";
    public static final String IMAGE_COLUMN_LOP = "link_or_png";
    public static final String IMAGE_COLUMN_DISPLAY = "display_order";

    //Initialize fav table
    public static final String FAV_TABLE_NAME = "favorites";
    public static final String FAV_COLUMN_ID = "id_favorite";

    //Initialize calendar table
    public static final String PLANNING_TABLE_NAME = "planning";
    public static final String PLANNING_COLUMN_ID = "id_planning";
    public static final String PLANNING_COLUMN_DATE = "date";
    public static final String PLANNING_COLUMN_TIME = "time_of_day";

    //Initialize grocery table
    public static final String GROCERY_TABLE_NAME = "groceries";
    public static final String GROCERY_COLUMN_ID = "id_grocery";
    public static final String GROCERY_COLUMN_NAME = "name";
    public static final String GROCERY_COLUMN_QUANTITY = "quantity";
    public static final String GROCERY_COLUMN_UNIT = "unit";
    public static final String GROCERY_COLUMN_CHECKED = "checked"; // 0 = not bought, 1 = bought

    //Initialize junction tables
    public static final String RECIPE_INGREDIENT_NAME = "Recipe_Ingredient";
    public static final String RECIPE_INGREDIENT_QUANTITY = "quantity";

    public static final String RECIPE_UTENSIL_NAME = "Recipe_Utensil";
    public static final String RECIPE_UTENSIL_QUANTITY = "quantity";

    public static final String RECIPE_TAG_NAME = "Recipe_Tag";

    public static final String RECIPE_GROUP_NAME = "Recipe_Group";




    //Create the tables
    public static final String RECIPE_TABLE_CREATE =
            "CREATE TABLE " + RECIPE_TABLE_NAME + " (" +
                    RECIPE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RECIPE_COLUMN_NAME + " TEXT, " +
                    RECIPE_COLUMN_DESCRIPTION + " TEXT, " +
                    RECIPE_COLUMN_SOURCE + " TEXT, " +
                    RECIPE_COLUMN_PREP_TIME + " INTEGER, " +
                    RECIPE_COLUMN_PRICE_LEVEL + " TEXT, " +
                    RECIPE_COLUMN_SERVINGS + " INTEGER " +
                    ");";

    public static final String STEP_TABLE_CREATE =
            "CREATE TABLE " + STEP_TABLE_NAME + " (" +
                    STEP_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    STEP_COLUMN_TITLE + " TEXT, " +
                    STEP_COLUMN_DESCRIPTION + " TEXT, " +
                    STEP_COLUMN_NUMBER + " INTEGER, " +
                    RECIPE_COLUMN_ID + " INTEGER, " +
                    "FOREIGN KEY (" + RECIPE_COLUMN_ID + ") REFERENCES " + RECIPE_TABLE_NAME + "(" + RECIPE_COLUMN_ID + ")" +
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
            "CREATE TABLE \"" + GROUP_TABLE_NAME + "\" (" +
                    GROUP_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    GROUP_COLUMN_NAME + " TEXT, " +
                    GROUP_COLUMN_COLOR + " TEXT" +
                    ");";

    public static final String RATING_TABLE_CREATE =
            "CREATE TABLE " + RATING_TABLE_NAME + " (" +
                    RATING_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RATING_COLUMN_VALUE + " INTEGER," +
                    RATING_COLUMN_COMMENT + " TEXT, " +
                    RECIPE_COLUMN_ID + " INTEGER UNIQUE, " +
                    "FOREIGN KEY (" + RECIPE_COLUMN_ID + ") REFERENCES " + RECIPE_TABLE_NAME + "(" + RECIPE_COLUMN_ID + ")" +
                    ");";

    public static final String IMAGE_TABLE_CREATE =
            "CREATE TABLE " + IMAGE_TABLE_NAME + " (" +
                    IMAGE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    IMAGE_COLUMN_LOP + " TEXT," +
                    IMAGE_COLUMN_DISPLAY + " INTEGER, " +
                    RECIPE_COLUMN_ID + " INTEGER, " +
                    "FOREIGN KEY (" + RECIPE_COLUMN_ID + ") REFERENCES " + RECIPE_TABLE_NAME + "(" + RECIPE_COLUMN_ID + ")" +
                    ");";

    public static final String FAV_TABLE_CREATE =
            "CREATE TABLE " + FAV_TABLE_NAME + " (" +
                    FAV_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RECIPE_COLUMN_ID + " INTEGER UNIQUE, " +
                    "FOREIGN KEY (" + RECIPE_COLUMN_ID + ") REFERENCES " + RECIPE_TABLE_NAME + "(" + RECIPE_COLUMN_ID + ")" +
                    ");";

    public static final String PLANNING_TABLE_CREATE =
            "CREATE TABLE " + PLANNING_TABLE_NAME + " (" +
                    PLANNING_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PLANNING_COLUMN_DATE + " TEXT, " +
                    PLANNING_COLUMN_TIME + " TEXT, " +
                    RECIPE_COLUMN_ID + " INTEGER, " +
                    "FOREIGN KEY (" + RECIPE_COLUMN_ID + ") REFERENCES " + RECIPE_TABLE_NAME + "(" + RECIPE_COLUMN_ID + ")" +
                    ");";

    public static final String GROCERY_TABLE_CREATE =
            "CREATE TABLE " + GROCERY_TABLE_NAME + " (" +
                    GROCERY_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    GROCERY_COLUMN_NAME + " TEXT, " +
                    GROCERY_COLUMN_QUANTITY + " REAL, " +
                    GROCERY_COLUMN_UNIT + " TEXT, " +
                    GROCERY_COLUMN_CHECKED + " INTEGER DEFAULT 0" +
                    ");";

    //Junction tables
    public static final String RECIPE_INGREDIENT_TABLE =
            "CREATE TABLE " + RECIPE_INGREDIENT_NAME + " (" +
                    RECIPE_COLUMN_ID + " INTEGER, " +
                    INGREDIENT_COLUMN_ID + " INTEGER," +
                    RECIPE_INGREDIENT_QUANTITY + " REAL, " +
                    "PRIMARY KEY (" + RECIPE_COLUMN_ID + ", " + INGREDIENT_COLUMN_ID + ")," +
                    "FOREIGN KEY (" + RECIPE_COLUMN_ID + ") REFERENCES " + RECIPE_TABLE_NAME + "(" + RECIPE_COLUMN_ID + ")," +
                    "FOREIGN KEY (" + INGREDIENT_COLUMN_ID + ") REFERENCES " + INGREDIENT_TABLE_NAME + "(" + INGREDIENT_COLUMN_ID + ")" +
                    ");";

    public static final String RECIPE_UTENSIL_TABLE =
            "CREATE TABLE " + RECIPE_UTENSIL_NAME + " (" +
                    RECIPE_COLUMN_ID + " INTEGER, " +
                    UTENSIL_COLUMN_ID + " INTEGER," +
                    RECIPE_UTENSIL_QUANTITY + " REAL, " +
                    "PRIMARY KEY (" + RECIPE_COLUMN_ID + ", " + UTENSIL_COLUMN_ID + ")," +
                    "FOREIGN KEY (" + RECIPE_COLUMN_ID + ") REFERENCES " + RECIPE_TABLE_NAME + "(" + RECIPE_COLUMN_ID + ")," +
                    "FOREIGN KEY (" + UTENSIL_COLUMN_ID + ") REFERENCES " + UTENSIL_TABLE_NAME + "(" + UTENSIL_COLUMN_ID + ")" +
                    ");";

    public static final String RECIPE_TAG_TABLE =
            "CREATE TABLE " + RECIPE_TAG_NAME + " (" +
                    RECIPE_COLUMN_ID + " INTEGER, " +
                    TAG_COLUMN_ID + " INTEGER," +
                    "PRIMARY KEY (" + RECIPE_COLUMN_ID + ", " + TAG_COLUMN_ID + ")," +
                    "FOREIGN KEY (" + RECIPE_COLUMN_ID + ") REFERENCES " + RECIPE_TABLE_NAME + "(" + RECIPE_COLUMN_ID + ")," +
                    "FOREIGN KEY (" + TAG_COLUMN_ID + ") REFERENCES " + TAG_TABLE_NAME + "(" + TAG_COLUMN_ID + ")" +
                    ");";

    public static final String RECIPE_GROUP_TABLE =
            "CREATE TABLE " + RECIPE_GROUP_NAME + " (" +
                    RECIPE_COLUMN_ID + " INTEGER, " +
                    GROUP_COLUMN_ID + " INTEGER," +
                    "PRIMARY KEY (" + RECIPE_COLUMN_ID + ", " + GROUP_COLUMN_ID + ")," +
                    "FOREIGN KEY (" + RECIPE_COLUMN_ID + ") REFERENCES " + RECIPE_TABLE_NAME + "(" + RECIPE_COLUMN_ID + ")," +
                    "FOREIGN KEY (" + GROUP_COLUMN_ID + ") REFERENCES \"" + GROUP_TABLE_NAME + "\" (" + GROUP_COLUMN_ID + ")" +
                    ");";




    //Constructor
    public KitchenDB ( Context context ) {
        super ( context , DATABASE_NAME , null , DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(RECIPE_TABLE_CREATE);

        // Insert initial dummy recipe(s)
        sqLiteDatabase.execSQL("INSERT INTO " + RECIPE_TABLE_NAME + " (" +
                RECIPE_COLUMN_NAME + ", " +
                RECIPE_COLUMN_DESCRIPTION + ", " +
                RECIPE_COLUMN_SERVINGS + ", " +
                RECIPE_COLUMN_PREP_TIME + ", " +
                RECIPE_COLUMN_PRICE_LEVEL + ", " +
                RECIPE_COLUMN_SOURCE + ") VALUES (" +
                "'Cinnamon Rolls'," +
                "'Delicious cinnamon rolls with icing.'," +
                "4," +
                "30," +
                "'$$'," +
                "'Grandma’s Kitchen');");

        sqLiteDatabase.execSQL("INSERT INTO " + RECIPE_TABLE_NAME + " (" +
                RECIPE_COLUMN_NAME + ", " +
                RECIPE_COLUMN_DESCRIPTION + ", " +
                RECIPE_COLUMN_SERVINGS + ", " +
                RECIPE_COLUMN_PREP_TIME + ", " +
                RECIPE_COLUMN_PRICE_LEVEL + ", " +
                RECIPE_COLUMN_SOURCE + ") VALUES (" +
                "'Avocado Toast'," +
                "'Crunchy toast with smashed avocado and toppings.'," +
                "2," +
                "10," +
                "'$'," +
                "'Instagram');");

        sqLiteDatabase.execSQL(STEP_TABLE_CREATE);
        sqLiteDatabase.execSQL(INGREDIENT_TABLE_CREATE);
        sqLiteDatabase.execSQL(UTENSIL_TABLE_CREATE);
        sqLiteDatabase.execSQL(TAG_TABLE_CREATE);
        sqLiteDatabase.execSQL(GROUP_TABLE_CREATE);
        sqLiteDatabase.execSQL(RATING_TABLE_CREATE);
        sqLiteDatabase.execSQL(IMAGE_TABLE_CREATE);
        sqLiteDatabase.execSQL(FAV_TABLE_CREATE);
        sqLiteDatabase.execSQL(PLANNING_TABLE_CREATE);
        sqLiteDatabase.execSQL(GROCERY_TABLE_CREATE);
        sqLiteDatabase.execSQL(RECIPE_INGREDIENT_TABLE);
        sqLiteDatabase.execSQL(RECIPE_UTENSIL_TABLE);
        sqLiteDatabase.execSQL(RECIPE_TAG_TABLE);
        sqLiteDatabase.execSQL(RECIPE_GROUP_TABLE);
    }

    @Override

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RECIPE_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + STEP_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + INGREDIENT_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UTENSIL_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TAG_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS \"" + GROUP_TABLE_NAME + "\"");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RATING_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + IMAGE_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FAV_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PLANNING_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RECIPE_INGREDIENT_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RECIPE_UTENSIL_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RECIPE_TAG_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RECIPE_GROUP_NAME);

        onCreate(sqLiteDatabase);
    }




    public Cursor getAllRecipes() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + RECIPE_TABLE_NAME, null);
    }

    private long getOrInsertIngredientId(SQLiteDatabase sqLiteDatabase, Ingredient ingredient) {
        Cursor cursor = sqLiteDatabase.query(
                INGREDIENT_TABLE_NAME,
                new String[]{INGREDIENT_COLUMN_ID},
                INGREDIENT_COLUMN_NAME + " = ? AND " + INGREDIENT_COLUMN_UNIT + " = ?",
                new String[]{ingredient.getName(), String.valueOf(ingredient.getQuantity())},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            long id = cursor.getLong(0);
            cursor.close();
            return id;
        } else {
            cursor.close();
            ContentValues values = new ContentValues();
            values.put(INGREDIENT_COLUMN_NAME, ingredient.getName());
            values.put(INGREDIENT_COLUMN_UNIT, ingredient.getQuantityType());
            return sqLiteDatabase.insert(INGREDIENT_TABLE_NAME, null, values);
        }
    }

    private long getOrInsertUtensilId(SQLiteDatabase db, String name) {
        Cursor cursor = db.query(
                UTENSIL_TABLE_NAME,
                new String[]{UTENSIL_COLUMN_ID},
                UTENSIL_COLUMN_NAME + " = ?",
                new String[]{name},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            long id = cursor.getLong(0);
            cursor.close();
            return id;
        } else {
            cursor.close();
            ContentValues values = new ContentValues();
            values.put(UTENSIL_COLUMN_NAME, name);
            return db.insert(UTENSIL_TABLE_NAME, null, values);
        }
    }

    public void insertRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues recipeValues = new ContentValues();
            recipeValues.put(RECIPE_COLUMN_NAME, recipe.getTitle());
            recipeValues.put(RECIPE_COLUMN_DESCRIPTION, recipe.getDescription());
            recipeValues.put(RECIPE_COLUMN_SERVINGS, recipe.getNumberOfServing());
            recipeValues.put(RECIPE_COLUMN_PREP_TIME, recipe.getTimeInMinute());
            recipeValues.put(RECIPE_COLUMN_PRICE_LEVEL, recipe.getCostDegree());
//            recipeValues.put(RECIPE_COLUMN_SOURCE, recipe.getSource());

            long recipeId = db.insert(RECIPE_TABLE_NAME, null, recipeValues);

            // Insert ingredients and link in junction table
            for (Ingredient ingredient : recipe.getIngredientList()) {
                long ingId = getOrInsertIngredientId(db, ingredient);
                ContentValues linkValues = new ContentValues();
                linkValues.put(RECIPE_COLUMN_ID, recipeId);
                linkValues.put(INGREDIENT_COLUMN_ID, ingId);
                linkValues.put(RECIPE_INGREDIENT_QUANTITY, ingredient.getQuantityType());
                db.insert(RECIPE_INGREDIENT_NAME, null, linkValues);
            }

            // Insert utensils and link in junction table
            for (Utensil utensil : recipe.getUtensilList()) {
                long utId = getOrInsertUtensilId(db, utensil.getName());
                ContentValues linkValues = new ContentValues();
                linkValues.put(RECIPE_COLUMN_ID, recipeId);
                linkValues.put(UTENSIL_COLUMN_ID, utId);
                linkValues.put(RECIPE_UTENSIL_QUANTITY, utensil.getQuantity());
                db.insert(RECIPE_UTENSIL_NAME, null, linkValues);
            }

            // Insert steps
            int stepNumber = 1;
            for (Step step : recipe.getSteps()) {
                ContentValues stepValues = new ContentValues();
                stepValues.put(STEP_COLUMN_TITLE, step.getName());
                stepValues.put(STEP_COLUMN_DESCRIPTION, step.getDescription());
                stepValues.put(STEP_COLUMN_NUMBER, stepNumber++);
                stepValues.put(RECIPE_COLUMN_ID, recipeId);
                db.insert(STEP_TABLE_NAME, null, stepValues);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }
}
