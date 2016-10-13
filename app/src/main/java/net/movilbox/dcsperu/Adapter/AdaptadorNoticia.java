package net.movilbox.dcsperu.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import net.movilbox.dcsperu.Entry.EntNoticia;
import net.movilbox.dcsperu.Entry.EntRuteroList;
import net.movilbox.dcsperu.Entry.ReferenciasSims;
import net.movilbox.dcsperu.R;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import static net.movilbox.dcsperu.R.id.android_pay_dark;

/**
 * Created by dianalopez on 10/10/16.
 */

public class AdaptadorNoticia extends BaseAdapter {

    private Activity actx;
    List<EntNoticia> data;


    public AdaptadorNoticia(Activity actx, List<EntNoticia> data) {
        this.actx = actx;
        this.data = data;
    }

    @Override
    public int getCount() {
        if (data == null) {
            return 0;
        } else {
            return data.size();
        }
    }

    @Override
    public EntNoticia getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(actx, R.layout.noticia, null);
            new ViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.name.setText(data.get(position).getTitle());
        holder.timestamp.setText(data.get(position).getDate());
        holder.txtStatusMsg.setText(data.get(position).getStatus());
        holder.txtUrl.setText(data.get(position).getUrl());
        loadeImagenView(holder.Image,data.get(position).getImge());
        if (data.get(position).getStatus()==0) {
            holder.read.setImageResource(R.drawable.ic_play_dark);

        } else {
            holder.read.setImageResource(R.drawable.ic_play_light);
        }


        return convertView;
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


    }

    class ViewHolder {

        TextView name;
        TextView timestamp;
        TextView txtStatusMsg;
        TextView txtUrl;
        ImageView Image;
        ImageView read;

        public ViewHolder(View view) {

            name = (TextView) view.findViewById(R.id.name);
            timestamp = (TextView) view.findViewById(R.id.timestamp);
            txtStatusMsg = (TextView) view.findViewById(R.id.txtStatusMsg);
            txtUrl = (TextView) view.findViewById(R.id.txtUrl);
            Image = (ImageView) view.findViewById(R.id.Image);
            read = (ImageView) view.findViewById(R.id.read);

            view.setTag(this);
        }
    }

}
