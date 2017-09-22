package com.wag.challenge;

import android.app.Application;

import com.wag.challenge.interfaces.ChallengeApi;
import com.wag.challenge.service.ImageCacheService;
import com.wag.challenge.service.NetworkService;
import com.wag.challenge.service.UserManagerService;

/**
 * Created by PGomez on 9/21/2017.
 */

public class WagChallengeApp extends Application implements ChallengeApi {

    //region variables
    private UserManagerService userManagerService;
    private NetworkService networkService;
    private ImageCacheService imageCache;
    //endregion

    @Override
    public void onCreate() {
        super.onCreate();

        imageCache = new ImageCacheService(this);
        networkService = new NetworkService(this, imageCache);
        userManagerService = new UserManagerService(this, networkService);
    }

    @Override
    public UserManagerService getUserManagerService() {
        return userManagerService;
    }

    @Override
    public NetworkService getNetworkService() {
        return networkService;
    }
}
