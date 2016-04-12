package fr.pchab.androidrtc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gumiMinh on 4/11/16.
 */
public class HistoryAdapter extends ArrayAdapter<HistoryItem> {
    private final Context context;
    private LayoutInflater inflater;
    private List<HistoryItem> values;
    private Map<String, HistoryItem> users;
    private ArrayList<HistoryItem> items;
    private ArrayList<HistoryItem> itemsAll;
    private ArrayList<HistoryItem> suggestions;

    public HistoryAdapter(Context context, ArrayList<HistoryItem> values) {
        super(context, R.layout.history_row_layout, android.R.id.text1, values);
        this.context  = context;
        this.inflater = LayoutInflater.from(context);
        this.values   = values;
        this.users    = new HashMap<String, HistoryItem>();
        this.items = values;
        this.itemsAll = (ArrayList<HistoryItem>) items.clone();
        this.suggestions = new ArrayList<HistoryItem>();
    }

    class ViewHolder {
        TextView user;
        TextView    status;
        TextView    id;
        ImageButton callBtn;
        HistoryItem histItem;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final HistoryItem hItem = this.values.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView    = inflater.inflate(R.layout.history_row_layout, parent, false);
            holder.user    = (TextView) convertView.findViewById(R.id.history_name);
            holder.status  = (TextView) convertView.findViewById(R.id.history_status);
            holder.id    = (TextView) convertView.findViewById(R.id.history_time);
            holder.callBtn = (ImageButton) convertView.findViewById(R.id.history_call);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.user.setText(hItem.getUserName());
        holder.id.setText(hItem.getUserId());
        holder.status.setText(hItem.getStatus());
//        if (hItem.getStatus().equals("Offline"))
//            getUserStatus(hItem, holder.status);
        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)v.getContext()).makeCall(holder.id.getText().toString());
            }
        });
        holder.histItem=hItem;
        return convertView;
    }

    @Override
    public int getCount() {
        return this.values.size();
    }

    public void removeButton(int loc){
        this.values.remove(loc);
        notifyDataSetChanged();
    }

//    @Override
//    public Filter getFilter() {
//        return nameFilter;
//    }
//
//    Filter nameFilter = new Filter() {
//        @Override
//        public String convertResultToString(Object resultValue) {
//            String str = ((HistoryItem)(resultValue)).getUserName();
//            return str;
//        }
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            if(constraint != null) {
//                suggestions.clear();
//                Log.d("minhminh","come here");
//                for (HistoryItem customer : itemsAll) {
//                    if(customer.getUserName().toLowerCase().startsWith(constraint.toString().toLowerCase())){
//                        suggestions.add(customer);
//                        Log.d("minhminh", "come here");
//                    }
//                }
//                FilterResults filterResults = new FilterResults();
//                filterResults.values = suggestions;
//                filterResults.count = suggestions.size();
//                return filterResults;
//            } else {
//                return new FilterResults();
//            }
//        }
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            ArrayList<HistoryItem> filteredList = (ArrayList<HistoryItem>) results.values;
//            if(results != null && results.count > 0) {
//                clear();
//                for (HistoryItem c : filteredList) {
//                    add(c);
//                }
//                notifyDataSetChanged();
//            }
//        }
//    };

//    private void getUserStatus(final ChatUser user, final TextView statusView){
//        String stdByUser = user.getUserId();
        //user status
//        this.mPubNub.getState(stdByUser, user.getUserId(), new Callback() {
//            @Override
//            public void successCallback(String channel, Object message) {
//                JSONObject jsonMsg = (JSONObject) message;
//                try {
//                    if (!jsonMsg.has(Constants.JSON_STATUS)) return;
//                    final String status = jsonMsg.getString(Constants.JSON_STATUS);
//                    user.setStatus(status);
//                    ((Activity)getContext()).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            statusView.setText(status);
//                        }
//                    });
//                } catch (JSONException e){
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    /**
     * Format the long System.currentTimeMillis() to a better looking timestamp. Uses a calendar
     *   object to format with the user's current time zone.
     * @param timeStamp
     * @return
     */
//    public static String formatTimeStamp(long timeStamp){
//        // Create a DateFormatter object for displaying date in specified format.
//        SimpleDateFormat formatter = new SimpleDateFormat("MMM d, h:mm a");
//
//        // Create a calendar object that will convert the date and time value in milliseconds to date.
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(timeStamp);
//        return formatter.format(calendar.getTime());
//    }
}

