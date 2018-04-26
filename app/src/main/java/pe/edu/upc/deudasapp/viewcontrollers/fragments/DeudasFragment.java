package pe.edu.upc.deudasapp.viewcontrollers.fragments;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.deudasapp.R;
import pe.edu.upc.deudasapp.models.Deuda;
import pe.edu.upc.deudasapp.network.NewsApi;
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
        deudasLayoutManager = new GridLayoutManager(view.getContext(),
                getSpanCount(getResources().getConfiguration()));
        deudasRecyclerView.setAdapter(deudasAdapter);
        deudasRecyclerView.setLayoutManager(deudasLayoutManager);
        updateData(view.getContext());
        return view;
    }

    private void updateData(Context context) {

        Log.d(TAG, " //////////////***** "+context);
        TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        Log.d(TAG, " ////////////// " + tm);
        if ( ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, " ////////////// ");
            String number = tm.getLine1Number();
            Log.d(TAG, "************* : "+number);
            return;
        }
        String number = tm.getLine1Number();
        Log.d(TAG, "************* : "+number);

        AndroidNetworking.get(NewsApi.getDeudasUrl())
                .addQueryParameter("tel", "456")
                .setTag(TAG)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if("error".equalsIgnoreCase(response.getString("status"))) {
                                Log.d(TAG, String.format("Response Error: %s",
                                        response.getString("message")));
                                return;
                            }
                            deudas = Deuda.from(response.getJSONArray("deudas"));
                            deudasAdapter.setDeudas(deudas);
                            deudasAdapter.notifyDataSetChanged();

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
