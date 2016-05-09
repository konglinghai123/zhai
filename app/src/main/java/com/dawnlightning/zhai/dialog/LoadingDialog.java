package com.dawnlightning.zhai.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.dawnlightning.zhai.R;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

/**
 * Created by Administrator on 2016/5/8.
 */
public class LoadingDialog extends ProgressDialog {
    private  Context context;
    private OnDismissListener onDismissListener;
    public LoadingDialog(Context context,OnDismissListener onDismissListener) {
        super(context);
        this.onDismissListener=onDismissListener;
    }

    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if (!hasFocus) {
            dismiss();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_loading);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        CircularProgressBar pb=(CircularProgressBar)this.findViewById(R.id.pb_loading);

        if (onDismissListener!=null){
            this.setOnDismissListener(onDismissListener);
        }
    }
}
