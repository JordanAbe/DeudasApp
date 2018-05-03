package pe.edu.upc.deudasapp.viewcontrollers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import pe.edu.upc.deudasapp.R;
import pe.edu.upc.deudasapp.models.Usuario;
import pe.edu.upc.deudasapp.viewcontrollers.fragments.DeudaFragment;
import pe.edu.upc.deudasapp.viewcontrollers.fragments.DeudasFragment;
import pe.edu.upc.deudasapp.viewcontrollers.fragments.HomeFragment;
import pe.edu.upc.deudasapp.viewcontrollers.fragments.SettingFragment;

public class MainActivity extends AppCompatActivity {

    private EditText textNumero;
    private Button buttonRegistrar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            if(!Usuario.existsNumeroTelefono()){
                Intent launchactivity= new Intent(MainActivity.this ,MainActivity.class);
                startActivity(launchactivity);
                return false;
            }
            return navigateTo(item);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean existeNumero = false;
        if (Usuario.existsNumeroTelefono()){
            setMainActivity();
        }else{
            setContentView(R.layout.register_main);
            textNumero = (EditText) findViewById(R.id.text_numero);
            buttonRegistrar = (Button) findViewById(R.id.button_registrar);
            buttonRegistrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String numero = textNumero.getText().toString();
                    Usuario.saveNumeroTelefono(numero);
                    setMainActivity();
                }
            });
        }
    }

    private void setMainActivity(){
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigateTo(navigation.getMenu().findItem(R.id.navigation_home));
    }

    private Fragment getFragmentFor(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home: return new HomeFragment();
            case R.id.navigation_deudas: return new DeudasFragment();
            //case R.id.navigation_deuda: return new DeudaFragment();
            case R.id.navigation_confing: return new SettingFragment();
            default: return new HomeFragment();
        }
    }

    private boolean navigateTo(MenuItem item) {
        item.setChecked(true);
        return getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, getFragmentFor(item))
                .commit() > 0;
    }




}
