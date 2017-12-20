package com.appodeal.iab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.appodeal.iab.mraid.MraidViewController;
import com.appodeal.iab.mraid.MraidViewControllerListener;
import com.appodeal.iab.views.CloseableLayout;

/**
 * This activity is used to show MRAID interstitial
 */
public class MraidActivity extends Activity implements MraidViewControllerListener {
    private final static String TAG = "MraidActivity";

    public static final String ID = "com.appodeal.mraid.MraidViewController.id";
    private static final String WAS_SHOWN = "com.appodeal.mraid.MraidViewController.wasShown";
    private static final String TIMER_POSITION = "com.appodeal.mraid.MraidViewController.timerPosition";
    private MraidInterstitial mraidInterstitial;
    private MraidViewController mraidViewController;
    private MraidInterstitialListener interstitialListener;
    private CloseableLayout closeableLayout;
    private boolean canSkip;

    private int timerPosition;
    private boolean wasShown;

    public static void startIntent(Context context, String id) {
        Intent intent = new Intent(context, MraidActivity.class);
        intent.putExtra(ID, id);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String interstitialId = getIntent().getStringExtra(ID);
        mraidInterstitial = MraidInterstitialStorage.get(interstitialId);

        if (mraidInterstitial == null) {
            Logger.e(TAG, String.format("Mraid interstitial with id %s not found", interstitialId));
            finish();
            return;
        }

        if (mraidInterstitial.isDestroyed()) {
            Logger.e(TAG, String.format("Mraid interstitial with id %s was destroyed", interstitialId));
            finish();
            return;
        }

        interstitialListener = mraidInterstitial.getMraidInterstitialListener();
        mraidViewController = mraidInterstitial.getController();
        if (mraidViewController == null) {
            Logger.e(TAG, "Mraid controller not found");
            closeActivity();
            return;
        }

        if (savedInstanceState != null) {
            wasShown = savedInstanceState.getBoolean(WAS_SHOWN, false);
            timerPosition = savedInstanceState.getInt(TIMER_POSITION, 0);
        }

        mraidViewController.attachActivity(this);
        mraidViewController.setMraidViewControllerListener(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        FrameLayout mraidView = mraidViewController.getMraidView();
        if (mraidView.getParent() != null) {
            ((ViewGroup) mraidView.getParent()).removeView(mraidView);
        }

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        closeableLayout = new CloseableLayout(this);
        closeableLayout.addView(mraidView, 0, layoutParams);
        closeableLayout.setOnCloseListener(new CloseableLayout.OnCloseListener() {
            @Override
            public void onClose() {
                closeActivity();
            }
        });
        closeableLayout.setSkippableStateListener(new CloseableLayout.SkippableStateListener() {
            @Override
            public void onSkippableStateChange() {
                canSkip = true;
            }
        });
        closeableLayout.startTimer(mraidInterstitial.getCloseTimeInterval(), timerPosition);
        setContentView(closeableLayout, layoutParams);
        if (interstitialListener != null && !wasShown) {
            wasShown = true;
            interstitialListener.onMraidInterstitialShown(mraidInterstitial);
        }
    }

    @Override
    public void onBackPressed() {
        if (canSkip) {
            closeActivity();
        }
    }

    private void closeActivity() {
        if (interstitialListener != null) {
            interstitialListener.onMraidInterstitialClosed(mraidInterstitial);
        }
        mraidInterstitial.destroy();
        mraidInterstitial = null;
        mraidViewController = null;
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mraidViewController != null) {
            mraidViewController.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mraidViewController != null) {
            mraidViewController.resume();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(WAS_SHOWN, wasShown);
        if (closeableLayout != null) {
            outState.putInt(TIMER_POSITION, closeableLayout.getCloseTimerPosition());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mraidInterstitial != null && !mraidInterstitial.isDestroyed()) {
            MraidInterstitialStorage.saveWithId(getIntent().getStringExtra(ID), mraidInterstitial);
        }
    }

    @Override
    public void onMraidViewControllerLoaded(MraidViewController mraidViewController) {
    }

    @Override
    public void onMraidViewControllerFailedToLoad(MraidViewController mraidViewController) {

    }

    @Override
    public void onMraidViewControllerUnloaded(MraidViewController mraidViewController) {
        if (interstitialListener != null) {
            interstitialListener.onMraidInterstitialUnloaded(mraidInterstitial);
        }
    }

    @Override
    public void onMraidViewControllerExpanded(MraidViewController mraidViewController) {

    }

    @Override
    public void onMraidViewControllerResized(MraidViewController mraidViewController) {

    }

    @Override
    public void onMraidViewControllerClicked(MraidViewController mraidViewController) {
        if (interstitialListener != null) {
            interstitialListener.onMraidInterstitialClicked(mraidInterstitial);
        }
    }

    @Override
    public void onMraidViewControllerClosed(MraidViewController mraidViewController) {
        closeActivity();
    }
}
