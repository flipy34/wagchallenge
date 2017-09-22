package com.wag.challenge.service;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.wag.challenge.interfaces.UserServiceListener;
import com.wag.challenge.network.UserListResponse;
import com.wag.challenge.util.LogCatLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by PGomez on 9/21/2017.
 */

public class UserManagerService {
    //region constants
    private static final String TAG = UserManagerService.class.getName();
    private static final int PAGE_SIZE = 20;
    //endregion

    //region variables
    private final Context context;
    private final NetworkService networkService;
    private final ExecutorService singleThreadExecutor;
    private final Handler uiHandler;
    private List<UserServiceListener> listeners = new ArrayList<>();
    //endregion

    public UserManagerService(Context context, NetworkService networkService) {
        this.context = context;
        this.networkService = networkService;
        singleThreadExecutor = Executors.newSingleThreadExecutor();
        uiHandler = new Handler(Looper.getMainLooper());
    }


    public void addUserListener(UserServiceListener userServiceListener){
        if(!listeners.contains(userServiceListener)) {
            listeners.add(userServiceListener);
        }
    }

    public void removeUserListListener(UserServiceListener userServiceListener) {
        listeners.remove(userServiceListener);
    }


    public void fetchUserList(final int currentItemCount) {
        if(!singleThreadExecutor.isShutdown()){
            singleThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    int currentPage = currentItemCount / PAGE_SIZE;
                    LogCatLogger.debug(TAG, "current page: " + String.valueOf(currentPage));

                    //TODO: handle quota of the API;
                    UserListResponse userListResponse = networkService.fetchExtraPage(++currentPage, PAGE_SIZE);
                    if(userListResponse!= null && userListResponse.getCode() >= 200 && userListResponse.getCode() <300) {
                        //Success
                        LogCatLogger.debug(TAG, "fetchUserList success " + userListResponse.getResponseBody());
                        if(userListResponse.getResponseBody() instanceof UserListResponse.UserListResponseBody) {
                            UserListResponse.UserListResponseBody responseBody = (UserListResponse.UserListResponseBody) userListResponse.getResponseBody();
                            reportUserListLoaded(responseBody.getUsersList());
                        } else {
                            reportUserListLoaded(null);
                        }
                    } else {
                        LogCatLogger.debug(TAG, "userListResponse " + userListResponse);
                        reportUserListLoaded(null);
                    }

                }
            });
        }

    }

    private void reportUserListLoaded(final List<UserListResponse.User> usersList) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                for(UserServiceListener userServiceListener : listeners) {
                    userServiceListener.onUserListLoaded(usersList);
                }
            }
        });
    }
}
