package com.wag.challenge.interfaces;

import com.wag.challenge.network.UserListResponse;

import java.util.List;

/**
 * Created by PGomez on 9/21/2017.
 */

public interface UserServiceListener {

    void onUserListLoaded(List<UserListResponse.User> user);
}
