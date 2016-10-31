package net.movilbox.dcsperu.Adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import net.movilbox.dcsperu.Entry.ReferenciasSims;
import net.movilbox.dcsperu.R;
import java.util.List;

/**
 * Created by jhonjimenez on 21/10/16.
 */

public class AdapterRecyclerSimcardAutoVenta extends RecyclerView.Adapter<AdapterRecyclerSimcardAutoVenta.ViewHolder>{

    private List<ReferenciasSims> listaSims;
    private ViewHolder viewHolder;
    private OnSetIdReferencia listener;

    //creamos interfaz que va  hacer implementada en el fragment FragmentSimcardAutoVenta
    public interface OnSetIdReferencia{
        void setIdReferencia(ReferenciasSims idReferencia);
    }

    public AdapterRecyclerSimcardAutoVenta(List<ReferenciasSims> listaSims,OnSetIdReferencia listener) {
        this.listaSims = listaSims;
        this.listener = listener;


    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simcard_auto_venta, parent, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        ReferenciasSims item = listaSims.get(position);
        viewHolder.bindPersona(item);

    }

    @Override
    public int getItemCount() {
        return listaSims.size();
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

        public void bindPersona (ReferenciasSims refeSims) {

            imgQuiebre.setVisibility(View.GONE);
            if (refeSims.getQuiebre() == 1) {
                imgQuiebre.setVisibility(View.VISIBLE);
            }

            txt_referemcia.setText(refeSims.getProducto());
            txtStock.setText(String.format("STOCK  %s", refeSims.getStock()));
            txtInven.setText(String.format("D. INV %s", (int) refeSims.getDias_inve()));
            txtCantidadPedida.setText(String.format("CANTIDAD %s",refeSims.getCantidadPedida()));//mydb.getCantidadSoli(refeSims.get,refeSims.getTipo_producto());
            txtprecio.setText(String.format("S/. %s", refeSims.getPrecio_referencia()));
            btnCatalogoSim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //mandamos el idReferencia al parametro de la interfaz
                    listener.setIdReferencia(listaSims.get(getAdapterPosition()));
                }
            });

        }
    }

}
