package com.manoj.recyclerviewtest.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.manoj.recyclerviewtest.R;
import com.manoj.recyclerviewtest.api.pojo.SpecificaData;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.manoj.recyclerviewtest.api.api.imageurl;

public class recyclerViewAdapter extends RecyclerView.Adapter<recyclerViewAdapter.viewHolder>{
    //model and interface
    public List<SpecificaData> recyclerModel;
    public recyclerAdapter recyclerAdapter;
    //step 1
    public recyclerViewAdapter(List<SpecificaData> recyclerModel,recyclerAdapter recyclerAdapter) {
        this.recyclerAdapter=recyclerAdapter;
        this.recyclerModel=recyclerModel;
    }
    ////step 2
    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public recyclerAdapter recyclerAdapter;

        TextView names,email;
        Context context;
        LinearLayout layout;
        CircleImageView circleImageView;
        public viewHolder(@NonNull View itemView, recyclerAdapter recyclerAdapter) {
            super(itemView);
            this.recyclerAdapter=recyclerAdapter;
            itemView.setOnClickListener(this);

            //initializtions of the UI like textview etc

            context=itemView.getContext();
            names = itemView.findViewById(R.id.tv_user_name_level);
            email = itemView.findViewById(R.id.tv_email);
            circleImageView=itemView.findViewById(R.id.user_profile_pic);
        }

        @Override
        public void onClick(View view) {
            recyclerAdapter.onitemclick(view,getAdapterPosition());
        }
    }

    //below are the implementations of recycler view
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //create views here like layout initializtions only and retrurn the layout view
        //retrun viewholder with params as view and interface
        // .inflate 3rd parameter should be false
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.name_list_row,viewGroup,false);

        return new viewHolder(view,recyclerAdapter);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
        //here  binding the data to the UI like textview etc.

        viewHolder.names.setText(recyclerModel.get(i).getFirstName()+" "+recyclerModel.get(i).getLastName());
        viewHolder.email.setText(String.valueOf(recyclerModel.get(i).getEmail()));
        Glide.with(viewHolder.context).load(recyclerModel.get(i).getAvatar()).into(viewHolder.circleImageView);
    }

    @Override
    public int getItemCount() {
        return recyclerModel.size();
    }




}
