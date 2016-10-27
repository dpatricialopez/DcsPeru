package net.movilbox.dcsperu.Adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import net.movilbox.dcsperu.Activity.ActMapsPunto;
import net.movilbox.dcsperu.Activity.ActNoticiaDetalle;
import net.movilbox.dcsperu.Activity.ActResponAvanBusqueda;
import net.movilbox.dcsperu.Entry.EntNoticia;
import net.movilbox.dcsperu.Entry.EntRuteroList;
import net.movilbox.dcsperu.Entry.ListaNoticias;
import net.movilbox.dcsperu.Entry.ResponseHome;
import net.movilbox.dcsperu.Fragment.FeedImageView;
import net.movilbox.dcsperu.R;
import net.movilbox.dcsperu.Services.ConnectionDetector;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class AdaptadorNoticia extends BaseAdapter {

    private Activity actx;
    private List<EntNoticia> data;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader1;
    private DisplayImageOptions options1;
    private DecimalFormat format;
    private ConnectionDetector connectionDetector;

    public AdaptadorNoticia(Activity actx, List<EntNoticia> data) {
        this.actx = actx;
        this.data = data;
        connectionDetector = new ConnectionDetector(actx);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(actx).build();
        imageLoader1 = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader1.init(config);
        format = new DecimalFormat("#.00");

        //Setup options for ImageLoader so it will handle caching for us.
        options1 = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .cacheOnDisc()
                .build();
    }

    @Override
    public int getCount() {
        if (data == null) {
            return 0;
        } else {
            return data.size();
        }
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
    @Override
    public EntNoticia getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(actx, R.layout.noticia, null);
            new ViewHolder(convertView);
        }

        final ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.name.setText(data.get(position).getTitle());
        holder.type.setText(data.get(position).getTipo());
        holder.timestamp.setText(data.get(position).getDate());

        String contenido;
        if (data.get(position).getContain()!=null){
            if (data.get(position).getContain().length()>70){
                contenido= data.get(position).getContain().substring(0,100)+"...";
            }
            else
            {
                contenido=data.get(position).getContain();
            }
            holder.txtStatusMsg.setText(contenido);
            holder.txtStatusMsg.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.txtStatusMsg.setVisibility(View.GONE);
        }

        if (data.get(position).getUrl() != null) {
            holder.txtUrl.setText(Html.fromHtml("<a href=\"" + data.get(position).getUrl() + "\">"
                    + data.get(position).getUrl() + "</a> "));

            // Making url clickable
            holder.txtUrl.setMovementMethod(LinkMovementMethod.getInstance());
            holder.txtUrl.setVisibility(View.VISIBLE);
        } else {
            // url is null, remove from the view
            holder.txtUrl.setVisibility(View.GONE);
        }

        if (data.get(position).getImage()!=null && connectionDetector.isConnected()){
            loadeImagenView(holder.Image, data.get(position).getImage());
            holder.Image.setVisibility(View.VISIBLE);
        }
        else{
            holder.Image.setVisibility(View.GONE);
        }

        if (data.get(position).getStatus()==1){

            holder.read.setColorFilter(Color.rgb(28, 144, 192));

        }
        else{
            holder.read.setColorFilter(Color.rgb(204, 204, 204));
        }


        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(actx, ActNoticiaDetalle.class);
                    bundle.putString("idNew", String.valueOf(data.get(position).getId()));
                    bundle.putString("indexTipo", String.valueOf(data.get(position).getId()));
                    intent.putExtras(bundle);
                    actx.startActivity(intent);
            }
        });

           return convertView;
    }

    class ViewHolder {

        TextView name;
        TextView type;
        TextView timestamp;
        TextView txtStatusMsg,txtUrl;
        ImageView read;
        ImageView Image;
        Button button;

        public ViewHolder(View view) {
            txtUrl = (TextView) view.findViewById(R.id.txtUrl);
            name = (TextView) view.findViewById(R.id.name);
            timestamp = (TextView) view.findViewById(R.id.timestamp);
            txtStatusMsg = (TextView) view.findViewById(R.id.txtStatusMsg);
            read = (ImageView) view.findViewById(R.id.read);
            Image = (ImageView) view.findViewById(R.id.ImageUrl);
            type=(TextView) view.findViewById(R.id.type);
            button=(Button)view.findViewById(R.id.button);

            view.setTag(this);

        }
    }


}
