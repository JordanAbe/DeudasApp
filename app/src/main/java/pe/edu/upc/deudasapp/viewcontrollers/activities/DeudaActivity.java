package pe.edu.upc.deudasapp.viewcontrollers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.androidnetworking.widget.ANImageView;

import pe.edu.upc.deudasapp.R;
import pe.edu.upc.deudasapp.models.Deuda;

public class DeudaActivity extends AppCompatActivity {

    private TextView departamentoTextView;
    private TextView periodoTextView;
    private TextView montoTextView;
    private Deuda deuda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deuda);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        if(intent == null) return;
        departamentoTextView = (TextView) findViewById(R.id.text_departamento);
        periodoTextView = (TextView) findViewById(R.id.text_periodo);
        montoTextView = (TextView) findViewById(R.id.text_monto);
        deuda = Deuda.from(intent.getExtras());
        updateViews(deuda);
    }

    private void updateViews(Deuda deuda) {
        departamentoTextView.setText(deuda.getDepartamento());
        periodoTextView.setText(deuda.getPeriodo());
        montoTextView.setText(String.valueOf(deuda.getMonto()));
    }

}
