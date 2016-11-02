package net.movilbox.dcsperu.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import net.movilbox.dcsperu.Entry.ReferenciasCombos;
import net.movilbox.dcsperu.R;
import net.movilbox.dcsperu.Services.ConnectionDetector;

import java.util.List;

/**
 * Created by jhonjimenez on 2/11/16.
 */

public class AdapterRecyclerCombosAutoVenta extends RecyclerView.Adapter<AdapterRecyclerCombosAutoVenta.ViewHolder> {

    private List<ReferenciasCombos> listaCombos;
    private AdapterRecyclerCombosAutoVenta.ViewHolder viewHolder;
    private AdapterRecyclerCombosAutoVenta.OnSetIdReferenciaGlobal listener;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader1;
    private DisplayImageOptions options1;
    private Context ctx;
    private ConnectionDetector connectionDetector;


    //creamos interfaz que va  hacer implementada en el fragment FragmentCombosAutoVenta
    public interface OnSetIdReferenciaGlobal{
        void setIdReferenciaGlobal(ReferenciasCombos idReferencia);
    }

    public AdapterRecyclerCombosAutoVenta(Context ctx,List<ReferenciasCombos> listaCombos, OnSetIdReferenciaGlobal listener) {
        this.listaCombos = listaCombos;
        this.listener = listener;
        this.ctx = ctx;

        connectionDetector = new ConnectionDetector(ctx);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(ctx).build();
        imageLoader1 = ImageLoader.getInstance();
        imageLoader1.init(config);

        //Setup options for ImageLoader so it will handle caching for us.
        options1 = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .cacheOnDisc()
                .build();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_combos, parent, false);
        viewHolder = new ViewHolder(v);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReferenciasCombos item = listaCombos.get(position);
        viewHolder.bindCombo(item);
    }

    @Override
    public int getItemCount() {
        return listaCombos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtReferencia;
        public TextView txtValorR;
        public TextView txtValorR2;
        public TextView txtcantidadGlo;
        public ImageView img_producto;
        public Button btnpedido;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txtReferencia = (TextView) itemView.findViewById(R.id.txtReferencia);
            this.txtValorR = (TextView) itemView.findViewById(R.id.txtValorR);
            this.txtValorR2 = (TextView) itemView.findViewById(R.id.txtValorR2);
            this.txtcantidadGlo = (TextView) itemView.findViewById(R.id.txtcantidadGlo);
            this.btnpedido = (Button) itemView.findViewById(R.id.btnpedido);
            this.img_producto = (ImageView) itemView.findViewById(R.id.img_producto);
        }

        public void bindCombo(final ReferenciasCombos item) {

            txtReferencia.setText(item.getDescripcion());
            txtValorR.setText(String.format("S/. %s", item.getPrecio_referencia()));
            txtValorR2.setText(String.format("PVP: S/. %s", item.getPrecio_publico()));
            //txtcantidadGlo.setText(String.format("Cantidad %s", mydb.countReferenceProduct(iduser, idpos, item.getId())));

            loadeImagenView(img_producto, item.getImg());

            img_producto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (connectionDetector.isConnected()) {
                        listener.setIdReferenciaGlobal(item);
                    } else {
                        Toast.makeText(ctx, "Esta opción solo es permitida si tiene internet", Toast.LENGTH_LONG).show();
                    }

                }
            });

            btnpedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /*Bundle bundle = new Bundle();
                    Intent intent = new Intent(context, ActDetalleComboPedir.class);
                    bundle.putSerializable("value", responseHomeList.get(position));
                    bundle.putInt("idPos", idpos);
                    bundle.putInt("idUser", iduser);
                    intent.putExtras(bundle);
                    context.startActivity(intent);*/
                }
            });
        }

        private void loadeImagenView(ImageView img_producto, String img) {

            ImageLoadingListener listener = new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String arg0, View arg1) {
                    // TODO Auto-generated method stub
                    //Inicia metodo
                    //holder.progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingCancelled(String arg0, View arg1) {
                    // TODO Auto-generated method stub
                    //Cancelar
                    //holder.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                    //Completado
                    //holder.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                    // TODO Auto-generated method stub
                    //Error al cargar la imagen.
                    //holder.progressBar.setVisibility(View.GONE);
                }
            };

            imageLoader1.displayImage(img, img_producto, options1, listener);

        }
    }
}
