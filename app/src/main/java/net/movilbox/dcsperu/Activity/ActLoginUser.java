package net.movilbox.dcsperu.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import net.movilbox.dcsperu.BuildConfig;
import net.movilbox.dcsperu.DataBase.ConnectManagment;
import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.EntLoginR;
import net.movilbox.dcsperu.Entry.ResponseUser;
import net.movilbox.dcsperu.Entry.TimeService;
import net.movilbox.dcsperu.R;
import net.movilbox.dcsperu.Services.ConnectionDetector;
import net.movilbox.dcsperu.Services.GpsServices;
import net.movilbox.dcsperu.Services.MonitoringService;
import net.movilbox.dcsperu.Services.SetTracingServiceWeb;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.movilbox.dcsperu.Entry.Coordenadas.setLatitud;
import static net.movilbox.dcsperu.Entry.Coordenadas.setLongitud;
import static net.movilbox.dcsperu.Entry.EntLoginR.setIndicador_refres;



public class ActLoginUser extends AppCompatActivity implements View.OnClickListener {

    private TextView editUsuario;
    private TextView editPassword;
    private CoordinatorLayout coordinatorLayout;
    private RequestQueue rq;
    public static final String TAG = "MyTag";
    private GpsServices gpsServices;
    private EditText edit_correo_edit;
    protected DialogEmail dialog;
    private DBHelper mydb;
    private ConnectionDetector connectionDetector;
    private int progressStatus = 0;
    private String mensaje = "";
    private Handler handler = new Handler();
    public ProgressDialog progressDialog;
    public ConnectManagment spref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);

        gpsServices = new GpsServices(this);

        mydb = new DBHelper(this);
        mydb.InsertConnect(1);
        spref= new ConnectManagment(this);
        spref.CreateConnect(true);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        connectionDetector = new ConnectionDetector(this);

        editUsuario = (EditText) findViewById(R.id.editUsuario);
        editPassword = (EditText) findViewById(R.id.editPassword);
        TextView txtVersion = (TextView) findViewById(R.id.txtVersion);


        TextView link_pass = (TextView) findViewById(R.id.link_pass);
        link_pass.setOnClickListener(this);

        Button btnIngresar = (Button) findViewById(R.id.btnIngresar);
        btnIngresar.setOnClickListener(this);

        String versionNametext = BuildConfig.VERSION_NAME;


        txtVersion.setText(String.format("Versión: %1$s", versionNametext));

    }

    private void enviarCorreo() {

        String url = getString(R.string.url_base_correo); //"http://192.168.2.24/web/movistar_peru/distribuidorbt/modulos/login/controlador.php";
        rq = Volley.newRequestQueue(this);
        dialog.dismiss();
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        RespuestaCorreo(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            //Toast.makeText(ActLoginUser.this, "Error de tiempo de espera", Toast.LENGTH_LONG).show();
                            Toast.makeText(ActLoginUser.this, "Hemos enviado un correo con la recuperación de la contraseña", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(ActLoginUser.this, "Error Servidor", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(ActLoginUser.this, "Server Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(ActLoginUser.this, "Error de red", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(ActLoginUser.this, "Error al serializar los datos", Toast.LENGTH_LONG).show();
                        }

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("accion", "Recover");
                params.put("correo", edit_correo_edit.getText().toString().trim());

                return params;
            }
        };

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(jsonRequest);
    }

    private void RespuestaCorreo(String response) {

        if (!response.equals("")) {

            switch (response) {
                case "1":
                    Toast.makeText(this, "Se enviaron los datos para recuperar la contraseña al correo indicado", Toast.LENGTH_LONG).show();
                    break;
                case "0":
                    Toast.makeText(this, "Error al intentar enviar el correo, intente nuevamente", Toast.LENGTH_LONG).show();
                    break;
                case "2":
                    Toast.makeText(this, "El correo: " + edit_correo_edit.getText() + " no se encuentra registrado", Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(this, "Error al enviar el correo", Toast.LENGTH_LONG).show();
                    break;
            }
            dialog.dismiss();
        } else {
            Toast.makeText(this, "No pudo enviar el correo", Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }

    }

    private boolean isValidNumber(String number) {
        return number == null || number.length() == 0;
    }

    private boolean isValidNumberEmail(String number) {

        Pattern Plantilla = null;
        Matcher Resultado = null;
        Plantilla = Pattern.compile("^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$");

        Resultado = Plantilla.matcher(number);

        return Resultado.find();

    }

    private void loginServices() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Sincronización de Información");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = String.format("%1$s%2$s", getString(R.string.url_base), "login_user");
        rq = Volley.newRequestQueue(this);
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        parseJsonLogin(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(ActLoginUser.this, "Error de tiempo de espera", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(ActLoginUser.this, "Error Servidor", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(ActLoginUser.this, "Server Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(ActLoginUser.this, "Error de red", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(ActLoginUser.this, "Error al serializar los datos", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                int versionCode = BuildConfig.VERSION_CODE;
                String versionName = BuildConfig.VERSION_NAME;
                params.put("user", editUsuario.getText().toString().trim());
                params.put("pass", editPassword.getText().toString().trim());
                params.put("versionCode", String.valueOf(versionCode));
                params.put("versionName", versionName);
                params.put("indicador", String.valueOf(mydb.validarTablas()));
                params.put("fecha", mydb.getUserLogin().getFecha_sincroniza() == null ? "": mydb.getUserLogin().getFecha_sincroniza());

                return params;
            }
        };
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(jsonRequest);
    }

    private void parseJsonLogin(String response) {
        Gson gson = new Gson();
        try {

            final EntLoginR loginR = gson.fromJson(response, EntLoginR.class);

            if (loginR.getEstado() == -1) {
                progressDialog.dismiss();
                Snackbar.make(coordinatorLayout, loginR.getMsg(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            } else if (loginR.getEstado() == -2) {
                progressDialog.dismiss();
                Snackbar snackbar = Snackbar.make(coordinatorLayout, loginR.getMsg(), Snackbar.LENGTH_INDEFINITE)
                        .setAction("DESCARGAR", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("market://details?id=net.movilbox.dcsperu"));
                                    startActivity(intent);
                                } catch (Exception e) { //google play app is not installed
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=net.movilbox.dcsperu"));
                                    startActivity(intent);
                                }
                            }
                        });
                snackbar.show();
            } else if (loginR.getEstado() == 1) {

                if (!mydb.validateLoginUser(editUsuario.getText().toString().toUpperCase(), editPassword.getText().toString())) {

                    loginR.setPassword(editPassword.getText().toString().trim());
                    mydb.deleteObject("login");
                    mydb.insertLoginUser(loginR);

                }

                mydb.deleteAllServiTime();

                TimeService timeService = new TimeService();

                timeService.setIdUser(loginR.getId());
                timeService.setTraking(loginR.getIntervalo());
                timeService.setTimeservice(loginR.getCantidad_envios());
                timeService.setIdDistri(loginR.getId_distri());
                timeService.setDataBase(loginR.getBd());
                timeService.setFechainicial(loginR.getHora_inicial());
                timeService.setFechaFinal(loginR.getHora_final());

                mydb.insertTimeServices(timeService);

                if (loginR.getDatosGenerales().size() > 0) {

                    progressDialog.setMax(loginR.getDatosGenerales().size());
                    mensaje = "";
                    progressStatus = 0;

                    // Start lengthy operation in a background thread
                    new Thread(new Runnable() {
                        public void run() {

                            while (progressStatus < loginR.getDatosGenerales().size()) {

                                switch (loginR.getDatosGenerales().get(progressStatus).getTipoTabla()) {
                                    case 1:
                                        mensaje = "Sincronizando Categorias de los Puntos de Venta.";
                                        mydb.insertCategoria(loginR.getDatosGenerales().get(progressStatus));
                                        break;
                                    case 2:
                                        mensaje = "Sincronizando Departamentos.";
                                        mydb.insertDepartamento(loginR.getDatosGenerales().get(progressStatus));
                                        break;
                                    case 3:
                                        mensaje = "Sincronizando Distritos.";
                                        mydb.insertDistritos(loginR.getDatosGenerales().get(progressStatus));
                                        break;
                                    case 4:
                                        mensaje = "Sincronizando Estados comerciales de los Puntos de Venta.";
                                        mydb.insertEstadoComercial(loginR.getDatosGenerales().get(progressStatus));
                                        break;
                                    case 5:
                                        mensaje = "Sincronizando Provincias.";
                                        mydb.insertMunicipios(loginR.getDatosGenerales().get(progressStatus));
                                        break;
                                    case 6:
                                        mensaje = "Sincronizando Tipos de dirección";
                                        mydb.insertNomenclaturas(loginR.getDatosGenerales().get(progressStatus));
                                        break;
                                    case 7:
                                        mensaje = "Sincronizando SubCategorias de los Puntos de Venta.";
                                        mydb.insertSubcategoriasPuntos(loginR.getDatosGenerales().get(progressStatus));
                                        break;
                                }

                                progressStatus++;

                                // Update the progress bar
                                handler.post(new Runnable() {
                                    public void run() {
                                        progressDialog.setMessage(mensaje);
                                        progressDialog.setProgress(progressStatus);
                                    }
                                });

                            }



                            setIndicador_refres(1);

                            mydb.updateFechaSincroLogin(loginR.getFecha_sincroniza(), loginR.getId());

                            startActivity(new Intent(ActLoginUser.this, ActMainPeru.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();

                            progressDialog.dismiss();

                        }

                    }).start();

                } else {

                    setIndicador_refres(1);

                    mydb.updateFechaSincroLogin(loginR.getFecha_sincroniza(), loginR.getId());

                    progressDialog.dismiss();

                    startActivity(new Intent(ActLoginUser.this, ActMainPeru.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();

                }
            }

        } catch (IllegalStateException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG);
        }

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
    public void onResume() {
        super.onResume();

        gpsServices = new GpsServices(this);

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnIngresar:
  //              mydb.updateConnect(1);
                spref.CreateConnect(true);

                if (gpsServices.getLatitude() == 0.0) {
                    gpsServices.showSettingsAlert();
                } else {
                    if (isValidNumber(editUsuario.getText().toString().trim())) {
                        editUsuario.setError("Campo requerido");
                        editUsuario.requestFocus();
                    } else if (isValidNumber(editPassword.getText().toString().trim())) {
                        editPassword.setError("Campo requerido");
                        editPassword.requestFocus();
                    } else {
                        if (connectionDetector.isConnected()) {
                            loginServices();
                        } else {

                            if (mydb.validateLoginUser(editUsuario.getText().toString().toUpperCase(), editPassword.getText().toString())) {
                                //Existe el usuario
                                setLatitud(gpsServices.getLatitude());
                                setLongitud(gpsServices.getLongitude());

                                ResponseUser login = mydb.getUserLogin(editUsuario.getText().toString());

                                //setResponseUserStatic(login);

                                mydb.deleteAllServiTime();

                                TimeService timeService = new TimeService();

                                timeService.setIdUser(login.getId());
                                timeService.setTraking(login.getIntervalo());
                                timeService.setTimeservice(login.getCantidad_envios());
                                timeService.setIdDistri(login.getId_distri());
                                timeService.setDataBase(login.getBd());
                                timeService.setFechainicial(login.getHora_inicial());
                                timeService.setFechaFinal(login.getHora_final());

                                mydb.insertTimeServices(timeService);

                                startActivity(new Intent(this, ActMainPeru.class));
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                finish();

                            } else {
                                Snackbar.make(coordinatorLayout, "Usuario no estan registrados Offline", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            }

                        }
                    }
                }

                break;

            case R.id.link_pass:

                dialog = new DialogEmail(ActLoginUser.this, "Recuperación de Contraseña");
                dialog.show();
                Button acceptButton = dialog.getButtonAccept();
                acceptButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        edit_correo_edit = (EditText) dialog.findViewById(R.id.edit_correo_edit);

                        if (isValidNumber(edit_correo_edit.getText().toString().trim())) {
                            edit_correo_edit.setError("Campo requerido");
                            edit_correo_edit.requestFocus();
                        } else if (!isValidNumberEmail(edit_correo_edit.getText().toString().trim())) {
                            edit_correo_edit.setError("El correo ingresado no es valido");
                            edit_correo_edit.requestFocus();
                        } else {
                            enviarCorreo();

                        }
                    }
                });

                break;

        }
    }

}
