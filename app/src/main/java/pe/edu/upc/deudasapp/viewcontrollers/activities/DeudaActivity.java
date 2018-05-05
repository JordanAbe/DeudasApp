package pe.edu.upc.deudasapp.viewcontrollers.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.widget.ANImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.deudasapp.R;
import pe.edu.upc.deudasapp.models.Deuda;
import pe.edu.upc.deudasapp.models.DeudaDetalle;
import pe.edu.upc.deudasapp.network.NewsApi;
import pe.edu.upc.deudasapp.viewcontrollers.adapters.DeudaDetalleAdapter;
import pe.edu.upc.deudasapp.viewcontrollers.adapters.DeudasAdapter;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class DeudaActivity extends AppCompatActivity {
 
    private static String TAG = "DeudasApp";
    private TextView departamentoTextView;
    private TextView periodoTextView;
    private TextView montoTextView;
    private TextView perfilTextView;

    private RecyclerView deudaDetallesRecyclerView;
    private DeudaDetalleAdapter deudaDetallesAdapter;
    private RecyclerView.LayoutManager deudaDetallesLayoutManager;
    private List<DeudaDetalle> detalles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = super.getApplicationContext();

        setContentView(R.layout.activity_deuda);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        if(intent == null) return;
        departamentoTextView = (TextView) findViewById(R.id.text_departamento);
        periodoTextView = (TextView) findViewById(R.id.text_periodo);
        montoTextView = (TextView) findViewById(R.id.text_monto);
        perfilTextView = (TextView) findViewById(R.id.text_perfil);

        int idDeuda = Deuda.from(intent.getExtras());
        setDeuda(idDeuda, context);
    }

    private void setDeuda(int idDeuda, final Context context) {
        String numTel = "123";
        AndroidNetworking.get(NewsApi.getDeudaUrl())
            .addQueryParameter("tel", numTel)
            .addQueryParameter("id", ""+idDeuda)
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
                        List<Deuda> deudas = Deuda.from(response.getJSONArray("deudas"));
                        updateViews(deudas.get(0), context);
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

    private void updateViews(Deuda deuda, Context context ) {
        departamentoTextView.setText(deuda.getDepartamento());
        periodoTextView.setText(deuda.getPeriodo());
        montoTextView.setText(String.valueOf(deuda.getMonto()));
        perfilTextView.setText(deuda.getPeriodo());

        deudaDetallesRecyclerView = (RecyclerView) findViewById(R.id.recycler_detalles);
        detalles = new ArrayList<>();
        deudaDetallesAdapter = new DeudaDetalleAdapter(detalles);
        deudaDetallesLayoutManager = new GridLayoutManager(context, getSpanCount(getResources().getConfiguration()));
        deudaDetallesRecyclerView.setAdapter(deudaDetallesAdapter);
        deudaDetallesRecyclerView.setLayoutManager(deudaDetallesLayoutManager);

        deudaDetallesAdapter.setDeudaDetalles(deuda.getDetalle());
        deudaDetallesAdapter.notifyDataSetChanged();

    }

    private int getSpanCount(Configuration configuration) {
        return configuration.orientation == ORIENTATION_PORTRAIT ? 1 : 1;
    }

    private void updateLayout(Configuration configuration) {
        ((GridLayoutManager) deudaDetallesLayoutManager)
                .setSpanCount(getSpanCount(configuration));
    }

}
