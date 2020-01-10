package trap7.menglin.filterme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<String> values;
    private String username;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView nameTxt, msgTxt;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            nameTxt = (TextView) v.findViewById(R.id.message_name);
            msgTxt = (TextView) v.findViewById(R.id.message_body);
        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public ChatAdapter(List<String> myDataset, String username) {
        values = myDataset;
        this.username = username;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(viewType == 0 ? R.layout.item_message_sent : R.layout.item_message_recieved, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public int getItemViewType(int position){
        return values.get(position).substring(0, values.get(position).indexOf(':')).equals(username) ? 0 : 1;
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        String nameAndMessage = values.get(position);
        String[] s = {nameAndMessage.substring(0, nameAndMessage.indexOf(':')), nameAndMessage.substring(nameAndMessage.indexOf(':') + 1)};
        if (holder.nameTxt != null)
            holder.nameTxt.setText(s[0]);
        holder.msgTxt.setText(s[1]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }

}