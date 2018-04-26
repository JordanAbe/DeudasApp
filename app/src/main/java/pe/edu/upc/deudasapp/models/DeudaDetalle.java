package pe.edu.upc.deudasapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 24/04/2018.
 */

public class DeudaDetalle {

    private int idDetalle;
    private int idDeuda;
    private int idServicio;
    private String servicio;
    private double monto;

    public DeudaDetalle(){}

    public DeudaDetalle(int idDetalle, int idDeuda, int idServicio, String servicio, double monto) {
        this.idDetalle = idDetalle;
        this.idDeuda = idDeuda;
        this.idServicio = idServicio;
        this.servicio = servicio;
        this.monto = monto;
    }

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getIdDeuda() {
        return idDeuda;
    }

    public void setIdDeuda(int idDeuda) {
        this.idDeuda = idDeuda;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public static List<DeudaDetalle> from(JSONArray jsonDeudaDetalle) {
        List<DeudaDetalle> detalle = new ArrayList<>();
        int length = jsonDeudaDetalle.length();
        for(int i = 0; i < length; i++) {
            try {
                detalle.add(DeudaDetalle.from(jsonDeudaDetalle.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return detalle;
    }

    public static DeudaDetalle from(JSONObject jsonDeudaDetalle) {
        try {
            return new DeudaDetalle(
                    jsonDeudaDetalle.getInt("idDetalle"),
                    jsonDeudaDetalle.getInt("idDeuda"),
                    jsonDeudaDetalle.getInt("idServicio"),
                    jsonDeudaDetalle.getString("servicio"),
                    jsonDeudaDetalle.getDouble("monto"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
