package net.movilbox.dcsperu.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.EntIndicadores;
import net.movilbox.dcsperu.Entry.EntRuteroList;
import net.movilbox.dcsperu.Entry.GrupoCombos;
import net.movilbox.dcsperu.Entry.GrupoSims;
import net.movilbox.dcsperu.R;
import net.movilbox.dcsperu.Services.ConnectionDetector;

import java.util.List;

import static net.movilbox.dcsperu.Entry.EntLoginR.getIndicador_refres;

@SuppressLint("ValidFragment")
public class FragmentDasboardVendedor extends BaseVolleyFragment {



    private ProgressBar mProgress;
    private ProgressBar progressBar2;
    private ProgressBar progressBarCumpli;
    private ProgressBar progressBar2Cumpli;
    private TextView txtFinal;
    private TextView txtPromedio;
    private TextView txtFinalPedido;
    private TextView txtPromediopedido;
    private TextView txtPorCum;
    private TextView txtPorEfec;
    private TextView txtFinalCumpli;
    private TextView txtFinalPedidoCumpli;
    private TextView txtCantidadCumpli;
    private TextView txtPromediopedidoCumpli;
    private TextView txtPorCumCumpli;
    private TextView txtPorCombos;
    private TextView txtcant, txtpercent, txtname, txtmeta;
    private ProgressBar pgr;
    private TabHost TbH;
    private int mProgressStatus = 0;
    private int visitasTotal = 0;
    private int totalConPedido = 0;
    private int vendedor;
    LinearLayout linearcombos, linearsims;
    private EntIndicadores entIndicadoresLocal;

    private List<GrupoSims> grupoSimsLocal;
    private List<GrupoCombos> grupoCombosLocal;

    private ConnectionDetector connectionDetector;
    private DBHelper mydb;
    private Handler mHandler = new Handler();
    View grupocombos, gruposims;
    List<EntRuteroList> entRuteroLists;

    public FragmentDasboardVendedor(int vende) {
        vendedor = vende;
    }

    public FragmentDasboardVendedor() { }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mydb = new DBHelper(getActivity());

        mProgress = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar2 = (ProgressBar) rootView.findViewById(R.id.progressBar2);
        progressBarCumpli = (ProgressBar) rootView.findViewById(R.id.progressBarCumpli);
        progressBar2Cumpli = (ProgressBar) rootView.findViewById(R.id.progressBar2Cumpli);
        txtFinal = (TextView) rootView.findViewById(R.id.txtFinal);
        txtPromedio = (TextView) rootView.findViewById(R.id.txtPromedio);
        txtFinalPedido = (TextView) rootView.findViewById(R.id.txtFinalPedido);
        txtPromediopedido = (TextView) rootView.findViewById(R.id.txtPromediopedido);
        txtPorCum = (TextView) rootView.findViewById(R.id.txtPorCum);
        txtPorEfec = (TextView) rootView.findViewById(R.id.txtPorEfec);
        txtFinalCumpli = (TextView) rootView.findViewById(R.id.txtFinalCumpli);
        txtFinalPedidoCumpli = (TextView) rootView.findViewById(R.id.txtFinalPedidoCumpli);
        txtCantidadCumpli = (TextView) rootView.findViewById(R.id.txtCantidadCumpli);
        txtPromediopedidoCumpli = (TextView) rootView.findViewById(R.id.txtPromediopedidoCumpli);
        txtPorCumCumpli = (TextView) rootView.findViewById(R.id.txtPorCumCumpli);
        txtPorCombos = (TextView) rootView.findViewById(R.id.txtPorCombos);
        linearsims=(LinearLayout) rootView.findViewById(R.id.ProgressSim);
        linearcombos=(LinearLayout) rootView.findViewById(R.id.ProgressCombo);
        TbH= (TabHost)rootView.findViewById(R.id.tabhost);
        TbH.setup();


