package com.example.contactdisplay.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactdisplay.R;
import com.example.contactdisplay.RecyclerView.FastScrollRecyclerViewInterface;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> implements FastScrollRecyclerViewInterface, Filterable {
    ArrayList<String> contactList;
    ArrayList<String> selectedContacts = new ArrayList<>();
    ArrayList<String> filteredContactList;
    private int lastSelectedPosition = -1;
    private HashMap<String, Integer> mMapIndex;
    private Context context;

    public ContactAdapter(ArrayList<String> contactList, HashMap<String, Integer> mapIndex, Context context) {
        this.contactList = contactList;
        filteredContactList = contactList;
        this.mMapIndex = mapIndex;
        this.context = context;
    }

    public ArrayList<String> getSelectedContacts() {
        return selectedContacts;
    }


    @Override
    public HashMap<String, Integer> getMapIndex() {
        return this.mMapIndex;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        CheckBox checkBox;
        Button button;

        public ContactViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.contact);
            checkBox = itemView.findViewById(R.id.contact_radio);
            button = itemView.findViewById(R.id.show_data);

            checkBox.setOnClickListener(view ->
            {
                lastSelectedPosition = getAdapterPosition();

                if (checkBox.isChecked()) {
                    selectedContacts.add(filteredContactList.get(lastSelectedPosition));
                } else if (!checkBox.isChecked())
                    selectedContacts.remove(filteredContactList.get(lastSelectedPosition));

                Toast.makeText(context, filteredContactList.get(lastSelectedPosition), Toast.LENGTH_SHORT).show();


            });


        }

        public void setTextView(String text) {
            textView.setText(text);
        }


    }


    @NonNull
    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item,parent,false);

        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ContactViewHolder holder, int position) {
        holder.setTextView(filteredContactList.get(position));
        holder.checkBox.setChecked(lastSelectedPosition == position);

    }

    @Override
    public int getItemCount() {
        return filteredContactList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charSeq = charSequence.toString();

                if (charSeq.isEmpty()) {
                    filteredContactList = contactList;
                } else {
                    ArrayList<String> filteredList = new ArrayList<>();
                    for (String contact : contactList) {
                        if (contact.toLowerCase().contains(charSeq.toLowerCase())) {

                            filteredList.add(contact);
                        }
                    }
                    filteredContactList = filteredList;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredContactList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredContactList = (ArrayList<String>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
