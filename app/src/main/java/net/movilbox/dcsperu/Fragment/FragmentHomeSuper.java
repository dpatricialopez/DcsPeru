package net.movilbox.dcsperu.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import net.movilbox.dcsperu.Adapter.TabsAdapter;
import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.EntIndicadores;
import net.movilbox.dcsperu.R;
import net.movilbox.dcsperu.Services.ConnectionDetector;

import java.util.HashMap;
import java.util.Map;

public class FragmentHomeSuper extends BaseVolleyFragment {

    private int vendedor = 0;
    private TabLayout tabLayout;
    private TabsAdapter tabsAdapter;
    private ViewPager viewPager;
    private ConnectionDetector connectionDetector;
    public ProgressDialog progressDialog;
    private DBHelper mydb;

    public FragmentHomeSuper() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_super, container, false);

        mydb = new DBHelper(getActivity());

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout);

        connectionDetector = new ConnectionDetector(getActivity());

        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            vendedor = getArguments().getInt("id_vendedor");
        }

        if (connectionDetector.isConnected()) {
            indicadorVendedor();
        } else {
            tabsAdapter = new TabsAdapter(getChildFragmentManager());
            tabsAdapter.addFragment(new FragmentDasboardVendedor(vendedor), "Dashboard");
            tabsAdapter.addFragment(new FragmentRuteroVendedor(vendedor), "Rutero");
            viewPager.setAdapter(tabsAdapter);
            tabLayout.setupWithViewPager(viewPager);
        }

    }

    private void indicadorVendedor() {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Alerta.");
        progressDialog.setMessage("Cargando Información");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = String.format("%1$s%2$s", getString(R.string.url_base), "indicadores_vendedor");
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        parseJSONVendedor(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        String error_string = "";

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            error_string = "Error de tiempo de espera";
                        } else if (error instanceof AuthFailureError) {
                            error_string = "Error Servidor";
                        } else if (error instanceof ServerError) {
                            error_string = "Server Error";
                        } else if (error instanceof NetworkError) {
                            error_string = "Error de red";
                        } else if (error instanceof ParseError) {
                            error_string = "Error al serializar los datos";
                        }
                        progressDialog.show();
                        onConnectionFailed(error_string);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("iduser", String.valueOf(vendedor));

                params.put("iddis", mydb.getUserLogin().getId_distri());
                params.put("db", mydb.getUserLogin().getBd());
                params.put("perfil", String.valueOf(mydb.getUserLogin().getPerfil()));

                return params;

            }
        };

        addToQueue(jsonRequest);

    }

    private void parseJSONVendedor(String response) {

        Gson gson = new Gson();

        final EntIndicadores entIndicadores = gson.fromJson(response, EntIndicadores.class);

        mydb.deleteObject("indicadoresdas");
        mydb.deleteObject("indicadoresdas_detalle");

        mydb.insertIndicadores(entIndicadores);
        mydb.insertDetalleIndicadores(entIndicadores);

        progressDialog.dismiss();

        tabsAdapter = new TabsAdapter(getChildFragmentManager());

        tabsAdapter.addFragment(new FragmentDasboardVendedor(vendedor), "Dashboard");
        tabsAdapter.addFragment(new FragmentRuteroVendedor(vendedor), "Rutero");

        viewPager.setAdapter(tabsAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

}
