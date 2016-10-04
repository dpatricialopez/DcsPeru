package net.movilbox.dcsperu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.baoyz.swipemenulistview.SwipeMenuListView;

import net.movilbox.dcsperu.Adapter.AdapterEntregaPedidoPedido;
import net.movilbox.dcsperu.Entry.ResponseEntregarPedido;
import net.movilbox.dcsperu.R;

public class ActEntregarPorPedido extends AppCompatActivity {

    private Bundle bundle;
    private ResponseEntregarPedido mDescribable;
    private SwipeMenuListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_entregar_por_o);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = this.getIntent();
        bundle = intent.getExtras();
        if (bundle != null) {
            mDescribable = (ResponseEntregarPedido) bundle.getSerializable("value");
        }

        mListView = (SwipeMenuListView) findViewById(R.id.listView);

        AdapterEntregaPedidoPedido adapterEntregaPedido = new AdapterEntregaPedidoPedido(this, mDescribable.getPedidosEntregaList());
        mListView.setAdapter(adapterEntregaPedido);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}
