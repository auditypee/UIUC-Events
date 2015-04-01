package com.example.audibayron.uiuc_events;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends Activity {
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    DBAdapter myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ADD HERE
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);


        openDB();
        Cursor cursor = myDb.getAllRows();
        displayRecordSet(cursor);
        setupListViewListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        closeDB();
    }

    private void closeDB() {
        myDb.close();
    }

    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }

    private void displayText(String message) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        itemsAdapter.add(message);
        etNewItem.setText("");
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        // Remove the item within array at position
                        items.remove(pos);
                        // Refresh the adapter
                        itemsAdapter.notifyDataSetChanged();
                        // Return true consumes the long click event (marks it handled)
                        return true;
                    }

                });
    }

    public void onAddItem(View v) {

        long newID = myDb.insertRow("Party", "123 Ave", 1, 2, 2016);
        Cursor cursor = myDb.getRow(newID);
        displayRecordSet(cursor);
    }

    private void displayRecordSet(Cursor cursor) {
        String message = "";

        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                int id = cursor.getInt(DBAdapter.COL_ROWID);
                String name = cursor.getString(DBAdapter.COL_NAME);
                String address = cursor.getString(DBAdapter.COL_ADDRESS);
                int date = cursor.getInt(DBAdapter.COL_DATE);
                int month = cursor.getInt(DBAdapter.COL_MONTH);
                int year = cursor.getInt(DBAdapter.COL_YEAR);

                // Append data to the message:
                message += "id=" + id
                        +", name=" + name
                        +", address=" + address
                        +", date=" + month + "/" + date + "/" + year
                        +"\n";
            } while(cursor.moveToNext());
        }
        cursor.close();

        displayText(message);
    }


}
