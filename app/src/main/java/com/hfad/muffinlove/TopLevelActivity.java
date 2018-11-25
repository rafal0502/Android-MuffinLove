package com.hfad.muffinlove;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.List;


public class TopLevelActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor favoritesCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);

        //Tworzę obiekt nasluchujacy OnitemClickListener
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long id) {
                if (position == 0){
                    Intent intent = new Intent(TopLevelActivity.this, MuffinCategoryActivity.class);
                    startActivity(intent);
                }
            }
        };
        ListView listView = (ListView) findViewById(R.id.list_options);
        listView.setOnItemClickListener(itemClickListener);

        //Zapisyje na liscie list_favorites ulubione napoje uzytkownika
        ListView listFavorites = (ListView)findViewById(R.id.list_favorites);
        try{
            SQLiteOpenHelper muffinDatabaseHelper = new MuffinDatabaseHelper(this);
            db = muffinDatabaseHelper.getReadableDatabase();
            favoritesCursor = db.query("MUFFIN",new String[]{"_id","NAME"},"FAVORITE = 1", null,null,null,null);
            CursorAdapter favoriteAdapter = new SimpleCursorAdapter(TopLevelActivity.this, android.R.layout.simple_list_item_1,favoritesCursor,
                    new String[]{"NAME"}, new int[]{android.R.id.text1},0);
            listFavorites.setAdapter(favoriteAdapter);
        } catch(SQLiteException e){
            Toast toast = Toast.makeText(this, "Baza danych napotkala problem", Toast.LENGTH_SHORT);
            toast.show();
        }

        // Po kliknieciu ulubionego muffina przechodzimy do aktywnosci MuffinActivity
        listFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TopLevelActivity.this, MuffinActivity.class);
                intent.putExtra(MuffinActivity.EXTRA_MUFFINNO, (int)id);
                startActivity(intent);
            }
        });
    }

    // Zamykam kursor i baze
    @Override
    public void onDestroy(){
        super.onDestroy();
        favoritesCursor.close();
        db.close();
    }

    // Metoda wywolywana, gdy uzytkownik wraca do aktywnosci TopLevelActivity

    public void onRestart()
    {
        super.onRestart();
        try{
            //tworzenie nowego kursora
            MuffinDatabaseHelper muffinDatabaseHelper = new MuffinDatabaseHelper(this);
            db = muffinDatabaseHelper.getReadableDatabase();
            Cursor newCursor = db.query("MUFFIN", new String[] {"_id","NAME"},
                    "FAVORITE=1", null, null, null, null);
            //Pobieram adapter CursorAdapter uzywany przez ListView
            ListView listFavorites = (ListView)findViewById(R.id.list_favorites);
            CursorAdapter adapter = (CursorAdapter) listFavorites.getAdapter();
            //Zamieniam kursor uzywany przez adapter CursorAdapter na nowy
            adapter.changeCursor(newCursor);
            favoritesCursor = newCursor;
        } catch(SQLiteException e){
            Toast toast = Toast.makeText(this,"Baza danych jest niedostepna", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}


