package pe.edu.upc.deudasapp.viewcontrollers.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import pe.edu.upc.deudasapp.models.DeudaDetalle;
import pe.edu.upc.deudasapp.models.Usuario;
import pe.edu.upc.deudasapp.network.NewsApi;
import pe.edu.upc.deudasapp.util.Constantes;
import pe.edu.upc.deudasapp.viewcontrollers.activities.DeudaActivity;
import pe.edu.upc.deudasapp.viewcontrollers.adapters.DeudaDetalleAdapter;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class DeudaFragment extends Fragment {

    View view;
    private static String TAG = "DeudasApp";
    private TextView departamentoTextView;
    private TextView periodoTextView;
    private TextView montoTextView;
    private TextView estadoTextView;
    private EditText operacionTextEdit;
    private TextView mensajeTextView;
    private LinearLayout linearLayout;

    private Button buttonPagar;
    private Button buttonConfirmar;

    private RecyclerView deudaDetallesRecyclerView;
    private DeudaDetalleAdapter deudaDetallesAdapter;
    private RecyclerView.LayoutManager deudaDetallesLayoutManager;
    private List<DeudaDetalle> detalles;

    private int idDeuda;

    public DeudaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_deuda, container, false);


        Context context = DeudaFragment.this.getContext();

        Intent intent = getActivity().getIntent();
        if(intent == null) return view;

        departamentoTextView = (TextView) view.findViewById(R.id.text_departamento);
        periodoTextView = (TextView) view.findViewById(R.id.text_periodo);
        montoTextView = (TextView) view.findViewById(R.id.text_monto);
        estadoTextView = (TextView) view.findViewById(R.id.text_estado);
        operacionTextEdit = (EditText) view.findViewById(R.id.text_operacion);
        mensajeTextView = (TextView) view.findViewById(R.id.text_mensaje_label);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);

        buttonPagar = (Button) view.findViewById(R.id.button_pagar);
        buttonConfirmar = (Button) view.findViewById(R.id.button_confirmar);

        idDeuda = Deuda.from(intent.getExtras());
        setDeuda(context);

        return view;
    }

    private void setDeuda(final Context context) {
        String numTel = Usuario.getNumeroTelefono();
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

    private void updateViews(Deuda deuda, Context context) {
        departamentoTextView.setText(deuda.getDepartamento());
        periodoTextView.setText(deuda.getPeriodo());
        montoTextView.setText(String.valueOf(deuda.getMonto()));
        estadoTextView.setText(deuda.getEstado());
        operacionTextEdit.setText(deuda.getOperacion());

        if(deuda.getEstado().equals(Constantes.ESTADO_PENDIENTE)){
            linearLayout.setBackgroundColor(Color.RED);
            buttonConfirmar.setVisibility(View.INVISIBLE);
            if(deuda.getPerfil() == Constantes.INQUILINO){
                buttonPagar.setVisibility(View.VISIBLE);
                operacionTextEdit.setEnabled(true);
            }else{
                buttonPagar.setVisibility(View.INVISIBLE);
                operacionTextEdit.setEnabled(false);
            }
        }else if(deuda.getEstado().equals(Constantes.ESTADO_EN_PROCESO)){
            linearLayout.setBackgroundColor(Color.YELLOW);
            buttonPagar.setVisibility(View.INVISIBLE);
            operacionTextEdit.setEnabled(false);
            if(deuda.getPerfil() == Constantes.ADMINISTRADOR){
                buttonConfirmar.setVisibility(View.VISIBLE);
            }else{
                buttonConfirmar.setVisibility(View.INVISIBLE);
            }
        } else {//Pagado
            linearLayout.setBackgroundColor(Color.GREEN);
            buttonPagar.setVisibility(View.INVISIBLE);
            buttonConfirmar.setVisibility(View.INVISIBLE);
            operacionTextEdit.setEnabled(false);
        }
        buttonPagar.setOnClickListener(onClickPagarListener);
        buttonConfirmar.setOnClickListener(onClickConfirmarListener);


        deudaDetallesRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_detalles);
        detalles = new ArrayList<>();
        deudaDetallesAdapter = new DeudaDetalleAdapter(detalles);
        deudaDetallesLayoutManager = new GridLayoutManager(context, getSpanCount(getResources().getConfiguration()));
        deudaDetallesRecyclerView.setAdapter(deudaDetallesAdapter);
        deudaDetallesRecyclerView.setLayoutManager(deudaDetallesLayoutManager);

        deudaDetallesAdapter.setDeudaDetalles(deuda.getDetalle());
        deudaDetallesAdapter.notifyDataSetChanged();

    }

    private View.OnClickListener onClickPagarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(operacionTextEdit.getText().toString().trim().equals("")){
                mensajeTextView.setText("Ingrese numero de operación");
            }else{
                String numTel = Usuario.getNumeroTelefono();
                AndroidNetworking.get(NewsApi.getPagarUrl())
                        .addQueryParameter("id", "" + idDeuda)
                        .addQueryParameter("ope", operacionTextEdit.getText().toString())
                        .addQueryParameter("est", "En proceso")
                        .setTag(TAG)
                        .setPriority(Priority.LOW)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, String.format("Response : ", response));
                                try {
                                    if ("error".equalsIgnoreCase(response.getString("status"))) {
                                        mensajeTextView.setText("ERROR REALIZANDO LA OPERACIÓN");
                                    } else {
                                        Deuda deuda = new Deuda(idDeuda);
                                        Intent intent = new Intent(DeudaFragment.this.getContext(), DeudaFragment.class);
                                        intent.putExtras(deuda.toBundle());
                                        startActivity(intent);
                                    }
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
        }
    };

    private View.OnClickListener onClickConfirmarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String numTel = Usuario.getNumeroTelefono();
            AndroidNetworking.get(NewsApi.getConfirmarUrl())
                    .addQueryParameter("id", ""+idDeuda)
                    .addQueryParameter("est", "Pagado")
                    .setTag(TAG)
                    .setPriority(Priority.LOW)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, String.format("Response : ",response));
                            try {
                                if("error".equalsIgnoreCase(response.getString("status"))) {
                                    mensajeTextView.setText("ERROR REALIZANDO LA OPERACIÓN");
                                }else{
                                    Deuda deuda = new Deuda(idDeuda);
                                    Intent intent = new Intent(DeudaFragment.this.getContext(), DeudaFragment.class);
                                    intent.putExtras(deuda.toBundle());
                                    startActivity(intent);
                                }
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
    };

    private int getSpanCount(Configuration configuration) {
        return configuration.orientation == ORIENTATION_PORTRAIT ? 1 : 1;
    }

    private void updateLayout(Configuration configuration) {
        ((GridLayoutManager) deudaDetallesLayoutManager)
                .setSpanCount(getSpanCount(configuration));
    }


}
