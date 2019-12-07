package com.example.harmit.swooshcar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter1 extends RecyclerView.Adapter<RecyclerViewAdapter1.ViewHolder> {

    String rtime, rprice;

    Context context;
    List<GetDataAdapter1> getDataAdapter;

    public RecyclerViewAdapter1(List<GetDataAdapter1> getDataAdapter, Context context) {

        super();

        this.getDataAdapter = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items1, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        GetDataAdapter1 getDataAdapter1 = getDataAdapter.get(position);

        holder.DriverNameTextView.setText(getDataAdapter1.getDriver_name());

        holder.PickupTextView.setText(getDataAdapter1.getPickup());

        holder.DestinationTextView.setText(getDataAdapter1.getDestination());

        holder.RideIdTextView.setText(getDataAdapter1.getRideid());

        String sqldate = getDataAdapter1.getDeparturedate();

        String day,month,year;
        String[] date;
        date = sqldate.split("-");

        day = date[2];
        month = date[1];
        year = date[0];

        StringBuilder date1 = new StringBuilder().append(day).append("-").append(month).append("-").append(year);
        holder.DepartureDateTextView.setText(date1.toString());


        rtime = getDataAdapter1.getDeparturetime();

        rprice = getDataAdapter1.getPrice();


        try {

            Picasso.with(context).load(getDataAdapter1.getDriverimage()).placeholder(R.drawable.ic_person_black_24dp).error(R.drawable.profile1).into(holder.ProfileImageView);
        } catch (Exception e) {
            e.printStackTrace();
            Picasso.with(context).load(R.drawable.ic_launcher_background).placeholder(R.drawable.ic_directions_car_black_24dp).into(holder.ProfileImageView);
        }


    }

    @Override
    public int getItemCount() {

        return getDataAdapter.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public CardView CardView;
        public TextView DriverNameTextView;
        public TextView DepartureDateTextView;
        public TextView PickupTextView;
        public TextView DestinationTextView;
        public TextView RideIdTextView;

        public de.hdodenhof.circleimageview.CircleImageView ProfileImageView;

        public ViewHolder(View itemView) {

            super(itemView);

            CardView = itemView.findViewById(R.id.cardview1);
            DriverNameTextView = itemView.findViewById(R.id.rdrivernametv1);
            RideIdTextView = itemView.findViewById(R.id.rrideidtv1);
            PickupTextView = itemView.findViewById(R.id.rpickuptv1);
            DestinationTextView = itemView.findViewById(R.id.rdestinationtv1);

            DepartureDateTextView = itemView.findViewById(R.id.rdatetv1);
            ProfileImageView = itemView.findViewById(R.id.profile_image1);

            CardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), BookedRideDetail.class);

                    intent.putExtra("brpickup", PickupTextView.getText().toString());
                    intent.putExtra("brdestination", DestinationTextView.getText().toString());
                    intent.putExtra("brdate", DepartureDateTextView.getText().toString());
                    intent.putExtra("brtime", rtime);
                    intent.putExtra("brdrivername", DriverNameTextView.getText().toString());
                    intent.putExtra("brrideid", RideIdTextView.getText().toString());
                    intent.putExtra("brprice", rprice + " Rs");

                    v.getContext().startActivity(intent);
                }
            });

        }
    }
}