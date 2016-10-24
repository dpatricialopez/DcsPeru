package net.movilbox.dcsperu.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.movilbox.dcsperu.Activity.ActSimcardPaqueteVenta;
import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.ReferenciasSims;
import net.movilbox.dcsperu.Entry.RowViewHolderSimcard;
import net.movilbox.dcsperu.R;

import java.util.List;

/**
 * Created by jhonjimenez on 21/10/16.
 */

public class AdapterRecyclerSimcardAutoVenta extends RecyclerView.Adapter<RowViewHolderSimcard>{

    private Activity context;
    private List<ReferenciasSims> responseHomeList;
    private DBHelper mydb;
    private int idPos;
    private int idUsuario;

    public AdapterRecyclerSimcardAutoVenta(Activity context, List<ReferenciasSims> responseHomeList, int idPos, int idUsuario) {
        super();
        this.context = context;
        this.responseHomeList = responseHomeList;
        this.idPos = idPos;
        this.idUsuario = idUsuario;

        mydb = new DBHelper(context);

    }
    @Override
    public RowViewHolderSimcard onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simcard_auto_venta, parent, false);
        return new RowViewHolderSimcard(v, context);

    }

    @Override
    public void onBindViewHolder(final RowViewHolderSimcard holder, final int position) {

        // 1 Esta en quiebre.
        // 0 No esta en quiebre.
        if(responseHomeList.get(position).getTipo_producto() == 1){

            holder.imgQuiebre.setVisibility(View.GONE);
            if (responseHomeList.get(position).getQuiebre() == 1) {
                holder.imgQuiebre.setVisibility(View.VISIBLE);
            }

            holder.txt_referemcia.setText(responseHomeList.get(position).getProducto());
            holder.txtStock.setText(String.format("STOCK  %s", responseHomeList.get(position).getStock()));

            holder.txtInven.setText(String.format("D. INV %s", (int) responseHomeList.get(position).getDias_inve()));

            //holder.txtCantidadPedida.setText(String.format("Cantidad %s", mydb.countSimcardProduct(idUsuario, idPos, responseHomeList.get(position).getId())));
            holder.txtCantidadPedida.setVisibility(View.GONE);;
            holder.txtprecio.setText(String.format("S/. %s", responseHomeList.get(position).getPrecio_referencia()));

            holder.btnCatalogoSim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,ActSimcardPaqueteVenta.class);
                    intent.putExtra("id_referencia",String.valueOf(responseHomeList.get(position).getId()));
                    context.startActivity(intent);

                /*LayoutInflater inflater = context.getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.dialog_sim_pedido, null);

                TextView txtCanSugerida = (TextView) dialoglayout.findViewById(R.id.txtCanSugerida);
                final EditText editCantidad = (EditText) dialoglayout.findViewById(R.id.editCantidad);

                txtCanSugerida.setText(String.format("%s", responseHomeList.get(position).getPed_sugerido()));

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                builder.setTitle(responseHomeList.get(position).getProducto());
                builder.setView(dialoglayout);

                builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (isValidNumber(editCantidad.getText().toString())) {
                            Toast.makeText(context, "La cantidad es un campo requerido", Toast.LENGTH_SHORT).show();
                        } else {
                            int cantidad_dig = Integer.parseInt(editCantidad.getText().toString());
                            if (cantidad_dig <= 0) {
                                Toast.makeText(context, "la cantidad digitada tiene que ser mayor a 0", Toast.LENGTH_SHORT).show();
                            } else {
                                //Guardar
                                responseHomeList.get(position).setCantidadPedida(cantidad_dig);
                                responseHomeList.get(position).setId_punto(getId_posStacti());
                                responseHomeList.get(position).setId_usuario(mydb.getUserLogin().getId());
                                responseHomeList.get(position).setTipo_producto(1); // Simcard

                                String resultado = mydb.insertCarritoPedido(responseHomeList.get(position));

                                if (resultado.equals("inserto")) {
                                    Toast.makeText(context, "Pedido guardado correctamente", Toast.LENGTH_SHORT).show();
                                    //holder.txtCantidadPedida.setVisibility(View.VISIBLE);
                                    holder.txtCantidadPedida.setText(String.format("Cantidad %s", cantidad_dig));
                                } else if (resultado.equals("no inserto")) {
                                    Toast.makeText(context, "Problemas al guardar el pedido", Toast.LENGTH_SHORT).show();
                                } else if (resultado.equals("update")) {
                                    Toast.makeText(context, "Pedido actualizado correctamente", Toast.LENGTH_SHORT).show();
                                    //holder.txtCantidadPedida.setVisibility(View.VISIBLE);
                                    holder.txtCantidadPedida.setText(String.format("Cantidad %s", cantidad_dig));
                                } else if (resultado.equals("no update")) {
                                    Toast.makeText(context, "Pedido no se pudo actualizar", Toast.LENGTH_SHORT).show();
                                }

                                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(editCantidad.getWindowToken(), 0);
                            }
                        }
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editCantidad.getWindowToken(), 0);
                    }
                });

                builder.show();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);*/
                }
            });
        }else if(responseHomeList.get(position).getTipo_producto() == 2){
            holder.imgQuiebre.setVisibility(View.GONE);
            //if (responseHomeList.get(position).getQuiebre() == 1) {
              //  holder.imgQuiebre.setVisibility(View.VISIBLE);
            //}

            holder.txt_referemcia.setText("Paquete ("+String.valueOf(responseHomeList.get(position).getId())+")");
            holder.txtStock.setText(String.format("STOCK  %s", responseHomeList.get(position).getStock()));

            holder.txtInven.setVisibility(View.GONE);

            holder.txtCantidadPedida.setVisibility(View.GONE);;

            holder.txtprecio.setVisibility(View.GONE);

            holder.btnCatalogoSim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("idpaquete",String.valueOf(responseHomeList.get(position).getId()));
                    //Intent intent = new Intent(context,ActSimcardPaqueteVenta.class);
                    //intent.putExtra("id_referencia",String.valueOf(responseHomeList.get(position).getId()));
                    //context.startActivity(intent);

                /*LayoutInflater inflater = context.getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.dialog_sim_pedido, null);

                TextView txtCanSugerida = (TextView) dialoglayout.findViewById(R.id.txtCanSugerida);
                final EditText editCantidad = (EditText) dialoglayout.findViewById(R.id.editCantidad);

                txtCanSugerida.setText(String.format("%s", responseHomeList.get(position).getPed_sugerido()));

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                builder.setTitle(responseHomeList.get(position).getProducto());
                builder.setView(dialoglayout);

                builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (isValidNumber(editCantidad.getText().toString())) {
                            Toast.makeText(context, "La cantidad es un campo requerido", Toast.LENGTH_SHORT).show();
                        } else {
                            int cantidad_dig = Integer.parseInt(editCantidad.getText().toString());
                            if (cantidad_dig <= 0) {
                                Toast.makeText(context, "la cantidad digitada tiene que ser mayor a 0", Toast.LENGTH_SHORT).show();
                            } else {
                                //Guardar
                                responseHomeList.get(position).setCantidadPedida(cantidad_dig);
                                responseHomeList.get(position).setId_punto(getId_posStacti());
                                responseHomeList.get(position).setId_usuario(mydb.getUserLogin().getId());
                                responseHomeList.get(position).setTipo_producto(1); // Simcard

                                String resultado = mydb.insertCarritoPedido(responseHomeList.get(position));

                                if (resultado.equals("inserto")) {
                                    Toast.makeText(context, "Pedido guardado correctamente", Toast.LENGTH_SHORT).show();
                                    //holder.txtCantidadPedida.setVisibility(View.VISIBLE);
                                    holder.txtCantidadPedida.setText(String.format("Cantidad %s", cantidad_dig));
                                } else if (resultado.equals("no inserto")) {
                                    Toast.makeText(context, "Problemas al guardar el pedido", Toast.LENGTH_SHORT).show();
                                } else if (resultado.equals("update")) {
                                    Toast.makeText(context, "Pedido actualizado correctamente", Toast.LENGTH_SHORT).show();
                                    //holder.txtCantidadPedida.setVisibility(View.VISIBLE);
                                    holder.txtCantidadPedida.setText(String.format("Cantidad %s", cantidad_dig));
                                } else if (resultado.equals("no update")) {
                                    Toast.makeText(context, "Pedido no se pudo actualizar", Toast.LENGTH_SHORT).show();
                                }

                                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(editCantidad.getWindowToken(), 0);
                            }
                        }
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editCantidad.getWindowToken(), 0);
                    }
                });

                builder.show();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);*/
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return responseHomeList.size();
    }
}
