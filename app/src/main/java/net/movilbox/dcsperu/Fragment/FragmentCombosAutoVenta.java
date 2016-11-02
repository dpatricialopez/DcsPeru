package net.movilbox.dcsperu.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.movilbox.dcsperu.Activity.ActDetalleProducto;
import net.movilbox.dcsperu.Activity.SpacesItemDecoration;
import net.movilbox.dcsperu.Adapter.AdapterRecyclerCombos;
import net.movilbox.dcsperu.Adapter.AdapterRecyclerCombosAutoVenta;
import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.ReferenciasCombos;
import net.movilbox.dcsperu.Entry.ResponseVenta;
import net.movilbox.dcsperu.R;
import net.movilbox.dcsperu.Services.ConnectionDetector;

import java.util.List;

import dmax.dialog.SpotsDialog;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class FragmentCombosAutoVenta extends Fragment implements AdapterRecyclerCombosAutoVenta.OnSetIdReferenciaGlobal{

    private int idpos;
    private int idZonaLocal;
    private RecyclerView.Adapter adapter2;
    private RecyclerView recycler2;
    private SpotsDialog alertDialog;
    private GridLayoutManager gridLayoutManagerVertical;
    private ResponseVenta responseVenta;
    private List<ReferenciasCombos> filterList;
    private ConnectionDetector connectionDetector;
    private DBHelper mydb;
    private ListenerComboRefeGlobal listener;

    public FragmentCombosAutoVenta() {

    }

    public FragmentCombosAutoVenta(int idpos, int idZona) {
        this.idpos = idpos;
        idZonaLocal = idZona;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_combos, container, false);

        alertDialog = new SpotsDialog(getActivity(), R.style.Custom);

        recycler2 = (RecyclerView) rootView.findViewById(R.id.recycler_view2);

        gridLayoutManagerVertical = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);

        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mydb = new DBHelper(getActivity());
        connectionDetector = new ConnectionDetector(getActivity());
        setHasOptionsMenu(true);
        getComboLocal();
    }

    private void getComboLocal() {

        List<ReferenciasCombos> listaCombosAgrupado = mydb.getCombosLocalVenta(String.valueOf(idZonaLocal));

        adapter2 = new AdapterRecyclerCombosAutoVenta(getActivity(),listaCombosAgrupado,this);
        recycler2.setAdapter(adapter2);
        recycler2.setLayoutManager(gridLayoutManagerVertical);
        int dips = 0;
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        switch(metrics.densityDpi) {
            case DisplayMetrics.DENSITY_XHIGH:
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                dips = 20;
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                break;
            case DisplayMetrics.DENSITY_HIGH: //HDPI
                dips = 6;
                break;
            case DisplayMetrics.DENSITY_MEDIUM: //MDPI
                break;
            case DisplayMetrics.DENSITY_LOW:  //LDPI
                break;
        }

        recycler2.addItemDecoration(new SpacesItemDecoration(dips));

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCombosAutoVenta.ListenerComboRefeGlobal) {
            listener = (FragmentCombosAutoVenta.ListenerComboRefeGlobal) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void setIdReferenciaGlobal(ReferenciasCombos referenciaGlobalSele) {

       listener.onFragmentComboRefeGlobal(referenciaGlobalSele);
    }

    public interface ListenerComboRefeGlobal {
        void onFragmentComboRefeGlobal(ReferenciasCombos referenciaGlobalSele);
    }
}
