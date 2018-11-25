package com.hfad.muffinlove;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class MuffinActivity extends Activity {

    public static final String EXTRA_MUFFINNO = "muffinno";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muffin);

        //Pobieram identyfikator muffina z intencji
        int muffinno = (Integer) getIntent().getExtras().get(EXTRA_MUFFINNO);

        //Tworze kursor
        try {
            SQLiteOpenHelper muffinDatabaseHelper = new MuffinDatabaseHelper(this);
            SQLiteDatabase db = muffinDatabaseHelper.getWritableDatabase(); //Do aktualizacji bazy potrzebny dostep w trybie odczytu i zapisu
            Cursor cursor = db.query("MUFFIN",
                    new String[]{"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"},
                    "_id = ?",
                    new String[]{Integer.toString(muffinno)}, null, null, null);

            if (cursor.moveToFirst()) {
                String nameText = cursor.getString(0);
                String descirptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);

                boolean isFavorite = (cursor.getInt(3) == 1);

                //Wyswietlam nazwe muffina
                TextView name = (TextView) findViewById(R.id.name);
                name.setText(nameText);

                //Wyswietlam opis muffina
                TextView description = (TextView) findViewById(R.id.description);
                description.setText(descirptionText);

                //Wyswietlam zdjecie muffina
                ImageView photo = (ImageView) findViewById(R.id.photo);
                photo.setImageResource(photoId);
                photo.setContentDescription(nameText);

                //Zaznaczam pole wyboru ulubiongo muffina
                CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
                favorite.setChecked(isFavorite);

            };
            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Baza danych jest niedostępna", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

        //Aktualizacja bazy danych po kliknieciu pola wyboru
        public void onFavoriteClicked(View view){
            int muffinno = (Integer)getIntent().getExtras().get(EXTRA_MUFFINNO);
            CheckBox favorite = (CheckBox)findViewById(R.id.favorite);
            ContentValues muffinValues = new ContentValues();
            // Dodaje wartosc pola wyboru favorite do obiektu drinkValues typu ContentValues
            muffinValues.put("FAVORITE",favorite.isChecked());

            SQLiteOpenHelper muffinDatabaseHelper = new MuffinDatabaseHelper(MuffinActivity.this);
            try{
                SQLiteDatabase db = muffinDatabaseHelper.getWritableDatabase();
                db.update("MUFFIN", muffinValues, "_id = ?", new String[] {Integer.toString(muffinno)});
                db.close();
            } catch (SQLiteException e){
                Toast toast = Toast.makeText(this, "Baza danych jest niedostepna", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
}

