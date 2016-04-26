package fr.pchab.androidrtc.Adapter;

import android.content.Context;
import android.graphics.Color;
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

import fr.pchab.androidrtc.Model.HistoryItem;
import fr.pchab.androidrtc.MainActivity;
import fr.pchab.androidrtc.R;

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
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.values = values;
        this.users = new HashMap<String, HistoryItem>();
        this.items = values;
        this.itemsAll = (ArrayList<HistoryItem>) items.clone();
        this.suggestions = new ArrayList<HistoryItem>();
    }

    class ViewHolder {
        TextView user;
        TextView status;
        TextView id;
        ImageButton callBtn;
        ImageButton browserBtn;
        HistoryItem histItem;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final HistoryItem hItem = this.values.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.history_row_layout, parent, false);
            holder.user = (TextView) convertView.findViewById(R.id.history_name);
            holder.status = (TextView) convertView.findViewById(R.id.history_status);
            holder.id = (TextView) convertView.findViewById(R.id.history_time);
            holder.callBtn = (ImageButton) convertView.findViewById(R.id.history_call);
            holder.browserBtn = (ImageButton) convertView.findViewById(R.id.history_call_browser);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.user.setText(hItem.getUserName());
        holder.id.setText(hItem.getUserId());
        if(hItem.getStatus().equals("Online")){
            holder.status.setTextColor(Color.GREEN);
        }else{
            holder.status.setTextColor(Color.RED);
        }
        holder.status.setText(hItem.getStatus());
        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) v.getContext()).makeCall(holder.id.getText().toString(), holder.status.getText().toString());
            }
        });
//        holder.browserBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((MainActivity) v.getContext()).makeBrowserCall(holder.id.getText().toString(), holder.status.getText().toString());
//            }
//        });
        holder.histItem = hItem;
        return convertView;
    }

    @Override
    public int getCount() {
        return this.values.size();
    }

    public void removeButton(int loc) {
        this.values.remove(loc);
        notifyDataSetChanged();
    }
}

