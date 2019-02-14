package lchj.ylnews.mvp.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.Button;

import lchj.ylnews.R;

/**
 * 短信倒计时
 */
@SuppressLint("AppCompatCustomView")
public class VerifyButton extends Button {
    private Handler mHandler;
    private int mCount = 60;
    private OnVerifyBtnClick mOnVerifyBtnClick= null;

    String[] text =new String[]{};
    public VerifyButton(Context context) {
        super(context);
        init();
    }


    public VerifyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VerifyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        mHandler = new Handler();
    }


    /**
     * 倒计时，并处理点击事件
     */
    public void requestSendVerifyNumber() {
        mHandler.postDelayed(countDown, 0);
        if (mOnVerifyBtnClick != null) {
            mOnVerifyBtnClick.onClick();
        }

    }

    /**
     * 倒计时
     */
    private Runnable countDown = new Runnable() {
        @Override
        public void run() {
            VerifyButton.this.setText(mCount + "s");
            VerifyButton.this.setBackgroundColor(getResources().getColor(R.color.common_disable));
            VerifyButton.this.setTextColor(getResources().getColor(R.color.common_white));
            VerifyButton.this.setEnabled(false);

            if (mCount > 0) {
                mHandler.postDelayed(this, 1000);
            } else {
                resetCounter();
            }
            mCount--;
        }
    };

    /**
     * 恢复到初始状态
     */
    private void resetCounter() {
        this.setEnabled(true);
        if (text.length>0 && "" != text[0]) {
            this.setText(text[0]);
        } else {
            this.setText( "重获验证码");
        }
        this.setBackgroundColor(getResources().getColor(R.color.transparent));
        this.setTextColor(getResources().getColor(R.color.common_blue));
        mCount = 60;
    }


    private void removeRunable() {
        mHandler.removeCallbacks(countDown);
    }

    /**
     * 点击事件接口
     */
    interface OnVerifyBtnClick {
        void onClick();
    }

    public void setOnVerifyBtnClick(OnVerifyBtnClick onVerifyBtnClick) {
        this.mOnVerifyBtnClick = onVerifyBtnClick;
    }
}
