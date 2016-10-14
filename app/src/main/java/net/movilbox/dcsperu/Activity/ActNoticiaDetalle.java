package net.movilbox.dcsperu.Activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.CategoriasEstandar;
import net.movilbox.dcsperu.Entry.Ciudad;
import net.movilbox.dcsperu.Entry.Departamentos;
import net.movilbox.dcsperu.Entry.Distrito;
import net.movilbox.dcsperu.Entry.EntNoticia;
import net.movilbox.dcsperu.Entry.ListHome;
import net.movilbox.dcsperu.Entry.ListResponsePedido;
import net.movilbox.dcsperu.Entry.ListaNoticias;
import net.movilbox.dcsperu.Entry.ResponseCreatePunt;
import net.movilbox.dcsperu.Entry.ResponseMarcarPedido;
import net.movilbox.dcsperu.Entry.Territorio;
import net.movilbox.dcsperu.Entry.Zona;
import net.movilbox.dcsperu.R;
import net.movilbox.dcsperu.Services.FileDownloader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static net.movilbox.dcsperu.Entry.EntLoginR.setIndicador_refres;
import static net.movilbox.dcsperu.Entry.ResponseHome.setResponseHomeListS;

/**
 * Created by dianalopez on 14/10/16.
 */

public class ActNoticiaDetalle  extends AppCompatActivity {

    private TextView txtUrl, name, timestamp, txtStatusMsg, file ;
    private ImageView Image;
    private List<EntNoticia> entNoticias;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader1;
    private DBHelper mydb;
    private DisplayImageOptions options1;
    private SpotsDialog alertDialog;
    private RequestQueue rq;
     int idNoticia;
    EntNoticia entNoticia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.noticiadetalle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mydb = new DBHelper(this);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        imageLoader1 = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader1.init(config);
        txtUrl = (TextView)findViewById(R.id.txtUrl);
        name = (TextView) findViewById(R.id.name);
        timestamp = (TextView) findViewById(R.id.timestamp);
        txtStatusMsg = (TextView) findViewById(R.id.txtStatusMsg);
        file = (TextView) findViewById(R.id.download);
        Image = (ImageView) findViewById(R.id.ImageUrl);
        options1 = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .cacheOnDisc()
                .build();

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            idNoticia = bundle.getInt("idNew");
        }

        entNoticia=mydb.getNoticia(idNoticia);

        if (entNoticia.getContain()!=null){
            txtStatusMsg.setText(entNoticia.getContain());
            txtStatusMsg.setVisibility(View.VISIBLE);
        }
        else
        {
            txtStatusMsg.setVisibility(View.GONE);
        }

        if (entNoticia.getUrl() != null) {
            txtUrl.setText(Html.fromHtml("<a href=\"" + entNoticia.getUrl() + "\">"
                    + entNoticia.getUrl() + "</a> "));

            // Making url clickable
            txtUrl.setMovementMethod(LinkMovementMethod.getInstance());
            txtUrl.setVisibility(View.VISIBLE);
        } else {
            // url is null, remove from the view
            txtUrl.setVisibility(View.GONE);
        }

        if (entNoticia.getImge()!=null ){
            loadeImagenView(Image, entNoticia.getImge()                                                                                                                                          );
            Image.setVisibility(View.VISIBLE);
        }
        else{
            Image.setVisibility(View.GONE);
        }

        if (entNoticia.getStatus()==0){
         //  MarcarComoLeìdo();

        }

        timestamp.setText(entNoticia.getDate());
        name.setText(entNoticia.getTitle());
        file.setText(entNoticia.getFileName());
        file.setClickable(true);

        loadeImagenView(Image, entNoticia.getImge());


   /*     getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/
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

    public void download(View v)
    {
        new DownloadFile().execute(entNoticia.getFile_url(), entNoticia.getFileName());
    }

    public void view(View v)
    {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/testthreepdf/" + "maven.pdf");  // -> filename = maven.pdf
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try{
            startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(ActNoticiaDetalle.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "testthreepdf");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }
    }


    private void MarcarComoLeìdo() {
        alertDialog.show();
        String url = String.format("%1$s%2$s", getString(R.string.url_base), "guardar_pedido");
        rq = Volley.newRequestQueue(this);

        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        parseJSON(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(ActNoticiaDetalle.this, "Error de tiempo de espera", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(ActNoticiaDetalle.this, "Error Servidor", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(ActNoticiaDetalle.this, "Server Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(ActNoticiaDetalle.this, "Error de red", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(ActNoticiaDetalle.this, "Error al serializar los datos", Toast.LENGTH_LONG).show();
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
                params.put("idNoticia", String.valueOf(idNoticia));
                return params;
            }
        };
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(jsonRequest);
    }

    public void parseJSON(String response){

        Gson gson = new Gson();

        final ListaNoticias listaNoticia =gson.fromJson(response, ListaNoticias.class);


        mydb.deleteObject("ListaNoticias");
        boolean success= mydb.insertNoticias(listaNoticia);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.act_main_peru, menu);
        return true;
    }

}
