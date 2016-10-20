package net.movilbox.dcsperu.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.movilbox.dcsperu.Entry.EntLisSincronizar;
import net.movilbox.dcsperu.R;

import java.util.List;

/**
 * Created by jhonjimenez on 20/10/16.
 */

public class AdapterInventario extends BaseAdapter{

    private Context ctx;
    private List<EntLisSincronizar> listReferencias;

    public AdapterInventario(Context ctx, List<EntLisSincronizar> listReferencias) {
        this.ctx = ctx;
        this.listReferencias = listReferencias;
    }

    @Override
    public int getCount() {
        if (listReferencias == null){
            return 0;
        }else{
            return listReferencias.size();
        }
    }

    @Override
    public EntLisSincronizar getItem(int i) {
        return listReferencias.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null){
            view = View.inflate(ctx, R.layout.item_inventario,null);
            new ViewHolder(view);
        }
        ViewHolder holder = (ViewHolder) view.getTag();

        EntLisSincronizar entListReferencias = getItem(i);

        holder.txt_referencia.setText(String.format("Referencia: %1$s", entListReferencias.getProducto()));
        String tipo = (entListReferencias.getTipo_pro() == 1) ? "Simcards": "Combo";
        holder.txt_tipo.setText(String.format("Tipo: %1$s", tipo));
        holder.txt_cantidad.setText(String.format("Cantidad: %1$s", entListReferencias.getStock()));

        return view;
    }

    class ViewHolder {

        TextView txt_referencia;
        TextView txt_cantidad;
        TextView txt_tipo;

        public ViewHolder(View view) {

            txt_referencia = (TextView) view.findViewById(R.id.txt_referencia);
            txt_cantidad = (TextView) view.findViewById(R.id.txt_cantidad);
            txt_tipo = (TextView) view.findViewById(R.id.txt_tipo);
            view.setTag(this);
        }
    }
}
