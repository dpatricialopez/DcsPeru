package net.movilbox.dcsperu.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.CategoriasEstandar;
import net.movilbox.dcsperu.Entry.RequestGuardarEditarPunto;
import net.movilbox.dcsperu.R;
import net.movilbox.dcsperu.Services.ConnectionDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static net.movilbox.dcsperu.Entry.EntLoginR.setIndicador_refres;

public class ActCrearPdvuno extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_nombres;
    private EditText edit_cedula;
    private EditText edit_nom_cli;
    private EditText edit_correo_edit;
    private EditText edit_tel_edit;
    private EditText edit_cel_edit;
    private EditText edit_codigo_cum;
    private Spinner spinnerTipoDocumento;
    private Switch switch1;
    private int venta_recarga = 2;
    private int editaPunto = 0;
    private int tipoDocumento = 0;
    private String accion;
    private SpotsDialog alertDialog;
    private RequestQueue rq;
    public static final String TAG = "MyTag";
    private RequestGuardarEditarPunto datos;
    private List<CategoriasEstandar> ListaTipoDoc = new ArrayList<>();
    private ConnectionDetector connectionDetector;
    private DBHelper mydb;
    private TextInputLayout textInputMovil;
    private EditText editMovil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_pdvuno);
        connectionDetector = new ConnectionDetector(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        mydb = new DBHelper(this);

        if (connectionDetector.isConnected()) {
            toolbar.setTitle("Crear Punto");
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else {
            toolbar.setBackgroundColor(Color.RED);
            toolbar.setTitle("Crear Punto Offline");
        }

        setSupportActionBar(toolbar);

        edit_nombres = (EditText) findViewById(R.id.edit_nombres);
        edit_cedula = (EditText) findViewById(R.id.edit_cedula);
        edit_nom_cli = (EditText) findViewById(R.id.edit_nom_cli);
        edit_correo_edit = (EditText) findViewById(R.id.edit_correo_edit);
        edit_tel_edit = (EditText) findViewById(R.id.edit_tel_edit);
        edit_cel_edit = (EditText) findViewById(R.id.edit_cel_edit);
        edit_codigo_cum = (EditText) findViewById(R.id.edit_codigo_cum);
        switch1 = (Switch) findViewById(R.id.switch1);

        textInputMovil = (TextInputLayout) findViewById(R.id.textInputMovil);
        editMovil = (EditText) findViewById(R.id.editMovil);

        alertDialog = new SpotsDialog(this, R.style.Custom);

        Button btn_siguiente_per = (Button) findViewById(R.id.btn_siguiente_per);
        Button btn_cancelar_per = (Button) findViewById(R.id.btn_cancelar_per);

        spinnerTipoDocumento = (Spinner) findViewById(R.id.spinner_tipo_documento);

        btn_siguiente_per.setOnClickListener(this);
        btn_cancelar_per.setOnClickListener(this);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            accion = bundle.getString("accion");
            editaPunto = bundle.getInt("idpos");
        }

        if (accion.equals("Editar")) {
            CargarDatosPunto();
        } else {
            loadSpinnerTipo();
        }

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    venta_recarga = 1;
                    textInputMovil.setVisibility(View.VISIBLE);
                } else {
                    venta_recarga = 2;
                    textInputMovil.setVisibility(View.GONE);
                    editMovil.setText("");
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIndicador_refres(1);
                Intent intent = new Intent(ActCrearPdvuno.this, ActMainPeru.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.act_main_buscar_punto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            if (connectionDetector.isConnected()) {
                buscarPunto();
            } else {
                Toast.makeText(this, "Esta opción solo es permitida si tiene internet", Toast.LENGTH_LONG).show();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void buscarPunto() {
        startActivity(new Intent(this, ActBuscarPunto.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_siguiente_per:

                if (!validarCampos()) {
                    // Siguientes

                    RequestGuardarEditarPunto requestGuardarEditarPunto = new RequestGuardarEditarPunto();

                    if (accion.equals("Editar")) {
                        requestGuardarEditarPunto = datos;
                    }

                    requestGuardarEditarPunto.setNombre_punto(edit_nombres.getText().toString());
                    requestGuardarEditarPunto.setCedula(edit_cedula.getText().toString());
                    requestGuardarEditarPunto.setNombre_cliente(edit_nom_cli.getText().toString());
                    requestGuardarEditarPunto.setEmail(edit_correo_edit.getText().toString().trim());
                    requestGuardarEditarPunto.setTelefono(edit_tel_edit.getText().toString());
                    requestGuardarEditarPunto.setCelular(edit_cel_edit.getText().toString());
                    requestGuardarEditarPunto.setVende_recargas(venta_recarga);
                    requestGuardarEditarPunto.setCodigo_cum(edit_codigo_cum.getText().toString());
                    requestGuardarEditarPunto.setTipo_documento(tipoDocumento);
                    requestGuardarEditarPunto.setAccion(accion);

                    if (textInputMovil.getVisibility() == View.VISIBLE) {
                        requestGuardarEditarPunto.setNro_movil(Integer.parseInt(editMovil.getText().toString()));
                    }

                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(this, ActCrearPdvdos.class);
                    bundle.putSerializable("value", requestGuardarEditarPunto);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }

                break;
            case R.id.btn_cancelar_per:
                limpiarCampos();
                break;
        }
    }

    private void CargarDatosPunto() {
        alertDialog.show();
        String url = String.format("%1$s%2$s", getString(R.string.url_base), "consultar_info_puntos");
        rq = Volley.newRequestQueue(this);
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        cargarDatos(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(ActCrearPdvuno.this, "Error de tiempo de espera", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(ActCrearPdvuno.this, "Error Servidor", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(ActCrearPdvuno.this, "Server Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(ActCrearPdvuno.this, "Error de red", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(ActCrearPdvuno.this, "Error al serializar los datos", Toast.LENGTH_LONG).show();
                        }

                        alertDialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("iduser", String.valueOf(mydb.getUserLogin().getId()));
                params.put("iddis", mydb.getUserLogin().getId_distri());
                params.put("db", mydb.getUserLogin().getBd());
                params.put("perfil", String.valueOf(mydb.getUserLogin().getPerfil()));
                params.put("idpos", String.valueOf(editaPunto)); // aqui va el idpos a consultar.!

                return params;
            }
        };

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(jsonRequest);

    }

    private void cargarDatos(String response) {
        Gson gson = new Gson();
        if (!response.equals("[]")) {
            try {

                datos = gson.fromJson(response, RequestGuardarEditarPunto.class);

                if(datos.getIdpos() != -1) {

                    loadSpinnerTipo();

                    selectSpinnerValue(ListaTipoDoc, spinnerTipoDocumento, datos.getTipo_documento());

                    edit_nombres.setText(datos.getNombre_punto());

                    edit_nom_cli.setText(datos.getNombre_cliente());
                    edit_correo_edit.setText(datos.getEmail());
                    edit_tel_edit.setText(String.valueOf((datos.getTelefono())));
                    edit_cel_edit.setText(String.valueOf((datos.getCelular())));

                    if (datos.getVende_recargas() == 1) {
                        switch1.setChecked(true);
                        textInputMovil.setVisibility(View.VISIBLE);
                        editMovil.setText(String.valueOf(datos.getNro_movil()));
                    } else {
                        switch1.setChecked(false);
                        textInputMovil.setVisibility(View.GONE);
                    }

                    edit_codigo_cum.setText(datos.getCodigo_cum());


                } else {
                    setIndicador_refres(1);
                    Toast.makeText(this, datos.getNombre_punto() ,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, ActMainPeru.class);
                    startActivity(intent);
                }

            } catch (IllegalStateException ex) {
                ex.printStackTrace();
                alertDialog.dismiss();
            } finally {
                alertDialog.dismiss();

            }
        } else {
            alertDialog.dismiss();
        }
    }

    private void selectSpinnerValue(List<CategoriasEstandar> ListaEstado, Spinner spinner, int id) {
        for (int i = 0; i < ListaEstado.size(); i++) {
            if (ListaEstado.get(i).getId() == id) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    public void loadSpinnerTipo() {

        ListaTipoDoc = new ArrayList<>();

        ListaTipoDoc.add(new CategoriasEstandar(1, "RUC"));
        ListaTipoDoc.add(new CategoriasEstandar(2, "DNI"));

        ArrayAdapter<CategoriasEstandar> adapterEstados = new ArrayAdapter<>(this, R.layout.textview_spinner, ListaTipoDoc);
        spinnerTipoDocumento.setAdapter(adapterEstados);
        spinnerTipoDocumento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipoDocumento = ListaTipoDoc.get(position).getId();
                edit_cedula.setHint("");
                edit_cedula.setText("");
                if (tipoDocumento == 1) {
                    int maxLength = 11;
                    edit_cedula.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
                    edit_cedula.setHint("Ruc Responsable");
                } else {
                    int maxLength = 8;
                    edit_cedula.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
                    edit_cedula.setHint("Dni Responsable");
                }

                if (accion.equals("Editar")) {
                    edit_cedula.setText(datos.getCedula());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }

        });
    }

    private boolean isValidNumber(String number) {
        return number == null || number.length() == 0;
    }


    public boolean isValiEmail(String dato) { return !dato.matches("[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+"); }

    public boolean validarCampos() {

        boolean indicadorValidate = false;

        if (isValidNumber(edit_codigo_cum.getText().toString().trim())) {
            edit_codigo_cum.setFocusable(true);
            edit_codigo_cum.setFocusableInTouchMode(true);
            edit_codigo_cum.requestFocus();
            edit_codigo_cum.setText("");
            edit_codigo_cum.setError("Este campo es obligatorio");
            indicadorValidate = true;
        } else if (isValidNumber(edit_nombres.getText().toString().trim())) {
            edit_nombres.setFocusable(true);
            edit_nombres.setFocusableInTouchMode(true);
            edit_nombres.requestFocus();
            edit_nombres.setText("");
            edit_nombres.setError("Este campo es obligatorio");
            indicadorValidate = true;
        } else if (isValidNumber(edit_cedula.getText().toString().trim())) {
            edit_cedula.setFocusable(true);
            edit_cedula.setFocusableInTouchMode(true);
            edit_cedula.requestFocus();
            edit_cedula.setText("");
            edit_cedula.setError("Este campo es obligatorio");
            indicadorValidate = true;
        } else if (tipoDocumento == 1 && edit_cedula.getText().toString().length() < 11) {
            edit_cedula.setFocusable(true);
            edit_cedula.setFocusableInTouchMode(true);
            edit_cedula.requestFocus();
            edit_cedula.setText("");
            edit_cedula.setError("El Ruc debe ser igual a 11");
            indicadorValidate = true;
        } else if (tipoDocumento != 1 && edit_cedula.getText().toString().length() < 8) {
            edit_cedula.setFocusable(true);
            edit_cedula.setFocusableInTouchMode(true);
            edit_cedula.requestFocus();
            edit_cedula.setText("");
            edit_cedula.setError("El Dni debe ser igual a 8");
            indicadorValidate = true;
        } else if (isValidNumber(edit_nom_cli.getText().toString().trim())) {
            edit_nom_cli.setFocusable(true);
            edit_nom_cli.setFocusableInTouchMode(true);
            edit_nom_cli.requestFocus();
            edit_nom_cli.setText("");
            edit_nom_cli.setError("Este campo es obligatorio");
            indicadorValidate = true;
        } else if (isValidNumber(edit_correo_edit.getText().toString().trim())) { // PENDIENTE VALIDAR CORREO FORMATO
            edit_correo_edit.setFocusable(true);
            edit_correo_edit.setFocusableInTouchMode(true);
            edit_correo_edit.requestFocus();
            edit_correo_edit.setText("");
            edit_correo_edit.setError("Este campo es obligatorio");
            indicadorValidate = true;
        } else if (isValiEmail(edit_correo_edit.getText().toString().trim())) { // PENDIENTE VALIDAR CORREO FORMATO
            edit_correo_edit.setFocusable(true);
            edit_correo_edit.setFocusableInTouchMode(true);
            edit_correo_edit.requestFocus();
            edit_correo_edit.setError("Correo no valido");
            indicadorValidate = true;
        } else if (isValidNumber(edit_tel_edit.getText().toString().trim())) {
            edit_tel_edit.setFocusable(true);
            edit_tel_edit.setFocusableInTouchMode(true);
            edit_tel_edit.requestFocus();
            edit_tel_edit.setText("");
            edit_tel_edit.setError("Este campo es obligatorio");
            indicadorValidate = true;
        } else if (isValidNumber(edit_cel_edit.getText().toString().trim())) {
            edit_cel_edit.setFocusable(true);
            edit_cel_edit.setFocusableInTouchMode(true);
            edit_cel_edit.requestFocus();
            edit_cel_edit.setText("");
            edit_cel_edit.setError("Este campo es obligatorio");
            indicadorValidate = true;
        } else if (textInputMovil.getVisibility() == View.VISIBLE) {

            if (editMovil.getText().toString().trim().length() < 9) {
                editMovil.setFocusable(true);
                editMovil.setFocusableInTouchMode(true);
                editMovil.requestFocus();
                editMovil.setError("El numero móvil debe tener 9 dígitos");
                indicadorValidate = true;
            } else {
                indicadorValidate = false;
            }
        }

        return indicadorValidate;
    }

    private void limpiarCampos() {

        edit_codigo_cum.setText("");
        edit_nombres.setText("");
        edit_cedula.setText("");
        edit_nom_cli.setText("");
        edit_correo_edit.setText("");
        edit_correo_edit.setText("");
        edit_tel_edit.setText("");
        edit_cel_edit.setText("");

        edit_codigo_cum.setFocusable(true);
        edit_codigo_cum.setFocusableInTouchMode(true);
        edit_codigo_cum.requestFocus();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (rq != null) {
            rq.cancelAll(TAG);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rq != null) {
            rq.cancelAll(TAG);
        }
    }

    @Override
    public void onBackPressed() {
        setIndicador_refres(1);
        startActivity(new Intent(this, ActMainPeru.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

}
