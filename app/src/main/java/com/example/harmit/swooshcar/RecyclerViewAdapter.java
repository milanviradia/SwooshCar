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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    String rtime, rseats, rpaymenttype, rdistance, rduration, rprice;

    Context context;
    List<GetDataAdapter> getDataAdapter;

    public RecyclerViewAdapter(List<GetDataAdapter> getDataAdapter, Context context) {

        super();

        this.getDataAdapter = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        GetDataAdapter getDataAdapter1 = getDataAdapter.get(position);

        holder.DriverNameTextView.setText(getDataAdapter1.getDrivername());

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

        rpaymenttype = getDataAdapter1.getPaymenttype();

        rdistance = getDataAdapter1.getDistance();

        rduration = getDataAdapter1.getDuration();

        rseats = getDataAdapter1.getSeats();

        rprice = getDataAdapter1.getPrice();

        try {

            Picasso.with(context).load(getDataAdapter1.getProfileimage()).placeholder(R.drawable.ic_person_black_24dp).error(R.drawable.profile1).into(holder.ProfileImageView);
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

        public TextView DriverNameTextView;
        public TextView DepartureDateTextView;
        public TextView PickupTextView;
        public TextView DestinationTextView;
        public TextView RideIdTextView;
        public CardView CardView;

        public de.hdodenhof.circleimageview.CircleImageView ProfileImageView;

        public ViewHolder(View itemView) {

            super(itemView);

            DriverNameTextView = itemView.findViewById(R.id.rdrivernametv);
            RideIdTextView = itemView.findViewById(R.id.rrideidtv);
            PickupTextView = itemView.findViewById(R.id.rpickuptv);
            DestinationTextView = itemView.findViewById(R.id.rdestinationtv);

            DepartureDateTextView = itemView.findViewById(R.id.rdatetv);
            ProfileImageView = itemView.findViewById(R.id.profile_image);

            CardView = itemView.findViewById(R.id.cardview);

            CardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), RideDetail.class);

                    intent.putExtra("rpickup", PickupTextView.getText().toString());
                    intent.putExtra("rdestination", DestinationTextView.getText().toString());
                    intent.putExtra("rdate", DepartureDateTextView.getText().toString());
                    intent.putExtra("rtime", rtime);
                    intent.putExtra("rdrivername", DriverNameTextView.getText().toString());
                    intent.putExtra("rrideid", RideIdTextView.getText().toString());
                    intent.putExtra("rpaymenttype", rpaymenttype);
                    intent.putExtra("rdistance", rdistance + " Km");
                    intent.putExtra("rduration", rduration);
                    intent.putExtra("rseats", rseats);
                    intent.putExtra("rprice", rprice + " Rs");

                    v.getContext().startActivity(intent);
                }
            });

        }
    }
}