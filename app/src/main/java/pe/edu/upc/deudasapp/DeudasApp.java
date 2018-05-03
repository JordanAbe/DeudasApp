package pe.edu.upc.deudasapp;

import com.androidnetworking.AndroidNetworking;
import com.orm.SugarApp;

/**
 * Created by User on 30/04/2018.
 */

public class DeudasApp extends SugarApp {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
    }
}
