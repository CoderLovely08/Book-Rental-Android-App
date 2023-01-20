package com.example.bookrental;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

public class LogoutUser extends AlertDialog {
    public LogoutUser(Context context) {
            super(context);
            setMessage("Are you sure you want to logout?");
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setPositiveButton("Logout", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    logout();
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.create().show();
        }

        private void logout(){
            SharedPreferences.Editor editor = getContext().getSharedPreferences("loginPreference", Context.MODE_PRIVATE).edit();
            editor.putBoolean("is_logged_in",false);
            editor.clear();
            editor.apply();

            // Redirect to the login page
            Intent intent = new Intent(getContext(), LoginActivity.class);
            getContext().startActivity(intent);
            ((Activity)getContext()).finish();
        }
}
