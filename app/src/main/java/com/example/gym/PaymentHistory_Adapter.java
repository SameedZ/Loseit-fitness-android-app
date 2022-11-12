package com.example.gym;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PaymentHistory_Adapter extends RecyclerView.Adapter<PaymentHistory_Adapter.PaymentHistory_AdapterVH> {
    private List<PaymentClass> paymentClassList;
    private Context context;

    public PaymentHistory_Adapter(List<PaymentClass> paymentClassList) {
        this.paymentClassList = paymentClassList;
    }

    @NonNull
    @Override
    public PaymentHistory_Adapter.PaymentHistory_AdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new PaymentHistory_AdapterVH(LayoutInflater.from(context).inflate(R.layout.payment_history_list_layout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentHistory_Adapter.PaymentHistory_AdapterVH holder, int position) {
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

    public class PaymentHistory_AdapterVH extends RecyclerView.ViewHolder {
        TextView date,time,amount;
        public PaymentHistory_AdapterVH(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.paymentdate);
            time = itemView.findViewById(R.id.paymenttime);
            amount = itemView.findViewById(R.id.paymentamount);
        }
    }
}
