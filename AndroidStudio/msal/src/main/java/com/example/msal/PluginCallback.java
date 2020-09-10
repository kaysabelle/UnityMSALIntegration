package com.example.msal;

public interface PluginCallback {
    public void onSuccess(String token);
    public void onError(String errorMessage);
}
