package com.example.bookrental;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {

    private static final int REQUEST_STORAGE = 1;
    private static final int UPI_PAYMENT = 1;
    TextInputEditText mNameField, mEmailField, mPasswordField, mConfirmPasswordField;
    private SharedPreferences mPreferences;

    Button singupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, permissions, REQUEST_STORAGE);


        mPreferences = getSharedPreferences("loginPreference",Context.MODE_PRIVATE);

        mEmailField = findViewById(R.id.userEmail);
        mNameField = findViewById(R.id.userName);
        mPasswordField = findViewById(R.id.userPassword);
        mConfirmPasswordField = findViewById(R.id.userConfirmPassword);
        singupButton = findViewById(R.id.signupButton);

        // Check if the user is already logged in
        if (mPreferences.getBoolean("is_logged_in", false)) {
            redirectHome();
        }else showStarterDialog();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with your action
            } else {
                // Permission denied, show a message or disable the functionality that depends on this permission
                Toast.makeText(SignupActivity.this, "Permission Required!", Toast.LENGTH_SHORT).show();
                System.exit(0);
            }
        }
    }


    private void redirectHome() {
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSignupClicked(View view) {
        String name = mNameField.getText().toString();
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        String confirmPassword = mConfirmPasswordField.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(this, "Name field is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Email field is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Password field is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(getFilesDir(), "signup_info.txt");
        try (FileOutputStream fos = new FileOutputStream(file, true)) {
            String info = "Name: " + name + "\nEmail: " + email + "\nPassword: " + password + "\n\n";
            fos.write(info.getBytes());
            Toast.makeText(this, "Signup successful", Toast.LENGTH_SHORT).show();
            redirectLogin();
        } catch (IOException e) {
            Toast.makeText(this, "Error saving signup info", Toast.LENGTH_SHORT).show();
        }
    }

    public void redirectLogin(View view) {
        redirectLogin();
    }

    public void redirectLogin() {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void showStarterDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
        builder.setMessage("Welcome to our Book Rental App"
        + "\nPlease pay a Registration fees of ₹50 in order to use the app."
        + "\nPress Ok to continue."
        + "\nPress Cancel to exit.");
        builder.setPositiveButton("Ok", null);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.exit(0);
            }
        });
        builder.create().show();
    }

    public void onPayClicked(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
        builder.setMessage("This is just a testing app so you're not really required to pay any registration fees. Enjoy!!");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(SignupActivity.this,"Payment Successful",Toast.LENGTH_SHORT).show();
                singupButton.setEnabled(true);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(SignupActivity.this,"Payment Failed",Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();

//        String payeeAddress = "8766405835@paytm";
//        String payeeName = "Lovely Sharma";
//        String transactionNote = "Registration Fees";
//        String amount = "50.00";
//
//        Uri uri = Uri.parse("upi://pay?pa=" + payeeAddress + "&pn=" + payeeName + "&tn=" + transactionNote + "&am=" + amount);
//        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW, uri);
//
//        if(null != upiPayIntent.resolveActivity(getPackageManager())) {
//            startActivityForResult(upiPayIntent, UPI_PAYMENT);
//        } else {
//            Toast.makeText(this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
//        }

    }


//    The below code handles the upi payment status
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }
    void upiPaymentDataOperation(ArrayList<String> data) {
        if (true) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Log.d("UPI", "payment successfull: "+approvalRefNo);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                //Code to handle canceled transaction here.
                Log.d("UPI", "Cancelled by user: "+approvalRefNo);

            }
            else {
                //Code to handle failed transaction here.
                Log.d("UPI", "failed payment: "+approvalRefNo);
            }
        } else {
            Log.e("UPI", "Internet issue: ");

        }
    }
*/
}