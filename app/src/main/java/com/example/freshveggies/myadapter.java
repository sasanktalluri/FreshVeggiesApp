package com.example.freshveggies;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.freshveggies.Model.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class myadapter extends FirebaseRecyclerAdapter<Product,myadapter.myviewholder>
{
    Context context;
    public myadapter(@NonNull FirebaseRecyclerOptions<Product> options, Context context) {
        super(options);
        this.context=(context);
    }

    @Override
    protected void onBindViewHolder(@NonNull myadapter.myviewholder holder,final int position, @NonNull final Product model) {

        holder.bproductName.setText(model.getPname());
        holder.bPrice.setText("OrgPrice: "+model.getPrice()+"/-");
        holder.bPluckedDate.setText("Plucked Date: "+model.getPdate());
        holder.bquantity.setText("Quantity Avail: "+model.getQuantity()+"kg/dozen");
        holder.bphone.setText("Phn no:"+model.getInfo());
        holder.bfname.setText("Farmer's name:"+model.getFname());


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        String saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        String saveCurrentTime = currentTime.format(calendar.getTime());
        String CurTime = saveCurrentDate +" "+ saveCurrentTime;
        String lastTime=model.getId();
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
        try {
            Date d1=sdf.parse(lastTime);
            Date d2=sdf.parse(CurTime);
            long diff=d2.getTime()-d1.getTime();
            long k=diff/(1000*60*60*6);
            long s1=Long.parseLong(model.getPrice())-k;
            String s2=String.valueOf(s1);
            holder.bcPrice.setText("CurPrice: "+s2+"/-");

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Picasso.get().load(model.getImage()).into(holder.bimage);
        String s=model.getPid();
        holder.bedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                View view=LayoutInflater.from(context).inflate(R.layout.check_pop,null);
                builder.setTitle("Edit the Product")
                        .setMessage("Enter Your Password")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText check=view.findViewById(R.id.check);
                                if(check.getText().toString().isEmpty()){
                                    check.setError("Required Field");
                                    return;
                                }
                                if(check.getText().toString().equals(s)) {

                                    final DialogPlus dialogPlus = DialogPlus.newDialog(holder.bphone.getContext())
                                            .setContentHolder(new ViewHolder(R.layout.dialogcontent))
                                            .setExpanded(true, 1300)
                                            .create();

                                    View myview = dialogPlus.getHolderView();
                                    EditText uname = myview.findViewById(R.id.uname);
                                    EditText ufname = myview.findViewById(R.id.ufname);
                                    EditText uqua = myview.findViewById(R.id.uqua);
                                    EditText uphone = myview.findViewById(R.id.uphone);
                                    Button upbtn = myview.findViewById(R.id.upbtn);

                                    uname.setText(model.getPname());
                                    ufname.setText(model.getFname());
                                    uqua.setText(model.getQuantity());
                                    uphone.setText(model.getInfo());
                                    dialogPlus.show();
                                    upbtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Map<String, Object> map = new HashMap<>();
                                            map.put("pname", uname.getText().toString());
                                            map.put("fname", ufname.getText().toString());
                                            map.put("quantity", uqua.getText().toString());
                                            map.put("info", uphone.getText().toString());
                                            FirebaseDatabase.getInstance().getReference().child("Products")
                                                    .child(getRef(position).getKey()).updateChildren(map)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            dialogPlus.dismiss();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    dialogPlus.dismiss();
                                                }
                                            });
                                        }
                                    });
                                }
                                else{
                                    Toast.makeText(context, "Your Password is incorrect for this product", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("Cancel",null)
                        .setView(view)
                        .create().show();
            }
        });


        holder.bdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                View view=LayoutInflater.from(context).inflate(R.layout.check_pop,null);
                builder.setTitle("Want to delete the Product")
                        .setMessage("Enter Your Password")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText check = view.findViewById(R.id.check);
                                if (check.getText().toString().isEmpty()) {
                                    check.setError("Required Field");
                                    return;
                                }
                                if (check.getText().toString().equals(s)) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.bPrice.getContext());
                                    builder.setTitle("Delete Panel");
                                    builder.setMessage("Delete...?");
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            FirebaseDatabase.getInstance().getReference().child("Products").child(getRef(position).getKey()).removeValue();
                                        }
                                    });
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    builder.show();
                                }
                                else{
                                    Toast.makeText(context, "Your Password is incorrect for this product", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("cancel",null)
                        .setView(view).create().show();
            }
        });
    }
    @Override

    @NonNull
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.edit,parent,false);
        return new myviewholder(view);

    }


    class myviewholder extends RecyclerView.ViewHolder{
        TextView bproductName,bPluckedDate,bPrice,bquantity,bphone,bfname,bcPrice;
        ImageView bimage;
        ImageButton bedit,bdelete;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            bphone=itemView.findViewById(R.id.ephone);
            bquantity=itemView.findViewById(R.id.equantity);
            bPrice=itemView.findViewById(R.id.eprice);
            bPluckedDate=itemView.findViewById(R.id.epluckedDate);
            bproductName=itemView.findViewById(R.id.eproduct_name);
            bimage=itemView.findViewById(R.id.eimage);
            bfname=itemView.findViewById(R.id.efname);
            bedit=itemView.findViewById(R.id.bedit);
            bdelete=itemView.findViewById(R.id.bdelete);
            bcPrice=itemView.findViewById(R.id.ecprice);


        }
    }
}
