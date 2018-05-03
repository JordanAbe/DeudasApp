package pe.edu.upc.deudasapp.models;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by User on 30/04/2018.
 */

public class Usuario extends SugarRecord {

    private String numero;

    public Usuario(){}

    public Usuario(String numero){
        this.numero = numero;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public static String getNumeroTelefono() {
        List<Usuario> numeros = Usuario.listAll(Usuario.class);
        if (numeros != null && numeros.size() > 0) {
            return numeros.get(0).getNumero();
        }else{
            return null;
        }
    }

    public static boolean existsNumeroTelefono(){
        List<Usuario> numeros = Usuario.listAll(Usuario.class);
        if (numeros != null && numeros.size() > 0) {
            return true;
        }else{
            return false;
        }
    }

    public static void saveNumeroTelefono(String numero) {
        deleteNumeroTelefono();
        Usuario.save(new Usuario(numero));
    }

    public static int deleteNumeroTelefono(){
        return Usuario.deleteAll(Usuario.class);
    }
}
