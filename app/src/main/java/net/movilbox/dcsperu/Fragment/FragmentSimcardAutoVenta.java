package net.movilbox.dcsperu.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
public class FragmentSimcardAutoVenta extends Fragment implements AdapterRecyclerSimcardAutoVenta.OnSetIdReferencia{


    private int idZonaLocal;
    private int idpos;
    private RecyclerView.Adapter adapter;
    private RecyclerView recycler;
    private GridLayoutManager gridLayoutManagerVertical;
    private DBHelper mydb;
    private ListenerSimsRefe mListener;



    public FragmentSimcardAutoVenta() {
        // Required empty public constructor
    }

    public FragmentSimcardAutoVenta(int idpos, int idZona) {
        this.idpos = idpos;
        idZonaLocal = idZona;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_simcard_ped, container, false);

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

        List<ReferenciasSims> referenciasSimsList = mydb.getSimcardLocalVenta(String.valueOf(idZonaLocal),idpos);

        //setId_posStacti(mPosition);
        adapter = new AdapterRecyclerSimcardAutoVenta(referenciasSimsList,this);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ListenerSimsRefe) {
            mListener = (ListenerSimsRefe) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuItem item2 = menu.add("Carrito");
        item2.setIcon(R.drawable.ic_shopping_cart_white_24dp); // sets icon
        item2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Bundle bundle = new Bundle();
                Intent intent = new Intent(getActivity(), ActCarritoPedido.class);
                bundle.putInt("id_punto", mPosition);
                bundle.putInt("id_usuario", mydb.getUserLogin().getId());
                intent.putExtras(bundle);
                startActivity(intent);

                return true;
            }

        });*/

        /*// Implementing ActionBar Search inside a fragment
        MenuItem item = menu.add("Search");
        item.setIcon(R.drawable.ic_search_white_24dp); // sets icon
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        SearchView sv = new SearchView(((ActTomarPedido) getActivity()).getSupportActionBar().getThemedContext());
        int id = sv.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) sv.findViewById(id);

        // implementing the listener
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //doSearch(s);
                return s.length() < 4;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase();
                filterList = getNewListFromFilter(newText);
                adapter = new AdapterRecyclerSimcard(getActivity(), filterList, mPosition, mydb.getUserLogin().getId());
                recycler.setAdapter(adapter);

                return true;
            }
        });

        item.setActionView(sv);

    }*/

    @Override
    public void setIdReferencia(ReferenciasSims objRefsims) {
        //Manadamos el idrefencia a la interfaz que va hacer implementada por la actividad ActTomarAutoVenta
        mListener.onFragmentSimsRefe(objRefsims);
    }



    public interface ListenerSimsRefe {
        void onFragmentSimsRefe(ReferenciasSims objRefsims);
    }

}
