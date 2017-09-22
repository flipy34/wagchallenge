package com.wag.challenge.util;

/**
 * Created by PGomez on 9/21/2017.
 */

public class UrlHelper {

    //region constants
    private static String STACKOVERFLOW_BASE_URL = "https://api.stackexchange.com/";
    private static String STACKOVERFLOW_API_VERSION = "2.2/";
    private static String STACKOVERFLOW_USER_PATH = "users/";
    private static String STACKOVERFLOW_PAGE_PARAM = "page=";
    private static String STACKOVERFLOW_PAGESIZE_PARAM = "pagesize=";
    private static String STACKOVERFLOW_SITE_PARAM = "site=stackoverflow";
    private static String HTTP_BEGIN_PARAMETERS = "?";
    private static String HTTP_CONCAT_PARAMETERS = "&";
    //endregion

    public static String getUserPageUrl(int nextPageIndex, int pagesize) {
        return new StringBuilder()
                .append(STACKOVERFLOW_BASE_URL)
                .append(STACKOVERFLOW_API_VERSION)
                .append(STACKOVERFLOW_USER_PATH)
                .append(HTTP_BEGIN_PARAMETERS)
                .append(STACKOVERFLOW_PAGE_PARAM)
                .append(String.valueOf(nextPageIndex))
                .append(HTTP_CONCAT_PARAMETERS)
                .append(STACKOVERFLOW_PAGESIZE_PARAM)
                .append(pagesize)
                .append(HTTP_CONCAT_PARAMETERS)
                .append(STACKOVERFLOW_SITE_PARAM)
                .toString();
    }
}
