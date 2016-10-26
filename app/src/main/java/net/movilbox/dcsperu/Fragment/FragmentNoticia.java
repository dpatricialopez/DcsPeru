package net.movilbox.dcsperu.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import net.movilbox.dcsperu.Adapter.AdaptadorNoticia;
import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.EntNoticia;
import net.movilbox.dcsperu.Entry.ListaNoticias;
import net.movilbox.dcsperu.R;
import net.movilbox.dcsperu.Services.ConnectionDetector;

import java.util.ArrayList;
import java.util.List;

public class FragmentNoticia extends BaseVolleyFragment {
    private EntNoticia entNoticia1;
    private DBHelper mydb;
    private AdaptadorNoticia adaptadorNoticia;
    private ConnectionDetector connectionDetector;
    private AdaptadorNoticia listadapter;
    private List<EntNoticia> listaNoticias;
    ListView listView;
    Spinner sptag;

    public FragmentNoticia() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.content_noticia, container, false);
        mydb = new DBHelper(getActivity());
        listView = (ListView) view.findViewById(R.id.lista_noticia);
        sptag=(Spinner) view.findViewById(R.id.spTag);
        connectionDetector = new ConnectionDetector(getActivity());
        entNoticia1=new EntNoticia();

        String response="[{'id':'0','tipo':'0','title':'noticia1','contenido':'Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos.', 'url':'http://www.w3schools.com/html/pic_mountain.jpg', 'url_image':'http://www.w3schools.com/html/pic_mountain.jpg', 'fecha':'Oct 1','file_name'='file1.doc','file_url':'https://sites.google.com/site/cursoscei/cursos/excel/docsexcel/AcumuladosporMeses.xls?attredirects=0&d=1','estado':'0','fecha_lectura':'Oct 6','sincronizado':'1','vigencia':'1'}, {'id':'1','tipo':'1','title':'noticia2','contenido':'', 'url':'http://www.w3schools.com/html/pic_mountain.jpg', 'url_image':'http://www.w3schools.com/html/pic_mountain.jpg', 'fecha':'Oct 2','status':'1','file_name'='file1.doc','file_url':'','estado':'0','fecha_lectura':'Oct 9','sincronizado':'1','vigencia':'1'}]";

        JSONnews(response );

        return view;
    }

    private void cargarNoticias() {
        listaNoticias = mydb.getNoticiaList();
        AdaptadorNoticia adaptadorNoticia = new AdaptadorNoticia(getActivity(), listaNoticias);
        listView.setAdapter(adaptadorNoticia);
    }

    public void JSONnews(String response){
        Gson gson = new Gson();
        final ListaNoticias listaNoticia =gson.fromJson(response, ListaNoticias.class);
        final ArrayList<Integer> eliminar=new ArrayList<>();
        final ArrayList<Integer> actualizar=new ArrayList<>();
        final ListaNoticias insertar = new ListaNoticias();

        for (int i = 0; i < listaNoticia.size(); i++) {
            if (listaNoticia.get(i).getVigencia() == 0) {
                eliminar.add(listaNoticia.get(i).getId());
            }
            else  if (listaNoticia.get(i).getStatus() == 1) {
                actualizar.add(listaNoticia.get(i).getId());
            }
            else{
                insertar.add(listaNoticia.get(i));
            }
        }

        mydb.insertNoticias(insertar);
        mydb.deleteNoticiabyId(eliminar);
        mydb.updateStatusNoticiabyId(actualizar);
        cargarNoticias();


    }

}
