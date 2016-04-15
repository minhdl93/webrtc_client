package fr.pchab.androidrtc.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.pchab.androidrtc.MainActivity;
import fr.pchab.androidrtc.Model.User;
import fr.pchab.androidrtc.R;

/**
 * Created by gumiMinh on 4/12/16.
 */
public class UserAdapter extends ArrayAdapter<User> {
    private LayoutInflater inflater;
    private List<User> values;
    private ArrayList<User> items;
    private ArrayList<User> itemsAll;
    private ArrayList<User> suggestions;

    public UserAdapter(Context context, ArrayList<User> values) {
        super(context, R.layout.user_row_layout, android.R.id.text1, values);
        this.inflater = LayoutInflater.from(context);
        this.values = values;
        this.items = values;
        this.itemsAll = (ArrayList<User>) items.clone();
        this.suggestions = new ArrayList<User>();
    }

    class ViewHolder {
        TextView user;
        TextView id;
        ImageButton addBtn;
        User histItem;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final User hItem = this.values.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.user_row_layout, parent, false);
            holder.user = (TextView) convertView.findViewById(R.id.user_name);
            holder.id = (TextView) convertView.findViewById(R.id.user_id);
            holder.addBtn = (ImageButton) convertView.findViewById(R.id.user_add);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.user.setText(hItem.getUserName());
        holder.id.setText(hItem.getUserId());
        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) v.getContext()).addfriend(holder.id.getText().toString(), holder.user.getText().toString());
            }
        });
        holder.histItem = hItem;
        return convertView;
    }

    @Override
    public int getCount() {
        return this.values.size();
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((User) (resultValue)).getUserName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (User customer : itemsAll) {
                    if (customer.getUserName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(customer);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<User> filteredList = (ArrayList<User>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (User c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
}
