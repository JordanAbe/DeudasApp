package pe.edu.upc.deudasapp.viewcontrollers.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ScrollingTabContainerView;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.deudasapp.R;
import pe.edu.upc.deudasapp.models.Deuda;
import pe.edu.upc.deudasapp.models.DeudaDetalle;
import pe.edu.upc.deudasapp.models.Periodo;
import pe.edu.upc.deudasapp.models.Usuario;
import pe.edu.upc.deudasapp.network.NewsApi;
import pe.edu.upc.deudasapp.util.Constantes;
import pe.edu.upc.deudasapp.viewcontrollers.adapters.DeudasAdapter;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeudasFragment extends Fragment {

    private RecyclerView deudasRecyclerView;
    private DeudasAdapter deudasAdapter;
    private RecyclerView.LayoutManager deudasLayoutManager;
    private List<Deuda> deudas;
    private static String TAG = "DeudasApp";
    private static final int PERMISSION_REQUEST_CODE = 1;
    String wantPermission = Manifest.permission.READ_PHONE_STATE;

    private TextView textDepartamento;
    private Spinner spinner;
    private List<Periodo> periodos;
    private String idPeriodo;

    public DeudasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_deudas, container, false);
        deudasRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_deudas);
        deudas = new ArrayList<>();
        deudasAdapter = new DeudasAdapter(deudas);
        deudasLayoutManager = new GridLayoutManager(view.getContext(), getSpanCount(getResources().getConfiguration()));
        deudasRecyclerView.setAdapter(deudasAdapter);
        deudasRecyclerView.setLayoutManager(deudasLayoutManager);

        textDepartamento = (TextView) view.findViewById(R.id.text_departamento);
        updateData();
        idPeriodo = "";
        setSpinner();
        return view;
    }

    private void setSpinner(){
        AndroidNetworking.get(NewsApi.getPeriodosUrl())
                .setTag(TAG)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        periodos = Periodo.from(response);
                        setPeriodos();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, String.format("anError: %s", anError.getErrorDetail()));
                    }
                });
    }

    private void setPeriodos(){
        spinner = (Spinner) DeudasFragment.this.getView().findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        list.add("Todos");
        if(periodos != null && periodos.size() > 0){
            for(Periodo periodo: periodos){
                list.add(periodo.getDescripcion());
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(DeudasFragment.this.getActivity(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idPeriodo = "";
                if(spinner.getSelectedItem().equals("Todos")){
                    updateData();
                }else{
                    for(Periodo periodo: periodos){
                        if(periodo.getDescripcion().equals(spinner.getSelectedItem())){
                            idPeriodo = periodo.getIdPeriodo()+"";
                            updateData();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.v("NothingSelected Item",
                        "" + spinner.getSelectedItem());
            }
        });
    }

    private void updateData() {

        /*TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String[] mPermission = {Manifest.permission.READ_PHONE_STATE};
        if ( ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this.getActivity(), mPermission, 2 );
            //return;
        }
        String numTel = tm.getLine1Number();*/

        String numTel = Usuario.getNumeroTelefono();
        Log.d(TAG, String.format("Número de teléfono : ",numTel));
        AndroidNetworking.get(NewsApi.getDeudasUrl())
                .addQueryParameter("tel", numTel)
                .addQueryParameter("per", ""+idPeriodo)
                .setTag(TAG)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, String.format("Response : ",response));
                        try {
                            if("error".equalsIgnoreCase(response.getString("status"))) {
                                Log.d(TAG, String.format("Response Error: %s",
                                        response.getString("message")));
                                return;
                            }
                            deudas = Deuda.from(response.getJSONArray("deudas"));
                            deudasAdapter.setDeudas(deudas);
                            deudasAdapter.notifyDataSetChanged();
                            updateFilters();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, String.format("anError: %s", anError.getErrorDetail()));
                    }
                });
    }

    private void updateFilters() {
        textDepartamento.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.INVISIBLE);
        if(deudas != null && deudas.size() > 0){
            if(deudas.get(0).getPerfil() == Constantes.INQUILINO){
                textDepartamento.setText(deudas.get(0).getDepartamento());
                textDepartamento.setVisibility(View.VISIBLE);
            }else{
                spinner.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateLayout(newConfig);
    }

    private int getSpanCount(Configuration configuration) {
        return configuration.orientation == ORIENTATION_PORTRAIT ? 1 : 1;
    }

    private void updateLayout(Configuration configuration) {
        ((GridLayoutManager) deudasLayoutManager)
                .setSpanCount(getSpanCount(configuration));
    }

}
