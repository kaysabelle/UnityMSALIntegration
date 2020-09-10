package com.example.msal;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.exception.MsalException;
import com.unity3d.player.UnityPlayer;

public class MsalWrapper extends UnityPlayerActivity {
    private final String[] SCOPES = {"User.Read"};
    private final String TAG = MsalWrapper.class.getSimpleName();

    private ISingleAccountPublicClientApplication singleAccountPublicClientApplication;

    public Activity getUnityActivity() {
        return UnityPlayer.currentActivity;
    }

    public Context getUnityContext() {
        return UnityPlayer.currentActivity.getApplicationContext();
    }

    public ISingleAccountPublicClientApplication getSingleAccountPublicClientApplication() throws MsalException, InterruptedException {
        if (singleAccountPublicClientApplication == null) {
            singleAccountPublicClientApplication = PublicClientApplication.createSingleAccountPublicClientApplication(getUnityContext(), R.raw.auth_config_single_account);
        }

        return singleAccountPublicClientApplication;
    }

    public void TryLogin(final PluginCallback callback) throws MsalException, InterruptedException {
        getSingleAccountPublicClientApplication().signIn(getUnityActivity(), "", SCOPES, new AuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                /* Successfully got a token, use it to call a protected resource - MSGraph */
                Log.d(TAG, "Successfully authenticated");
                callback.onSuccess(authenticationResult.getAccessToken());
            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());
                callback.onError(exception.toString());
            }

            @Override
            public void onCancel() {
                /* User canceled the authentication */
                Log.d(TAG, "User cancelled login.");
                callback.onError("User cancelled login.");
            }
        });
    }

    public void TryLogout(final PluginCallback callback) throws MsalException, InterruptedException {
        Boolean signedOut = getSingleAccountPublicClientApplication().signOut();
        if (signedOut) {
            callback.onSuccess("Successfully signed out");
        }
        else {
            callback.onError("Unable to sign out");
        }
    }
}
