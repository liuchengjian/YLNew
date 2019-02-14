package lchj.ylnews.mvp.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lchj.ylnews.R;

public class HeaderBar extends RelativeLayout {
    //所需控件
    @BindView(R.id.mLeftIv)
    ImageView mLeftIv;
    @BindView(R.id.mTitleTv)
    TextView mTitleTv;
    @BindView(R.id.mRightTv)
    TextView mRightTv;
    private String titleText = null;
    private String rightText = null;
    private Boolean isShowBack;

    private LayoutParams ivParams,titleParams,rightParams;
    private Context context ;

    public HeaderBar(Context context) {
        super(context);

    }

    public HeaderBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context,attrs);
    }

    public HeaderBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context,AttributeSet attrs) {
        TypedArray typedArray = context
                .obtainStyledAttributes(attrs, R.styleable.HeaderBar);
        //获取对应的属性值
        isShowBack = typedArray.getBoolean(R.styleable.HeaderBar_isShowBack,true);
        titleText = typedArray.getString(R.styleable.HeaderBar_titleText);
        rightText = typedArray.getString(R.styleable.HeaderBar_rightText);
        //回收 避免内存泄露
        typedArray.recycle();
        View view = LayoutInflater.from(context).inflate(R.layout.view_header, this);//使用this，相当于使用null，在addView
        ButterKnife.bind(view);//这里使用了butterKnife注解，比较常见的一个库
        if (isShowBack){
            mLeftIv.setVisibility(VISIBLE);
        }else {
            mLeftIv.setVisibility(GONE);
        }
        if(rightText!=null){
            mRightTv.setText(rightText);
            mRightTv.setVisibility(View.VISIBLE);
        }
        if(titleText!=null){
            mTitleTv.setText(titleText);
            mTitleTv.setVisibility(View.VISIBLE);
        }
        view.findViewById(R.id.mLeftIv).setOnClickListener(v -> {
            if(context instanceof Activity){
                ((Activity) context).finish();
            }
        });
    }
//    @OnClick(R.id.mLeftIv)
//    public void onClickLeftTV(OnClickListener listener) {
//        //设置左侧监听
//       if(context instanceof Activity){
//           ((Activity) context).finish();
//       }
//    }
    @OnClick(R.id.mRightTv)
    public void onClickRightTV(OnClickListener listener) {
        //设置右侧监听
        mRightTv.setOnClickListener(listener);
    }

    /**
     * 获取左侧视图
     * @return
     */
    public ImageView getLeftView(){
        return mLeftIv;
    }

    /**
     * 获取标题视图
     * @return
     */
    public TextView getTitleView(){
        return mTitleTv;
    }

    /**
     * 获取右侧视图
     * @return
     */
    public TextView getRightView(){
        return mRightTv;
    }

    /**
     * 获取右侧文字
     */
    public String getRightText(){
        return mRightTv.getText().toString();
    }


    /**
     * 获取标题文字
     * @return
     */
    public String getTitleText(){
        return mTitleTv.getText().toString();
    }

}
