package net.movilbox.dcsperu.Fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.movilbox.dcsperu.Activity.SpacesItemDecoration;
import net.movilbox.dcsperu.Adapter.AdapterRecyclerSimcardAutoVenta;
import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.ReferenciasSims;
import net.movilbox.dcsperu.R;

import java.util.List;

import dmax.dialog.SpotsDialog;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class FragmentSimcardAutoVenta extends Fragment {

    private int mPosition;
    private int idZonaLocal;
    private SpotsDialog alertDialog;
    private RecyclerView.Adapter adapter;
    private RecyclerView recycler;
    //private List<ReferenciasSims> filterList;
    //private ResponseVenta responseVenta;
    private GridLayoutManager gridLayoutManagerVertical;
    private DBHelper mydb;
    public FragmentSimcardAutoVenta() {
        // Required empty public constructor
    }

    public FragmentSimcardAutoVenta(int position, int idZona) {
        mPosition = position;
        idZonaLocal = idZona;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_simcard_ped, container, false);

        alertDialog = new SpotsDialog(getActivity(), R.style.Custom);

        recycler = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recycler.setHasFixedSize(true);

        RecyclerView.LayoutManager lManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(lManager);
        gridLayoutManagerVertical = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mydb = new DBHelper(getActivity());

        setHasOptionsMenu(true);

        getSimcardLocal();
    }

    private void getSimcardLocal() {

        List<ReferenciasSims> referenciasSimsList = mydb.getSimcardLocalVenta(String.valueOf(idZonaLocal));

        //setId_posStacti(mPosition);
        adapter = new AdapterRecyclerSimcardAutoVenta(getActivity(), referenciasSimsList, mPosition, mydb.getUserLogin().getId());
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(gridLayoutManagerVertical);
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

        recycler.addItemDecoration(new SpacesItemDecoration(dips));
    }

}
