package com.wag.challenge.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by PGomez on 9/21/2017.
 */

public class UserListResponse<T> extends ChallengeResponse {

    public UserListResponse() {
        super(UserListResponseBody.class);
    }

    public static class UserListResponseBody {

        @SerializedName("items")
        private List<User> usersList;
        @SerializedName("has_more")
        private boolean hasMore;
        @SerializedName("quota_max")
        private int quotaMax;
        @SerializedName("quota_remaining")
        private int quotaRemaining;

        public List<User> getUsersList() {
            return usersList;
        }

        public boolean isHasMore() {
            return hasMore;
        }

        public int getQuotaMax() {
            return quotaMax;
        }

        public int getQuotaRemaining() {
            return quotaRemaining;
        }

        @Override
        public String toString() {
            return "UserListResponseBody{" +
                    "usersList=" + usersList +
                    ", hasMore=" + hasMore +
                    ", quotaMax=" + quotaMax +
                    ", quotaRemaining=" + quotaRemaining +
                    '}';
        }
    }

    public static class User{
        @SerializedName("account_id")
        private long accountId;
        @SerializedName("is_employee")
        private boolean isEmployee;
        @SerializedName("location")
        private String location;
        @SerializedName("display_name")
        private String displayName;
        @SerializedName("badge_counts")
        private Badges badges;
        @SerializedName("profile_image")
        private String profileUrl;

        public long getAccountId() {
            return accountId;
        }

        public boolean isEmployee() {
            return isEmployee;
        }

        public String getLocation() {
            return location;
        }

        public String getDisplayName() {
            return displayName;
        }

        public Badges getBadges() {
            return badges;
        }

        public String getProfileUrl() {
            return profileUrl;
        }

        @Override
        public String toString() {
            return "Users{" +
                    "accountId=" + accountId +
                    ", isEmployee=" + isEmployee +
                    ", location='" + location + '\'' +
                    ", displayName='" + displayName + '\'' +
                    ", badges=" + badges +
                    ", profileUrl='" + profileUrl + '\'' +
                    '}';
        }
    }

    public static class Badges {
        @SerializedName("bronze")
        private int bronze;
        @SerializedName("silver")
        private int silver;
        @SerializedName("gold")
        private int gold;


        public int getBronze() {
            return bronze;
        }

        public int getSilver() {
            return silver;
        }

        public int getGold() {
            return gold;
        }
    }
}
