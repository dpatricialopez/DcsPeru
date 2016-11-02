package net.movilbox.dcsperu.Activity;


import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import net.movilbox.dcsperu.Adapter.AdapterRecyclerSimcard;
import net.movilbox.dcsperu.Adapter.TabsAdapter;
import net.movilbox.dcsperu.Entry.ReferenciasCombos;
import net.movilbox.dcsperu.Entry.ReferenciasSims;
import net.movilbox.dcsperu.Entry.ResponseMarcarPedido;
import net.movilbox.dcsperu.Fragment.FragmentCombosAutoVenta;
import net.movilbox.dcsperu.Fragment.FragmentSimcardAutoVenta;
import net.movilbox.dcsperu.R;

public class ActTomarAutoVenta extends AppCompatActivity implements FragmentSimcardAutoVenta.ListenerSimsRefe,FragmentCombosAutoVenta.ListenerComboRefeGlobal{

    private Bundle bundle;
    private ResponseMarcarPedido thumbs = new ResponseMarcarPedido();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_tomar_auto_venta);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Autoventa");
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);

        Intent intent = this.getIntent();
        bundle = intent.getExtras();
        if (bundle != null) {
            thumbs = (ResponseMarcarPedido) bundle.getSerializable("value");
            //indicadorPage = bundle.getString("page");
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);

        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager());

        tabsAdapter.addFragment(new FragmentSimcardAutoVenta(thumbs.getId_pos(), thumbs.getIdZona()), "SIMCARD");

        tabsAdapter.addFragment(new FragmentCombosAutoVenta(thumbs.getId_pos(), thumbs.getIdZona()), "COMBOS");

        tabsAdapter.addFragment(new FragmentCombosAutoVenta(thumbs.getId_pos(), thumbs.getIdZona()), "EQUIPOS");


        viewPager.setAdapter(tabsAdapter);
        tabLayout.setupWithViewPager(viewPager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override//metodo de la interfaz ListenerSimsRefe del fragmento FragmentSimcardAutoVenta
    public void onFragmentSimsRefe(ReferenciasSims objRefSims) {
        //a la mierda con esto creemos una actividad
        Intent intent = new Intent(this,ActSeleccionarSimsPaquete.class);
        intent.putExtra("ID_REFERENCIA",objRefSims);
        intent.putExtra("value",thumbs);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem item2 = menu.add("Carrito");
        item2.setIcon(R.drawable.ic_shopping_cart_white_24dp); // sets icon
        item2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Bundle bundle = new Bundle();
                Intent intent = new Intent(ActTomarAutoVenta.this, ActCarritoAutoVenta.class);
                bundle.putInt("id_punto", thumbs.getId_pos());
                intent.putExtra("value",thumbs);
                intent.putExtras(bundle);
                startActivity(intent);

                return true;
            }

        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onFragmentComboRefeGlobal(ReferenciasCombos referenciaGlobalSele) {

        if (referenciaGlobalSele != null) {

            Bundle bundle = new Bundle();
            Intent intent = new Intent(ActTomarAutoVenta.this, ActDetalleProducto.class);
            bundle.putSerializable("value", referenciaGlobalSele);
            intent.putExtras(bundle);
            startActivity(intent);

        } else {
            Toast.makeText(ActTomarAutoVenta.this, "Esta opción solo es permitida si tiene internet", Toast.LENGTH_LONG).show();
        }
    }
}

