package net.movilbox.dcsperu.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import net.movilbox.dcsperu.Adapter.AdapterInventario;
import net.movilbox.dcsperu.Adapter.ExpandableListAdapterInventario;
import net.movilbox.dcsperu.Adapter.ExpandableListDataPump;
import net.movilbox.dcsperu.Adapter.ExpandableListDataPumpInventario;
import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.EntEstandar;
import net.movilbox.dcsperu.Entry.EntLisSincronizar;
import net.movilbox.dcsperu.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentInventarioVendedor extends BaseVolleyFragment {

    private ListView listViewInventario;
    private DBHelper mydb;
    public FragmentInventarioVendedor() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mydb = new DBHelper(getActivity());

        View view = inflater.inflate(R.layout.fragment_inventario_vendedor, container, false);

        listViewInventario = (ListView) view.findViewById(R.id.listViewInventario);

        setHasOptionsMenu(false);

        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final List<EntLisSincronizar> listReferenciases = mydb.listReferenciasesReport(1);

        AdapterInventario adapterCarrito = new AdapterInventario(getActivity(), listReferenciases);
        listViewInventario.setAdapter(adapterCarrito);
        listViewInventario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LayoutInflater inflater = getActivity().getLayoutInflater();

                View dialoglayout = inflater.inflate(R.layout.dialog_inventario, null);

                ExpandableListView expandableListView = (ExpandableListView) dialoglayout.findViewById(R.id.expandableListView);

                //Recuperar los paquetes con sus respectivos seriales.
                List<EntLisSincronizar> entListReferenciases = mydb.listPaqueteInvent(listReferenciases.get(position).getId_referencia());

                HashMap<String, List<EntEstandar>> expandableListDetail = ExpandableListDataPumpInventario.getData(entListReferenciases);
                ArrayList<String> expandableListTitle = new ArrayList<>(expandableListDetail.keySet());

                ExpandableListAdapterInventario expandableListAdapter = new ExpandableListAdapterInventario(getActivity(), expandableListTitle, expandableListDetail);
                expandableListView.setAdapter(expandableListAdapter);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false);
                builder.setTitle("Detalle Referencia");
                builder.setView(dialoglayout).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });

    }
}
