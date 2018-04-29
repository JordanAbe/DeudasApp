package pe.edu.upc.deudasapp.models;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 24/04/2018.
 */

public class Deuda {

    private int idDeuda;
    private int idDepartamento;
    private int idPeriodo;
    private String departamento;
    private String periodo;
    private double monto;
    private String estado;
    private int perfil;
    private List<DeudaDetalle> detalle;

    public Deuda(){
        //comment
    }

    public Deuda(int idDeuda){
        this.idDeuda = idDeuda;
    }

    public Deuda(int idDeuda, int idDepartamento, int idPeriodo, String departamento, String periodo, double monto, List<DeudaDetalle> detalle, String estado, int perfil) {
        this.idDeuda = idDeuda;
        this.idDepartamento = idDepartamento;
        this.idPeriodo = idPeriodo;
        this.departamento = departamento;
        this.periodo = periodo;
        this.monto = monto;
        this.detalle = detalle;
        this.estado = estado;
        this.perfil = perfil;
    }

    public int getIdDeuda() {
        return idDeuda;
    }

    public void setIdDeuda(int idDeuda) {
        this.idDeuda = idDeuda;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getPerfil() {
        return perfil;
    }

    public void setPerfil(int perfil) {
        this.perfil = perfil;
    }

    public List<DeudaDetalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DeudaDetalle> detalle) {
        this.detalle = detalle;
    }

    public static List<Deuda> from(JSONArray jsonDeudas) {
        List<Deuda> articles = new ArrayList<>();
        int length = jsonDeudas.length();
        for(int i = 0; i < length; i++) {
            try {
                articles.add(Deuda.from(jsonDeudas.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return articles;
    }

    public static Deuda from(JSONObject jsonArticle) {
        try {
            return new Deuda(
                    jsonArticle.getInt("idDeuda"),
                    jsonArticle.getInt("idDepartamento"),
                    jsonArticle.getInt("idPeriodo"),
                    jsonArticle.getString("departamento"),
                    jsonArticle.getString("periodo"),
                    jsonArticle.getDouble("monto"),
                    DeudaDetalle.from(jsonArticle.getJSONArray("detalle")),
                    jsonArticle.getString("estado"),
                    jsonArticle.getInt("perfil")
                    );
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
//public Deuda(int idDeuda, int idDepartamento, int idPeriodo, String departamento, String periodo, double monto, List<DeudaDetalle> detalle) {
    public static int from(Bundle bundle) {
        return bundle.getInt("idDeuda");
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        int idDeuda= getIdDeuda();
        bundle.putInt("idDeuda", idDeuda);
        return bundle;
    }
}
