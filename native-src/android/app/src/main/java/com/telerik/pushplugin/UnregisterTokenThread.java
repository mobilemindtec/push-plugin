package com.telerik.pushplugin;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

/**
 * Responsible for unregister device from GCM service functionality.
 * By design, this must happen in async way in a Thread.
 */
public class UnregisterTokenThread extends Thread {
    private static final String TAG = "UnregisterTokenThread";

    private final String projectId;
    private final Context appContext;
    private final PushPluginListener callbacks;
    private boolean fcm;

    public UnregisterTokenThread(String projectID, Context appContext, PushPluginListener callbacks){
        this(false, projectID, appContext, callbacks);
    }
    public UnregisterTokenThread(boolean fcm, String projectID, Context appContext, PushPluginListener callbacks) {
        this.projectId = projectID;
        this.appContext = appContext;
        this.callbacks = callbacks;
        this.fcm = fcm;
    }

    @Override
    public void run() {
        try {
            if(this.fcm)
                deleteTokenFromFCM();
            else
                deleteTokenFromGCM();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteTokenFromGCM() throws IOException {
        InstanceID instanceID = InstanceID.getInstance(this.appContext);
        instanceID.deleteToken(this.projectId,
                GoogleCloudMessaging.INSTANCE_ID_SCOPE);

        Log.d(TAG, "Token deleted!");

        if(callbacks != null) {
            callbacks.success("Device unregistered!");
        }

        // TODO: Wrap the whole callback.
        PushPlugin.isActive = false;        
    }

    private void deleteTokenFromFCM() throws IOException {
        FirebaseInstanceId instanceId = FirebaseInstanceId.getInstance();
        instanceId.deleteToken(this.projectId,
                GoogleCloudMessaging.INSTANCE_ID_SCOPE);

        Log.d(TAG, "Token deleted!");

        if(callbacks != null) {
            callbacks.success("Device unregistered!");
        }

        // TODO: Wrap the whole callback.
        com.telerik.pushplugin.fcm.PushPlugin.isActive = false;
    }
}