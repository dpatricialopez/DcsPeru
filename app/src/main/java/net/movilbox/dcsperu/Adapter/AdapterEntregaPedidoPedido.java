package net.movilbox.dcsperu.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import net.movilbox.dcsperu.Activity.ActMainPeru;
import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.DetallePedido;
import net.movilbox.dcsperu.Entry.PedidosEntrega;
import net.movilbox.dcsperu.Entry.ResponseMarcarPedido;
import net.movilbox.dcsperu.R;
import net.movilbox.dcsperu.Services.ConnectionDetector;
import net.movilbox.dcsperu.Services.GpsServices;

import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import dmax.dialog.SpotsDialog;

import static net.movilbox.dcsperu.Entry.EntLoginR.setIndicador_refres;

public class AdapterEntregaPedidoPedido extends BaseAdapter {

    private Activity actx;
    private List<PedidosEntrega> data;
    private Format format;
    private SpotsDialog alertDialog;
    private RequestQueue rq;
    public static final String TAG = "MyTag";
    private String comentario;
    private GpsServices gpsServices;
    private ConnectionDetector connectionDetector;
    private DBHelper mydb;

    public AdapterEntregaPedidoPedido(Activity actx, List<PedidosEntrega> data) {
        this.actx = actx;
        this.data = data;
        format = new DecimalFormat("#,###.##");

        mydb = new DBHelper(actx);

        gpsServices = new GpsServices(actx);

        alertDialog = new SpotsDialog(actx, R.style.Custom);

        connectionDetector = new ConnectionDetector(actx);



    }

    @Override
    public int getCount() {
        if (data == null) {
            return 0;
        } else {
            return data.size();
        }
    }

    @Override
    public PedidosEntrega getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(actx, R.layout.item_entrega_por_pedido, null);
            new ViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        final PedidosEntrega referencias = getItem(position);

        holder.txt_idpos.setText(String.format("Pedido Número: %1$s", referencias.getNroPedido()));
        holder.txtVendedor.setText(String.format("%1$s", referencias.getNombre_usu()));
        holder.txtFeEntrega.setText(String.format("%1$s", referencias.getFecha_entrega()));
        holder.txtSolFec.setText(String.format("%1$s", referencias.getFecha_pedido()));
        holder.txtArtDes.setText(String.format("%1$s", referencias.getCant_pedido_p()));
        holder.txtImpuesto.setText(String.format("%1$s", referencias.getIgv()));
        holder.txtSubTotal.setText(String.format("S/. %1$s", format.format(referencias.getSub_total())));
        holder.txtImpuestoTotal.setText(String.format("S/. %1$s", format.format(referencias.getTotal_impueto_igv())));
        holder.txtTotalPedido.setText(String.format("S/. %1$s", format.format(referencias.getTotal_pedido_p())));
        holder.txtArtSol.setText(String.format("%1$s", referencias.getCant_pedido()));



