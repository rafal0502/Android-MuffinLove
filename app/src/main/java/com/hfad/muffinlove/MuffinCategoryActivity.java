package com.hfad.muffinlove;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MuffinCategoryActivity extends ListActivity {
    private SQLiteDatabase db;
    private Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ListView listMuffins = getListView();

        try{
            SQLiteOpenHelper muffinDatabaseHelper = new MuffinDatabaseHelper(this);
            // pobieram referencje do bazy danych
            db = muffinDatabaseHelper.getReadableDatabase();
            cursor = db.query("MUFFIN", new String[]{"_id","NAME"},null,null,null,null,null);
            CursorAdapter listAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"NAME"},
                    new int[]{android.R.id.text1},
                    0);
            listMuffins.setAdapter(listAdapter);
        } catch(SQLiteException e){
            Toast toast = Toast.makeText(this, "Baza danych nie jest dostępna",Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    // Zamykam kursor i baze danych. Kursor bedzie otwarty do momentu gdy adapter nie bedzie go potrzebowal.
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        cursor.close();
        db.close();
    }



    @Override
    public void onListItemClick(ListView listView, View itemView, int position, long id){
        Intent intent = new Intent(MuffinCategoryActivity.this, MuffinActivity.class );
        intent.putExtra(MuffinActivity.EXTRA_MUFFINNO,(int) id);
        startActivity(intent);
    }

    

}
