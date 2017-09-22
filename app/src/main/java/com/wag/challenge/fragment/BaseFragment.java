package com.wag.challenge.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.wag.challenge.interfaces.ChallengeApi;

/**
 * Created by PGomez on 9/21/2017.
 */

public class BaseFragment extends Fragment {

    ChallengeApi challengeApi;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.challengeApi = (ChallengeApi) getActivity().getApplication();
    }

}
