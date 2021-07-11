package com.example.freshveggies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.freshveggies.Model.Product;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;


public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>implements Filterable {
    ArrayList<Product>list;
    ArrayList<Product>backup;
    Context context;
    public Adapter(ArrayList<Product> list, Context context) {
        this.list = list;
        this.context=(context);
        backup=new ArrayList<>(list);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_buying,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  Adapter.MyViewHolder holder, int position) {
        holder.bproductName.setText(list.get(position).getPname());
        holder.bPluckedDate.setText("Plucked Date: "+list.get(position).getPdate());
        holder.bquantity.setText("Quantity Avail: "+list.get(position).getQuantity()+"kg/dozen");
        holder.bphone.setText("Phn no:"+list.get(position).getInfo());
        holder.bfname.setText("Farmer's name:"+list.get(position).getFname());

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        String saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        String saveCurrentTime = currentTime.format(calendar.getTime());
        String CurTime = saveCurrentDate +" "+ saveCurrentTime;
        String lastTime=list.get(position).getId();
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
        try {
            Date d1=sdf.parse(lastTime);
            Date d2=sdf.parse(CurTime);
            long diff=d2.getTime()-d1.getTime();
            long k=diff/(1000*60*60*6);
            long s=Long.parseLong(list.get(position).getPrice())-k;
            String s1=String.valueOf(s);
            holder.bPrice.setText("Product Price: "+s1+"/-");

        } catch (ParseException e) {
            e.printStackTrace();
        }


        Picasso.get().load(list.get(position).getImage()).into(holder.bimage);
        holder.bcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile=list.get(position).getInfo();
                String call="tel:"+mobile.trim();
                if(call.length()>0){
                    Intent intent=new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(call));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter=new Filter() {
        @Override
        //background thread
        protected FilterResults performFiltering(CharSequence keyword) {
            ArrayList<Product>filtereddata=new ArrayList<>();
            if(keyword.toString().isEmpty()){
                filtereddata.addAll(backup);
            }
            else{
                for(Product obj:backup){
                    if(obj.getPname().toString().toLowerCase().contains(keyword.toString().toLowerCase())){
                        filtereddata.add(obj);
                    }
                    if(obj.getFname().toString().toLowerCase().contains(keyword.toString().toLowerCase())){
                        filtereddata.add(obj);
                    }
                    if(obj.getPdate().contains(keyword)){
                        filtereddata.add(obj);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filtereddata;
            return results;
        }

        @Override//main UI thread
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((ArrayList<Product>)results.values);
            notifyDataSetChanged();
        }
    };
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView bproductName,bPluckedDate,bPrice,bquantity,bphone,bfname;
        ImageView bimage;
        ImageButton bcall;
        public MyViewHolder(@NonNull  View itemView) {
            super(itemView);
            bphone=itemView.findViewById(R.id.bphone);
            bquantity=itemView.findViewById(R.id.bquantity);
            bPrice=itemView.findViewById(R.id.bprice);
            bPluckedDate=itemView.findViewById(R.id.bpluckedDate);
            bproductName=itemView.findViewById(R.id.bproduct_name);
            bimage=itemView.findViewById(R.id.bimage);
            bcall=itemView.findViewById(R.id.bcall);
            bfname=itemView.findViewById(R.id.bfname);

        }
    }
}
