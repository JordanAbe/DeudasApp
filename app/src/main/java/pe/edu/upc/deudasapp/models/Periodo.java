package pe.edu.upc.deudasapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 02/05/2018.
 */

public class Periodo {

    private int idPeriodo;
    private String descripcion;

    public Periodo() {
    }

    public Periodo(int idPeriodo, String descripcion) {
        this.idPeriodo = idPeriodo;
        this.descripcion = descripcion;
    }

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public static List<Periodo> from(JSONArray jsonPeriodos) {
        List<Periodo> periodos = new ArrayList<>();
        int length = jsonPeriodos.length();
        for(int i = 0; i < length; i++) {
            try {
                periodos.add(Periodo.from(jsonPeriodos.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return periodos;
    }

    public static Periodo from(JSONObject jsonPeriodo) {
        try {
            return new Periodo(jsonPeriodo.getInt("idPeriodo"), jsonPeriodo.getString("descripcion"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
