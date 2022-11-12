package com.example.gym;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserPayment_Adapter extends RecyclerView.Adapter<UserPayment_Adapter.UserPayment_AdapterVH> {
    private List<PaymentClass> paymentClassList;
    private Context context;

    public UserPayment_Adapter(List<PaymentClass> paymentClassList) {
        this.paymentClassList = paymentClassList;
    }

    @NonNull
    @Override
    public UserPayment_Adapter.UserPayment_AdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new UserPayment_AdapterVH(LayoutInflater.from(context).inflate(R.layout.userpayment_adapter_layout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull UserPayment_Adapter.UserPayment_AdapterVH holder, int position) {
        PaymentClass paymentClass = paymentClassList.get(position);
        String date = paymentClass.getPayment_Date();
        String time = paymentClass.getPayment_Time();
        String amount = paymentClass.getPayment_Amount();

        holder.amount.setText("GHȼ "+amount);
        holder.time.setText(time);
        holder.date.setText(date);

    }

    @Override
    public int getItemCount() {
        return paymentClassList.size();
    }

    public class UserPayment_AdapterVH extends RecyclerView.ViewHolder {
        TextView date,amount,time;
        public UserPayment_AdapterVH(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.PaymentDate);
            time = itemView.findViewById(R.id.PaymentTime);
            amount = itemView.findViewById(R.id.PaymentAmount);
        }
    }
}
