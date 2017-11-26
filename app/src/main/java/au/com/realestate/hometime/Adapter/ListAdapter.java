package au.com.realestate.hometime.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import au.com.realestate.hometime.R;
import au.com.realestate.hometime.models.Tram;


/**
 * Created by vaishali on 22/11/17.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private List<Tram> tramList;
    private Context context;

    public ListAdapter(List<Tram> tramList, Context context) {
        this.tramList = tramList;
        this.context = context;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list, parent, false);


        return new ListViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        final Tram tram = tramList.get(position);
        holder.txtDestination.setText(tram.getDestination());
        holder.txtDate.setText(dateFromDotNetDate(tram.getPredictedArrival()));
    }

    /////////////
    // Convert .NET Date to String
    ////////////
    private String dateFromDotNetDate(String dotNetDate) {

        int startIndex = dotNetDate.indexOf("(") + 1;
        int endIndex = dotNetDate.indexOf("+");
        String date = dotNetDate.substring(startIndex, endIndex);

        Long unixTime = Long.parseLong(date);

        SimpleDateFormat dateformate = new SimpleDateFormat("dd-MMM-yyyy h:mm a");
        String datestr = "";
        datestr = dateformate.format(new Date(unixTime));
        return datestr;
    }

    @Override
    public int getItemCount() {
        return tramList.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDestination, txtDate;

        public ListViewHolder(View view) {
            super(view);
            txtDestination = (TextView) view.findViewById(R.id.txt_destination);
            txtDate = (TextView) view.findViewById(R.id.txt_date);
        }
    }
}
