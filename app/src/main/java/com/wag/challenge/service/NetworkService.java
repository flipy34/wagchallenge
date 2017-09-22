package com.wag.challenge.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.wag.challenge.network.ChallengeRequest;
import com.wag.challenge.network.ChallengeResponse;
import com.wag.challenge.network.UserListResponse;
import com.wag.challenge.util.LogCatLogger;
import com.wag.challenge.util.UrlHelper;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by PGomez on 9/21/2017.
 */

public class NetworkService {

    //region constants
    private static final String TAG = NetworkService.class.getName();
    //endregion

    //region variables
    private final Context context;
    private final OkHttpClient okHttpClient;
    private final Gson gson;
    private final Handler uiHandler;
    private final ExecutorService singleThreadExecutor;
    private final ImageCacheService imageCache;
    //endregion

    public NetworkService(Context context, ImageCacheService imageCache) {
        this.context = context;
        this.imageCache = imageCache;
        okHttpClient = new OkHttpClient.Builder()
                .followRedirects(false)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        gson = new Gson();
        singleThreadExecutor = Executors.newSingleThreadExecutor();
        uiHandler = new Handler(Looper.getMainLooper());
    }

    public UserListResponse fetchExtraPage(int nextPageIndex, int pageSize) {
        String stackOverflowUrl = UrlHelper.getUserPageUrl(nextPageIndex, pageSize);

        ChallengeRequest userListRequest = new ChallengeRequest.Builder()
                .method("GET")
                .url(stackOverflowUrl)
                .build();

        UserListResponse userListResponse = new UserListResponse();

        return doRequest(userListRequest, userListResponse);

    }


    public <T extends ChallengeResponse, U extends ChallengeRequest> T doRequest(U request, T response) {
        Request okhttpRequest;
        if(request.getBody() == null) {
            okhttpRequest = new Request.Builder()
                    .get()
                    .url(request.getUrl())
                    .build();
        } else {
            okhttpRequest = new Request.Builder()
                    .url(request.getUrl())
                    .method(request.getMethod(), RequestBody.create(MediaType.parse("application/json"), gson.toJson(request.getBody())))
                    .build();
        }

        try {
            Response okhttpResponse = okHttpClient.newCall(okhttpRequest).execute();
            if(okhttpResponse != null) {
                String stringyfiedJson = new String(okhttpResponse.body().bytes(), "UTF-8");
                LogCatLogger.debug(TAG,"doRequest stringyfiedJson " + stringyfiedJson);
                response.setCode(okhttpResponse.code());
                response.setMessage(okhttpResponse.message());
                response.setResponseBody(gson.fromJson(stringyfiedJson, response.getResponseBodyClass()));
                return response;
            } else {
                return null;
            }

        } catch (IOException e) {
            LogCatLogger.error(TAG,e);
        }

        return null;
    }

    public void downloadAvatar(final String profileUrl, ImageView userAvatarImageView, ProgressBar avatarLoadingProgressBar) {
        final WeakReference<ImageView> weakImageView = new WeakReference<ImageView>(userAvatarImageView);
        final WeakReference<ProgressBar> weakProgressBar = new WeakReference<ProgressBar>(avatarLoadingProgressBar);
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if(imageCache.doesBitmapExistOnCache(profileUrl)) {
                   final Bitmap bitmap = imageCache.getBitmapFromCache(profileUrl);
                    LogCatLogger.debug(TAG, "cache hit, not using network");
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            setBitmap(weakImageView, weakProgressBar, bitmap);
                        }
                    });
                } else {
                    //Something failed and we need fecth the bitmap from network.
                    Request okhttpRequest = new Request.Builder()
                            .get()
                            .url(profileUrl)
                            .build();

                    try {
                        Response bitmapResponse = okHttpClient.newCall(okhttpRequest).execute();
                        if(bitmapResponse.body() != null ) {
                            byte[] bitmapBytes = bitmapResponse.body().bytes();
                            final Bitmap networkBitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
                            imageCache.saveBitmap(networkBitmap, profileUrl);
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                   setBitmap(weakImageView, weakProgressBar, networkBitmap);
                                }
                            });

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void setBitmap(WeakReference<ImageView> weakImageView, WeakReference<ProgressBar> weakProgressBar, Bitmap networkBitmap) {
        ImageView imageView = weakImageView.get();
        ProgressBar progressBar = weakProgressBar.get();
        if(imageView != null) {
            progressBar.setVisibility(View.GONE);
            imageView.setImageBitmap(networkBitmap);
        } else {
            LogCatLogger.debug(TAG, "imageview is null, it might have been destroyed.");
        }
    }
}
