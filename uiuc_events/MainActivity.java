package com.example.audibayron.uiuc_events;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class MainActivity extends Activity {
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private ArrayList<Integer> eventsId = new ArrayList<>();
    private ArrayList<String> eventsName = new ArrayList<>();
    private ArrayList<String> eventsAddress = new ArrayList<>();
    private ArrayList<Integer> eventsDate = new ArrayList<>();
    private ArrayList<Integer> eventsMonth = new ArrayList<>();
    private ArrayList<Integer> eventsYear = new ArrayList<>();
    private ArrayList<String> eventsDetails = new ArrayList<>();
    private ArrayList<String> eventsLatLng = new ArrayList<>();


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

        Cursor crs = myDb.getAllRows();
        copyRecordSet(crs);

        for(int i = 0; i <= eventsId.size(); i++){
            Cursor cursor = myDb.getRow(i);
            displayRecordSet(cursor);
        }
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

        double lat = 45.4;
        double lng = 46.5;
        LatLng latlng = new LatLng(lat, lng);

        long newID = myDb.insertRow("Party", "123 Ave", 1, 2, 2016, "party", latlng);
        Cursor cursor = myDb.getRow(newID);
        displayRecordSet(cursor);
    }

    private void displayRecordSet(Cursor cursor) {
        String message = "";

        if (cursor.moveToFirst()) {
                // Process the data:
            do {
                int id = cursor.getInt(DBAdapter.COL_ROWID);
                String name = cursor.getString(DBAdapter.COL_NAME);
                String address = cursor.getString(DBAdapter.COL_ADDRESS);
                int date = cursor.getInt(DBAdapter.COL_DATE);
                int month = cursor.getInt(DBAdapter.COL_MONTH);
                int year = cursor.getInt(DBAdapter.COL_YEAR);
                String details = cursor.getString(DBAdapter.COL_DETAILS);
                String latlng = cursor.getString(DBAdapter.COL_LATLNG);

                // Append data to the message:
                message += "id=" + id
                        + ", name=" + name
                        + ", address=" + address
                        + ", date=" + month + "/" + date + "/" + year
                        + ", details=" + details
                        + ", " + latlng
                        + "\n";

                displayText(message);

            } while (cursor.moveToNext()) ;
        }

        cursor.close();


    }

    private void copyRecordSet(Cursor cursor){

        if (cursor.moveToFirst()) {
            // Process the data:
            do {
                int id = cursor.getInt(DBAdapter.COL_ROWID);
                String name = cursor.getString(DBAdapter.COL_NAME);
                String address = cursor.getString(DBAdapter.COL_ADDRESS);
                int date = cursor.getInt(DBAdapter.COL_DATE);
                int month = cursor.getInt(DBAdapter.COL_MONTH);
                int year = cursor.getInt(DBAdapter.COL_YEAR);
                String details = cursor.getString(DBAdapter.COL_DETAILS);
                String latlng = cursor.getString(DBAdapter.COL_LATLNG);

                eventsId.add(id);
                eventsName.add(name);
                eventsAddress.add(address);
                eventsDate.add(date);
                eventsMonth.add(month);
                eventsYear.add(year);
                eventsDetails.add(details);
                eventsLatLng.add(latlng);


            } while (cursor.moveToNext()) ;
        }

        cursor.close();

    }


}
