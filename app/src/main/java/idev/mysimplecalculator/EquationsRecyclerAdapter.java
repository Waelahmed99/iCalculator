package idev.mysimplecalculator;

import android.content.ClipData;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class EquationsRecyclerAdapter extends RecyclerView.Adapter<EquationsRecyclerAdapter.PlaceHolder> {

    private List<EquationsModel> models;
    private int resourceID;

    public EquationsRecyclerAdapter(List<EquationsModel> list, int resource) {
        models = list;
        resourceID = resource;
    }

    @NonNull
    @Override
    public PlaceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(resourceID, viewGroup, false);
        return new PlaceHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceHolder placeHolder, int position) {
        EquationsModel model = models.get(position);
        placeHolder.equationView.setText(model.equation);
        placeHolder.dateView.setText(model.date);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public void removeItem(int position) {
        models.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(EquationsModel item, int position) {
        models.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public class PlaceHolder extends RecyclerView.ViewHolder{
        TextView equationView;
        TextView dateView;
        CardView viewForeground;

        public PlaceHolder(@NonNull View itemView) {
            super(itemView);
            equationView = itemView.findViewById(R.id.row_equation);
            dateView = itemView.findViewById(R.id.row_date);
            viewForeground = itemView.findViewById(R.id.viewForeground);
        }
    }
}
