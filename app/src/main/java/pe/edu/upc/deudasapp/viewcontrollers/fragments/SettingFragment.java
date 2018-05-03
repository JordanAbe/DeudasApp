package pe.edu.upc.deudasapp.viewcontrollers.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import pe.edu.upc.deudasapp.R;
import pe.edu.upc.deudasapp.models.Usuario;
import pe.edu.upc.deudasapp.viewcontrollers.activities.MainActivity;


public class SettingFragment extends Fragment {

    private EditText textNumero;
    private Button buttonGrabar;
    private Button buttonEliminar;
    private TextView mensaje;
    LayoutInflater inflater;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        // Inflate the layout for this fragment
        textNumero = (EditText)  view.findViewById(R.id.text_numero);
        mensaje = (TextView) view.findViewById(R.id.text_mensaje_label);
        buttonGrabar = (Button) view.findViewById(R.id.button_grabar);
        buttonEliminar = (Button) view.findViewById(R.id.button_eliminar);
        buttonGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numero = textNumero.getText().toString();
                Usuario.saveNumeroTelefono(numero);
                mensaje.setText("Número de teléfono modificado correctamente");
            }
        });
        buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario.deleteNumeroTelefono();
                Intent launchactivity= new Intent(SettingFragment.this.getActivity(),MainActivity.class);
                startActivity(launchactivity);
            }
        });
        return view;
    }



}