        holder.btnDevolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = actx.getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.dialog_comentario_aproba, null);

                final EditText editTextComent = (EditText) dialoglayout.findViewById(R.id.EditComment);

                AlertDialog.Builder builder2 = new AlertDialog.Builder(actx);
                builder2.setCancelable(false);
                builder2.setTitle("Motivo de devolución # " + referencias.getNroPedido());
                builder2.setView(dialoglayout).setPositiveButton("Devolver", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (isValidNumber(editTextComent.getText().toString().trim())) {
                            Toast.makeText(actx, "El comentario es un campo requerido", Toast.LENGTH_LONG).show();
                        } else {
                            comentario = editTextComent.getText().toString();

                            if (connectionDetector.isConnected()) {
                                setDevolver(referencias.getNroPedido(), comentario, referencias.getIdpos(), position);
                            } else {
                                if (!mydb.validarPedidosDuplicados(referencias.getNroPedido(), referencias.getIdpos())) {
                                    if (mydb.insetEntregaPedido(gpsServices.getLatitude(), gpsServices.getLongitude(), mydb.getUserLogin().getId(), Integer.parseInt(mydb.getUserLogin().getId_distri()),
                                            mydb.getUserLogin().getBd(), referencias.getIdpos(), comentario, referencias.getNroPedido(), "devolucion")) {

                                        Toast.makeText(actx, "El pedido fue devuelto corectamente", Toast.LENGTH_LONG).show();
                                        data.remove(position);
                                        notifyDataSetChanged();

                                        if (data == null || data.size() == 0) {
                                            //Activity Principal, Para acceder al fragment
                                            setIndicador_refres(1);
                                            Intent intent = new Intent(actx, ActMainPeru.class);
                                            actx.startActivity(intent);


                                        }
                                    }
                                } else {
                                    Toast.makeText(actx, "El pedido ya se encuentra en estado devuelto o entregado por favor sincronizar", Toast.LENGTH_LONG).show();
                                }

                            }

                        }

                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                builder2.show();
            }
        });

        holder.btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Entregar...
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(actx);
                builder.setCancelable(false);
                builder.setTitle("Alerta");
                builder.setMessage("¿ Estas seguro de entregar el pedido # " + referencias.getNroPedido() + " ?");
                builder.setPositiveButton("Entregar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (connectionDetector.isConnected()) {
                            setEntregarPedido(referencias.getNroPedido(), referencias.getIdpos(), position);
                        } else {
                            if (!mydb.validarPedidosDuplicados(referencias.getNroPedido(), referencias.getIdpos())) {
                                if (mydb.insetEntregaPedido(gpsServices.getLatitude(), gpsServices.getLongitude(), mydb.getUserLogin().getId(), Integer.parseInt(mydb.getUserLogin().getId_distri()),
                                        mydb.getUserLogin().getBd(), referencias.getIdpos(), comentario, referencias.getNroPedido(), "entrega")) {

                                    Toast.makeText(actx, "El pedido fue entregado localmente Sincronizar para entregarlo", Toast.LENGTH_LONG).show();
                                    data.remove(position);
                                    notifyDataSetChanged();

                                    if (data == null || data.size() == 0) {
                                        //Activity Principal, Para acceder al fragment
                                        Intent intent = new Intent(actx, ActMainPeru.class);
                                        actx.startActivity(intent);
                                    }

                                }
                            } else {
                                Toast.makeText(actx, "El pedido ya se encuentra en estado devuelto o entregado por favor sincronizar", Toast.LENGTH_LONG).show();
                            }

                        }

                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                builder.show();


            }
        });

        holder.txtDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogDetalle(position, data.get(position).getDetallePedidoList());

            }
        });

        return convertView;

    }

    private void DialogDetalle(final int position, final  List<DetallePedido> referencias) {

        LayoutInflater inflater = actx.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.dialog_aceptar_pedido, null);

        ListView listView = (ListView) dialoglayout.findViewById(R.id.listView2);

        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                if (referencias == null) {
                    return 0;
                } else {
                    return referencias.size();
                }
            }

            @Override
            public DetallePedido getItem(int position) {
                return referencias.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                ViewHolder holder;
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = View.inflate(actx, R.layout.item_detalle_aceptar_pedido, null);
                    holder.txtReferencia = (TextView) convertView.findViewById(R.id.txtReferencia);
                    holder.txtTipoProducto = (TextView) convertView.findViewById(R.id.txtTipoProducto);
                    holder.txtCantidadSol = (TextView) convertView.findViewById(R.id.txtCantidadSol);
                    holder.txtCantidaDes = (TextView) convertView.findViewById(R.id.txtCantidaDes);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                DetallePedido aceptarPedido = getItem(position);

                holder.txtReferencia.setText(String.format("%1$s", aceptarPedido.getNombre_sku()));
                holder.txtTipoProducto.setText(String.format("%1$s", aceptarPedido.getTipo_pro()));
                holder.txtCantidadSol.setText(String.format("%1$s", aceptarPedido.getCant_pedido()));
                holder.txtCantidaDes.setText(String.format("%1$s", aceptarPedido.getCant_pedido_p()));

                return convertView;
            }

            class ViewHolder {

                TextView txtReferencia;
                TextView txtTipoProducto;
                TextView txtCantidadSol;
                TextView txtCantidaDes;
            }
        });

        AlertDialog.Builder builder2 = new AlertDialog.Builder(actx);
        builder2.setCancelable(false);
        builder2.setTitle("");
        builder2.setView(dialoglayout).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        builder2.show();

    }

    private void setEntregarPedido(final int nroPedido, final int idpos, final int position) {
        alertDialog.show();
        String url = String.format("%1$s%2$s", actx.getString(R.string.url_base), "entregar_pedido");
        rq = Volley.newRequestQueue(actx);
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        parseJSONEntregar(response, position);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(actx, "Error de tiempo de espera", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(actx, "Error Servidor", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(actx, "Server Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(actx, "Error de red", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(actx, "Error al serializar los datos", Toast.LENGTH_LONG).show();
                        }

                        alertDialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("latitud", String.valueOf(gpsServices.getLatitude()));
                params.put("longitud", String.valueOf(gpsServices.getLongitude()));

                params.put("iduser", String.valueOf(mydb.getUserLogin().getId()));
                params.put("iddis", mydb.getUserLogin().getId_distri());
                params.put("db", mydb.getUserLogin().getBd());
                params.put("idpos", String.valueOf(idpos));
                params.put("idpedido", String.valueOf(nroPedido));

                return params;
            }
        };
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(jsonRequest);
    }

    private void parseJSONEntregar(String response, int position) {
        Gson gson = new Gson();
        if (!response.equals("[]")) {
            try {

                Charset.forName("UTF-8").encode(response);

                byte ptext[] = response.getBytes(Charset.forName("ISO-8859-1"));

                String value = new String(ptext, Charset.forName("UTF-8"));

                ResponseMarcarPedido responseMarcarPedido = gson.fromJson(value, ResponseMarcarPedido.class);

                if (responseMarcarPedido.getEstado() == -1) {
                    //El punto no tiene regional
                    Toast.makeText(actx, responseMarcarPedido.getMsg(), Toast.LENGTH_LONG).show();
                } else if (responseMarcarPedido.getEstado() == 0) {
                    //Error al intentar el pedido no tiene zona o territorio

                    Toast.makeText(actx, responseMarcarPedido.getMsg(), Toast.LENGTH_LONG).show();
                    data.remove(position);
                    notifyDataSetChanged();
                    if (data == null || data.size() == 0) {
                        //Activity Principal, Para acceder al fragment
                        Intent intent = new Intent(actx, ActMainPeru.class);
                        actx.startActivity(intent);
                    }
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

    private boolean isValidNumber(String number) {
        return number == null || number.length() == 0;
    }

    class ViewHolder {

        TextView txt_idpos;
        TextView txtVendedor;
        TextView txtFeEntrega;
        TextView txtSolFec;
        TextView txtArtSol;
        TextView txtArtDes;
        TextView txtImpuesto;
        TextView txtSubTotal;
        TextView txtImpuestoTotal;
        TextView txtTotalPedido;
        TextView txtDetalle;
        Button btnDevolver;
        Button btn_guardar;

        public ViewHolder(View view) {

            txt_idpos = (TextView) view.findViewById(R.id.txt_idpos);
            txtVendedor = (TextView) view.findViewById(R.id.txtVendedor);
            txtFeEntrega = (TextView) view.findViewById(R.id.txtFeEntrega);
            txtSolFec = (TextView) view.findViewById(R.id.txtSolFec);
            txtArtDes = (TextView) view.findViewById(R.id.txtArtDes);
            txtImpuesto = (TextView) view.findViewById(R.id.txtImpuesto);
            txtSubTotal = (TextView) view.findViewById(R.id.txtSubTotal);
            txtImpuestoTotal = (TextView) view.findViewById(R.id.txtImpuestoTotal);
            txtTotalPedido = (TextView) view.findViewById(R.id.txtTotalPedido);
            txtArtSol = (TextView) view.findViewById(R.id.txtArtSol);
            txtDetalle = (TextView) view.findViewById(R.id.txtDetalle);
            btnDevolver = (Button) view.findViewById(R.id.btnDevolver);
            btn_guardar = (Button) view.findViewById(R.id.btn_guardar);

            view.setTag(this);
        }
    }

    private void setDevolver(final int nroPedido, final String comentario, final int idpos, final int idposition) {
        alertDialog.show();
        String url = String.format("%1$s%2$s", actx.getString(R.string.url_base), "devolver_pedido");
        rq = Volley.newRequestQueue(actx);
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        parseJSON(response, idposition);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(actx, "Error de tiempo de espera", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(actx, "Error Servidor", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(actx, "Server Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(actx, "Error de red", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(actx, "Error al serializar los datos", Toast.LENGTH_LONG).show();
                        }

                        alertDialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("latitud", String.valueOf(gpsServices.getLatitude()));
                params.put("longitud", String.valueOf(gpsServices.getLongitude()));

                params.put("iduser", String.valueOf(mydb.getUserLogin().getId()));
                params.put("iddis", mydb.getUserLogin().getId_distri());
                params.put("db", mydb.getUserLogin().getBd());
                params.put("idpos", String.valueOf(idpos));
                params.put("obs", comentario);
                params.put("idpedido", String.valueOf(nroPedido));

                return params;
            }
        };
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(jsonRequest);
    }

    private void parseJSON(String response, int idposition) {

        Gson gson = new Gson();
        if (!response.equals("[]")) {
            try {

                Charset.forName("UTF-8").encode(response);

                byte ptext[] = response.getBytes(Charset.forName("ISO-8859-1"));

                String value = new String(ptext, Charset.forName("UTF-8"));

                ResponseMarcarPedido responseMarcarPedido = gson.fromJson(value, ResponseMarcarPedido.class);

                if (responseMarcarPedido.getEstado() == -1) {
                    //El punto no tiene regional
                    Toast.makeText(actx, responseMarcarPedido.getMsg(), Toast.LENGTH_LONG).show();
                } else if (responseMarcarPedido.getEstado() == 0) {
                    //Error al intentar el pedido no tiene zona o territorio

                    Toast.makeText(actx, responseMarcarPedido.getMsg(), Toast.LENGTH_LONG).show();
                    data.remove(idposition);
                    notifyDataSetChanged();

                    if (data == null || data.size() == 0) {
                        //Activity Principal, Para acceder al fragment
                        Intent intent = new Intent(actx, ActMainPeru.class);
                        actx.startActivity(intent);
                    }
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

}
