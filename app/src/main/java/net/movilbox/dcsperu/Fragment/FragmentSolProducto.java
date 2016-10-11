package net.movilbox.dcsperu.Fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

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
import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.EntReferenciaSol;
import net.movilbox.dcsperu.Entry.ExpandableListAdapterSol;
import net.movilbox.dcsperu.Entry.ListEntReferenciaSol;
import net.movilbox.dcsperu.R;

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
    private ListEntReferenciaSol listEntReferenciaSol;
    List<String> listDataHeader;
    HashMap<String, List<EntReferenciaSol>> listDataChild;

    public FragmentSolProducto() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sol_producto, container, false);

        expandable_sol = (ExpandableListView) view.findViewById(R.id.expandable_sol);
        alertDialog = new SpotsDialog(getActivity(), R.style.Custom);
        //controllerLogin = new ControllerLogin(getActivity());
        // Inflate the layout for this fragment
        return view;
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
                    //cancelarPedido(listEntReferenciaSol.getEntSolPedidos2().get(0).getId());
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
            prepareListData(listEntReferenciaSol);
            ExpandableListAdapterSol expandableListAdapter = new ExpandableListAdapterSol(getActivity(), listDataHeader, listDataChild);
            expandable_sol.setAdapter(expandableListAdapter);
            //Puede realizar o solicitar inventario.
            /*expandableListDetail = ExpandableListDataPumpSol.getData(listEntReferenciaSol);
            final ArrayList<String> expandableListTitle = new ArrayList<>(expandableListDetail.keySet());

            final ExpandableListAdapterSol expandableListAdapter = new ExpandableListAdapterSol(getActivity(), expandableListTitle, expandableListDetail);
            expandable_sol.setAdapter(expandableListAdapter);

            expandable_sol.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, final int groupPosition, final int childPosition, long id) {

                    dialog = new DialogEmail(getActivity(), listEntReferenciaSol.getEntSolPedidos().get(groupPosition).getEntReferenciaSols().get(childPosition).getProducto());
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

                                listEntReferenciaSol.getEntSolPedidos().get(groupPosition).getEntReferenciaSols().get(childPosition).setCantidadSol(Integer.parseInt(editSol.getText().toString().trim()));

                                expandableListDetail = ExpandableListDataPumpSol.getData(listEntReferenciaSol);

                                expandableListAdapter.setData(expandableListDetail);

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
            });*/
        }

    }

    private void prepareListData(ListEntReferenciaSol data) {

        if (data != null) {
            for (int i = 0; i < data.getEntSolPedidos().size(); i++) {

                List<EntReferenciaSol> listDataHeader = new ArrayList<>();

                EntReferenciaSol entEstandar;
                for (int a = 0; a < data.getEntSolPedidos().get(i).getEntReferenciaSols().size(); a++) {

                    entEstandar = new EntReferenciaSol();
                    entEstandar.setId_bodega(data.getEntSolPedidos().get(i).getEntReferenciaSols().get(a).getId_bodega());
                    entEstandar.setId_referencia(data.getEntSolPedidos().get(i).getEntReferenciaSols().get(a).getId_referencia());
                    entEstandar.setProducto(data.getEntSolPedidos().get(i).getEntReferenciaSols().get(a).getProducto());
                    entEstandar.setTotal(data.getEntSolPedidos().get(i).getEntReferenciaSols().get(a).getTotal());
                    entEstandar.setTipo_bodega(data.getEntSolPedidos().get(i).getEntReferenciaSols().get(a).getTipo_bodega());
                    entEstandar.setTipo_ref(data.getEntSolPedidos().get(i).getEntReferenciaSols().get(a).getTipo_ref());
                    entEstandar.setCantidadSol(data.getEntSolPedidos().get(i).getEntReferenciaSols().get(a).getCantidadSol());

                    listDataHeader.add(entEstandar);

                }

                listDataChild.put(data.getEntSolPedidos().get(i).getNombre_bodega()+ " - " +data.getEntSolPedidos().get(i).getId(), listDataHeader);

            }
        }
    }
}
