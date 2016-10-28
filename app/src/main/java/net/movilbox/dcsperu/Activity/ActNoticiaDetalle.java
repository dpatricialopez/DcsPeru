package net.movilbox.dcsperu.Activity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dmax.dialog.SpotsDialog;

/**
 * Created by dianalopez on 14/10/16.
 */

public class ActNoticiaDetalle  extends AppCompatActivity {

    private TextView txtUrl, name, timestamp, txtStatusMsg ;
    private ImageView Image;
    private TextView file;
    private DownloadManager.Request request;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader1;
    private DBHelper mydb;
    ScrollView Scroll;
    private DisplayImageOptions options1;
    Integer[] array;
    protected DialogEmail dialog;
    int idNoticia,idsiguiente, idanterior,indexTipo;
    private ConnectionDetector connectionDetector;
    LinearLayout swipe;
    private long downloadReferenceId;
    TextView txtlabelfile, txtlabelurl;
    private float x1, x2,y1,y2,deltaX, deltaY;
    EntNoticia entNoticia;
    Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        connectionDetector = new ConnectionDetector(this);
        setContentView(R.layout.noticia_detalle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (connectionDetector.isConnected()) {
            toolbar.setTitle("Noticia");
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else {
            toolbar.setBackgroundColor(Color.RED);
            toolbar.setTitle("Noticia Offline");
        }
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
            indexTipo = Integer.parseInt(bundle.getString("indexTipo"));
        }

        txtlabelurl.setText(R.string.Enlace);
        txtlabelfile.setText(R.string.Adjuntos);
        List<ListaNoticias> noticiasArrayList=mydb.getNoticiaList();
        if(indexTipo==0) {

            List<Integer> arr2= new ArrayList<>();
            for (int i=0; i<noticiasArrayList.size();i++){
                for (int j=0; j<noticiasArrayList.get(i).size();j++){
                    arr2.add(noticiasArrayList.get(i).get(j).getId());
                }
            }
            array= new Integer[arr2.size()];
            arr2.toArray(array);
        }
        else{
            array = new Integer[noticiasArrayList.get(indexTipo-1).size()];
            for (int i=0; i<noticiasArrayList.get(indexTipo-1).size();i++){
                array[i]=noticiasArrayList.get(i).get(i).getId();
            }
        }

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
                                if (deltaX < 0 && Math.abs(deltaY)+10<Math.abs(deltaX))
                                  siguiente(null);

                                else if(deltaX >0 && Math.abs(deltaY)+10<Math.abs(deltaX)){
                                    anterior(null);
                                }
                                break;
                        }

                        return false;
                    }
                });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
        if (connectionDetector.isConnected()) {
            Snackbar.make(v, R.string.download_file, Snackbar.LENGTH_LONG)
                    .setAction("Descargar", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            downloadByDownloadManager(entNoticia.getFile_url(), entNoticia.getFile_name());
                        }
                    })
                    .show();
        } else {
            Snackbar.make(v, R.string.download_file_offline, Snackbar.LENGTH_LONG).show();
        }


    }


    public void loadnoticia( int idNoticia){

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

        if (entNoticia.getImage()!=null ){
            loadeImagenView(Image, entNoticia.getImage()                                                                                                                                          );
            Image.setVisibility(View.VISIBLE);
        }
        else{
            Image.setVisibility(View.GONE);
        }

        if (entNoticia.getStatus()==0){
            //  MarcarComoLeìdo();

        }

        if (entNoticia.getFile_name() != null) {
            file.setText(Html.fromHtml("<a>"
                    + entNoticia.getFile_name() + "</a> "));
            file.setMovementMethod(LinkMovementMethod.getInstance());
            file.setVisibility(View.VISIBLE);

        } else {
            file.setVisibility(View.GONE);
        }
        timestamp.setText(entNoticia.getDate());
        name.setText(entNoticia.getTitle());

        loadeImagenView(Image, entNoticia.getImage());
        Scroll.fullScroll(View.FOCUS_UP);

        if(mydb.getNoticia(idNoticia).getStatus()==0)
            MarcarComoLeìdo(idNoticia);

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
        request.setMimeType(getMimeFromFileName(entNoticia.getFile_name()));
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

        if (array.length>1) {
            if (Arrays.asList(array).indexOf(idNoticia)>=mydb.getNoticiaList().size()-1)
                idsiguiente=array[0];
            else
                idsiguiente=array[Arrays.asList(array).indexOf(idNoticia)+1];

           /* Bundle bundle = new Bundle();
            Intent intent = new Intent(this, ActNoticiaDetalle.class);
            bundle.putString("idNew", String.valueOf(idsiguiente));
            intent.putExtras(bundle);
            this.startActivity(intent);

            overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_right);
            finish();*/
    /*        if(mydb.getNoticia(idNoticia).getStatus()==0)
                MarcarComoLeìdo();*/
            Scroll.startAnimation( AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_left));
            idNoticia=idsiguiente;
            loadnoticia(idsiguiente);
            Scroll.startAnimation( AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right));
        }
    }

    public void anterior(View view){

        if (array.length>1) {
            if (Arrays.asList(array).indexOf(idNoticia) == 0)
                idanterior = array[mydb.getNoticiaList().size() - 1];
            else
                idanterior = array[Arrays.asList(array).indexOf(idNoticia) - 1];
            Scroll.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right));
            idNoticia = idanterior;
            loadnoticia(idanterior);
            Scroll.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_left));
        }

/*        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, ActNoticiaDetalle.class);
        bundle.putString("idNew", String.valueOf(idanterior));
        intent.putExtras(bundle);
        this.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        finish();*/
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

    private void MarcarComoLeìdo(int id) {
        mydb.updateStatusNoticiabyId(id);

    }


}
