package net.movilbox.dcsperu.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.gson.Gson;

import net.movilbox.dcsperu.Adapter.AdaptadorNoticia;
import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.EntNoticia;
import net.movilbox.dcsperu.Entry.ListaNoticias;
import net.movilbox.dcsperu.Entry.Motivos;
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
    private List<ListaNoticias> noticiasArrayList;
    ArrayList<String> tipos;
    ListView listView;
    int indexTipo;
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
        tipos= new ArrayList();
        tipos=mydb.getTiposNoticia();
        tipos.add(0,"Todos");
        String[] type = tipos.toArray(new String[tipos.size()]);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.textview_spinner, type);;
        sptag.setAdapter(spinnerArrayAdapter);
        sptag.setSelection(0);
        sptag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                indexTipo = position;
                cargarNoticias();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }

        });

       cargarNoticias();
        return view;

    }

public void hola(){}
    private void cargarNoticias() {
        ListaNoticias listaNoticias= new ListaNoticias();
        noticiasArrayList=mydb.getNoticiaList();
        if (indexTipo==0) {
            for (int i=0; i<tipos.size()-1;i++) {
                listaNoticias.addAll(noticiasArrayList.get(i));
            }
        }
        else{
            listaNoticias = noticiasArrayList.get(indexTipo-1);
        }

        AdaptadorNoticia adaptadorNoticia = new AdaptadorNoticia(getActivity(), listaNoticias, sptag.getSelectedItemPosition());
        listView.setAdapter(adaptadorNoticia);
    }

    /*public void JSONnews(String response){
        Gson gson = new Gson();
         ListaNoticias listaNoticia =gson.fromJson(response, ListaNoticias.class);
         ArrayList<Integer> eliminar=new ArrayList<>();
         ArrayList<Integer> actualizar=new ArrayList<>();
        ListaNoticias insertar = new ListaNoticias();




        for (int i = 0; i < listaNoticia.size(); i++) {
            if (mydb.getNoticia(listaNoticia.get(i).getId())!=null){ //existe
                if (listaNoticia.get(i).getVigencia() == 0) {
                    eliminar.add(listaNoticia.get(i).getId());
                }
                else {//Actualizar
                    actualizar.add(listaNoticia.get(i).getId());
                }
            }
            else{ //Nueva
                insertar.add(listaNoticia.get(i));
            }
        }

        mydb.insertNoticias(insertar);
        mydb.deleteNoticiabyId(eliminar);
        mydb.updateStatusNoticiabyId(actualizar);
        cargarNoticias();


    }*/

}
