package pe.edu.upc.deudasapp.network;

/**
 * Created by User on 24/04/2018.
 */

public class NewsApi {

    private static String BASE_URL = "http://192.168.1.37:8081/deudas-service2";

    public static String getDeudasUrl() {
        return BASE_URL + "/deudas";
    }

    public static String getDeudaUrl() {
        return BASE_URL + "/deuda";
    }

}
