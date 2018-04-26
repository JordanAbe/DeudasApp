package pe.edu.upc.deudasapp.network;

/**
 * Created by User on 24/04/2018.
 */

public class NewsApi {

    private static String BASE_URL = "http://localhost:8081/deudas-service2";

    public static String getDeudasUrl() {
        return BASE_URL + "/deudas";
    }

}
