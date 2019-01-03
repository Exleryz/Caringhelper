package com.weimore.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.weimore.base.R;


/**
 * @author Weimore
 *         2018/3/9.
 *         description:
 */

public class Toolbar extends FrameLayout {

    private ImageView mLeftImageIm;
    private ImageView mRightImageIm;
    private TextView mLeftTitleTv;
    private TextView mMainTitleTv;
    private TextView mRightTitleTv;

    private View view;

    private int mBackgoundRes = R.color.white;
    private int mLeftImageRes = R.drawable.ic_back_white;
    private String mLeftTitle;
    private String mMainTitle;
    private String mRightTitle;
    private int mRightImageRes = -1;

    public Toolbar(Context context) {
        this(context, null);
    }

    public Toolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Toolbar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        view = LayoutInflater.from(context).inflate(R.layout.layout_toolbar, this);

        //下面的代码是给toolbar添加边界阴影效果时候使用的
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setElevation(ScreenUtils.dp2px(context,3));
//            setTranslationZ(ScreenUtils.dp2px(context,3));
//            setOutlineProvider(ViewOutlineProvider.BOUNDS);
//        }
        initResource(context, attrs);
        initView(view);
        initData();
    }

    private void initResource(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Toolbar);
        mBackgoundRes = ta.getResourceId(R.styleable.Toolbar_toolbar_background, mBackgoundRes);
        mLeftImageRes = ta.getResourceId(R.styleable.Toolbar_toolbar_left_image, mLeftImageRes);
        mLeftTitle = ta.getString(R.styleable.Toolbar_toolbar_left_title);
        mMainTitle = ta.getString(R.styleable.Toolbar_toolbar_title);
        mRightTitle = ta.getString(R.styleable.Toolbar_toolbar_right_title);
        mRightImageRes = ta.getResourceId(R.styleable.Toolbar_toolbar_right_image, mRightImageRes);
        ta.recycle();
    }

    private void initView(View view) {
        mLeftImageIm = view.findViewById(R.id.im_left);
        mLeftTitleTv = view.findViewById(R.id.tv_left_title);
        mMainTitleTv = view.findViewById(R.id.tv_main_title);
        mRightTitleTv = view.findViewById(R.id.tv_right_title);
        mRightImageIm = view.findViewById(R.id.im_right);
    }

    private void initData() {
        setBackgroundRes(mBackgoundRes);
        if (!TextUtils.isEmpty(mLeftTitle)) {
            setLeftTitle(mLeftTitle);
        }
        if (!TextUtils.isEmpty(mMainTitle)) {
            setMainTitle(mMainTitle);
        }
        if (!TextUtils.isEmpty(mRightTitle)) {
            setRightTitle(mRightTitle);
        }
        if (mRightImageRes > 0) {
            setRightImage(mRightImageRes);
        }
        if (mLeftImageRes > 0) {
            setLeftImage(mLeftImageRes);
            mLeftImageIm.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AppCompatActivity)getContext()).finish();
                }
            });
        }
    }

    public void setBgResource(int resource){
        view.setBackgroundResource(resource);
    }

    public Toolbar setBackgroundRes(int res){
        view.setBackgroundResource(res);
        return this;
    }

    public Toolbar setLeftTitle(String title) {
        mLeftTitleTv.setText(title);
        mLeftTitleTv.setVisibility(VISIBLE);
        return this;
    }

    public Toolbar setMainTitle(String title) {
        mMainTitleTv.setText(title);
        return this;
    }

    public Toolbar setRightTitle(String title) {
        mRightTitleTv.setText(title);
        mRightTitleTv.setVisibility(VISIBLE);
        return this;
    }

    public Toolbar setLeftImage(int imageRes) {
        mLeftImageIm.setImageResource(imageRes);
        mLeftImageIm.setVisibility(VISIBLE);
        return this;
    }

    public Toolbar setRightImage(int imageRes) {
        mRightImageIm.setImageResource(imageRes);
        mRightImageIm.setVisibility(VISIBLE);
        return this;
    }

    public Toolbar hideLeftImage() {
        mLeftImageIm.setVisibility(GONE);
        return this;
    }

    public Toolbar hideRightImage() {
        mRightImageIm.setVisibility(GONE);
        return this;
    }

    public Toolbar hideRightTitle(){
        mRightTitleTv.setVisibility(GONE);
        return this;
    }

    public void setLeftClickListener(OnClickListener listener){
        mLeftImageIm.setVisibility(VISIBLE);
        mLeftImageIm.setOnClickListener(listener);
    }

    public void setRightClickListener(OnClickListener listener){
        mRightImageIm.setOnClickListener(listener);
        mRightTitleTv.setOnClickListener(listener);
    }

    public void setMainClickListener(OnClickListener listener){
        mMainTitleTv.setOnClickListener(listener);
    }

    public View getRightView(){
        if(mRightTitleTv.isShown()){
            return mRightTitleTv;
        }else {
            return mRightImageIm;
        }
    }

    public TextView getMainTitleTextView(){
        return mMainTitleTv;
    }


}
