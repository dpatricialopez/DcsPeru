package net.movilbox.dcsperu.Activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import net.movilbox.dcsperu.Adapter.TabsAdapter;
import net.movilbox.dcsperu.Entry.ResponseMarcarPedido;
import net.movilbox.dcsperu.Fragment.FragmentCombosAutoVenta;
import net.movilbox.dcsperu.Fragment.FragmentSimcardAutoVenta;
import net.movilbox.dcsperu.R;

public class ActTomarAutoVenta extends AppCompatActivity {
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


        viewPager.setAdapter(tabsAdapter);
        tabLayout.setupWithViewPager(viewPager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }
}
