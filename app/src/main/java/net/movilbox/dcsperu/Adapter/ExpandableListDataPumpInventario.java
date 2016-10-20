package net.movilbox.dcsperu.Adapter;

import net.movilbox.dcsperu.Entry.EntEstandar;
import net.movilbox.dcsperu.Entry.EntLisSincronizar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by jhonjimenez on 20/10/16.
 */

public class ExpandableListDataPumpInventario {

    public static HashMap<String, List<EntEstandar>> getData(List<EntLisSincronizar> data) {
        int elements = 0;

        HashMap<String, List<EntEstandar>> expandableListDetail = new LinkedHashMap<>();

        if (data != null) {

            for (int i = 0; i < data.size(); i++) {

                List<EntEstandar> technology = new ArrayList<>();

                EntEstandar entEstandar;
                for (int a = 0; a < data.get(i).getEntEstandarList().size(); a++) {

                    entEstandar = new EntEstandar();
                    entEstandar.setId(data.get(i).getEntEstandarList().get(a).getId());
                    entEstandar.setDescripcion(data.get(i).getEntEstandarList().get(a).getDescripcion());
                    entEstandar.setTipo_prod(data.get(i).getEntEstandarList().get(a).getTipo_prod());
                    technology.add(entEstandar);
                }

                expandableListDetail.put(data.get(i).getPaquete()+"", technology);
            }
        }
        return expandableListDetail;
    }
}
