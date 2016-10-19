package net.movilbox.dcsperu.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.movilbox.dcsperu.Activity.DialogRechazar;
import net.movilbox.dcsperu.Entry.AceptarAsignacionProductos;
import net.movilbox.dcsperu.Entry.AceptarPedido;
import net.movilbox.dcsperu.Entry.DetalleAsignacionProductos;
import net.movilbox.dcsperu.R;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.List;

/**
 * Created by jhonjimenez on 18/10/16.
 */

public class AdapterAceptAsigProducto extends BaseAdapter {

    private Activity actx;
    private List<AceptarAsignacionProductos> data;
    protected DialogRechazar dialog;

    public AdapterAceptAsigProducto(Activity actx, List<AceptarAsignacionProductos> data) {
        this.actx = actx;
        this.data = data;
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
    public AceptarAsignacionProductos getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        convertView = View.inflate(actx, R.layout.item_aceptar_asignacion_producto, null);
        new ViewHolder(convertView);

        final ViewHolder holder = (ViewHolder) convertView.getTag();
        final AceptarAsignacionProductos pedido =  data.get(position);

        holder.txtNumeroComprobante.setText(String.format("Número de Pedido: %1$s", pedido.getId_pedido()));
        //holder.txtNumero_pedido.setText(String.format("%1$s",referencias.getNroPedido()));
        holder.txtCantidadSolicitada.setText(String.format("%1$s", pedido.getTotal_cantidad()));
        holder.txtCantidadDespachada.setText(String.format("%1$s", pedido.getTotal_cantidad_carga()));
        holder.txtDetalleLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDetalle(position, pedido);
            }
        });

        holder.radioAceptar.setChecked(pedido.getIndicadorChekend());

        holder.radioDevolver.setChecked(pedido.getIndicadorChekendDevolver());

        holder.radioDevolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new DialogRechazar(actx, "Observación");
                dialog.show();
                Button acceptButton = dialog.getButtonAccept();
                acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isValidNumber(dialog.getEmail().getText().toString().trim())) {
                            dialog.getEmail().setError("Este campo es obligatorio");
                            dialog.getEmail().requestFocus();
                        } else {
                            pedido.setComentarioRechazo(dialog.getEmail().getText().toString());
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

       holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = group.findViewById(checkedId);
                int radioId = group.indexOfChild(radioButton);

                if (radioId == 0) {
                    pedido.marcaProducto = "devolver";
                    pedido.indicadorChekendDevolver = true;
                    pedido.indicadorChekend = false;
                } else {

                    if (pedido.marcaProducto.equals("devolver")) {
                        Toast.makeText(actx, "La observación", Toast.LENGTH_LONG).show();
                    }

                    pedido.marcaProducto = "aceptar";
                    pedido.indicadorChekend = true;
                    pedido.indicadorChekendDevolver = false;
                }

            }
        });


        return convertView;
    }

    private boolean isValidNumber(String number) {
        return number == null || number.length() == 0;
    }

    private void DialogDetalle(final int position, final AceptarAsignacionProductos pedido) {

        LayoutInflater inflater = actx.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.dialog_aceptar_pedido, null);

        ListView listView = (ListView) dialoglayout.findViewById(R.id.listView2);

        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                if (pedido.getDetalleAsignacionProductos() == null) {
                    return 0;
                } else {
                    return pedido.getDetalleAsignacionProductos().size();
                }
            }

            @Override
            public DetalleAsignacionProductos getItem(int position) {
                return pedido.getDetalleAsignacionProductos().get(position);
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

                DetalleAsignacionProductos detallePedido = getItem(position);

                holder.txtReferencia.setText(String.format("%1$s", detallePedido.getReferencia()));
                holder.txtTipoProducto.setText(String.format("%1$s", detallePedido.getTipo_producto()));
                holder.txtCantidadSol.setText(String.format("%1$s", detallePedido.getCantidad()));
                holder.txtCantidaDes.setText(String.format("%1$s", detallePedido.getCantidad_carga()));

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

    class ViewHolder {

        TextView txtNumeroComprobante;
        //TextView txtNumero_pedido;
        TextView txtCantidadSolicitada;
        TextView txtCantidadDespachada;
        TextView txt_idpos;
        TextView txtFechaEstimada;
        TextView txtDetalleLink;
        RadioGroup radioGroup;
        RadioButton radioAceptar;
        RadioButton radioDevolver;

        public ViewHolder(View view) {

            txtNumeroComprobante = (TextView) view.findViewById(R.id.txtNumeroComprobante);
            txtCantidadSolicitada = (TextView) view.findViewById(R.id.txtCantidadSolicitada);
            txtCantidadDespachada = (TextView) view.findViewById(R.id.txtCantidadDespachada);
            txt_idpos = (TextView) view.findViewById(R.id.txt_idpos);
            txtFechaEstimada = (TextView) view.findViewById(R.id.txtFechaEstimada);
            txtDetalleLink = (TextView) view.findViewById(R.id.txtDetalleLink);

            radioGroup = (RadioGroup) view.findViewById(R.id.radio);

            radioAceptar = (RadioButton) view.findViewById(R.id.radioAceptar);
            radioDevolver = (RadioButton) view.findViewById(R.id.radioDevolver);

            view.setTag(this);
        }
    }
}
