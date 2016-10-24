package net.movilbox.dcsperu.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import net.movilbox.dcsperu.Adapter.AdapterRecyclerSimcardAutoVenta;
import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.EntLisSincronizar;
import net.movilbox.dcsperu.Entry.ReferenciasSims;
import net.movilbox.dcsperu.R;

import java.util.List;

public class ActSimcardPaqueteVenta extends AppCompatActivity {
    private RecyclerView rvPaquete;
    private DBHelper mydb;
    private RecyclerView.Adapter adapter;
    private GridLayoutManager gridLayoutManagerVertical;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_simcard_auto_venta);
        rvPaquete = (RecyclerView) findViewById(R.id.recycler_view);
        mydb = new DBHelper(this);
        rvPaquete.setHasFixedSize(true);

        RecyclerView.LayoutManager lManager = new LinearLayoutManager(this);
        rvPaquete.setLayoutManager(lManager);
        gridLayoutManagerVertical = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    protected void onStart() {
        super.onStart();

        List<ReferenciasSims> listaPaquetes = mydb.getPaqueteSims(getIntent().getStringExtra("id_referencia"));

        adapter = new AdapterRecyclerSimcardAutoVenta(this, listaPaquetes, 1, mydb.getUserLogin().getId());
        rvPaquete.setAdapter(adapter);
        rvPaquete.setLayoutManager(gridLayoutManagerVertical);
        int dips = 0;
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

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

        rvPaquete.addItemDecoration(new SpacesItemDecoration(dips));


    }
}
