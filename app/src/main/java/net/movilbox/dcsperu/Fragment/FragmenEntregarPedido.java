package net.movilbox.dcsperu.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import net.movilbox.dcsperu.Activity.ActBusquedaAvan;
import net.movilbox.dcsperu.Activity.ActEntregarPedido;
import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.ListEntregarPedido;
import net.movilbox.dcsperu.R;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class FragmenEntregarPedido extends BaseVolleyFragment implements View.OnClickListener {

    private SpotsDialog alertDialog;
    private EditText edit_buscar;
    private DBHelper mydb;

    public FragmenEntregarPedido() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_marcarvisita, container, false);
        // Inflate the layout for this fragment

        mydb = new DBHelper(getActivity());


        Button btnBuscar = (Button) view.findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(this);

        Button btnAvBus = (Button) view.findViewById(R.id.btnAvBus);
        btnAvBus.setOnClickListener(this);

        edit_buscar = (EditText) view.findViewById(R.id.edit_buscar);
        alertDialog = new SpotsDialog(getActivity(), R.style.Custom);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBuscar:

                if (isValidNumber(edit_buscar.getText().toString().trim())) {
                    edit_buscar.setFocusable(true);
                    edit_buscar.setFocusableInTouchMode(true);
                    edit_buscar.requestFocus();
                    edit_buscar.setText("");
                    edit_buscar.setError("Este campo es obligatorio");
                } else {
                    buscarIdPos(edit_buscar);
                }

                break;

            case R.id.btnAvBus:

                Bundle bundle = new Bundle();
                Intent intent = new Intent(getActivity(), ActBusquedaAvan.class);
                bundle.putSerializable("value", "Repartidor");
                intent.putExtras(bundle);
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                break;
        }
    }

    private boolean isValidNumber(String number) {
        return number == null || number.length() == 0;
    }

    private void buscarIdPos(final EditText buscar) {
        alertDialog.show();
        String url = String.format("%1$s%2$s", getString(R.string.url_base), "datos_entrega");
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        parseJSONEntrega(response);
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

                params.put("iduser", String.valueOf(mydb.getUserLogin().getId()));
                params.put("iddis", mydb.getUserLogin().getId_distri());
                params.put("db", mydb.getUserLogin().getBd());
                params.put("perfil", String.valueOf(mydb.getUserLogin().getPerfil()));
                params.put("idpos", buscar.getText().toString().trim());

                return params;
            }
        };

        addToQueue(jsonRequest);

    }

    private void parseJSONEntrega(String response) {
        Gson gson = new Gson();

        if (!response.equals("[]")) {
            try {

                ListEntregarPedido responseEntregarPedido = gson.fromJson(response, ListEntregarPedido.class);

                Bundle bundle = new Bundle();
                Intent intent = new Intent(getActivity(), ActEntregarPedido.class);
                bundle.putSerializable("value", responseEntregarPedido);
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
