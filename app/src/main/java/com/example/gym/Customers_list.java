package com.example.gym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class Customers_list extends AppCompatActivity implements UsersAdapter.SelectedUser{
    Toolbar toolbar;
    RecyclerView recyclerView;
    View layoutview;
    AVLoadingIndicatorView listavi;

    List<User> userList;
    String[] names={"Rich","Alice","Rose"};

    UsersAdapter usersAdapter;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_list);

        layoutview = (View)findViewById(R.id.layoutview);
        listavi = (AVLoadingIndicatorView)findViewById(R.id.listavi);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(" ");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        userList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                run(snapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar snackbar = Snackbar.make(layoutview,"Error fetching customers info",Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

//        for (String s:names){
//            User user = new User(s);
//            userList.add(user);
//        }


    }

    private void startAnim() {
        listavi.smoothToShow();
    }

    private void run(DataSnapshot snapshot) {
        startAnim();
        for (DataSnapshot ds : snapshot.getChildren()){
            User data = ds.getValue(User.class);
            userList.add(data);
        }
        //uve got to move wai
        if (userList.size()<=0){
            Snackbar snackbar = Snackbar.make(layoutview,"No cutomers to fetch",Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        stopAnim();
        try {
            usersAdapter = new UsersAdapter(userList,this);
            recyclerView.setAdapter(usersAdapter);
        }catch (Exception e){
            Snackbar snackbar = Snackbar.make(layoutview,"No cutomers to fetch",Snackbar.LENGTH_LONG);
            snackbar.show();
        }


    }

    private void stopAnim() {
        listavi.hide();
    }

    @Override
    public void selectedUser(User user) {
        //go to next activity and put extra+
        Intent intent = new Intent(Customers_list.this,SelectedUser.class);
        intent.putExtra("data",user);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_through_right);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem menuItem = menu.findItem(R.id.search_view);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                usersAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search_view){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void dashboard(View view) {
        Intent intent = new Intent(Customers_list.this,AdminDashboard.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_through_left);
        finish();
    }
}