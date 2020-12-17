package proingsoftware.Adapters;

import  android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.R;

import proingsoftware.activities.funcionario.ModificarProductoActivity;
import proingsoftware.model.ReclamoFirebase;

import java.util.ArrayList;

public class ReclamoFirebaseAdapter extends RecyclerView.Adapter<ReclamoFirebaseAdapter.MyViewHolder> {

    Context context;
    ArrayList<ReclamoFirebase> reclamos;

    public ReclamoFirebaseAdapter(Context c , ArrayList<ReclamoFirebase> p)
    {
        context = c;
        reclamos = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.reclamo_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.descripcion.setText(reclamos.get(position).getExt());
        holder.nombre.setText(reclamos.get(position).getNombre());
        holder.prodserv.setText(reclamos.get(position).getProducto());
      //  Picasso.get().load(reclamos.get(position).getFoto()).into(holder.fotoRec);
            holder.tarjeta.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view) {
                    context.startActivity(new
                            Intent(context, ModificarProductoActivity.class));	          }
            });
    }

    @Override
    public int getItemCount() {
        return reclamos.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView nombre, descripcion, prodserv;
        LinearLayout tarjeta;
      //  ImageView fotoRec;
        public MyViewHolder(View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.nombreRec);
            descripcion = (TextView) itemView.findViewById(R.id.descripcionRec);
            prodserv= (TextView) itemView.findViewById(R.id.prodservRec);
         //   fotoRec = (ImageView) itemView.findViewById(R.id.fotoRec);
            tarjeta= (LinearLayout)itemView.findViewById(R.id.tarjetaReclamo);
         }
    }
}