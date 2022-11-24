package com.example.schedulebus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardBusStopActivity extends AppCompatActivity {

    String id_stop_bus;
    private Connection connect;
    SimpleAdapter ad;

    //click button in toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
                // todo: goto back activity from here
                finish();
                return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_bus_stop);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_bus_stop);// set icon in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id_stop_bus = getIntent().getStringExtra("ID_STOP_BUS");
        ListView listViewItems = (ListView) findViewById(R.id.list_item);
        TextView editTextActiveStatus = (TextView) findViewById(R.id.textViewActiveStatus);
        TextView editTextViewPavilionStatus = (TextView) findViewById(R.id.textViewPavilionStatus);

        //Fill info bus stop
        try {
            DBcontext contextOpen = new DBcontext();
            connect = contextOpen.connect();
            if (connect != null) {
                String query = "SELECT title, pavilion, active FROM public.transport_stop WHERE id_transport_stop=" + id_stop_bus + ";";
                System.out.println(query);
                ResultSet res = connect.createStatement().executeQuery(query);

                //fill info stop bus
                while (res.next()) {
                    //set title in status bar
                    setTitle(res.getString("title"));

                    //set active status
                    if(!res.getBoolean("active")) {
                        editTextActiveStatus.setText("не активна");
                        editTextActiveStatus.setTextColor(Color.RED);
                    }
                    //set pavilion status
                    if(!res.getBoolean("pavilion"))
                        editTextViewPavilionStatus.setText("отсутствует");
                    break;
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        //Fill ListView
        try {

            //run sql query (select all bus stop in database)
            DBcontext contextOpen = new DBcontext();
            connect = contextOpen.connect();

            String query = "SELECT \n" +
                            "t.\"desc\",\n" +
                            "t.type_bus\n" +
                            "FROM public.timetable t\n" +
                            "where t.id_bus_stop = " + id_stop_bus + ";";
            System.out.println(query);
            ResultSet res = connect.createStatement().executeQuery(query);

            List<Map<String, String>> data = new ArrayList<Map<String, String>>();

                    while (res.next()) {
                        Map<String, String> dtname = new HashMap<String, String>();
                        dtname.put("header_title", res.getString("type_bus"));
                        dtname.put("timetable", res.getString("desc"));
                        data.add(dtname);
                    }
                    String[] fromw = {"header_title", "timetable"};
                    int[] tow = {R.id.header_title, R.id.timetable};
                    ad = new SimpleAdapter(CardBusStopActivity.this,
                            data, R.layout.template_list_timetable, fromw, tow);
                    listViewItems.setAdapter(ad);

                }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}