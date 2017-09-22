package com.wag.challenge.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.LinearLayout;

import com.wag.challenge.R;
import com.wag.challenge.fragment.UserListFragment;
import com.wag.challenge.util.LogCatLogger;

public class MainActivity extends BaseActivity {
    //region constants
    private static final String TAG = "MainActivity";
    //endregion

    //region variables
    private LinearLayout rootViewLinearLayout;
    private Fragment userListFragment;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindUiElements();
        addListFragment();
    }

    private void addListFragment() {
        LogCatLogger.debug(TAG, "addListFragment");
        userListFragment = UserListFragment.getInstance();
        addFragmentIfNeeded(UserListFragment.TAG, userListFragment, R.id.root_view_linearLayout);
    }

    private void bindUiElements() {
        rootViewLinearLayout = (LinearLayout) findViewById(R.id.root_view_linearLayout);
    }
}
