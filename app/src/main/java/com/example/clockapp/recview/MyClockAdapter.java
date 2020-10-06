package com.example.clockapp.recview;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clockapp.R;
import com.example.clockapp.models.ClockDetails;
import com.example.clockapp.models.User;

import java.util.ArrayList;
import java.util.List;

public class MyClockAdapter extends RecyclerView.Adapter<MyClockAdapter.ClockViewHolder> {

    List<ClockDetails> mItems = new ArrayList<>();
//    private final Handler mHandler = new Handler();

    @NonNull
    @Override
    public ClockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClockViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerrlist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClockViewHolder holder, int position) {
        ClockDetails model = mItems.get(position);
        holder.timeIn.setText(model.getTimeIn());
        holder.timeOut.setText(model.getTimeOut());
        holder.location.setText(model.latitude + ", " + model.longitude );
        holder.details.setText(model.status.toString());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setItems(List<ClockDetails> items){
        this.mItems.clear();
        this.mItems.addAll(items);
        notifyDataSetChanged();
    }


    class ClockViewHolder extends RecyclerView.ViewHolder {


        TextView timeIn;
        TextView timeOut;
        TextView location;
        ImageView caution;
        TextView details;

        public ClockViewHolder(View itemView) {
            super(itemView);
            timeIn = itemView.findViewById(R.id.time_in);
            timeOut = itemView.findViewById(R.id.time_out);
            location = itemView.findViewById(R.id.location_item);
            caution = itemView.findViewById(R.id.caution_img);
            details = itemView.findViewById(R.id.details_item);
        }
    }
}
