package com.example.contactdisplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;

import com.example.contactdisplay.Adapters.ContactAdapter;
import com.example.contactdisplay.RecyclerView.FastScrollRecyclerViewItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private SearchManager searchManager;
    private SearchView searchView;
    private Button contact_list_btn;
    private HashMap<String, Integer> mapIndex;
    private ContactAdapter contactAdapter;
    private ArrayList<String> StoreContacts;
    private Cursor cursor;
    private String name, phonenumber;
    public static final int RequestPermissionCode = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==RequestPermissionCode)
        {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            StoreContacts = new ArrayList<>();
            getContactsIntoArrayList();
            calculateIndexesForName(StoreContacts);
            contactAdapter = new ContactAdapter(StoreContacts, mapIndex, this);
            recyclerView.setAdapter(contactAdapter);

            FastScrollRecyclerViewItemDecoration decoration = new FastScrollRecyclerViewItemDecoration(this);
            recyclerView.addItemDecoration(decoration);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
        else
        {
            Toast.makeText(this, "Not granted", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, RequestPermissionCode);
        }
        else
            {
                recyclerView = findViewById(R.id.recycler_contact);
//      searchView = view.findViewById(R.id.action_search);
                searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);

                contact_list_btn = findViewById(R.id.show_data);

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));

            StoreContacts = new ArrayList<>();
            getContactsIntoArrayList();
            calculateIndexesForName(StoreContacts);
            contactAdapter = new ContactAdapter(StoreContacts, mapIndex, this);
            recyclerView.setAdapter(contactAdapter);

            FastScrollRecyclerViewItemDecoration decoration = new FastScrollRecyclerViewItemDecoration(this);
            recyclerView.addItemDecoration(decoration);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

        }

        contact_list_btn.setOnClickListener(view1 -> {
            Toast.makeText(this, contactAdapter.getSelectedContacts().toString(), Toast.LENGTH_SHORT).show();
        });


        searchView = findViewById(R.id.action_search);
        SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(Color.WHITE);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Search Contact");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                contactAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                contactAdapter.getFilter().filter(newText);
                return false;
            }
        });


    }

    public void getContactsIntoArrayList() {

        cursor = this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        while (cursor.moveToNext()) {

            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            StoreContacts.add(name + " " + ":" + " " + phonenumber);
        }

        cursor.close();

    }

    private void calculateIndexesForName(ArrayList<String> contacts) {
        mapIndex = new LinkedHashMap<>();

        for (int i = 0; i < contacts.size(); i++) {
            String name = contacts.get(i);
            String index = name.substring(0, 1);
            index = index.toUpperCase();

            if (!mapIndex.containsKey(index)) {
                mapIndex.put(index, i);
            }
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        getMenuInflater().inflate(R.menu.search_menu,menu);
//
//        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
//        searchAutoComplete.setHintTextColor(Color.WHITE);
//
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
//        searchView.setMaxWidth(Integer.MAX_VALUE);
//        searchView.setQueryHint("Search Contact");
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                contactAdapter.getFilter().filter(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                contactAdapter.getFilter().filter(newText);
//                return false;
//            }
//        });
//        return true;
//    }

}
