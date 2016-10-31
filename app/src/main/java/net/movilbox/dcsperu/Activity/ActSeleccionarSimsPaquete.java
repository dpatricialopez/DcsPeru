package net.movilbox.dcsperu.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.movilbox.dcsperu.Adapter.AdapterRecyclerSimcardAutoVentaPaquete;
import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.EntReferenciaSol;
import net.movilbox.dcsperu.Entry.ListaPaquete;
import net.movilbox.dcsperu.Entry.ReferenciasSims;
import net.movilbox.dcsperu.Entry.ResponseMarcarPedido;
import net.movilbox.dcsperu.Entry.Serie;
import net.movilbox.dcsperu.R;
import net.movilbox.dcsperu.Services.ConnectionDetector;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

/**
 * Created by jhonjimenez on 25/10/16.
 */

public class ActSeleccionarSimsPaquete extends AppCompatActivity implements AdapterRecyclerSimcardAutoVentaPaquete.OnSetIdPaquete{

    private ReferenciasSims idReferencia;
    private ResponseMarcarPedido mDescribable;
    private RecyclerView rvSimsPaquete;




    private GridLayoutManager gridLayoutManagerVertical;
    private AdapterRecyclerSimcardAutoVentaPaquete adapter;
    private DBHelper mydb;
    private int posicion;
    private ConnectionDetector connectionDetector;
    private ReferenciasSims simsPaquete;
    private static final int ID_SELECCIONAR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_sims_paquete);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        connectionDetector = new ConnectionDetector(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        if (connectionDetector.isConnected()) {
            toolbar.setTitle("Paquete");
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else {
            toolbar.setBackgroundColor(Color.RED);
            toolbar.setTitle("Paquete Offline");
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = this.getIntent();
        idReferencia = (ReferenciasSims) intent.getSerializableExtra("ID_REFERENCIA");
        mDescribable = (ResponseMarcarPedido) intent.getSerializableExtra("value");

        rvSimsPaquete = (RecyclerView) findViewById(R.id.recycler_view);
        rvSimsPaquete.setHasFixedSize(true);

        RecyclerView.LayoutManager lManager = new LinearLayoutManager(this);
        rvSimsPaquete.setLayoutManager(lManager);
        gridLayoutManagerVertical = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mydb = new DBHelper(this);
        getSimcardLocalPaquete();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuItem item2 = menu.add("Guardar");
        item2.setIcon(R.drawable.ic_save_white_24dp); // sets icon
        item2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Guardar Aceptacion de pedidos validaciones
                return true;
            }
        });
    }

    private void getSimcardLocalPaquete() {

        simsPaquete = mydb.getPaqueteSims(idReferencia);

        //setId_posStacti(mPosition);
        adapter = new AdapterRecyclerSimcardAutoVentaPaquete(this,simsPaquete.getListaPaquete(),this);
        rvSimsPaquete.setAdapter(adapter);
        rvSimsPaquete.setLayoutManager(gridLayoutManagerVertical);
        int dips = 0;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

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

        rvSimsPaquete.addItemDecoration(new SpacesItemDecoration(dips));
    }

    @Override
    public void setIdPaquete(int position) {
        //position es la posicion del elemento seleccionado
        int resultado = mydb.insertCarritoPaquete(simsPaquete,position,1);//1 venta por paquete

        switch (resultado){
            case 0:
                //ya esta en el carrito
                Toast.makeText(this,"El producto ya esta en el carrito",Toast.LENGTH_LONG).show();
                break;
            case 1:
                //No se inserto en la db
                Toast.makeText(this,"El producto NO se agrego al carrito",Toast.LENGTH_LONG).show();
                break;
            case 2:
                //Inserto
                Bundle bundle = new Bundle();
                Intent intent = new Intent(this, ActTomarAutoVenta.class);

                bundle.putSerializable("value",mDescribable);
                intent.putExtras(bundle);
                startActivity(intent);
                Toast.makeText(this,"El producto se agrego al carrito",Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void setSerie(int position) {
        posicion = position;
        Intent intent = new Intent(this,ActSeleccionarSerieSims.class);
        intent.putExtra("ID_REFERENCIA",idReferencia.getId());
        intent.putExtra("ID_PAQUETE",simsPaquete.getListaPaquete().get(position));//lista de seriales
        startActivityForResult(intent,ID_SELECCIONAR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ID_SELECCIONAR){
            if (resultCode == RESULT_OK){
                //obtenemos a lista de seriales seleccionados
                simsPaquete.getListaPaquete().set(posicion, (ListaPaquete) data.getSerializableExtra("PAQUETE"));
                int resultado = mydb.insertCarritoUnidad(simsPaquete,posicion,2);//2 venta por unidad

                switch (resultado){
                    case 0:
                        //ya esta en el carrito
                        Toast.makeText(this,"El producto ya esta en el carrito",Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        //No se inserto en la db
                        Toast.makeText(this,"El producto NO se agrego al carrito",Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        //Inserto
                        Bundle bundle = new Bundle();
                        Intent intent = new Intent(this, ActTomarAutoVenta.class);

                        bundle.putSerializable("value",mDescribable);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        Toast.makeText(this,"Los productos se agregaron al carrito",Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        //No se inserto en la db
                        Toast.makeText(this,"No selecciono ningun producto",Toast.LENGTH_LONG).show();
                        break;
                }

            }else{
                Toast.makeText(this,"No se agrego ningun producto",Toast.LENGTH_LONG).show();
            }
        }
    }


}

