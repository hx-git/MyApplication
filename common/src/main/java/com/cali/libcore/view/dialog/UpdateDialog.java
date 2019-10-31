package com.cali.libcore.view.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.cali.common.R;

/**
 * Created by Dinosa on 2018/2/1.
 */

public class UpdateDialog {


    static AlertDialog alertDialog=null;

    public  void show (Context context, String content, final OnClickUpdateListener onClickUpdateListener){

        //红包的对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.NoBackGroundDialog);
        View view = View.inflate(context, R.layout.dialog_update, null);


        //view.setLayoutParams();
        TextView tvContent = view.findViewById(R.id.tv_content);

        tvContent.setText(content);

        view.findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog != null) {
                    alertDialog.dismiss();
                    //这里是点击了确定的操作；
                    onClickUpdateListener.onClickUpdate();
                }
            }
        });
        builder.setView(view);
        alertDialog= builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }


    public interface OnClickUpdateListener{
        void onClickUpdate();
    }

    public void dismiss(){
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

}
