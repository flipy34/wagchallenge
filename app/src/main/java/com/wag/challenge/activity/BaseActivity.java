package com.wag.challenge.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.wag.challenge.interfaces.ChallengeApi;

/**
 * Created by PGomez on 9/21/2017.
 */

public class BaseActivity extends AppCompatActivity {

    protected ChallengeApi challengeApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.challengeApi = (ChallengeApi) getApplication();
    }

    /**
     * Base method to adding fragments to any activity
     * @param tag TAG of the fragment to be added
     * @param fragmentToAdd actual instance of the fragment to add
     * @param idViewGroup id of the Layout or ViewGroup to be added to.
     * @return if the fragment was already added or not.
     */
    protected boolean addFragmentIfNeeded(String tag, Fragment fragmentToAdd, @IdRes int idViewGroup) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        Fragment findFragment = supportFragmentManager
                .findFragmentByTag(tag);
        if(findFragment == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(idViewGroup, fragmentToAdd, tag)
                    .commitAllowingStateLoss();

            return true;
        } else {
            return false;
        }
    }
}
