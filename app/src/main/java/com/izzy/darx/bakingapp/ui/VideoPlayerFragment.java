package com.izzy.darx.bakingapp.ui;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.izzy.darx.bakingapp.R;
import com.izzy.darx.bakingapp.model.Step;
import com.izzy.darx.bakingapp.utils.BakingConstants;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoPlayerFragment extends Fragment {
    public static final String STEP_URI =  "step_uri";
    public static final String STEP_VIDEO_POSITION =  "step_video_position";
    public static final String STEP_PLAY_WHEN_READY =  "step_play_when_ready";
    public static final String STEP_PLAY_WINDOW_INDEX =  "step_play_window_index";
    public static final String STEP_SINGLE =  "step_single";

    @BindView(R.id.tv_step_title)
    TextView mStepTitle;

    @BindView(R.id.player_view)
    PlayerView mPlayerView;

    @BindView(R.id.tv_step_description)
    TextView mStepDescription;

    @BindView(R.id.iv_video_placeholder)
    ImageView mImageViewPlaceholder;

    SimpleExoPlayer mExoPlayer;

    Step mStep;
    Uri mVideoUri;
    String mVideoThumbnail;
    Bitmap mVideoThumbnailImage;
    boolean mShouldPlayWhenReady = true;
    long mPlayerPosition;
    int mWindowIndex;

    public VideoPlayerFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_video_player, container, false);
        ButterKnife.bind(this, root);

        if(savedInstanceState != null){
            mStep = savedInstanceState.getParcelable(STEP_SINGLE);
            mShouldPlayWhenReady = savedInstanceState.getBoolean(STEP_PLAY_WHEN_READY);
            mPlayerPosition = savedInstanceState.getLong(STEP_VIDEO_POSITION);
            mWindowIndex = savedInstanceState.getInt(STEP_PLAY_WINDOW_INDEX);
            mVideoUri = Uri.parse(savedInstanceState.getString(STEP_URI));
        }else {
            if (getArguments() != null) {
                mImageViewPlaceholder.setVisibility(View.GONE);
                mPlayerView.setVisibility(View.VISIBLE);

                //GetArguments
                mStep = getArguments().getParcelable(BakingConstants.STEP_SINGLE);

                //No video found for item
                assert mStep != null;
                if (mStep.getVideoURL().equals("")) {
                    // Check thumbnail
                    if (mStep.getThumbnailURL().equals("")) {
                        // If no video or thumbnail, use placeholder image
                        mPlayerView.setUseArtwork(true);
                        mImageViewPlaceholder.setVisibility(View.VISIBLE);
                        mPlayerView.setUseController(false);
                    } else {
                        mImageViewPlaceholder.setVisibility(View.GONE);
                        mPlayerView.setVisibility(View.VISIBLE);
                        mVideoThumbnail = mStep.getThumbnailURL();
                        mVideoThumbnailImage = ThumbnailUtils.createVideoThumbnail(mVideoThumbnail, MediaStore.Video.Thumbnails.MICRO_KIND);
                        mPlayerView.setUseArtwork(true);
                        mPlayerView.setDefaultArtwork(mVideoThumbnailImage);
                    }
                } else {
                    mVideoUri = Uri.parse(mStep.getVideoURL());
                }
            }
        }

        return root;
    }

    //Initialize ExoPlayer
    public void initPlayer( Uri mVideoUri) {

        mStepDescription.setText(mStep.getDescription());
        mStepTitle.setText(mStep.getShortDescription());

        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            /*LoadControl loadControl = new DefaultLoadControl();*/ // is deprecated
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
            mPlayerView.setPlayer(mExoPlayer);

            //Prepare MediaSource
            String userAgent = Util.getUserAgent(getActivity(), getString(R.string.app_name));
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(Objects.requireNonNull(getActivity()), userAgent);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mVideoUri);


            mExoPlayer.prepare(mediaSource);
            if (mPlayerPosition != C.TIME_UNSET) {
                mExoPlayer.seekTo(mPlayerPosition);
            }
            mExoPlayer.setPlayWhenReady(mShouldPlayWhenReady);
        }

    }

    private void EndPlayer() {
        if (mExoPlayer != null) {
            /*updateStartPosition();*/
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initPlayer(mVideoUri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mExoPlayer == null) {
            initPlayer(mVideoUri);
        }
        if(mExoPlayer != null){
            mExoPlayer.setPlayWhenReady(mShouldPlayWhenReady);
            mExoPlayer.seekTo(mPlayerPosition);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mExoPlayer != null){
            updateStartPosition();
            if (Util.SDK_INT <= 23) {
                EndPlayer();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mExoPlayer != null){
            updateStartPosition();
            if (Util.SDK_INT > 23) {
                EndPlayer();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        updateStartPosition();
        outState.putString(STEP_URI, mStep.getVideoURL());
        outState.putParcelable(STEP_SINGLE, mStep);
        outState.putLong(STEP_VIDEO_POSITION, mPlayerPosition);
        outState.putBoolean(STEP_PLAY_WHEN_READY, mShouldPlayWhenReady);
    }

    private void updateStartPosition(){
        if (mExoPlayer != null) {
            mShouldPlayWhenReady = mExoPlayer.getPlayWhenReady();
            mWindowIndex = mExoPlayer.getCurrentWindowIndex();
            mPlayerPosition = mExoPlayer.getCurrentPosition();
        }
    }
}
