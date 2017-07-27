/*
 * Copyright 2013 Priboi Tiberiu
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hitesh_sahu.retailapp.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hitesh_sahu.retailapp.R;
import com.hitesh_sahu.retailapp.util.Utils;
import com.hitesh_sahu.retailapp.util.Utils.AnimationType;
import com.hitesh_sahu.retailapp.view.activities.ECartHomeActivity;

// TODO: Auto-generated Javadoc
/**
 * Fragment that appears in the "content_frame", shows a animal.
 */
public class SettingsFragment extends Fragment {

	private TextView submitLog;
	private Toolbar mToolbar;

	/**
	 * Instantiates a new settings fragment.
	 */
	public SettingsFragment() {
		// Empty constructor required for fragment subclasses
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frag_settings, container,
				false);

		getActivity().setTitle("About App");

		mToolbar = (Toolbar) rootView.findViewById(R.id.htab_toolbar);
		if (mToolbar != null) {
			((ECartHomeActivity) getActivity()).setSupportActionBar(mToolbar);
		}

		if (mToolbar != null) {
			((ECartHomeActivity) getActivity()).getSupportActionBar()
					.setDisplayHomeAsUpEnabled(true);

			mToolbar.setNavigationIcon(R.drawable.ic_drawer);

		}

		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((ECartHomeActivity) getActivity()).getmDrawerLayout()
						.openDrawer(GravityCompat.START);
			}
		});

		mToolbar.setTitleTextColor(Color.WHITE);




		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_UP
						&& keyCode == KeyEvent.KEYCODE_BACK) {

					Utils.switchContent(R.id.frag_container,
							Utils.HOME_FRAGMENT,
							((ECartHomeActivity) (getContext())),
							AnimationType.SLIDE_UP);

				}
				return true;
			}
		});









		return rootView;
	}

	public static Fragment newInstance() {
		// TODO Auto-generated method stub
		return new SettingsFragment();
	}
}
