package pe.edu.upc.deudasapp.network;

/**
 * Created by User on 24/04/2018.
 */

public class NewsApi {

    //private static String BASE_URL = "http://192.168.50.45:8081/deudas-service2";
    private static String BASE_URL = "http://208.78.163.73:8081/deudas-service2";


    public static String getDeudasUrl() {
        return BASE_URL + "/deudas";
    }

    public static String getDeudaUrl() {return BASE_URL + "/deuda";}

    public static String getPagarUrl() {return BASE_URL + "/pagarDeuda";}

    public static String getConfirmarUrl() {return BASE_URL + "/confirmarDeuda";}

    public static String getPeriodosUrl() {return BASE_URL + "/periodos";}

}
