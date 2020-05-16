package com.telerik.pushplugin;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.telerik.pushplugin.fcm.PushPlugin;


/**
 * Responsible for obtaining a Token from the GCM service.
 * By design, this must happen in async way in a Thread.
 */
public class ObtainTokenThread {
    private static final String TAG = "ObtainTokenThread";
    private final PushPluginListener callbacks;

    private String token;
    private final String projectId;
    private final Context appContext;

    public ObtainTokenThread(String projectID, Context appContext, PushPluginListener callbacks) {
        this.projectId = projectID;
        this.appContext = appContext;
        this.callbacks = callbacks;
    }

    public void run() {
        getToken2();
    }
    /*
    private String getToken() throws IOException {

        Log.d(TAG, "getTokenFromFCM.");

        FirebaseInstanceId instanceId = FirebaseInstanceId.getInstance();
        this.token = instanceId.getInstanceId().getResult().getToken();

        Log.d(TAG, ""+this.token);

        if(callbacks != null) {
            Log.d(TAG, "Calling listener callback with token: " + token);
            callbacks.success(token);
        } else {
            Log.d(TAG, "Token call returned, but no callback provided.");
        }

        // TODO: Wrap the whole callback.
        PushPlugin.isActive = true;
        return this.token;
    }*/


    public void getToken2(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            callbacks.error("Error while retrieving a token: " + task.getException().getMessage());
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();
                        PushPlugin.isActive = true;

                        if(callbacks != null) {
                            Log.d(TAG, "Calling listener callback with token: " + token);
                            callbacks.success(token);
                        } else {
                            Log.d(TAG, "Token call returned, but no callback provided.");
                        }

                    }
                });
    }
}