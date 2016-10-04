package net.movilbox.dcsperu.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.EntIndicadores;
import net.movilbox.dcsperu.Entry.EntRuteroList;
import net.movilbox.dcsperu.R;
import net.movilbox.dcsperu.Services.ConnectionDetector;

import java.util.List;

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
    private int mProgressStatus = 0;
    private int visitasTotal = 0;
    private int totalConPedido = 0;
    private int vendedor;
    private EntIndicadores entIndicadoresLocal;
    private ConnectionDetector connectionDetector;
    private DBHelper mydb;
    private Handler mHandler = new Handler();
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
            entIndicadoresLocal = mydb.getIndicadores(mydb.getUserLogin().getId(), Integer.parseInt(mydb.getUserLogin().getId_distri()));
            entRuteroLists = mydb.getRuteroDia(mydb.getUserLogin().getId());
        } else {
            entIndicadoresLocal = mydb.getIndicadores(vendedor, Integer.parseInt(mydb.getUserLogin().getId_distri()));
            entRuteroLists = mydb.getRuteroDia(vendedor);
        }

        txtFinal.setText(String.format("%1$s", entRuteroLists.size()));

        txtFinalCumpli.setText(String.format("%1$s", entIndicadoresLocal.getCant_cumplimiento_sim()));
        txtFinalPedidoCumpli.setText(String.format("%1$s", entIndicadoresLocal.getCant_cumplimiento_combo()));

        txtCantidadCumpli.setText(String.format("%1$s",entIndicadoresLocal.getCant_ventas_sim()));
        txtPromediopedidoCumpli.setText(String.format("%1$s",entIndicadoresLocal.getCant_ventas_combo()));

        new Thread(new Runnable() {
            public void run() {

                while (mProgressStatus < entRuteroLists.size()) {

                    if (entRuteroLists.get(mProgressStatus).getTipo_visita() == 1 || entRuteroLists.get(mProgressStatus).getTipo_visita() == 2)
                        visitasTotal++;

                    if (entRuteroLists.get(mProgressStatus).getTipo_visita() == 1)
                        totalConPedido++;

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

                            //Cumplimiento
                            //Progress 1
                            float progress3 = ((float) entIndicadoresLocal.getCant_ventas_sim() / (float) entIndicadoresLocal.getCant_cumplimiento_sim()) * 100;

                            progressBarCumpli.setProgress(entIndicadoresLocal.getCant_ventas_sim());
                            progressBarCumpli.setProgress((int) progress3);
                            txtPorCumCumpli.setText((int) progress3 +"%");

                            //Progress 2
                            float progress4 = ((float) entIndicadoresLocal.getCant_ventas_combo() / (float) entIndicadoresLocal.getCant_cumplimiento_combo()) * 100;

                            progressBar2Cumpli.setMax(entIndicadoresLocal.getCant_cumplimiento_sim());
                            progressBar2Cumpli.setProgress((int) progress4);
                            txtPorCombos.setText((int) progress4 +"%");

                        }
                    });

                    mProgressStatus++;

                }
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