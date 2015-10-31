package com.lukekorth.android_500px.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.lukekorth.android_500px.R;
import com.lukekorth.android_500px.WallpaperApplication;
import com.lukekorth.android_500px.models.Photo;
import com.lukekorth.android_500px.models.PhotoResponse;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static com.lukekorth.android_500px.helpers.Utils.dpToPx;

public class FavoriteButton extends Button implements View.OnClickListener {

    private Photo mPhoto;

    public FavoriteButton(Context context) {
        super(context);
    }

    public FavoriteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FavoriteButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FavoriteButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setPhoto(Photo photo) {
        mPhoto = photo;

        setText(Integer.toString(mPhoto.favorites));
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.button_action_favorite, 0, 0, 0);
        if (mPhoto.favorited) {
            setBackgroundResource(R.drawable.button_action_bg_favorited);
        }

        setOnClickListener(this);
        setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (mPhoto.favorited) {
            setBackgroundResource(R.drawable.button_action_bg);

            WallpaperApplication.getApiClient()
                    .unfavorite(mPhoto.id)
                    .enqueue(new Callback<PhotoResponse>() {
                        @Override
                        public void onResponse(Response<PhotoResponse> response, Retrofit retrofit) {
                            if (response.isSuccess()) {
                                Photo photo = response.body().photo;
                                photo.favorited = false;
                                setPhoto(photo);
                            } else {
                                onFailure(null);
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            setBackgroundResource(R.drawable.button_action_bg_favorited);
                        }
                    });
        } else {
            setBackgroundResource(R.drawable.button_action_bg_favorited);

            WallpaperApplication.getApiClient()
                    .favorite(mPhoto.id)
                    .enqueue(new Callback<PhotoResponse>() {
                        @Override
                        public void onResponse(Response<PhotoResponse> response, Retrofit retrofit) {
                            if (response.isSuccess()) {
                                Photo photo = response.body().photo;
                                photo.favorited = true;
                                setPhoto(photo);
                            } else {
                                onFailure(null);
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            setBackgroundResource(R.drawable.button_action_bg);
                        }
                    });
        }
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);

        setPadding(dpToPx(getContext(), 2), dpToPx(getContext(), 4), dpToPx(getContext(), 8),
                dpToPx(getContext(), 4));
    }
}
