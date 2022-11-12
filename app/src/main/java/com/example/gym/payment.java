package com.example.gym;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Objects;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class payment extends AppCompatActivity {
    private TextView history;
    private View paymentview;
    private TextInputLayout mCardnumber,mCvv,mExpiry;
    private String email;
    private int amount;
    private DatabaseReference Reference;
    private FirebaseDatabase database;
    private PaymentClass newpayment;
    private AVLoadingIndicatorView avi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        avi=(AVLoadingIndicatorView)findViewById(R.id.avipayment);

        paymentview = (View)findViewById(R.id.layoutid);
        history = (TextView)findViewById(R.id.paymenthistory);

        database=FirebaseDatabase.getInstance();
        Reference = database.getReference("payment");

        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("email");


        initializePaystack();
        initializeFormVariables();

        //oclick listerner for history
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("email",email);

                DialogFragment dialogFragment = Payment_history_bottomsheet_dialog.newInstance();
                dialogFragment.setArguments(bundle1);
                dialogFragment.show(getSupportFragmentManager(),"taggg");
            }
        });
    }

    private void initializePaystack() {
        PaystackSdk.initialize(getApplicationContext());
        PaystackSdk.setPublicKey("pk_test_fc44085d9f9d62457e1596974c");
    }
    private void initializeFormVariables(){
        mCardnumber = findViewById(R.id.cardnuber);
        mCvv = findViewById(R.id.cardcvv);
        mExpiry = findViewById(R.id.cardexpiry);

        Objects.requireNonNull(mExpiry.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() == 2 && !s.toString().contains("/")) {
                    s.append("/");
                }
            }
        });

        Button button = findViewById(R.id.makepayment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performCharge(v);
            }
        });
    }

    private void performCharge(View v) {
        startAnim();
        String cardnumber = mCardnumber.getEditText().getText().toString();
        String cardexpiry = mExpiry.getEditText().getText().toString();
        String cardcvv = mCvv.getEditText().getText().toString();

        if (TextUtils.isEmpty(cardnumber) | TextUtils.isEmpty(cardcvv) | TextUtils.isEmpty(cardexpiry)){
            Snackbar snackbar = Snackbar.make(v,"Fill all fields",Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }

        String[] cardExpiryArray = cardexpiry.split("/");
        int expiryMonth = Integer.parseInt(cardExpiryArray[0]);
        int expiryYear = Integer.parseInt(cardExpiryArray[1]);
        amount = 20;
        amount *=100;

        Card card = new Card(cardnumber,expiryMonth,expiryYear,cardcvv);

        Charge charge = new Charge();
        charge.setAmount(amount);
        charge.setCurrency("GHS");
        charge.setEmail(email);
        charge.setCard(card);

        PaystackSdk.chargeCard(this, charge, new Paystack.TransactionCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(Transaction transaction) {
                parseResponse(transaction.getReference());
            }

            @Override
            public void beforeValidate(Transaction transaction) {

            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                stopAnim();
                Toast.makeText(payment.this, "Error: "+error.fillInStackTrace(), Toast.LENGTH_LONG).show();
                Log.d("pay", "onError: "+error.getCause());
                Log.d("pay", "onError: "+error.getMessage());
            }
        });

    }

    private void stopAnim() {
        avi.hide();
    }

    private void startAnim() {
        avi.smoothToShow();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void parseResponse(String reference) {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        String dateformat = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withZone(ZoneId.systemDefault()));
        String timeformat = time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault()));
        String Payment_Time = timeformat;
        String Payment_Date = dateformat;
        String Payment_Amount = String.valueOf(amount/100);

        String message = "Payment Successful";
        Snackbar snackbar = Snackbar.make(paymentview,""+message,Snackbar.LENGTH_SHORT);
        snackbar.show();
        //Toast.makeText(this, " "+message, Toast.LENGTH_LONG).show();
        //save date, time, amount to user
//        HashMap<String ,Object> hashMap = new HashMap<>();
//        hashMap.put("Payment_Date",dateformat);
//        hashMap.put("Payment_Time",timeformat);
//        hashMap.put("Amount",String.valueOf(amount));

        newpayment = new PaymentClass();
        newpayment.setEmail(email);
        newpayment.setPayment_Amount(Payment_Amount);
        newpayment.setPayment_Date(Payment_Date);
        newpayment.setPayment_Time(Payment_Time);


        String keyID = Reference.push().getKey();
        //updating database
        Reference.child(keyID != null ? keyID : null).setValue(newpayment)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        stopAnim();
                        Snackbar snackbar = Snackbar.make(paymentview,"History Update Successful",Snackbar.LENGTH_LONG);
                        snackbar.show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        stopAnim();
                        Snackbar snackbar = Snackbar.make(paymentview,"History Update Failed",Snackbar.LENGTH_LONG);
                        Log.d("pay", "onFailure: "+e.fillInStackTrace());
                        snackbar.show();
                    }
                });
    }

    public void todash(View view) {
        Intent intent = new Intent(payment.this,UserDashboard.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_through_left);
        finish();
    }
}
