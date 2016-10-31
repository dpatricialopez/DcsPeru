package net.movilbox.dcsperu.DataBase;


/**
 * Created by dianalopez on 31/10/16.
 */
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteBindOrColumnIndexOutOfRangeException;

public class ConnectManagment {


        // Shared Preferences
        SharedPreferences pref;

        // Editor for Shared preferences
        SharedPreferences.Editor editor;

        // Context
        Context _context;

        // Shared pref mode
        int PRIVATE_MODE = 0;

        // Sharedpref file name
        private static final String PREF_NAME = "Connect";

        // All Shared Preferences Keys
        private static final String ModoOnline = "ModoOffline";



        // Constructor
        public ConnectManagment(Context context) {
            this._context = context;
            pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
            editor = pref.edit();
        }

    public void CreateConnect(boolean connect){
        // Storing login value as TRUE
        editor.putBoolean(ModoOnline, connect);


        // commit changes
        editor.commit();
    }

    public boolean getModoConnect(){
        Boolean connect= pref.getBoolean(ModoOnline,true);
        return connect;
    }

    }
