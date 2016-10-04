package net.movilbox.dcsperu.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.movilbox.dcsperu.Activity.ActMainPeru;
import net.movilbox.dcsperu.Activity.ActMarcarVisita;
import net.movilbox.dcsperu.Activity.ActTomarPedido;
import net.movilbox.dcsperu.Adapter.AdapterRutero;
import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.EntRuteroList;
import net.movilbox.dcsperu.Entry.ResponseMarcarPedido;
import net.movilbox.dcsperu.R;
import net.movilbox.dcsperu.Services.ConnectionDetector;

import java.util.List;

import dmax.dialog.SpotsDialog;

@SuppressLint("ValidFragment")
public class FragmentRuteroVendedor extends BaseVolleyFragment {

    List<EntRuteroList> listList;
    private ListView mListView;
    private SpotsDialog alertDialog;
    private int vendedor;
    private ConnectionDetector connectionDetector;
    private DBHelper mydb;

    public FragmentRuteroVendedor(int vende) {
        vendedor = vende;
    }

    public FragmentRuteroVendedor() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_rutero_menu, container, false);
        mListView = (ListView) rootView.findViewById(R.id.listView);

        mydb = new DBHelper(getActivity());

        alertDialog = new SpotsDialog(getActivity(), R.style.Custom);

        connectionDetector = new ConnectionDetector(getActivity());

        return rootView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // Implementing ActionBar Search inside a fragment
        MenuItem item = menu.add("Search");
        item.setIcon(R.drawable.abc_ic_search_api_mtrl_alpha); // sets icon
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        SearchView sv = new SearchView(((ActTomarPedido) getActivity()).getSupportActionBar().getThemedContext());
        int id = sv.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) sv.findViewById(id);
        textView.setHint("Buscar...");
        textView.setHintTextColor(getResources().getColor(R.color.color_gris));
        textView.setTextColor(getResources().getColor(R.color.actionBarColorText));

        // implementing the listener
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //doSearch(s);
                return s.length() < 4;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });

        item.setActionView(sv);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cargarRutero();

    }

    private void cargarRutero() {

        if(vendedor == 0) {
            listList = mydb.getRuteroDia(mydb.getUserLogin().getId());
        } else {
            listList = mydb.getRuteroDia(vendedor);
        }

        AdapterRutero appAdapterRutero = new AdapterRutero(getActivity(), listList, "rutero");

        mListView.setAdapter(appAdapterRutero);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.dialog_detalle, null);

                TextView txt_id_numero = (TextView) dialoglayout.findViewById(R.id.txt_id_numero);
                txt_id_numero.setText(String.format("%1$s", listList.get(position).getIdpos()));

                TextView cod_cum = (TextView) dialoglayout.findViewById(R.id.cod_cum);
                if (listList.get(position).getCodigo_cum().equals(""))
                    cod_cum.setText(String.format("%1$s", "N/A"));
                else
                    cod_cum.setText(String.format("%1$s", listList.get(position).getCodigo_cum()));

                TextView txt_nombre = (TextView) dialoglayout.findViewById(R.id.nombre_punto);
                txt_nombre.setText(String.format("%1$s", listList.get(position).getNombre_punto()));

                TextView txt_visitado = (TextView) dialoglayout.findViewById(R.id.txt_visitado);
                String visita;
                if (listList.get(position).getTipo_visita() == 0)
                    visita = "No";
                else
                    visita = "Si";

                txt_visitado.setText(String.format("%1$s", visita));

                TextView txt_direccion = (TextView) dialoglayout.findViewById(R.id.txt_direccion);
                txt_direccion.setText(String.format("%1$s", listList.get(position).getTexto_direccion()));

                TextView txt_distrito = (TextView) dialoglayout.findViewById(R.id.txt_distrito);
                txt_distrito.setText(String.format("%1$s", listList.get(position).getNom_distrito()));

                TextView txt_telefono = (TextView) dialoglayout.findViewById(R.id.txt_telefono);
                txt_telefono.setText(String.format("%1$s", listList.get(position).getTelefono()));

                TextView txt_dias = (TextView) dialoglayout.findViewById(R.id.txt_dias);
                txt_dias.setText(String.format("%1$s", listList.get(position).getDetalle()));

                String fecha_hora = listList.get(position).getUltima_visita();
                if(fecha_hora.trim().isEmpty())
                    fecha_hora = "N/A";

                TextView txt_hora_visita = (TextView) dialoglayout.findViewById(R.id.txt_hora_visita);
                txt_hora_visita.setText(String.format("%1$s",fecha_hora));

                TextView txt_stock_c = (TextView) dialoglayout.findViewById(R.id.txt_stock_c);
                txt_stock_c.setText(String.format("%1$s", listList.get(position).getStock_combo()));

                TextView txt_stock_s = (TextView) dialoglayout.findViewById(R.id.txt_stock_s);
                txt_stock_s.setText(String.format("%1$s", listList.get(position).getStock_sim()));

                TextView txt_dias_s = (TextView) dialoglayout.findViewById(R.id.txt_dias_s);
                txt_dias_s.setText(String.format("%1$s", listList.get(position).getDias_inve_sim()));

                TextView txt_dias_c = (TextView) dialoglayout.findViewById(R.id.txt_dias_c);
                txt_dias_c.setText(String.format("%1$s", listList.get(position).getDias_inve_combo()));


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false);
                builder.setTitle("Detalle");
                builder.setView(dialoglayout).setPositiveButton("Visitar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        buscarPuntoLocal(listList.get(position).getIdpos());
                    }

                }).setNegativeButton("Editar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (connectionDetector.isConnected()) {
                            editarPunto(listList.get(position).getIdpos());
                        } else {
                            Toast.makeText(getActivity(), "Esta opción solo es permitida si tiene internet", Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNeutralButton("Cerrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });

    }

    private void buscarPuntoLocal(int edit_buscar) {
        //Recuperar punto local
        ResponseMarcarPedido responseMarcarPedido = mydb.getPuntoLocal(String.valueOf(edit_buscar));

        if (responseMarcarPedido.getRazon_social() == null) {
            Toast.makeText(getActivity(), "El punto no se encuentra en la base de datos local", Toast.LENGTH_LONG).show();
        } else {
            Bundle bundle = new Bundle();
            Intent intent = new Intent(getActivity(), ActMarcarVisita.class);
            bundle.putSerializable("value", responseMarcarPedido);
            bundle.putString("page", "marcar_rutero");
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    /*private void buscarIdPos(final int idPos) {
        alertDialog.show();
        String url = String.format("%1$s%2$s", getString(R.string.url_base), "buscar_punto_visita");
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        parseJSONVisita(response);
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
                params.put("idpos", String.valueOf(idPos));

                return params;

            }
        };

        addToQueue(jsonRequest);
    }

    private void parseJSONVisita(String response) {

        Gson gson = new Gson();
        if (!response.equals("[]")) {
            try {

                ResponseMarcarPedido responseMarcarPedido = gson.fromJson(response, ResponseMarcarPedido.class);

                if (responseMarcarPedido.getEstado() == -1) {
                    //No tiene permisos del punto
                    Toast.makeText(getActivity(), responseMarcarPedido.getMsg(), Toast.LENGTH_LONG).show();
                } else if (responseMarcarPedido.getEstado() == -2) {
                    //El punto no existe
                    Toast.makeText(getActivity(), responseMarcarPedido.getMsg(), Toast.LENGTH_LONG).show();
                } else {
                    //Activity Detalle
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(getActivity(), ActMarcarVisita.class);
                    bundle.putSerializable("value", responseMarcarPedido);
                    bundle.putString("page", "marcar_rutero");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            } catch (IllegalStateException ex) {
                ex.printStackTrace();
                alertDialog.dismiss();
            } finally {
                alertDialog.dismiss();
            }
        } else {
            alertDialog.dismiss();
        }

    }
    */

    private void editarPunto(int idpos){
        Bundle bundle = new Bundle();
        Intent intent = new Intent(getContext(), ActMainPeru.class);
        bundle.putInt("edit_punto", idpos);
        bundle.putInt("accion", 1);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}