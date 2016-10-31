package net.movilbox.dcsperu.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.movilbox.dcsperu.Entry.ReferenciasSims;
import net.movilbox.dcsperu.Entry.Serie;
import net.movilbox.dcsperu.R;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by jhonjimenez on 27/10/16.
 */

public class AdapterSerie extends RecyclerView.Adapter<AdapterSerie.ViewHolder> {

    private List<Serie> listaSerie;
    private ViewHolder viewHolder;

    public AdapterSerie(List<Serie> listaSerie) {
        this.listaSerie = listaSerie;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_serie, parent, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Serie serie = listaSerie.get(position);
        viewHolder.bindSerie(serie);

    }

    @Override
    public int getItemCount() {
        return listaSerie.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvSerie;
        public CheckBox cbSerie;
        public ImageView ivCheck;

        public ViewHolder (View itemView) {

            super(itemView);

            this.tvSerie = (TextView) itemView.findViewById(R.id.tvSerie);
            this.cbSerie = (CheckBox) itemView.findViewById(R.id.cbSerie);
            this.ivCheck = (ImageView) itemView.findViewById(R.id.ivCheck);

        }

        public void bindSerie (Serie serie) {

            tvSerie.setText(serie.getSerie());
            if (serie.getCheck() == 1){
                cbSerie.setVisibility(View.GONE);
                ivCheck.setVisibility(View.VISIBLE);
            }

            cbSerie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (compoundButton.isChecked()){
                        compoundButton.setChecked(true);
                        listaSerie.get(getAdapterPosition()).setCheck(1);
                    }else{
                        compoundButton.setChecked(false);
                        listaSerie.get(getAdapterPosition()).setCheck(0);
                    }

                }
            });
        }

    }
}
