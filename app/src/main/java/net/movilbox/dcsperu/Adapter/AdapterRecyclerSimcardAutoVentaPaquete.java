package net.movilbox.dcsperu.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.movilbox.dcsperu.Entry.ListaPaquete;
import net.movilbox.dcsperu.Entry.ReferenciasSims;
import net.movilbox.dcsperu.R;

import java.util.List;

/**
 * Created by jhonjimenez on 26/10/16.
 */

public class AdapterRecyclerSimcardAutoVentaPaquete extends RecyclerView.Adapter<AdapterRecyclerSimcardAutoVentaPaquete.ViewHolder> {

    private List<ListaPaquete> listaPaquetes;
    private OnSetIdPaquete listener;
    private Context ctx;
    private ViewHolder viewHolder;

    public interface OnSetIdPaquete{
        void setIdPaquete(int position);
        void setSerie(int position);
    }


    public AdapterRecyclerSimcardAutoVentaPaquete(Context ctx, List<ListaPaquete> listaPaquetes, OnSetIdPaquete listener) {
        this.listaPaquetes = listaPaquetes;
        this.ctx = ctx;
        this.listener = listener;
    }

    @Override
    public AdapterRecyclerSimcardAutoVentaPaquete.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simcard_auto_venta, parent, false);
        viewHolder = new AdapterRecyclerSimcardAutoVentaPaquete.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterRecyclerSimcardAutoVentaPaquete.ViewHolder holder, int position) {
        ListaPaquete item = listaPaquetes.get(position);
        viewHolder.bindPersona(item);
    }

    @Override
    public int getItemCount() {
        return listaPaquetes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_referemcia;
        public TextView txtStock;
        public Button btnCatalogoSim;
        public TextView txtCantidadPedida;
        public TextView txtInven;
        public TextView txtprecio;
        public ImageView imgQuiebre;
        public ImageView img_producto;

        public ViewHolder (View itemView) {

            super(itemView);

            this.txt_referemcia = (TextView) itemView.findViewById(R.id.txt_referemcia);
            this.txtprecio = (TextView) itemView.findViewById(R.id.txtprecio);
            this.txtStock = (TextView) itemView.findViewById(R.id.txtStock);
            this.txtCantidadPedida = (TextView) itemView.findViewById(R.id.txtCantidadPedida);
            this.txtInven = (TextView) itemView.findViewById(R.id.txtInven);
            this.btnCatalogoSim = (Button) itemView.findViewById(R.id.btnCatalogoSim);
            this.imgQuiebre = (ImageView) itemView.findViewById(R.id.imgQuiebre);
            this.img_producto = (ImageView) itemView.findViewById(R.id.img_producto);

        }

        public void bindPersona (final ListaPaquete refeSims) {

            imgQuiebre.setVisibility(View.GONE);
            if(refeSims.getIdPaquete() == 0){
                txt_referemcia.setText(String.format("SIN PAQUETE (%s)",refeSims.getIdPaquete()));
            }else{
                txt_referemcia.setText(String.format("PAQUETE (%s)",refeSims.getIdPaquete()));
            }

            txtStock.setText(String.format("STOCK  %s", refeSims.getCantidadPaquete()));
            img_producto.setImageResource(R.mipmap.ic_packsims2);
            txtInven.setVisibility(View.GONE);
            txtCantidadPedida.setText(String.format("CANTIDAD %s",refeSims.getCantidadSoli()));
            txtprecio.setVisibility(View.GONE);
            btnCatalogoSim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LayoutInflater inflater = ((Activity)ctx).getLayoutInflater();

                    View view = inflater.inflate(R.layout.dialog_autoventa, null);

                    final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setView(view);
                    builder.setCancelable(false);

                    Button btnCancelar = (Button) view.findViewById(R.id.btnCancelar);
                    Button btnAgregar = (Button) view.findViewById(R.id.btnAgregar);
                    Button btnSeleccionar = (Button) view.findViewById(R.id.btnSelecionar);

                    if(refeSims.getIdPaquete() == 0){
                        btnAgregar.setVisibility(View.GONE);
                    }

                    final AlertDialog alertDialog = builder.create();

                    btnCancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                    btnAgregar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.setIdPaquete(getAdapterPosition());
                            alertDialog.dismiss();
                        }
                    });
                    btnSeleccionar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.setSerie(getAdapterPosition());
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.show();
                }
            });

        }
    }
}
