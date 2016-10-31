package net.movilbox.dcsperu.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import net.movilbox.dcsperu.Adapter.AdapterSerie;
import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.ListaPaquete;
import net.movilbox.dcsperu.R;

public class ActSeleccionarSerieSims extends AppCompatActivity {
    private RecyclerView rvSerie;
    private int idReferencia;
    private ListaPaquete listaPaquete;
    private AdapterSerie adapter;
    private DBHelper mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_seleccionar_serie_sims);
        rvSerie = (RecyclerView)findViewById(R.id.rvSerie);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //obtenemos los parametros mandados desde la actividad ActSeleccionarSimsPaquete
        idReferencia = getIntent().getIntExtra("ID_REFERENCIA",0);
        listaPaquete = (ListaPaquete) getIntent().getSerializableExtra("ID_PAQUETE");
        //instancia de la db
        mydb = new DBHelper(this);
        //obtenemos la lsita de seriales
        listaPaquete = mydb.getSerieSims(listaPaquete,idReferencia);
        //setiamos la lista
        adapter = new AdapterSerie(listaPaquete.getListaSerie());
        rvSerie.setAdapter(adapter);
        rvSerie.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvSerie.setItemAnimator(new DefaultItemAnimator());
    }

    public void btn_guardar_sele(View v){
        boolean flat = false;
        for (int i = 0;i < listaPaquete.getListaSerie().size();i++){
            if (listaPaquete.getListaSerie().get(i).getCheck() == 1){
                flat = true;
            }
        }
        Intent intentResult = new Intent();

        if(flat){
            intentResult.putExtra("PAQUETE",listaPaquete);
            setResult(RESULT_OK,intentResult);
            finish();
        }else{
            setResult(RESULT_CANCELED,intentResult);
            finish();
        }
    }
}
