package mp3player.durgesh.com.mp3player;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import mp3player.durgesh.com.mp3player.Interface.CustomDialogListner;
//
//import mp3player.durgesh.com.mp3player.db.DatabaseHelper;
//import mp3player.durgesh.com.mp3player.model.DataBaseRecFileModel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Interface.CustomDialogListner;

public class Utility {


    public static void showCustomDialoge(Context context, boolean isIconVisible, int drawableid,
                                         String message, final CustomDialogListner customDialogListner, String from) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_synch_msg);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow()
                .setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        Button textOk = (Button) dialog.findViewById(R.id.okBtn);
        Button textCancel = (Button) dialog.findViewById(R.id.cancelBtn);
        TextView textMessage = (TextView) dialog.findViewById(R.id.message);
        textMessage.setText(message);

        if (!isIconVisible){
            ImageView imgDialogicon = (ImageView)dialog.findViewById(R.id.imgDialogIcon);
            imgDialogicon.setVisibility(View.GONE);
        }

        if (from.equalsIgnoreCase("musicPlayer")){
            textOk.setText("Exit");
            textCancel.setText("Stay");
        }


        textOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListner.onOkClick();
                dialog.dismiss();
            }
        });
        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListner.onCancelClick();
                dialog.dismiss();


            }
        });
        dialog.show();
    }

}
