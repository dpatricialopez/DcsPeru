package net.movilbox.dcsperu.Entry;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import net.movilbox.dcsperu.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jhonjimenez on 10/10/16.
 */

public class ExpandableListAdapterSol extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<EntReferenciaSol>> expandableListDetail;

    public ExpandableListAdapterSol(Context context, List<String> expandableListTitle, HashMap<String, List<EntReferenciaSol>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public EntReferenciaSol getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final EntReferenciaSol expandedListText = (EntReferenciaSol) getChild(listPosition, expandedListPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_sol_producto, null);
        }

        TextView txt_referencia_sol = (TextView) convertView.findViewById(R.id.txt_referencia_sol);
        TextView txt_cantidad_sol = (TextView) convertView.findViewById(R.id.txt_cantidad_sol);
        TextView txtCantSol = (TextView) convertView.findViewById(R.id.txtCantSol);
        TextView txtPrecio = (TextView) convertView.findViewById(R.id.txtPrecio);

        txt_referencia_sol.setText(String.format("%s", expandedListText.getProducto()));
        txt_cantidad_sol.setText(String.format("Disponible: %s", expandedListText.getTotal()));
        txtPrecio.setText(String.format("S/.  %s", expandedListText.getPrecio_pdv()));

        txtCantSol.setVisibility(View.GONE);
        txtCantSol.setText(String.format("%s", 0));

        if (expandedListText.getCantidadSol() > 0) {
            txtCantSol.setVisibility(View.VISIBLE);
            txtCantSol.setText(String.format("Cantidad Solicitada: %s", expandedListText.getCantidadSol()));
        } else {
            txtCantSol.setVisibility(View.GONE);
            txtCantSol.setText(String.format("%s", 0));
        }


        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        if (this.expandableListDetail.get(this.expandableListTitle.get(listPosition)) == null) {
            return 0;
        } else {
            return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).size();
        }
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        if (this.expandableListTitle == null) {
            return 0;
        } else {
            return this.expandableListTitle.size();
        }
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_inventario_list_detalle, null);
        }

        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        String nombretitulo;

        if (listTitle.equals("0"))
            nombretitulo = "Sin Paquete";
        else
            nombretitulo = String.format("Paquete: %s", listTitle);

        listTitleTextView.setText(nombretitulo);
        return convertView;
    }


    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    public void setData(HashMap<String, List<EntReferenciaSol>> expandableListDetail){
        this.expandableListDetail = expandableListDetail;
        notifyDataSetChanged();
    }
}
