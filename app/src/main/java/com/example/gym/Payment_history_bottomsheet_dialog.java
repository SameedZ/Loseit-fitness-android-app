package com.example.gym;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Payment_history_bottomsheet_dialog extends BottomSheetDialogFragment {
    private RecyclerView recyclerView;
    private Context context;
    View view;
    PaymentHistory_Adapter paymentHistoryAdapter;
    List<PaymentClass> paymentClassList;

    static Payment_history_bottomsheet_dialog newInstance(){
        return new Payment_history_bottomsheet_dialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.payment_history_layout,container,false);

        Bundle bundle = getArguments();
        assert bundle != null;
        String email = bundle.getString("email");
        context = getContext();
        recyclerView = view.findViewById(R.id.rcvpaymenthistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));

        paymentClassList = new ArrayList<>();

        Query query = FirebaseDatabase.getInstance().getReference("payment").orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                run(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return view;
    }

    private void run(DataSnapshot snapshot) {
        for (DataSnapshot ds : snapshot.getChildren()){
            PaymentClass data = ds.getValue(PaymentClass.class);
            paymentClassList.add(data);
            Collections.reverse(paymentClassList);
        }

        if (paymentClassList.size()==0){
            Snackbar snackbar = Snackbar.make(view,"No payment history",Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        try {
            //where i initialize recyclerview adapter
            paymentHistoryAdapter = new PaymentHistory_Adapter(paymentClassList);
            recyclerView.setAdapter(paymentHistoryAdapter);

        }catch (Exception e){
            Snackbar snackbar = Snackbar.make(view,"Error: "+e,Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}
