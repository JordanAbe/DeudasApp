package pe.edu.upc.deudasapp.viewcontrollers.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pe.edu.upc.deudasapp.R;
import pe.edu.upc.deudasapp.models.DeudaDetalle;
import pe.edu.upc.deudasapp.viewcontrollers.activities.DeudaActivity;

/**
 * Created by User on 29/04/2018.
 */

public class DeudaDetalleAdapter extends RecyclerView.Adapter<DeudaDetalleAdapter.ViewHolder> {
    private List<DeudaDetalle> detalles;

    public DeudaDetalleAdapter(List<DeudaDetalle> detalles) {this.detalles = detalles;}

    public DeudaDetalleAdapter() {
    }

    @NonNull
    @Override
    public DeudaDetalleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_detalle, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DeudaDetalleAdapter.ViewHolder holder, int position) {
        holder.updateViews(detalles.get(position));
    }

    @Override
    public int getItemCount() {
        return detalles.size();
    }

    public List<DeudaDetalle> getItems() {
        return detalles;
    }

    public DeudaDetalleAdapter setDeudaDetalles(List<DeudaDetalle> detalles) {
        this.detalles = detalles;
        return this;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView servicioTextView;
        private TextView montoTextView;
        private ConstraintLayout deudaLayout;

        public ViewHolder(View view) {
            super(view);
            servicioTextView = (TextView) view.findViewById(R.id.text_servicio);
            montoTextView = (TextView) view.findViewById(R.id.text_monto);
            deudaLayout = (ConstraintLayout) view.findViewById(R.id.layout_detalle);
        }

        public void updateViews(final DeudaDetalle detalle) {
            servicioTextView.setText(detalle.getServicio());
            montoTextView.setText(String.valueOf(detalle.getMonto()));
        }
    }
}
