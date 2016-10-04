package net.movilbox.dcsperu.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.movilbox.dcsperu.Entry.ResponseMisPedidos;
import net.movilbox.dcsperu.R;

import java.util.List;

public class AdapterMisPedidos extends BaseAdapter {

    private Activity actx;
    List<ResponseMisPedidos> data;

    public AdapterMisPedidos(Activity actx, List<ResponseMisPedidos> data) {
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
    public ResponseMisPedidos getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(actx, R.layout.item_mis_pedidos, null);
            new ViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        ResponseMisPedidos resMis = getItem(position);

        holder.txtNombrePunto.setText(resMis.getNombre_punto());
        holder.txtIdPunto.setText(String.format("ID PDV: %1$s", resMis.getIdpos()));
        holder.txtFecha.setText(String.format("FECHA: %1$s", resMis.getFecha()));
        holder.txtEstado.setText(String.format("ESTADO: %1$s", resMis.getEstado()));
        holder.txtDireccion.setText(String.format("%1$s", resMis.getDireccion()));

        return convertView;
    }

    class ViewHolder {

        TextView txtIdPunto;
        TextView txtFecha;
        TextView txtNombrePunto;
        TextView txtEstado;
        TextView txtDireccion;

        public ViewHolder(View view) {
            txtIdPunto = (TextView) view.findViewById(R.id.txtIdPunto);
            txtFecha = (TextView) view.findViewById(R.id.txtFecha);
            txtNombrePunto = (TextView) view.findViewById(R.id.txtNombrePunto);
            txtEstado = (TextView) view.findViewById(R.id.txtEstado);
            txtDireccion = (TextView) view.findViewById(R.id.txtDireccion);


            view.setTag(this);
        }
    }
}
