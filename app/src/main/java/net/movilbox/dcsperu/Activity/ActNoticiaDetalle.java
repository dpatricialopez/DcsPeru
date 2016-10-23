package net.movilbox.dcsperu.Activity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.EntNoticia;
import net.movilbox.dcsperu.Entry.ListaNoticias;
import net.movilbox.dcsperu.R;
import net.movilbox.dcsperu.Services.ConnectionDetector;

import java.util.Arrays;
import java.util.List;

import dmax.dialog.SpotsDialog;

import static net.movilbox.dcsperu.Entry.EntLoginR.setIndicador_refres;

/**
 * Created by dianalopez on 14/10/16.
 */

public class ActNoticiaDetalle  extends AppCompatActivity {

    private TextView txtUrl, name, timestamp, txtStatusMsg ;
    private ImageView Image;
    private TextView file;
    private DownloadManager.Request request;
    private List<EntNoticia> entNoticias;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader1;
    private DBHelper mydb;
    ScrollView Scroll;
    private DisplayImageOptions options1;
    private SpotsDialog alertDialog;
    private RequestQueue rq;
    Integer[] array;
    protected DialogEmail dialog;
    int idNoticia,idsiguiente, idanterior;
    private ConnectionDetector connectionDetector;
    LinearLayout swipe;
    private long downloadReferenceId;
    TextView txtlabelfile, txtlabelurl;
    private float x1, x2,y1,y2,deltaX, deltaY;
    EntNoticia entNoticia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.noticia_detalle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Noticia");
        connectionDetector = new ConnectionDetector(this);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        setSupportActionBar(toolbar);

        mydb = new DBHelper(this);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        imageLoader1 = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader1.init(config);
        txtUrl = (TextView)findViewById(R.id.txtUrl);
        name = (TextView) findViewById(R.id.name);
        txtlabelfile = (TextView)findViewById(R.id.labelfile);
        txtlabelurl = (TextView) findViewById(R.id.labelEnlace);
        timestamp = (TextView) findViewById(R.id.timestamp);
        txtStatusMsg = (TextView) findViewById(R.id.txtStatusMsg);
        file = (TextView) findViewById(R.id.download);
        Image = (ImageView) findViewById(R.id.ImageUrl);
        swipe=(LinearLayout)findViewById(R.id.swipe);
        Scroll=(ScrollView)findViewById(R.id.scroll);
        options1 = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .cacheOnDisc()
                .build();

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            idNoticia = Integer.parseInt(bundle.getString("idNew"));
        }

        txtlabelurl.setText(R.string.Enlace);
        txtlabelfile.setText(R.string.Adjuntos);
        loadnoticia(idNoticia);

        Scroll.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // TODO Auto-generated method stub
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                x1 = event.getX();
                                y1=event.getY();
                                break;
                            case MotionEvent.ACTION_UP:
                                x2 = event.getX();
                                y2=event.getY();
                                deltaX = x2 - x1;
                                deltaY=y2-y1;
                                Log.e("coord",Math.abs(deltaX)+"/"+Math.abs(deltaY));
                                if (deltaX < 0 && Math.abs(deltaY)+10<Math.abs(deltaX)) {
                                   siguiente(null);

                                }else if(deltaX >0 && Math.abs(deltaY)+10<Math.abs(deltaX)){
                                    anterior(null);
                                }
                                break;
                        }

                        return false;
                    }
                });


   /*     getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIndicador_refres(1);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        array = new Integer[mydb.getNoticiaList().size()];
        for (int i=0; i<mydb.getNoticiaList().size();i++){
            array[i]=mydb.getNoticiaList().get(i).getId();
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

    public void download(View v)
    {
        Log.d("Strin", "uryl" );
        downloadByDownloadManager(entNoticia.getFile_url(), entNoticia.getFileName());
    }


    public void loadnoticia( int idNoticia){

        entNoticia=mydb.getNoticia(idNoticia);
        entNoticias=mydb.getNoticiaList();
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

        if (entNoticia.getFileName() != null) {
            file.setText(Html.fromHtml("<a>"
                    + entNoticia.getFileName() + "</a> "));
            file.setMovementMethod(LinkMovementMethod.getInstance());
            file.setVisibility(View.VISIBLE);

        } else {
            file.setVisibility(View.GONE);
        }
        timestamp.setText(entNoticia.getDate());
        name.setText(entNoticia.getTitle());

        loadeImagenView(Image, entNoticia.getImge());
        Scroll.fullScroll(View.FOCUS_UP);

    }

    public void downloadByDownloadManager(String url, String outputFileName) {
        final DownloadManager downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);

        request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Archivo adjunto");
        request.setTitle("DcsDealer");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.allowScanningByMediaScanner();
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, outputFileName);
        request.setVisibleInDownloadsUi(true);
        request.setMimeType(getMimeFromFileName(entNoticia.getFileName()));
        BroadcastReceiver receiver;

         downloadReferenceId = downloadManager.enqueue(request);


        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String mAction = intent.getAction();
                if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(mAction)){
                    long returnedId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    if(returnedId == downloadReferenceId){
                        DownloadManager.Query mQuery = new DownloadManager.Query();
                        mQuery.setFilterById(returnedId);
                        Cursor cursor = downloadManager.query(mQuery);
                        if(cursor.moveToFirst()){
                            int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                            if (DownloadManager.STATUS_SUCCESSFUL != cursor.getInt(statusIndex)) {
                                Toast.makeText(ActNoticiaDetalle.this, "Fallo", Toast.LENGTH_LONG).show();
                                return;
                            }

                        }
                    }
                }
            }
        };
        IntentFilter mIntentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(receiver, mIntentFilter);
    }

    public void siguiente(View view){

        if (Arrays.asList(array).indexOf(idNoticia)>=mydb.getNoticiaList().size()-1)
            idsiguiente=array[0];
        else
            idsiguiente=array[Arrays.asList(array).indexOf(idNoticia)+1];

        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, ActNoticiaDetalle.class);
        bundle.putString("idNew", String.valueOf(idsiguiente));
        intent.putExtras(bundle);
        this.startActivity(intent);

        overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_right);
        finish();
/*        if(mydb.getNoticia(idNoticia).getStatus()==0)
            MarcarComoLeìdo();

        idNoticia=idsiguiente;
        loadnoticia(idsiguiente);*/
    }

    public void anterior(View view){
        if (Arrays.asList(array).indexOf(idNoticia)==0)
            idanterior=array[mydb.getNoticiaList().size()-1];
        else
            idanterior=array[Arrays.asList(array).indexOf(idNoticia)-1];
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, ActNoticiaDetalle.class);
        bundle.putString("idNew", String.valueOf(idanterior));
        intent.putExtras(bundle);
        this.startActivity(intent);
        overridePendingTransition(R.animator.slide_in_left, R.animator.slide_out_left);
        finish();
      /*  if(mydb.getNoticia(idNoticia).getStatus()==0)
            MarcarComoLeìdo();

        idNoticia=idanterior;
        loadnoticia(idanterior)*/;
    }

    public void finish(View view){
        finish();
    }

    private String getMimeFromFileName(String fileName) {
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String ext = MimeTypeMap.getFileExtensionFromUrl(fileName);
        return map.getMimeTypeFromExtension(ext);
    }

    private void MarcarComoLeìdo() {


        /*alertDialog.show();
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
        rq.add(jsonRequest);*/
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
        //if(mydb.getUserLogin().getPerfil() == 1) {
        getMenuInflater().inflate(R.menu.menu_new, menu);
        //}

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh_new) {
           Log.d("refresh","refresh");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
