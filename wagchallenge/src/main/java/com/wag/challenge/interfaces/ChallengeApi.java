package com.wag.challenge.interfaces;

import com.wag.challenge.service.NetworkService;
import com.wag.challenge.service.UserManagerService;

/**
 * Created by PGomez on 9/21/2017.
 */

public interface ChallengeApi {

    UserManagerService getUserManagerService();

    NetworkService getNetworkService();
}
