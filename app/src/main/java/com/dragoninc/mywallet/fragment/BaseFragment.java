package com.dragoninc.mywallet.fragment;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dragoninc.mywallet.R;

public abstract class BaseFragment extends Fragment {

	protected View mParentView;
	protected View mRootView;
	protected TextView mTitle;
	protected ImageView mBtnLeft;
	protected ImageView mBtnRight;
	protected Context mContext;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		mTitle = (TextView)mRootView.findViewById(R.id.toolbar_title);
		mBtnLeft = (ImageView) mRootView.findViewById(R.id.btnLeft);
		mBtnRight = (ImageView) mRootView.findViewById(R.id.btnRight);
		initActions();
	}

	protected void setTitle(String text) {
		mTitle.setText(text);
	}

	protected void showButtonLeft(boolean show) {
		if (show)
			mBtnLeft.setVisibility(View.VISIBLE);
		else
			mBtnLeft.setVisibility(View.GONE);
	}

	protected void showButtonRight(boolean show) {
		if (show)
			mBtnRight.setVisibility(View.VISIBLE);
		else
			mBtnRight.setVisibility(View.GONE);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = getActivity();
		initVariables();
	}

	protected abstract void initVariables();
	protected abstract void initActions();
}
