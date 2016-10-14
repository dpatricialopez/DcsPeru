package net.movilbox.dcsperu.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import net.movilbox.dcsperu.Activity.ActMainPeru;
import net.movilbox.dcsperu.Adapter.AdaptadorNoticia;
import net.movilbox.dcsperu.Adapter.AdapterCarrito;
import net.movilbox.dcsperu.Adapter.AdapterRutero;
import net.movilbox.dcsperu.Adapter.TabsAdapter;
import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.EntNoticia;
import net.movilbox.dcsperu.Entry.ListEntregaPediSincronizar;
import net.movilbox.dcsperu.Entry.ListResponseEntregaPedido;
import net.movilbox.dcsperu.Entry.ListaGrupos;
import net.movilbox.dcsperu.Entry.ListaNoticias;
import net.movilbox.dcsperu.Entry.PedidosEntregaSincronizar;
import net.movilbox.dcsperu.R;
import net.movilbox.dcsperu.Services.ConnectionDetector;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentNoticia extends BaseVolleyFragment {
    private EntNoticia entNoticia1;
    private DBHelper mydb;
    private AdaptadorNoticia adaptadorNoticia;
    private ConnectionDetector connectionDetector;
    private AdaptadorNoticia listadapter;
    private List<EntNoticia> listaNoticias;
    ListView listView;

    public FragmentNoticia() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.content_noticia, container, false);
        mydb = new DBHelper(getActivity());
        listView = (ListView) view.findViewById(R.id.lista_noticia);
        connectionDetector = new ConnectionDetector(getActivity());
        entNoticia1=new EntNoticia();
        String response="[{'id':'0','title':'noticia1','contain':'Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas \"Letraset\", las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.', 'url':'http://www.w3schools.com/html/pic_mountain.jpg', 'image':'http://www.w3schools.com/html/pic_mountain.jpg', 'date':'Oct 1','status':'1','file_name'='file1.doc','file_url':'https://sites.google.com/site/cursoscei/cursos/excel/docsexcel/AcumuladosporMeses.xls?attredirects=0&d=1'},{'id':'1','title':'noticia2','contain':'Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas \"Letraset\", las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.', 'url':'http://www.w3schools.com/html/pic_mountain.jpg', 'image':'http://www.w3schools.com/html/pic_mountain.jpg', 'date':'Oct 2','status':'0','file_name'='file1.doc','file_url':'https://sites.google.com/site/cursoscei/cursos/excel/docsexcel/AcumuladosporMeses.xls?attredirects=0&d=1'},{'id':'3',title:'noticia3','contain':'Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas , las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.', 'url':'http://www.w3schools.com/html/pic_mountain.jpg', 'image':'http://www.w3schools.com/html/pic_mountain.jpg', 'date':'Oct 7','status':'0','file_name'='file1.doc','file_url':'https://sites.google.com/site/cursoscei/cursos/excel/docsexcel/AcumuladosporMeses.xls?attredirects=0&d=1'}]";

        JSONnews(response );

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuItem item2 = menu.add("Carrito");
        item2.setIcon(R.drawable.ic_cloud_upload_white_24dp); // sets icon
        item2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Sincronizar...
                if (connectionDetector.isConnected()) {
                    //LoadNews();

                } else {
                    Toast.makeText(getActivity(), "No tiene acceso a internet para sincronizar", Toast.LENGTH_LONG).show();
                }

                return true;

            }

        });
    }
    private void cargarNoticias() {
        listaNoticias = mydb.getNoticiaList();
        AdaptadorNoticia adaptadorNoticia = new AdaptadorNoticia(getActivity(), listaNoticias);
        listView.setAdapter(adaptadorNoticia);
    }

    public void JSONnews(String response){
        Gson gson = new Gson();
        final ListaNoticias listaNoticia =gson.fromJson(response, ListaNoticias.class);
        mydb.deleteObject("ListaNoticias");
        boolean success= mydb.insertNoticias(listaNoticia);
        cargarNoticias();


    }

}
