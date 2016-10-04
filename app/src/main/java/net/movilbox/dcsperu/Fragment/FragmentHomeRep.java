package net.movilbox.dcsperu.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import net.movilbox.dcsperu.Activity.ActMainPeru;
import net.movilbox.dcsperu.Adapter.TabsAdapter;
import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.ListEntregaPediSincronizar;
import net.movilbox.dcsperu.Entry.ListResponseEntregaPedido;
import net.movilbox.dcsperu.Entry.PedidosEntregaSincronizar;
import net.movilbox.dcsperu.R;
import net.movilbox.dcsperu.Services.ConnectionDetector;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentHomeRep extends BaseVolleyFragment {

    private DBHelper mydb;
    private ConnectionDetector connectionDetector;

    public FragmentHomeRep() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home_rep, container, false);

        mydb = new DBHelper(getActivity());

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tablayout);

        TabsAdapter tabsAdapter = new TabsAdapter(getChildFragmentManager());
        tabsAdapter.addFragment(new FragmentHomeRepartidor(), "Dashboard");
        tabsAdapter.addFragment(new FragmentRuteroRepartidor(), "Rutero");

        viewPager.setAdapter(tabsAdapter);
        tabLayout.setupWithViewPager(viewPager);

        setHasOptionsMenu(true);



        connectionDetector = new ConnectionDetector(getActivity());

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuItem item2 = menu.add("Carrito");
        item2.setIcon(R.drawable.ic_cloud_upload_white_24dp); // sets icon
        item2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Sincronizar...
                if (connectionDetector.isConnected()) {
                    setSincroinizarEntregaVenta();
                } else {
                    Toast.makeText(getActivity(), "No tiene acceso a internet para sincronizar", Toast.LENGTH_LONG).show();
                }

                return true;

            }

        });
    }

    private void setSincroinizarEntregaVenta() {

        String url = String.format("%1$s%2$s", getString(R.string.url_base), "entregar_pedido_offline");
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        parseJSONEntregaPedido(response);
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
                        onConnectionFailed(error_string);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                List<PedidosEntregaSincronizar> pedidosEntregaSincronizarList = mydb.sincronizarsEntregaPedido();

                String parJSON = new Gson().toJson(pedidosEntregaSincronizarList, ListEntregaPediSincronizar.class);

                params.put("datos", parJSON);

                params.put("iduser", String.valueOf(mydb.getUserLogin().getId()));
                params.put("iddis", mydb.getUserLogin().getId_distri());
                params.put("db", mydb.getUserLogin().getBd());
                params.put("perfil", String.valueOf(mydb.getUserLogin().getPerfil()));

                return params;

            }
        };

        addToQueue(jsonRequest);
    }

    private void parseJSONEntregaPedido(String response) {

        Gson gson = new Gson();
        if (!response.equals("[]")) {
            try {

                Charset.forName("UTF-8").encode(response);
                byte ptext[] = response.getBytes(Charset.forName("ISO-8859-1"));
                String value = new String(ptext, Charset.forName("UTF-8"));

                ListResponseEntregaPedido entregaPedido = gson.fromJson(value, ListResponseEntregaPedido.class);

                for (int e = 0; e < entregaPedido.size(); e++) {
                    if (entregaPedido.get(e).getEstado().equals("-1")) {
                        Toast.makeText(getActivity(), entregaPedido.get(e).getMsg(), Toast.LENGTH_LONG).show();
                    } else if (entregaPedido.get(e).getEstado().equals("0")) {
                        if (mydb.deleteAllPedidosEntrega(entregaPedido.get(e).getIdpos(), entregaPedido.get(e).getNropedido())) {
                            Toast.makeText(getActivity(), entregaPedido.get(e).getMsg(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getActivity(), ActMainPeru.class);
                            startActivity(intent);
                        }
                    } else if (entregaPedido.get(e).getEstado().equals("-2")) {
                        if (mydb.deleteAllPedidosEntrega(entregaPedido.get(e).getIdpos(), entregaPedido.get(e).getNropedido())) {
                            Toast.makeText(getActivity(), entregaPedido.get(e).getMsg(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getActivity(), ActMainPeru.class);
                            startActivity(intent);
                        }
                    }

                }

            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            } finally {
            }
        } else {

        }
    }
}