        //Construcciòn de pestañas
        TabHost.TabSpec tab1 = TbH.newTabSpec("tab1");
        TabHost.TabSpec tab2 = TbH.newTabSpec("tab2");
        tab1.setContent(R.id.tab1);
        tab2.setIndicator("Combos", null);
        tab1.setIndicator("Simcards",null);
        tab2.setContent(R.id.tab2);
        TbH.addTab(tab1);
        TbH.addTab(tab2);
        for(int i=0;i<TbH.getTabWidget().getChildCount();i++)
        {
            TextView tv = (TextView) TbH.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(getResources().getColor(R.color.colorAccent));
        }

        connectionDetector = new ConnectionDetector(getActivity());

        //if (connectionDetector.isConnected()) {

        //}

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        indicadorVendedor();


    }

    private void indicadorVendedor() {

        if(vendedor == 0) {
            entRuteroLists = mydb.getRuteroDia(mydb.getUserLogin().getId());

        } else {
            entRuteroLists = mydb.getRuteroDia(vendedor);

        }

        grupoCombosLocal=mydb.getGrupoCombos();
        grupoSimsLocal=mydb.getGrupoSims();
        txtFinal.setText(String.format("%1$s", entRuteroLists.size()));


        new Thread(new Runnable() {
            public void run() {

                while (mProgressStatus < entRuteroLists.size()) {

                    if (entRuteroLists.get(mProgressStatus).getTipo_visita() == 1 || entRuteroLists.get(mProgressStatus).getTipo_visita() == 2)
                        visitasTotal++;

                    if (entRuteroLists.get(mProgressStatus).getTipo_visita() == 1)
                        totalConPedido++;
                    mProgressStatus++;

                }

                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        public void run() {
                            //Visitas
                            // Progress 1
                            float promedio = (float) visitasTotal / (float) entRuteroLists.size();
                            promedio = promedio * 100;
                            mProgress.setProgress((int) promedio);
                            txtPromedio.setText(String.format("%1$s", visitasTotal));

                            // Progress 2
                            txtFinalPedido.setText(String.format("%1$s", visitasTotal));
                            txtPromediopedido.setText(String.format("%1$s", totalConPedido));
                            float promedio2 = (float) totalConPedido / (float) visitasTotal;

                            promedio2 = promedio2 * 100;

                            progressBar2.setProgress((int) promedio2);

                            txtPorCum.setText((int) promedio + "%");
                            txtPorEfec.setText((int) promedio2 + "%");

                            linearsims.removeView(gruposims);
                            linearcombos.removeView(grupocombos);


                            for (int i=0; i<grupoSimsLocal.size(); i++){
                                LayoutInflater inflater =(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                gruposims = inflater.inflate(R.layout.single_progress, null);
                                txtcant = (TextView ) gruposims.findViewById(R.id.txtcant);
                                txtname = (TextView ) gruposims.findViewById(R.id.txtname);
                                txtmeta = (TextView ) gruposims.findViewById(R.id.txtmeta);
                                txtpercent = (TextView ) gruposims.findViewById(R.id.txtpercent);
                                pgr= (ProgressBar) gruposims.findViewById(R.id.progressBar);
                                linearsims.addView(gruposims);
                                txtcant.setText(String.valueOf(grupoSimsLocal.get(i).getCant_grupo_vendedor()));
                                txtname.setText(grupoSimsLocal.get(i).getNombre_grupo());
                                txtmeta.setText(String.valueOf(grupoSimsLocal.get(i).getCant_grupo_distri()));
                                float progressfloat = ((float) grupoSimsLocal.get(i).getCant_grupo_vendedor() / (float) grupoSimsLocal.get(i).getCant_grupo_distri()) * 100;
                                txtpercent.setText(progressfloat+"%");
                                pgr.setProgress((int) progressfloat);}

                            for (int i=0; i<grupoCombosLocal.size(); i++){


                                LayoutInflater inflater =(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                grupocombos = inflater.inflate(R.layout.single_progress, null);
                                txtcant = (TextView ) grupocombos.findViewById(R.id.txtcant);
                                txtname = (TextView ) grupocombos.findViewById(R.id.txtname);
                                txtmeta = (TextView ) grupocombos.findViewById(R.id.txtmeta);
                                txtpercent = (TextView ) grupocombos.findViewById(R.id.txtpercent);
                                pgr= (ProgressBar) grupocombos.findViewById(R.id.progressBar);
                                linearcombos.addView(grupocombos);
                                txtcant.setText(String.valueOf(grupoCombosLocal.get(i).getCant_grupo_vendedor()));
                                txtname.setText(grupoCombosLocal.get(i).getNombre_grupo());
                                txtmeta.setText(String.valueOf(grupoCombosLocal.get(i).getCant_grupo_distri()));
                                float progressfloat2 = ((float) grupoCombosLocal.get(i).getCant_grupo_vendedor() / (float) grupoCombosLocal.get(i).getCant_grupo_distri()) * 100;
                                txtpercent.setText(progressfloat2+"%");
                                pgr.setProgress((int) progressfloat2);}
                            }


                    });



            }
        }).start();

    }



    /*private void parseJSONVendedor(String response) {

        Gson gson = new Gson();

        final EntIndicadores entIndicadores = gson.fromJson(response, EntIndicadores.class);

        mydb.deleteObject("indicadoresdas");
        mydb.deleteObject("indicadoresdas_detalle");

        mydb.insertIndicadores(entIndicadores);
        mydb.insertDetalleIndicadores(entIndicadores);

        txtFinal.setText(String.format("%1$s", entIndicadores.getEntPuntoIndicadoList().size()));

        txtFinalCumpli.setText(String.format("%1$s",entIndicadores.getCant_cumplimiento_sim()));
        txtFinalPedidoCumpli.setText(String.format("%1$s",entIndicadores.getCant_cumplimiento_combo()));

        txtCantidadCumpli.setText(String.format("%1$s",entIndicadores.getCant_ventas_sim()));
        txtPromediopedidoCumpli.setText(String.format("%1$s",entIndicadores.getCant_ventas_combo()));

        new Thread(new Runnable() {
            public void run() {

                while (mProgressStatus < entIndicadores.getEntPuntoIndicadoList().size()) {

                    if (entIndicadores.getEntPuntoIndicadoList().get(mProgressStatus).getTipo_visita() == 1 || entIndicadores.getEntPuntoIndicadoList().get(mProgressStatus).getTipo_visita() == 2)
                        visitasTotal++;

                    if (entIndicadores.getEntPuntoIndicadoList().get(mProgressStatus).getTipo_visita() == 1)
                        totalConPedido++;

                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        public void run() {
                            //Visitas
                            // Progress 1
                            float promedio = (float) visitasTotal / (float) entIndicadores.getEntPuntoIndicadoList().size();
                            promedio = promedio * 100;
                            mProgress.setProgress((int) promedio);
                            txtPromedio.setText(String.format("%1$s", visitasTotal));

                            // Progress 2
                            txtFinalPedido.setText(String.format("%1$s", visitasTotal));
                            txtPromediopedido.setText(String.format("%1$s", totalConPedido));
                            float promedio2 = (float) totalConPedido / (float) visitasTotal;

                            promedio2 = promedio2 * 100;

                            progressBar2.setProgress((int) promedio2);

                            txtPorCum.setText((int) promedio + "%");
                            txtPorEfec.setText((int) promedio2 + "%");

                            //Cumplimiento
                            //Progress 1
                            float progress3 = ((float) entIndicadores.getCant_ventas_sim() / (float) entIndicadores.getCant_cumplimiento_sim()) * 100;

                            progressBarCumpli.setProgress(entIndicadores.getCant_ventas_sim());
                            progressBarCumpli.setProgress((int) progress3);
                            txtPorCumCumpli.setText((int) progress3 +"%");

                            //Progress 2
                            float progress4 = ((float) entIndicadores.getCant_ventas_combo() / (float) entIndicadores.getCant_cumplimiento_combo()) * 100;

                            progressBar2Cumpli.setMax(entIndicadores.getCant_cumplimiento_sim());
                            progressBar2Cumpli.setProgress((int) progress4);
                            txtPorCombos.setText((int) progress4 +"%");

                        }
                    });

                    mProgressStatus++;
                }
            }
        }).start();

    }*/

}