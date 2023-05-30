package com.example.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private Context context;
    private ArrayList<weathermodel> weatherlist;

    public WeatherAdapter(Context context, ArrayList<weathermodel> weatherlist) {
        this.context = context;
        this.weatherlist = weatherlist;
    }

    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.forecast_rv,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.ViewHolder holder, int position) {
        weathermodel model=weatherlist.get(position);
        holder.idtemp.setText(model.getTemperature()+"°C");
        holder.idwind.setText(model.getWind()+"km/h");
        SimpleDateFormat date=new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat outdate=new SimpleDateFormat("hh:mm");
        try{
            Date d=date.parse(model.getTime());
            holder.idtime.setText(outdate.format(d));
        }catch (ParseException e){
            e.printStackTrace();
        }

        Picasso.get().load("http:".concat(model.getIcon())).into(holder.idicon);
    }

    @Override
    public int getItemCount() {
        return weatherlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView idtemp,idwind,idtime;
        ImageView idicon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idtemp=itemView.findViewById(R.id.idtemp);
            idwind=itemView.findViewById(R.id.idwind);
            idicon=itemView.findViewById(R.id.idicon);
            idtime=itemView.findViewById(R.id.idtime);
        }
    }
}
