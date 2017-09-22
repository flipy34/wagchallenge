package com.wag.challenge.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wag.challenge.R;
import com.wag.challenge.adapter.UserListAdapter;
import com.wag.challenge.interfaces.ChallengeApi;
import com.wag.challenge.interfaces.UserServiceListener;
import com.wag.challenge.network.UserListResponse;
import com.wag.challenge.util.LogCatLogger;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * Created by PGomez on 9/21/2017.
 */

public class UserListFragment extends BaseFragment implements UserServiceListener {

    //region constants
    public static final String TAG = UserListFragment.class.getName();
    //endregion

    //region variables
    private View userListView;
    private RecyclerView userRecyclerView;
    private LinearLayoutManager userLinearLayoutManager;
    private UserListAdapter userAdapater;
    private UserListScrollListener userListScrollListener;
    //endregion

    //region listeners
    //endregion

    /**
     * Method that returns a self instance
     * @return instance of the fragment.
     */
    public static Fragment getInstance() {
        UserListFragment userListFragment = new UserListFragment();

        Bundle args =  new Bundle();

        userListFragment.setArguments(args);

        return userListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        challengeApi.getUserManagerService().addUserListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        userListView = inflater.inflate(R.layout.fragment_user_list, container, false);

        bindUiElements(userListView);
        setUpRecyclerView();
        setUpListeners();

        challengeApi.getUserManagerService().fetchUserList(userAdapater.getItemCount());

        return userListView;
    }

    private void setUpListeners() {
        userListScrollListener = new UserListScrollListener(challengeApi, userLinearLayoutManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        challengeApi.getUserManagerService().removeUserListListener(this);
    }

    private void setUpRecyclerView() {
        userLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        userAdapater = new UserListAdapter(getActivity(), (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE), challengeApi);

        userRecyclerView.setLayoutManager(userLinearLayoutManager);
        userRecyclerView.setAdapter(userAdapater);
    }

    private void bindUiElements(View userListView) {
        userRecyclerView = (RecyclerView) userListView.findViewById(R.id.user_recyclerView);
   }

    @Override
    public void onUserListLoaded(List<UserListResponse.User> user) {
        //TODO: hide the loading spinner.
        userAdapater.setShouldShowLoading(false);
        userRecyclerView.removeOnScrollListener(userListScrollListener);
        userListScrollListener.onItemsLoaded();

        if(user!=null) {
            userAdapater.setUsers(user);
        }

        userRecyclerView.addOnScrollListener(userListScrollListener);
    }

    //region private classes
    private class UserListScrollListener extends RecyclerView.OnScrollListener {

       private static final String TAG = "UserListScrollListener";
       private static final int INFINITE_SCROLL_THRESHOLD = 5;

       private final LinearLayoutManager linearLayoutManager;
       private final ChallengeApi wagChallengeApp;
       private AtomicBoolean isAlreadyRequesting = new AtomicBoolean(false);

       private UserListScrollListener(ChallengeApi wagChallengeApp, LinearLayoutManager linearLayoutManager) {
           this.linearLayoutManager = linearLayoutManager;
           this.wagChallengeApp = wagChallengeApp;
       }

       @Override
       public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
           LogCatLogger.debug(TAG, "onScrolled");
           int lastVisiblePos = linearLayoutManager.findLastCompletelyVisibleItemPosition();
           UserListAdapter adapter = (UserListAdapter) recyclerView.getAdapter();
           if(adapter.getItemCount() > 0) {
               if (lastVisiblePos + INFINITE_SCROLL_THRESHOLD > adapter.getItemCount() ) {
                   if (isAlreadyRequesting.compareAndSet(false, true)) {
                       //TODO: display a loading spinner.
                       LogCatLogger.debug(TAG, "fetching more elements");
                       wagChallengeApp.getUserManagerService().fetchUserList(adapter.getItemCount());
                       adapter.setShouldShowLoading(true);
                   } else {
                       LogCatLogger.debug(TAG, "onScrolled already requesting next page.");
                   }
               }
           }
       }

//        @Override
//        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//            super.onScrollStateChanged(recyclerView, newState);
//            switch (newState){
//                case SCROLL_STATE_IDLE:
//                case SCROLL_STATE_SETTLING:
//                    userAdapater.setShouldDownloadAvatars(true);
//                    break;
//                case SCROLL_STATE_DRAGGING:
//                    userAdapater.setShouldDownloadAvatars(false);
//
//            }
//        }

        public void onItemsLoaded() {
            isAlreadyRequesting.set(false);
        }
    }
    //endregion
}
