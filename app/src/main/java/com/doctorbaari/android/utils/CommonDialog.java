package com.doctorbaari.android.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Nowfel Mashnoor on 11/26/2017.
 */

public class CommonDialog {
    public static void showDialog(final Activity activity, final String msg)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);

        builder1.setCancelable(true);

        builder1.setMessage("Your Hash: " + msg);

        builder1.setPositiveButton(
                "Copy Hash",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ClipboardManager manager =
                                (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);

                        manager.setText( msg );
                        // Show a message:
                        Toast.makeText(activity, "Hash copied to clipboard",
                                Toast.LENGTH_SHORT)
                                .show();
                        dialog.cancel();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}
