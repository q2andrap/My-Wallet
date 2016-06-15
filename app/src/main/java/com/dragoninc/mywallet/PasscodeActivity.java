package com.dragoninc.mywallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dragoninc.mywallet.database.DataHelper;

/**
 * Created by quan.le on 6/1/2016.
 */
public class PasscodeActivity  extends Activity implements View.OnClickListener {

    protected TextView mTitle;
    protected ImageView mBtnLeft;
    protected ImageView mBtnRight;
    private TextView mBtn0, mBtn1, mBtn2, mBtn3, mBtn4, mBtn5, mBtn6, mBtn7, mBtn8, mBtn9;
    private RadioButton mRButton1, mRButton2, mRButton3, mRButton4, mRButton5;
    private String mPasscode = "";
    private int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);

        mTitle = (TextView)this.findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.string_passcode_title);
        mBtnLeft = (ImageView) this.findViewById(R.id.btnLeft);
        mBtnRight = (ImageView) this.findViewById(R.id.btnRight);
        initRadioButton();
        initKeyBoardButtons();
    }

    private void initRadioButton() {
        mRButton1 = (RadioButton)this.findViewById(R.id.number1);
        mRButton2 = (RadioButton)this.findViewById(R.id.number2);
        mRButton3 = (RadioButton)this.findViewById(R.id.number3);
        mRButton4 = (RadioButton)this.findViewById(R.id.number4);
        mRButton5 = (RadioButton)this.findViewById(R.id.number5);
    }

    private void initKeyBoardButtons() {
        mBtn1 = (TextView) this.findViewById(R.id.btn1);
        mBtn1.setOnClickListener(this);
        mBtn2 = (TextView) this.findViewById(R.id.btn2);
        mBtn2.setOnClickListener(this);
        mBtn3 = (TextView) this.findViewById(R.id.btn3);
        mBtn3.setOnClickListener(this);
        mBtn4 = (TextView) this.findViewById(R.id.btn4);
        mBtn4.setOnClickListener(this);
        mBtn5 = (TextView) this.findViewById(R.id.btn5);
        mBtn5.setOnClickListener(this);
        mBtn6 = (TextView) this.findViewById(R.id.btn6);
        mBtn6.setOnClickListener(this);
        mBtn7 = (TextView) this.findViewById(R.id.btn7);
        mBtn7.setOnClickListener(this);
        mBtn8 = (TextView) this.findViewById(R.id.btn8);
        mBtn8.setOnClickListener(this);
        mBtn9 = (TextView) this.findViewById(R.id.btn9);
        mBtn9.setOnClickListener(this);
        mBtn0 = (TextView) this.findViewById(R.id.btn0);
        mBtn0.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn1:
            case R.id.btn2:
            case R.id.btn3:
            case R.id.btn4:
            case R.id.btn5:
            case R.id.btn6:
            case R.id.btn7:
            case R.id.btn8:
            case R.id.btn9:
            case R.id.btn0:
                mPasscode += ((TextView)v).getText().toString();
                switch (count++){
                    case 0:
                        mRButton1.performClick();
                        break;
                    case 1:
                        mRButton2.performClick();
                        break;
                    case 2:
                        mRButton3.performClick();
                        break;
                    case 3:
                        mRButton4.performClick();
                        break;
                    case 4:
                        mRButton5.performClick();
                        break;
                }

                if (count > 4)
                {
                    String pass = DataHelper.getInstance(this).getString("PASSCODE");
                    boolean isFirstTimeReady = DataHelper.getInstance(this).getBoolean("FIRSTTIME");
                    if (!isFirstTimeReady) {
                        DataHelper.getInstance(this).setBoolean("FIRSTTIME", true);
                        DataHelper.getInstance(this).setString("PASSCODE", mPasscode);
                        Intent i = new Intent(this, MainActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    }

                    if (mPasscode.equals(pass)) {
                        Intent i = new Intent(this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(this, R.string.string_wrong_passcode, Toast.LENGTH_SHORT).show();
                        mRButton1.setChecked(false);
                        mRButton2.setChecked(false);
                        mRButton3.setChecked(false);
                        mRButton4.setChecked(false);
                        mRButton5.setChecked(false);
                        count = 0;
                        mPasscode = "";
                    }
                }
                break;
        }
    }
}
