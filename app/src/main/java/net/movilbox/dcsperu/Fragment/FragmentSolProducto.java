package net.movilbox.dcsperu.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
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
import net.movilbox.dcsperu.Activity.DialogEmail;
import net.movilbox.dcsperu.Activity.DialogSolProducto;
import net.movilbox.dcsperu.Adapter.ExpandableListDataPumpSol;
import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.EntCupo;
import net.movilbox.dcsperu.Entry.EntReferenciaSol;
import net.movilbox.dcsperu.Entry.EntRespuestaServices;
import net.movilbox.dcsperu.Entry.ExpandableListAdapterSol;
import net.movilbox.dcsperu.Entry.LisSolicitarProduct;
import net.movilbox.dcsperu.Entry.ListEntReferenciaSol;
import net.movilbox.dcsperu.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSolProducto extends BaseVolleyFragment {

    private ExpandableListView expandable_sol;
    private SpotsDialog alertDialog;
    private DBHelper mydb;
    protected DialogSolProducto dialog;
    private ListEntReferenciaSol listEntReferenciaSol;
    private HashMap<String, List<EntReferenciaSol>> expandableListDetail;
    private EditText editSol;
    public TextView txtCupo;
    public ProgressDialog progressDialog;
    private DecimalFormat format;

    public FragmentSolProducto() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sol_producto, container, false);
        format = new DecimalFormat("#.00");
        expandable_sol = (ExpandableListView) view.findViewById(R.id.expandable_sol);
        txtCupo = (TextView) view.findViewById(R.id.txtCupo);
        alertDialog = new SpotsDialog(getActivity(), R.style.Custom);
        //controllerLogin = new ControllerLogin(getActivity());
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuItem item2 = menu.add("Guardar");
        item2.setIcon(R.drawable.ic_save_white_24dp); // sets icon
        item2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Guardar Aceptacion de pedidos validaciones
                List<EntReferenciaSol> referenciaSolList = new ArrayList<>();

                for (int i = 0; i < listEntReferenciaSol.getEntSolPedidos().size(); i++) {
                    for (int t = 0; t < listEntReferenciaSol.getEntSolPedidos().get(i).getEntReferenciaSols().size(); t++) {
                        if (listEntReferenciaSol.getEntSolPedidos().get(i).getEntReferenciaSols().get(t).getCantidadSol() > 0) {
                            EntReferenciaSol entReferenciaSol = new EntReferenciaSol();

                            entReferenciaSol.setId_referencia(listEntReferenciaSol.getEntSolPedidos().get(i).getEntReferenciaSols().get(t).getId_referencia());
                            entReferenciaSol.setCantidadSol(listEntReferenciaSol.getEntSolPedidos().get(i).getEntReferenciaSols().get(t).getCantidadSol());
                            entReferenciaSol.setId_bodega(listEntReferenciaSol.getEntSolPedidos().get(i).getEntReferenciaSols().get(t).getId_bodega());
                            entReferenciaSol.setTipo_bodega(listEntReferenciaSol.getEntSolPedidos().get(i).getEntReferenciaSols().get(t).getTipo_bodega());
                            entReferenciaSol.setTipo_ref(listEntReferenciaSol.getEntSolPedidos().get(i).getEntReferenciaSols().get(t).getTipo_ref());

                            referenciaSolList.add(entReferenciaSol);
                        }
                    }
                }

                if (referenciaSolList.size() > 0) {
                    //Llamar servicio para realizar el pedido.
                    solicitarProducto(referenciaSolList);
                } else {
                    Toast.makeText(getActivity(), "Digite una cantidad para realizar el pedido", Toast.LENGTH_LONG).show();
                }

                return true;
            }

        });

    }

    private void solicitarProducto(final List<EntReferenciaSol> referenciaSolList) {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Alerta.");
        progressDialog.setMessage("Cargando Información");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = String.format("%1$s%2$s", getString(R.string.url_base), "save_pedido");
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        JSONSolicitud(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        progressDialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                String parJSON = new Gson().toJson(referenciaSolList, LisSolicitarProduct.class);

                params.put("iduser", String.valueOf(mydb.getUserLogin().getId()));
                params.put("iddis", mydb.getUserLogin().getId_distri());
                params.put("db", mydb.getUserLogin().getBd());

                params.put("datos", parJSON);

                return params;

            }
        };

        addToQueue(jsonRequest);

    }

    private void JSONSolicitud(String response) {



        Gson gson = new Gson();
        EntRespuestaServices entRespuestaServices = gson.fromJson(response, EntRespuestaServices.class);

        if (entRespuestaServices.getEstado() == 0) {
            Toast.makeText(getActivity(), entRespuestaServices.getMsg(), Toast.LENGTH_LONG).show();
            startActivity(new Intent(getActivity(), ActMainPeru.class));
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            getActivity().finish();

        } else if (entRespuestaServices.getEstado() == -1) {
            Toast.makeText(getActivity(), entRespuestaServices.getMsg(), Toast.LENGTH_LONG).show();
        }
        // progressDialog.dismiss();

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mydb = new DBHelper(getActivity());

        setHasOptionsMenu(true);
        consultarReporte();
    }

    private void consultarReporte() {
        alertDialog.show();
        String url = String.format("%1$s%2$s", getString(R.string.url_base), "solicitar_pedido");
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        JSONresponce(response);
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

                return params;

            }
        };
        addToQueue(jsonRequest);
    }

    private void JSONresponce(String response) {

        alertDialog.dismiss();

        Gson gson = new Gson();
        final List<EntCupo> listaCupo = new ArrayList<>();
        listEntReferenciaSol = gson.fromJson(response, ListEntReferenciaSol.class);

        if (listEntReferenciaSol.getAccion() == 0) {
            //Tiene pedidos pendientes
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
            builder.setCancelable(false);
            builder.setTitle("Pedido: "+listEntReferenciaSol.getEntSolPedidos2().get(0).getId());
            builder.setMessage(listEntReferenciaSol.getMsg());
            builder.setPositiveButton("Cancelar Pedido", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Llamar servicio para cancelar el pedido
                    cancelarPedido(listEntReferenciaSol.getEntSolPedidos2().get(0).getId());
                }
            }).setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    startActivity(new Intent(getActivity(), ActMainPeru.class));
                    getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    getActivity().finish();
                }
            });

            builder.show();

        } else if (listEntReferenciaSol.getAccion() == 1) {

            txtCupo.setText(String.format("S/. %s", format.format(listEntReferenciaSol.getCupo_disponible())));
            //Puede realizar o solicitar inventario.
            expandableListDetail = ExpandableListDataPumpSol.getData(listEntReferenciaSol);
            final ArrayList<String> expandableListTitle = new ArrayList<>(expandableListDetail.keySet());

            final ExpandableListAdapterSol expandableListAdapter = new ExpandableListAdapterSol(getActivity(), expandableListTitle, expandableListDetail);
            expandable_sol.setAdapter(expandableListAdapter);

            expandable_sol.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, final int groupPosition, final int childPosition, long id) {

                    dialog = new DialogSolProducto(getActivity(), listEntReferenciaSol.getEntSolPedidos().get(groupPosition).getEntReferenciaSols().get(childPosition).getProducto());
                    dialog.show();
                    dialog.setCancelable(false);
                    editSol = (EditText) dialog.findViewById(R.id.editSol);
                    Button acceptButton = dialog.getButtonAccept();
                    acceptButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            if (isValidNumber(editSol.getText().toString().trim())) {
                                editSol.setError("Campo requerido");
                                editSol.requestFocus();
                            } else if (Integer.parseInt(editSol.getText().toString().trim()) > listEntReferenciaSol.getEntSolPedidos().get(groupPosition).getEntReferenciaSols().get(childPosition).getTotal()) {
                                editSol.setError("La cantidad solicitada es mayor al inventario");
                                editSol.requestFocus();
                            } else {
                                //...
                                dialog.dismiss();

                                EntCupo pojoCupo = new EntCupo();

                                int idBode = listEntReferenciaSol.getEntSolPedidos().get(groupPosition).getEntReferenciaSols().get(childPosition).getId_bodega();
                                int idReferencia = listEntReferenciaSol.getEntSolPedidos().get(groupPosition).getEntReferenciaSols().get(childPosition).getId_referencia();
                                int tipoBode = listEntReferenciaSol.getEntSolPedidos().get(groupPosition).getEntReferenciaSols().get(childPosition).getTipo_bodega();
                                boolean bandera = true;
                                double totalView = 0;
                                double precio = listEntReferenciaSol.getEntSolPedidos().get(groupPosition).getEntReferenciaSols().get(childPosition).getPrecio_pdv();
                                int cantidad = Integer.parseInt(editSol.getText().toString().trim());
                                double total = cantidad * precio;



                                if(listaCupo.size() == 0)
                                {

                                    if(total <= listEntReferenciaSol.getCupo_disponible())
                                    {
                                        pojoCupo.setIdBode(idBode);
                                        pojoCupo.setIdReferencia(idReferencia);
                                        pojoCupo.setTipo_bodega(tipoBode);
                                        pojoCupo.setTotalRef(total);
                                        listaCupo.add(pojoCupo);
                                        //listEntReferenciaSol.setCupo_disponible(listEntReferenciaSol.getCupo_disponible() - total);

                                        txtCupo.setText(String.format("S/. %s", format.format(listEntReferenciaSol.getCupo_disponible() - total)));

                                        listEntReferenciaSol.getEntSolPedidos().get(groupPosition).getEntReferenciaSols().get(childPosition).setCantidadSol(cantidad);

                                        expandableListDetail = ExpandableListDataPumpSol.getData(listEntReferenciaSol);

                                        expandableListAdapter.setData(expandableListDetail);
                                    }
                                    else
                                    {
                                        Toast.makeText(getActivity(),"La cantidas solicitada supera el valor del cupo disonible",Toast.LENGTH_LONG).show();
                                    }
                                }
                                else
                                {
                                    for (int j = 0;j < listaCupo.size(); j++ )
                                    {
                                        totalView += listaCupo.get(j).getTotalRef();
                                    }

                                    for (int i = 0;i < listaCupo.size(); i++ )
                                    {
                                        if (idBode == listaCupo.get(i).getIdBode() && idReferencia == listaCupo.get(i).getIdReferencia() && tipoBode == listaCupo.get(i).getTipo_bodega())
                                        {
                                            totalView = (totalView - listaCupo.get(i).getTotalRef()) + total;

                                            if(totalView <= listEntReferenciaSol.getCupo_disponible())
                                            {
                                                listaCupo.get(i).setTotalRef(total);

                                                txtCupo.setText(String.format("S/. %s", format.format(listEntReferenciaSol.getCupo_disponible() - totalView)));

                                                listEntReferenciaSol.getEntSolPedidos().get(groupPosition).getEntReferenciaSols().get(childPosition).setCantidadSol(cantidad);

                                                expandableListDetail = ExpandableListDataPumpSol.getData(listEntReferenciaSol);

                                                expandableListAdapter.setData(expandableListDetail);

                                                bandera = false;
                                            }
                                            else
                                            {
                                                Toast.makeText(getActivity(),"La cantidad solicitada supera el valor del cupo disonible",Toast.LENGTH_LONG).show();
                                            }
                                            break;
                                        }
                                    }

                                    if (bandera)
                                    {    totalView = totalView + total;
                                        if(totalView <= listEntReferenciaSol.getCupo_disponible())
                                        {
                                            pojoCupo.setIdBode(idBode);
                                            pojoCupo.setIdReferencia(idReferencia);
                                            pojoCupo.setTipo_bodega(tipoBode);
                                            pojoCupo.setTotalRef(total);
                                            listaCupo.add(pojoCupo);

                                            txtCupo.setText(String.format("S/. %s", format.format(listEntReferenciaSol.getCupo_disponible() - totalView)));

                                            listEntReferenciaSol.getEntSolPedidos().get(groupPosition).getEntReferenciaSols().get(childPosition).setCantidadSol(cantidad);

                                            expandableListDetail = ExpandableListDataPumpSol.getData(listEntReferenciaSol);

                                            expandableListAdapter.setData(expandableListDetail);

                                        }
                                        else
                                        {
                                            Toast.makeText(getActivity(),"La cantidad solicitada supera el valor del cupo disonible",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }


                            }

                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(editSol.getWindowToken(), 0);

                        }
                    });

                    Button cancelButton = dialog.getButtonCancel();
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(editSol.getWindowToken(), 0);
                        }
                    });

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                    return false;

                }
            });
        }

    }

    private void cancelarPedido(final int id) {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Alerta.");
        progressDialog.setMessage("Cargando Información");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = String.format("%1$s%2$s", getString(R.string.url_base), "cancel_pedido");
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        JSONCancelPedido(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        progressDialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("iduser", String.valueOf(mydb.getUserLogin().getId()));
                params.put("iddis", String.valueOf(mydb.getUserLogin().getId_distri()));
                params.put("db", mydb.getUserLogin().getBd());
                params.put("id", String.valueOf(id));

                return params;

            }
        };

        addToQueue(jsonRequest);

    }

    private void JSONCancelPedido(String response) {

        progressDialog.dismiss();

        Gson gson = new Gson();
        EntRespuestaServices entRespuestaServices = gson.fromJson(response, EntRespuestaServices.class);

        if (entRespuestaServices.getEstado() == 0) {
            Toast.makeText(getActivity(), entRespuestaServices.getMsg(), Toast.LENGTH_LONG).show();
            consultarReporte();
        } else if (entRespuestaServices.getEstado() == -1) {
            Toast.makeText(getActivity(), entRespuestaServices.getMsg(), Toast.LENGTH_LONG).show();
        }

    }

    private boolean isValidNumber(String number) {
        return number == null || number.length() == 0;
    }
}
