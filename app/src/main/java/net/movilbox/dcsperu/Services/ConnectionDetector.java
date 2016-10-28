package net.movilbox.dcsperu.Services;

/**
 * Created by germangarcia on 18/05/16.
 */
import android.content.Context;
import android.net.ConnectivityManager;

import net.movilbox.dcsperu.DataBase.DBHelper;

public class ConnectionDetector {

    private Context _context;
    private DBHelper mydb;

    public ConnectionDetector(Context context){
        this._context = context;
        mydb = new DBHelper(context);
    }

    public boolean isConnected() {

        ConnectivityManager check = (ConnectivityManager) this._context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( (check.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                check.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                check.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                check.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) && mydb.getConnect()==1) {
            //Toast.makeText(_context, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        }else if (check.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED || check.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
            //Toast.makeText(_context, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }

        return false;
    }

}