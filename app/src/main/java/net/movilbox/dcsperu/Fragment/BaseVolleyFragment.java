package net.movilbox.dcsperu.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import net.movilbox.dcsperu.R;

import dmax.dialog.SpotsDialog;


public class BaseVolleyFragment extends Fragment {

    private VolleyS volley;
    protected RequestQueue fRequestQueue;
    private SpotsDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        volley = VolleyS.getInstance(getActivity().getApplicationContext());
        fRequestQueue = volley.getRequestQueue();
        alertDialog = new SpotsDialog(getActivity(), R.style.Custom);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (fRequestQueue != null) {
            fRequestQueue.cancelAll(this);
        }
    }

    public void addToQueue(Request request) {
        if (request != null) {
            request.setTag(this);
            if (fRequestQueue == null)
                fRequestQueue = volley.getRequestQueue();
            request.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            //onPreStartConnection(alertDialog);

            fRequestQueue.add(request);
        }
    }

    public void onPreStartConnection(SpotsDialog spotsDialog) {
        spotsDialog.show();
    }

    public void onConnectionFinished(SpotsDialog spotsDialog) {
        spotsDialog.dismiss();
    }

    public void onConnectionFailed(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
        onConnectionFinished(alertDialog);
    }
}
