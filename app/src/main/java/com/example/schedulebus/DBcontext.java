package com.example.schedulebus;

import android.os.StrictMode;
import android.util.Log;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DBcontext {

    Connection connect;

    public static Connection connect(){

        Connection conn = null;

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String url = BuildConfig.DB_URL;
            String login = BuildConfig.DB_USER;
            String password = BuildConfig.DB_PASSWORD;

            conn = DriverManager.getConnection(url, login, password);
            if (conn != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;

    }

}
