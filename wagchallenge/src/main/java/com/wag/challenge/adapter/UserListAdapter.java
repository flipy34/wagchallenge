package com.wag.challenge.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wag.challenge.R;
import com.wag.challenge.interfaces.ChallengeApi;
import com.wag.challenge.network.UserListResponse;
import com.wag.challenge.util.LogCatLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PGomez on 9/21/2017.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    //region constants
    private static final String TAG = "UserListAdapter";
    private static final int USER_VIEW_TYPE = 0;
    private static final int LOADING_VIEW_TYPE = 1;
    //endregion

    //region variables
    private final FragmentActivity activity;
    private final LayoutInflater layoutInflater;
    private final ChallengeApi challengeApi;
    private boolean shouldShowLoading = false;
    private List<UserListResponse.User> userList;
    private boolean shouldDownloadAvatars = true;
    //endregion

    public UserListAdapter(FragmentActivity activity, LayoutInflater systemService, ChallengeApi challengeApi) {
        this.activity = activity;
        this.layoutInflater = systemService;
        this.challengeApi = challengeApi;
        userList = new ArrayList<>();
    }

    @Override
    public UserListAdapter.UserListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case LOADING_VIEW_TYPE:
                View loadingView = layoutInflater.inflate(R.layout.adapter_view_loading_user, parent, false);
                return new UserListLoadingViewHolder(loadingView);
            case USER_VIEW_TYPE:
            default:
                View userView = layoutInflater.inflate(R.layout.adapter_view_user_data, parent, false);
                return new UserListUserDataViewHolder(userView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(userList.size() == position) {
            return LOADING_VIEW_TYPE;
        } else {
            return USER_VIEW_TYPE;
        }
    }

    @Override
    public void onViewRecycled(UserListViewHolder holder) {
        super.onViewRecycled(holder);
        if(holder instanceof UserListUserDataViewHolder) {
            UserListUserDataViewHolder userListUserDataViewHolder = (UserListUserDataViewHolder) holder;
            userListUserDataViewHolder.userAvatarImageView.setImageBitmap(null);
            userListUserDataViewHolder.avatarLoadingProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBindViewHolder(UserListAdapter.UserListViewHolder holder, int position) {
        if(holder instanceof UserListUserDataViewHolder) {
            UserListResponse.User user = userList.get(position);
            UserListUserDataViewHolder userListUserDataViewHolder = (UserListUserDataViewHolder) holder;
            userListUserDataViewHolder.userName.setText(user.getDisplayName());
            userListUserDataViewHolder.silverTextView.setText(String.valueOf(user.getBadges().getSilver()));
            userListUserDataViewHolder.goldTextView.setText(String.valueOf(user.getBadges().getGold()));
            userListUserDataViewHolder.bronzeTextView.setText(String.valueOf(user.getBadges().getBronze()));
            userListUserDataViewHolder.avatarLoadingProgressBar.setVisibility(View.VISIBLE);
            if(shouldDownloadAvatars) {
                //TODO: get avatar.
                challengeApi.getNetworkService().downloadAvatar(user.getProfileUrl(), userListUserDataViewHolder.userAvatarImageView, userListUserDataViewHolder.avatarLoadingProgressBar);
            }
        } else {
            //Nothing to do if it is the loading spinner.
        }
    }

    @Override
    public int getItemCount() {
        LogCatLogger.debug(TAG, "getItemCount " + userList.size());
        //Extra value for loading spinner
        if (shouldShowLoading) {
            return userList.size() + 1;
        } else {
            return userList.size();
        }
    }

    public void setShouldShowLoading(boolean b) {
        this.shouldShowLoading = b;
        notifyDataSetChanged();
    }

    public void setUsers(List<UserListResponse.User> user) {
        userList.addAll(user);
        notifyDataSetChanged();
    }

    public void setShouldDownloadAvatars(boolean b) {
        this.shouldDownloadAvatars = (b);
    }


    class UserListViewHolder extends RecyclerView.ViewHolder {

        public UserListViewHolder(View itemView) {
            super(itemView);
        }
    }

    class UserListLoadingViewHolder extends UserListViewHolder {

        public UserListLoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    class UserListUserDataViewHolder extends UserListViewHolder {
        ImageView userAvatarImageView;
        TextView userName;
        TextView goldTextView, silverTextView, bronzeTextView;
        ProgressBar avatarLoadingProgressBar;

        public UserListUserDataViewHolder(View itemView) {
            super(itemView);
            userAvatarImageView = (ImageView) itemView.findViewById(R.id.user_avatar_imageView);
            userName = (TextView) itemView.findViewById(R.id.username_textView);
            goldTextView = (TextView) itemView.findViewById(R.id.user_gold_textView);
            avatarLoadingProgressBar = (ProgressBar) itemView.findViewById(R.id.user_avatar_progressBar);
            silverTextView = (TextView) itemView.findViewById(R.id.user_silver_textView);
            bronzeTextView = (TextView) itemView.findViewById(R.id.user_bronze_textView);
        }
    }
}
