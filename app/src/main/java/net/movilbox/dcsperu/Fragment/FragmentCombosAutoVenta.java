package net.movilbox.dcsperu.Fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.movilbox.dcsperu.R;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class FragmentCombosAutoVenta extends Fragment {

    private int mPosition;
    private int idZonaLocal;

    public FragmentCombosAutoVenta() {

    }

    public FragmentCombosAutoVenta(int position, int idZona) {
        mPosition = position;
        idZonaLocal = idZona;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_combos_auto_venta, container, false);
    }

}
