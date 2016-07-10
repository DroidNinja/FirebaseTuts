package vi.firebasenotifcations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

/**
 * Created by droidNinja on 09/07/16.
 */
public class DeepLinkManager implements GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<AppInviteInvitationResult>{

    private static final String TAG = DeepLinkManager.class.getSimpleName();
    private final GoogleApiClient mGoogleApiClient;
    private final FragmentActivity context;

    private DeepLinkListener deepLinkListener;

    public interface DeepLinkListener{
        void onConnectionError(String errorMessage);
    }

    public DeepLinkManager(FragmentActivity activity, DeepLinkListener linkListener) {
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(AppInvite.API)
                .enableAutoManage(activity, this)
                .build();

        this.context = activity;
        this.deepLinkListener = linkListener;
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        deepLinkListener.onConnectionError("Google Play Services error!");
    }

    public void checkForInvites(boolean autoLaunchDeepLink)
    {
        // Check for App Invite invitations and launch deep-link activity if possible.
        // Requires that an Activity is registered in AndroidManifest.xml to handle
        // deep-link URLs.

        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, context, autoLaunchDeepLink)
                .setResultCallback(this);
    }

    @Override
    public void onResult(@NonNull AppInviteInvitationResult result) {
        Log.d(TAG, "getInvitation:onResult:" + result.getStatus());
        if (result.getStatus().isSuccess()) {
            // Extract information from the intent
            Intent intent = result.getInvitationIntent();
            String deepLink = AppInviteReferral.getDeepLink(intent);
            String invitationId = AppInviteReferral.getInvitationId(intent);

            // Because autoLaunchDeepLink = true we don't have to do anything
            // here, but we could set that to false and manually choose
            // an Activity to launch to handle the deep link here.
            // ...
        }
    }
}
