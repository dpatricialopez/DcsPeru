package net.movilbox.dcsperu.Adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.movilbox.dcsperu.Activity.ActMapsPunto;
import net.movilbox.dcsperu.Entry.EntNoticia;
import net.movilbox.dcsperu.Entry.EntRuteroList;
import net.movilbox.dcsperu.Entry.ListaNoticias;
import net.movilbox.dcsperu.Entry.ResponseHome;
import net.movilbox.dcsperu.R;

import java.util.List;

public class AdaptadorNoticia extends BaseAdapter {

    private Activity actx;
    private List<EntNoticia> data;

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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(actx, R.layout.noticia, null);
            new ViewHolder(convertView);
        }

        final ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.name.setText(data.get(position).getTitle());
        holder.timestamp.setText(data.get(position).getDate());
        Log.e("fecha","fecha:"+data.get(position).getDate());
        holder.txtStatusMsg.setText(data.get(position).getContain());
        holder.txtUrl.setText(data.get(position).getUrl());
        holder.Image.setImageResource(R.drawable.ic_play_light);
        holder.read.setImageResource(R.drawable.ic_play_light);
        return convertView;
    }

    class ViewHolder {

        TextView name;
        TextView timestamp;
        TextView txtStatusMsg,txtUrl;
        ImageView Image,read;

        public ViewHolder(View view) {
            txtUrl = (TextView) view.findViewById(R.id.txtUrl);
            name = (TextView) view.findViewById(R.id.name);
            timestamp = (TextView) view.findViewById(R.id.timestamp);
            txtStatusMsg = (TextView) view.findViewById(R.id.txtStatusMsg);
            Image = (ImageView) view.findViewById(R.id.Image);
            read = (ImageView) view.findViewById(R.id.read);
            view.setTag(this);

        }
    }
}
