package pe.edu.upc.deudasapp.viewcontrollers.adapters;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.widget.ANImageView;

import java.util.List;

import pe.edu.upc.deudasapp.R;
import pe.edu.upc.deudasapp.models.Deuda;
import pe.edu.upc.deudasapp.util.Constantes;
import pe.edu.upc.deudasapp.viewcontrollers.activities.DeudaActivity;
import pe.edu.upc.deudasapp.viewcontrollers.fragments.DeudaFragment;

/**
 * Created by User on 24/04/2018.
 */

public class DeudasAdapter extends RecyclerView.Adapter<DeudasAdapter.ViewHolder> {
    private List<Deuda> deudas;

    public DeudasAdapter(List<Deuda> deudas) {this.deudas = deudas;}

    public DeudasAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_deuda, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.updateViews(deudas.get(position));
    }

    @Override
    public int getItemCount() {
        return deudas.size();
    }

    public List<Deuda> getDeudas() {
        return deudas;
    }

    public DeudasAdapter setDeudas(List<Deuda> deudas) {
        this.deudas = deudas;
        return this;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView departamentoTextView;
        private TextView periodoTextView;
        private TextView montoTextView;
        private TextView estadoTextView;
        private ConstraintLayout deudaLayout;

        public ViewHolder(View view) {
            super(view);
            departamentoTextView = (TextView) view.findViewById(R.id.text_departamento);
            periodoTextView = (TextView) view.findViewById(R.id.text_periodo);
            montoTextView = (TextView) view.findViewById(R.id.text_monto);
            estadoTextView = (TextView) view.findViewById(R.id.text_estado);
            deudaLayout = (ConstraintLayout) view.findViewById(R.id.layout_deuda);
        }

        public void updateViews(final Deuda deuda) {
            departamentoTextView.setText(deuda.getDepartamento());
            periodoTextView.setText(deuda.getPeriodo());
            montoTextView.setText(String.valueOf(deuda.getMonto()));
            estadoTextView.setText(deuda.getEstado());

            if(deuda.getEstado().equals(Constantes.ESTADO_EN_PROCESO)){
                deudaLayout.setBackgroundColor(Color.YELLOW);
            }else if(deuda.getEstado().equals(Constantes.ESTADO_PAGADO)){
                deudaLayout.setBackgroundColor(Color.GREEN);
            } else{
                deudaLayout.setBackgroundColor(Color.RED);
            }

            deudaLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    context.startActivity(new Intent(context,
                            DeudaActivity.class).putExtras(deuda.toBundle()));

                    /*Intent intent = new Intent(v.getContext(), DeudaActivity.class);
                    intent.putExtras(deuda.toBundle());
                    startActivity(intent);*/

                    /*FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();*/

                    /*AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    DeudaFragment myFragment = new DeudaFragment();
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content, myFragment)
                            .addToBackStack(null).commit();*/




                }
            });
        }
    }
}
