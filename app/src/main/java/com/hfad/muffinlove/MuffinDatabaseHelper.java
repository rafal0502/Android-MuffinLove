package com.hfad.muffinlove;


import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;



public class MuffinDatabaseHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "muffin"; // Nazwa bazy
    private static final int DB_VERSION = 1; // Numer wersji bazy  danych

    MuffinDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        updateMyDatabase(db, oldVersion, newVersion);
    }


    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion){
        if (oldVersion < 1){
            db.execSQL("CREATE TABLE MUFFIN (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "NAME TEXT," + "DESCRIPTION TEXT," + "IMAGE_RESOURCE_ID INTEGER);");
            insertMuffin(db,"Muffin brownie z gruszkami","Rewelacyjne babeczki brownie - " +
                    "pieczone w papilotkach z zatopioną w środku gruszką. ",R.drawable.gruszka);
            insertMuffin(db,"Muffin porzeczkowy z porzeczkowym lukrem","Nadają się na wszelkiego rodzaju imprezy na świeżym powietrzu, " +
                    "dodatkowo lukier chroni ciasto babeczkowe przed wysychaniem. ",R.drawable.porzeczka);
            insertMuffin(db,"Muffin szpinakowo jogurtowy","Babeczki z kremem śmietankowym" +
                    " i udekorowane świeżymi truskawkami. Są wilgotne, mięciutkie, lekko cytrynowe.", R.drawable.szpinak);
            insertMuffin(db,"Muffin czekoladowy z solonym karmelem","Bezbłędne babeczki czekoladowe z kremem o smaku solonego karmelu.",R.drawable.karmel);
        }
        if (oldVersion < 2){
            db.execSQL("ALTER TABLE MUFFIN ADD COLUMN FAVORITE NUMERIC;");
        }
    }



    private static void insertMuffin(SQLiteDatabase db, String name, String description, int resourceId){
        ContentValues muffinValues = new ContentValues();
        muffinValues.put("NAME", name);
        muffinValues.put("DESCRIPTION", description);
        muffinValues.put("IMAGE_RESOURCE_ID", resourceId);
        db.insert("MUFFIN",null,muffinValues);
    }




}
