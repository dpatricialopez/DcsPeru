package net.movilbox.dcsperu.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import net.movilbox.dcsperu.Activity.ActReporteInventarioRep;
import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.CategoriasEstandar;
import net.movilbox.dcsperu.Entry.ListResponseInventario;
import net.movilbox.dcsperu.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class FragmentInventarioRepartidor extends BaseVolleyFragment {

    private Spinner spinner_agrupa;
    private int agrupa;
    private SpotsDialog alertDialog;
    private DBHelper mydb;

    public FragmentInventarioRepartidor() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inventario_repartidor, container, false);
        mydb = new DBHelper(getActivity());

        spinner_agrupa = (Spinner) view.findViewById(R.id.spinner_agrupa);
        alertDialog = new SpotsDialog(getActivity(), R.style.Custom);
        loadSpinner();
        FloatingActionButton btn_buscar = (FloatingActionButton) view.findViewById(R.id.cargar_reporte_inventario);
        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConsultaInforme();
            }
        });
        return view;
    }

    private void loadSpinner() {
        final List<CategoriasEstandar> ListaEstados = new ArrayList<>();
        ListaEstados.add(new CategoriasEstandar(1, "Pedido"));
        ListaEstados.add(new CategoriasEstandar(2, "Referencia"));

        ArrayAdapter<CategoriasEstandar> adapterEstados = new ArrayAdapter<>(getActivity(), R.layout.textview_spinner, ListaEstados);
        spinner_agrupa.setAdapter(adapterEstados);
        spinner_agrupa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                agrupa = ListaEstados.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
    }

    private void ConsultaInforme() {
        alertDialog.show();
        String url = String.format("%1$s%2$s", getString(R.string.url_base), "informe_inventario_repartidor");
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        CargarInform(response);
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
                        alertDialog.dismiss();
                        onConnectionFailed(error_string);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("iduser", String.valueOf(mydb.getUserLogin().getId()));
                params.put("iddis", mydb.getUserLogin().getId_distri());
                params.put("db", mydb.getUserLogin().getBd());
                params.put("perfil", String.valueOf(mydb.getUserLogin().getPerfil()));
                params.put("agrupa", String.valueOf(agrupa));

                return params;

            }
        };

        addToQueue(jsonRequest);
    }

    private void CargarInform(String response) {
        Gson gson = new Gson();

        if (!response.equals("[]")) {
            try {
                ListResponseInventario listResponseInventario = gson.fromJson(response, ListResponseInventario.class);

                Bundle bundle = new Bundle();
                Intent intent = new Intent(getActivity(), ActReporteInventarioRep.class);
                bundle.putSerializable("value", listResponseInventario);
                bundle.putInt("tipo", agrupa);
                intent.putExtras(bundle);
                startActivity(intent);

            } catch (IllegalStateException ex) {
                ex.printStackTrace();
                alertDialog.dismiss();
            } finally {
                alertDialog.dismiss();
            }
        } else {
            alertDialog.dismiss();
            Toast.makeText(getContext(), "No se encontraron datos para mostrar", Toast.LENGTH_SHORT).show();
        }
    }

}
