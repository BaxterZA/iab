package com.appodeal.mraid;

import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.widget.FrameLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


@RunWith(AndroidJUnit4.class)
public class MraidJsTests {

    @Rule
    public ActivityTestRule<TestActivity> mActivityRule = new ActivityTestRule<>(TestActivity.class);

    @Test
    public void defaultMRAID_ENV() throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        mActivityRule.runOnUiThread(new Runnable() {
            public void run() {
                MraidView mraidView = mActivityRule.getActivity().mraidView;
                mActivityRule.getActivity().drawView();

                mraidView.setMraidWebViewDebugListener(new MraidWebViewDebugListener() {
                    @Override
                    public boolean onJsAlert(@NonNull String message, @NonNull JsResult result) {
                        if (message.equals("pass")) {
                            countDownLatch.countDown();
                        } else {
                            fail();
                        }
                        return false;
                    }

                    @Override
                    public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                        return false;
                    }
                });

                String html = TestHelper.readHtml(mActivityRule.getActivity(), "mraid_env_default.html");
                mraidView.setHtml(html);
                mraidView.load();
            }
        });

        countDownLatch.await();
    }

    @Test
    public void customMRAID_ENV() throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mActivityRule.runOnUiThread(new Runnable() {
            public void run() {
                MraidView mraidView = mActivityRule.getActivity().mraidView;
                mActivityRule.getActivity().drawView();

                MraidEnvironment mraidEnvironment = new MraidEnvironment.Builder().setCoppa(true)
                        .setIfa("ifa")
                        .setLimitAdTracking(true)
                        .setAppId("com.appodeal.mraid")
                        .build();
                mraidView.setMraidEnvironment(mraidEnvironment);
                mraidView.setMraidWebViewDebugListener(new MraidWebViewDebugListener() {
                    @Override
                    public boolean onJsAlert(@NonNull String message, @NonNull JsResult result) {
                        if (message.equals("pass")) {
                            countDownLatch.countDown();
                        } else {
                            fail();
                        }
                        return false;
                    }

                    @Override
                    public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                        return false;
                    }
                });

                String html = TestHelper.readHtml(mActivityRule.getActivity(), "mraid_env_custom.html");
                mraidView.setHtml(html);
                mraidView.load();
            }
        });

        countDownLatch.await();
    }


    @Test
    public void readyState() throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mActivityRule.runOnUiThread(new Runnable() {
            public void run() {
                MraidView mraidView = mActivityRule.getActivity().mraidView;
                mActivityRule.getActivity().drawView();

                mraidView.setMraidWebViewDebugListener(new MraidWebViewDebugListener() {
                    @Override
                    public boolean onJsAlert(@NonNull String message, @NonNull JsResult result) {
                        if (message.equals("pass")) {
                            countDownLatch.countDown();
                        } else {
                            fail();
                        }
                        return false;
                    }

                    @Override
                    public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                        return false;
                    }
                });

                String html = TestHelper.readHtml(mActivityRule.getActivity(), "check_ready_state.html");
                mraidView.setHtml(html);
                mraidView.load();
            }
        });

        countDownLatch.await();
    }

    @Test
    public void viewableChange_falseByDefault() throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mActivityRule.runOnUiThread(new Runnable() {
            public void run() {
                MraidView mraidView = mActivityRule.getActivity().mraidView;
                mActivityRule.getActivity().setVisibility(false);
                mActivityRule.getActivity().drawView();

                mraidView.setMraidWebViewDebugListener(new MraidWebViewDebugListener() {
                    @Override
                    public boolean onJsAlert(@NonNull String message, @NonNull JsResult result) {
                        if (message.equals("pass")) {
                            countDownLatch.countDown();
                        } else {
                            fail();
                        }
                        return false;
                    }

                    @Override
                    public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                        return false;
                    }
                });

                String html = TestHelper.readHtml(mActivityRule.getActivity(), "check_visibility_false_by_default.html");
                mraidView.setHtml(html);
                mraidView.load();
            }
        });

        countDownLatch.await();
    }

    @Test
    public void viewableChange_changeToTrue() throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mActivityRule.runOnUiThread(new Runnable() {
            public void run() {
                MraidView mraidView = mActivityRule.getActivity().mraidView;
                mActivityRule.getActivity().setVisibility(true);
                mActivityRule.getActivity().drawView();

                mraidView.setMraidWebViewDebugListener(new MraidWebViewDebugListener() {
                    @Override
                    public boolean onJsAlert(@NonNull String message, @NonNull JsResult result) {
                        if (message.equals("pass")) {
                            countDownLatch.countDown();
                        } else {
                            fail();
                        }
                        return false;
                    }

                    @Override
                    public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                        return false;
                    }
                });

                String html = TestHelper.readHtml(mActivityRule.getActivity(), "check_visibility_change_to_true.html");
                mraidView.setHtml(html);
                mraidView.load();
            }
        });

        countDownLatch.await();
    }

    @Test
    public void viewableChange_changeToFalse() throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        mActivityRule.runOnUiThread(new Runnable() {
            public void run() {
                final MraidView testMraidView = mActivityRule.getActivity().mraidView;
                mActivityRule.getActivity().setVisibility(true);
                mActivityRule.getActivity().drawView();

                testMraidView.setMraidWebViewDebugListener(new MraidWebViewDebugListener() {
                    @Override
                    public boolean onJsAlert(@NonNull String message, @NonNull JsResult result) {
                        if (message.equals("visible")) {
                            countDownLatch.countDown();
                            testMraidView.setVisibility(View.GONE);
                            return false;
                        }

                        if (message.equals("pass")) {
                            countDownLatch.countDown();
                        } else {
                            fail();
                        }
                        return false;
                    }

                    @Override
                    public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                        return false;
                    }
                });

                String html = TestHelper.readHtml(mActivityRule.getActivity(), "check_visibility_change_to_false.html");
                testMraidView.setHtml(html);
                testMraidView.load();
            }
        });

        countDownLatch.await();
    }

    @Test
    public void exposureChange_changeTo100() throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mActivityRule.runOnUiThread(new Runnable() {
            public void run() {
                final MraidView testMraidView = mActivityRule.getActivity().mraidView;
                mActivityRule.getActivity().setVisibility(true);
                mActivityRule.getActivity().drawView();

                testMraidView.setMraidWebViewDebugListener(new MraidWebViewDebugListener() {
                    @Override
                    public boolean onJsAlert(@NonNull String message, @NonNull JsResult result) {
                        if (message.equals("pass")) {
                            countDownLatch.countDown();
                        } else {
                            fail();
                        }
                        return false;
                    }

                    @Override
                    public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                        return false;
                    }
                });

                String html = TestHelper.readHtml(mActivityRule.getActivity(), "check_exposure_change_to_100.html");
                testMraidView.setHtml(html);
                testMraidView.load();
            }
        });

        countDownLatch.await();
    }

    @Test
    public void exposureChange_changeVisibility() throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mActivityRule.runOnUiThread(new Runnable() {
            public void run() {
                final MraidView testMraidView = mActivityRule.getActivity().mraidView;
                mActivityRule.getActivity().setVisibility(true);
                mActivityRule.getActivity().drawView();

                testMraidView.setMraidWebViewDebugListener(new MraidWebViewDebugListener() {
                    @Override
                    public boolean onJsAlert(@NonNull String message, @NonNull JsResult result) {
                        switch (message) {
                            case "next":
                                mActivityRule.getActivity().setVisibility(false);
                                break;
                            case "pass":
                                countDownLatch.countDown();
                                break;
                            default:
                                fail();
                                break;
                        }
                        return false;
                    }

                    @Override
                    public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                        return false;
                    }
                });

                String html = TestHelper.readHtml(mActivityRule.getActivity(), "check_exposure_changes.html");
                testMraidView.setHtml(html);
                testMraidView.load();
            }
        });

        countDownLatch.await();
    }

    @Test
    public void exposureChange_removeView() throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mActivityRule.runOnUiThread(new Runnable() {
            public void run() {
                final MraidView testMraidView = mActivityRule.getActivity().mraidView;
                mActivityRule.getActivity().setVisibility(true);
                mActivityRule.getActivity().drawView();

                testMraidView.setMraidWebViewDebugListener(new MraidWebViewDebugListener() {
                    @Override
                    public boolean onJsAlert(@NonNull String message, @NonNull JsResult result) {
                        switch (message) {
                            case "next":
                                mActivityRule.getActivity().removeView();
                                break;
                            case "pass":
                                countDownLatch.countDown();
                                break;
                            default:
                                fail();
                                break;
                        }
                        return false;
                    }

                    @Override
                    public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                        return false;
                    }
                });

                String html = TestHelper.readHtml(mActivityRule.getActivity(), "check_exposure_changes.html");
                testMraidView.setHtml(html);
                testMraidView.load();
            }
        });

        countDownLatch.await();
    }

    @Test
    public void exposureChange_setAlpha() throws Throwable { //currently failed
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mActivityRule.runOnUiThread(new Runnable() {
            public void run() {
                final MraidView testMraidView = mActivityRule.getActivity().mraidView;
                mActivityRule.getActivity().setVisibility(true);
                mActivityRule.getActivity().drawView();

                testMraidView.setMraidWebViewDebugListener(new MraidWebViewDebugListener() {
                    @Override
                    public boolean onJsAlert(@NonNull String message, @NonNull JsResult result) {
                        switch (message) {
                            case "next":
                                mActivityRule.getActivity().mraidView.setAlpha(0);
                                break;
                            case "pass":
                                countDownLatch.countDown();
                                break;
                            default:
                                fail();
                                break;
                        }
                        return false;
                    }

                    @Override
                    public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                        return false;
                    }
                });

                String html = TestHelper.readHtml(mActivityRule.getActivity(), "check_exposure_changes.html");
                testMraidView.setHtml(html);
                testMraidView.load();
            }
        });

        countDownLatch.await();
    }

    @Test
    public void exposureChange_outOfScreen() throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mActivityRule.runOnUiThread(new Runnable() {
            public void run() {
                final MraidView testMraidView = mActivityRule.getActivity().mraidView;
                mActivityRule.getActivity().setVisibility(true);
                mActivityRule.getActivity().drawView();

                testMraidView.setMraidWebViewDebugListener(new MraidWebViewDebugListener() {
                    @Override
                    public boolean onJsAlert(@NonNull String message, @NonNull JsResult result) {
                        switch (message) {
                            case "next":
                                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mActivityRule.getActivity().mraidView.getLayoutParams();
                                layoutParams.topMargin = -mActivityRule.getActivity().mraidView.getHeight();
                                mActivityRule.getActivity().mraidView.setLayoutParams(layoutParams);
                                break;
                            case "pass":
                                countDownLatch.countDown();
                                break;
                            default:
                                fail();
                                break;
                        }
                        return false;
                    }

                    @Override
                    public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                        return false;
                    }
                });

                String html = TestHelper.readHtml(mActivityRule.getActivity(), "check_exposure_changes.html");
                testMraidView.setHtml(html);
                testMraidView.load();
            }
        });

        countDownLatch.await();
    }

    @Test
    public void exposureChange_zeroWidth() throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mActivityRule.runOnUiThread(new Runnable() {
            public void run() {
                final MraidView testMraidView = mActivityRule.getActivity().mraidView;
                mActivityRule.getActivity().setVisibility(true);
                mActivityRule.getActivity().drawView();

                testMraidView.setMraidWebViewDebugListener(new MraidWebViewDebugListener() {
                    @Override
                    public boolean onJsAlert(@NonNull String message, @NonNull JsResult result) {
                        switch (message) {
                            case "next":
                                mActivityRule.getActivity().mraidView.setLayoutParams(new FrameLayout.LayoutParams(0, 0));
                                break;
                            case "pass":
                                countDownLatch.countDown();
                                break;
                            default:
                                fail();
                                break;
                        }
                        return false;
                    }

                    @Override
                    public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                        return false;
                    }
                });

                String html = TestHelper.readHtml(mActivityRule.getActivity(), "check_exposure_changes.html");
                testMraidView.setHtml(html);
                testMraidView.load();
            }
        });

        countDownLatch.await();
    }

    @Test
    public void exposureChange_visibleRectNotFull() throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mActivityRule.runOnUiThread(new Runnable() {
            public void run() {
                final MraidView testMraidView = mActivityRule.getActivity().mraidView;
                mActivityRule.getActivity().setVisibility(true);
                mActivityRule.getActivity().drawView();
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mActivityRule.getActivity().mraidView.getLayoutParams();
                DisplayMetrics displayMetrics = mActivityRule.getActivity().getResources().getDisplayMetrics();
                layoutParams.leftMargin = ViewHelper.dip2px(-TestActivity.width / 2, displayMetrics.densityDpi);
                layoutParams.topMargin = ViewHelper.dip2px(-TestActivity.height / 2, displayMetrics.densityDpi);
                mActivityRule.getActivity().mraidView.setLayoutParams(layoutParams);
                testMraidView.setMraidWebViewDebugListener(new MraidWebViewDebugListener() {
                    @Override
                    public boolean onJsAlert(@NonNull String message, @NonNull JsResult result) {
                        try {
                            JSONObject visibleRectangle = new JSONObject(message);
                            assertEquals(visibleRectangle.getInt("width"), TestActivity.width / 2);
                            assertEquals(visibleRectangle.getInt("height"), TestActivity.height / 2);
                            countDownLatch.countDown();
                        } catch (JSONException e) {
                            fail();
                        }
                        return false;
                    }

                    @Override
                    public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                        return false;
                    }
                });

                String html = TestHelper.readHtml(mActivityRule.getActivity(), "check_exposure_change_visible_rect.html");
                testMraidView.setHtml(html);
                testMraidView.load();
            }
        });

        countDownLatch.await();
    }

    @Test
    public void exposureChange_visibleRect() throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mActivityRule.runOnUiThread(new Runnable() {
            public void run() {
                final MraidView testMraidView = mActivityRule.getActivity().mraidView;
                mActivityRule.getActivity().setVisibility(true);
                mActivityRule.getActivity().drawView();

                testMraidView.setMraidWebViewDebugListener(new MraidWebViewDebugListener() {
                    @Override
                    public boolean onJsAlert(@NonNull String message, @NonNull JsResult result) {
                        try {
                            JSONObject visibleRectangle = new JSONObject(message);
                            assertEquals(visibleRectangle.getInt("width"), TestActivity.width);
                            assertEquals(visibleRectangle.getInt("height"), TestActivity.height);
                            countDownLatch.countDown();
                        } catch (JSONException e) {
                            fail();
                        }
                        return false;
                    }

                    @Override
                    public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                        return false;
                    }
                });

                String html = TestHelper.readHtml(mActivityRule.getActivity(), "check_exposure_change_visible_rect.html");
                testMraidView.setHtml(html);
                testMraidView.load();
            }
        });

        countDownLatch.await();
    }

    @Test
    public void getPlacementType_INLINE() throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mActivityRule.runOnUiThread(new Runnable() {
            public void run() {
                final MraidView testMraidView = mActivityRule.getActivity().mraidView;
                mActivityRule.getActivity().setVisibility(true);
                mActivityRule.getActivity().drawView();

                testMraidView.setMraidWebViewDebugListener(new MraidWebViewDebugListener() {
                    @Override
                    public boolean onJsAlert(@NonNull String message, @NonNull JsResult result) {
                        if (message.equals("pass")) {
                            countDownLatch.countDown();
                        } else {
                            fail();
                        }
                        return false;
                    }

                    @Override
                    public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                        return false;
                    }
                });

                String html = TestHelper.readHtml(mActivityRule.getActivity(), "mraid_placement_type_inline.html");
                testMraidView.setHtml(html);
                testMraidView.load();
            }
        });

        countDownLatch.await();
    }

    @Test
    public void getPlacementType_INTERSTITIAL() throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mActivityRule.runOnUiThread(new Runnable() {
            public void run() {
                MraidInterstitial mraidInterstitial = new MraidInterstitial(mActivityRule.getActivity());
                mraidInterstitial.setMraidWebViewDebugListener(new MraidWebViewDebugListener() {
                    @Override
                    public boolean onJsAlert(@NonNull String message, @NonNull JsResult result) {
                        if (message.equals("pass")) {
                            countDownLatch.countDown();
                        } else {
                            fail();
                        }
                        return false;
                    }

                    @Override
                    public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                        return false;
                    }
                });

                String html = TestHelper.readHtml(mActivityRule.getActivity(), "mraid_placement_type_interstitial.html");
                mraidInterstitial.setHtml(html);
                mraidInterstitial.setMraidInterstitialListener(new MraidInterstitialListener() {
                    @Override
                    public void onMraidInterstitialLoaded(MraidInterstitial mraidInterstitial) {
                        mraidInterstitial.show();
                    }

                    @Override
                    public void onMraidInterstitialFailedToLoad(MraidInterstitial mraidInterstitial) {

                    }

                    @Override
                    public void onMraidInterstitialShown(MraidInterstitial mraidInterstitial) {

                    }

                    @Override
                    public void onMraidInterstitialFailedToShow(MraidInterstitial mraidInterstitial) {

                    }

                    @Override
                    public void onMraidInterstitialUnloaded(MraidInterstitial mraidInterstitial) {

                    }

                    @Override
                    public void onMraidInterstitialClicked(MraidInterstitial mraidInterstitial) {

                    }

                    @Override
                    public void onMraidInterstitialClosed(MraidInterstitial mraidInterstitial) {

                    }
                });
                mraidInterstitial.load();

            }
        });

        countDownLatch.await();
    }

    @Test
    public void getAppOrientation_portrait() throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mActivityRule.runOnUiThread(new Runnable() {
            public void run() {
                final MraidView testMraidView = mActivityRule.getActivity().mraidView;
                mActivityRule.getActivity().setVisibility(true);
                mActivityRule.getActivity().drawView();
                mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                testMraidView.setMraidWebViewDebugListener(new MraidWebViewDebugListener() {
                    @Override
                    public boolean onJsAlert(@NonNull String message, @NonNull JsResult result) {
                        if (message.equals("pass")) {
                            countDownLatch.countDown();
                        } else {
                            fail();
                        }
                        return false;
                    }

                    @Override
                    public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                        return false;
                    }
                });

                String html = TestHelper.readHtml(mActivityRule.getActivity(), "mraid_getAppOrientation_portrait.html");
                testMraidView.setHtml(html);
                testMraidView.load();
            }
        });

        countDownLatch.await();
    }

    @Test
    public void getAppOrientation_landscape() throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mActivityRule.runOnUiThread(new Runnable() {
            public void run() {
                final MraidView testMraidView = mActivityRule.getActivity().mraidView;
                mActivityRule.getActivity().setVisibility(true);
                mActivityRule.getActivity().drawView();
                mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                testMraidView.setMraidWebViewDebugListener(new MraidWebViewDebugListener() {
                    @Override
                    public boolean onJsAlert(@NonNull String message, @NonNull JsResult result) {
                        if (message.equals("pass")) {
                            countDownLatch.countDown();
                        } else {
                            fail();
                        }
                        return false;
                    }

                    @Override
                    public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                        return false;
                    }
                });

                String html = TestHelper.readHtml(mActivityRule.getActivity(), "mraid_getAppOrientation_landscape.html");
                testMraidView.setHtml(html);
                testMraidView.load();
            }
        });

        countDownLatch.await();
    }

    @Test
    public void getLocation() throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mActivityRule.runOnUiThread(new Runnable() {
            public void run() {
                final MraidView testMraidView = mActivityRule.getActivity().mraidView;
                mActivityRule.getActivity().setVisibility(true);
                mActivityRule.getActivity().drawView();
                testMraidView.setMraidWebViewDebugListener(new MraidWebViewDebugListener() {
                    @Override
                    public boolean onJsAlert(@NonNull String message, @NonNull JsResult result) {
                        if (message.equals("pass")) {
                            countDownLatch.countDown();
                        } else {
                            fail();
                        }
                        return false;
                    }

                    @Override
                    public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                        return false;
                    }
                });

                String html = TestHelper.readHtml(mActivityRule.getActivity(), "mraid_getLocation.html");
                testMraidView.setHtml(html);
                List<MraidNativeFeature> nativeFeatureList = new ArrayList<>();
                nativeFeatureList.add(MraidNativeFeature.LOCATION);
                testMraidView.setSupportedFeatures(nativeFeatureList, new MraidNativeFeatureListener() {
                    @Override
                    public void mraidNativeFeatureSendSms(String url) {

                    }

                    @Override
                    public void mraidNativeFeatureCallTel(String url) {

                    }

                    @Override
                    public void mraidNativeFeatureCreateCalendarEvent(String eventJSON) {

                    }

                    @Override
                    public void mraidNativeFeaturePlayVideo(String url) {

                    }

                    @Override
                    public void mraidNativeFeatureStorePicture(String url) {

                    }

                    @Override
                    public void mraidNativeFeatureOpenBrowser(String url) {

                    }

                    @Override
                    public Location mraidNativeFeatureGetLocation() {
                        Location location = new Location("TEST");
                        location.setLatitude(1.0);
                        location.setLongitude(2.0);
                        return location;
                    }
                });
                testMraidView.load();
            }
        });

        countDownLatch.await();
    }

    @Test
    public void open() throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mActivityRule.runOnUiThread(new Runnable() {
            public void run() {
                final MraidView testMraidView = mActivityRule.getActivity().mraidView;
                mActivityRule.getActivity().setVisibility(true);
                mActivityRule.getActivity().drawView();

                String html = TestHelper.readHtml(mActivityRule.getActivity(), "mraid_open.html");
                testMraidView.setHtml(html);
                List<MraidNativeFeature> nativeFeatureList = new ArrayList<>();
                testMraidView.setSupportedFeatures(nativeFeatureList, new MraidNativeFeatureListener() {
                    @Override
                    public void mraidNativeFeatureSendSms(String url) {

                    }

                    @Override
                    public void mraidNativeFeatureCallTel(String url) {

                    }

                    @Override
                    public void mraidNativeFeatureCreateCalendarEvent(String eventJSON) {

                    }

                    @Override
                    public void mraidNativeFeaturePlayVideo(String url) {

                    }

                    @Override
                    public void mraidNativeFeatureStorePicture(String url) {

                    }

                    @Override
                    public void mraidNativeFeatureOpenBrowser(String url) {
                        assertEquals(url, "http://appodeal.com");
                        countDownLatch.countDown();
                    }

                    @Override
                    public Location mraidNativeFeatureGetLocation() {
                        return null;
                    }
                });

                testMraidView.setMraidViewListener(new MraidViewListener() {
                    @Override
                    public void onMraidViewLoaded(MraidView mraidView) {
                        clickOnView(mraidView);
                    }

                    @Override
                    public void onMraidViewFailedToLoad(MraidView mraidView) {

                    }

                    @Override
                    public void onMraidViewUnloaded(MraidView mraidView) {

                    }

                    @Override
                    public void onMraidViewExpanded(MraidView mraidView) {

                    }

                    @Override
                    public void onMraidViewResized(MraidView mraidView) {

                    }

                    @Override
                    public void onMraidViewClicked(MraidView mraidView) {

                    }

                    @Override
                    public void onMraidViewClosed(MraidView mraidView) {

                    }
                });
                testMraidView.load();
            }
        });

        countDownLatch.await();
    }

    @Test
    public void playVideo() throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mActivityRule.runOnUiThread(new Runnable() {
            public void run() {
                final MraidView testMraidView = mActivityRule.getActivity().mraidView;
                mActivityRule.getActivity().setVisibility(true);
                mActivityRule.getActivity().drawView();

                String html = TestHelper.readHtml(mActivityRule.getActivity(), "mraid_playVideo.html");
                testMraidView.setHtml(html);
                List<MraidNativeFeature> nativeFeatureList = new ArrayList<>();
                testMraidView.setSupportedFeatures(nativeFeatureList, new MraidNativeFeatureListener() {
                    @Override
                    public void mraidNativeFeatureSendSms(String url) {

                    }

                    @Override
                    public void mraidNativeFeatureCallTel(String url) {

                    }

                    @Override
                    public void mraidNativeFeatureCreateCalendarEvent(String eventJSON) {

                    }

                    @Override
                    public void mraidNativeFeaturePlayVideo(String url) {
                        assertEquals(url, "http://appodeal.com");
                        countDownLatch.countDown();
                    }

                    @Override
                    public void mraidNativeFeatureStorePicture(String url) {

                    }

                    @Override
                    public void mraidNativeFeatureOpenBrowser(String url) {

                    }

                    @Override
                    public Location mraidNativeFeatureGetLocation() {
                        return null;
                    }
                });

                testMraidView.setMraidViewListener(new MraidViewListener() {
                    @Override
                    public void onMraidViewLoaded(MraidView mraidView) {
                        clickOnView(mraidView);
                    }

                    @Override
                    public void onMraidViewFailedToLoad(MraidView mraidView) {

                    }

                    @Override
                    public void onMraidViewUnloaded(MraidView mraidView) {

                    }

                    @Override
                    public void onMraidViewExpanded(MraidView mraidView) {

                    }

                    @Override
                    public void onMraidViewResized(MraidView mraidView) {

                    }

                    @Override
                    public void onMraidViewClicked(MraidView mraidView) {

                    }

                    @Override
                    public void onMraidViewClosed(MraidView mraidView) {

                    }
                });
                testMraidView.load();
            }
        });

        countDownLatch.await();
    }

    @Test
    public void unload() throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mActivityRule.runOnUiThread(new Runnable() {
            public void run() {
                final MraidView testMraidView = mActivityRule.getActivity().mraidView;
                mActivityRule.getActivity().setVisibility(true);
                mActivityRule.getActivity().drawView();

                String html = TestHelper.readHtml(mActivityRule.getActivity(), "mraid_unload.html");
                testMraidView.setHtml(html);

                testMraidView.setMraidViewListener(new MraidViewListener() {
                    @Override
                    public void onMraidViewLoaded(MraidView mraidView) {
                        clickOnView(mraidView);
                    }

                    @Override
                    public void onMraidViewFailedToLoad(MraidView mraidView) {

                    }

                    @Override
                    public void onMraidViewUnloaded(MraidView mraidView) {
                        countDownLatch.countDown();
                    }

                    @Override
                    public void onMraidViewExpanded(MraidView mraidView) {

                    }

                    @Override
                    public void onMraidViewResized(MraidView mraidView) {

                    }

                    @Override
                    public void onMraidViewClicked(MraidView mraidView) {

                    }

                    @Override
                    public void onMraidViewClosed(MraidView mraidView) {

                    }
                });
                testMraidView.load();
            }
        });

        countDownLatch.await();
    }




    private static void clickOnView(View mraidView) {
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis() + 100;
        float x = 0.0f;
        float y = 0.0f;
        int metaState = 0;
        MotionEvent motionEventUp = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_UP,
                x,
                y,
                metaState
        );
        MotionEvent motionEventDown = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_DOWN,
                x,
                y,
                metaState
        );
        mraidView.dispatchTouchEvent(motionEventDown);
        mraidView.dispatchTouchEvent(motionEventUp);
        mraidView.performClick();
    }
}
