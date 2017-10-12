package com.telerik.pushplugin;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.FirebaseApp;
import java.io.IOException;

/**
 * Responsible for obtaining a Token from the GCM service.
 * By design, this must happen in async way in a Thread.
 */
public class ObtainTokenThread extends Thread {
    private static final String TAG = "ObtainTokenThread";
    private final PushPluginListener callbacks;

    private String token;
    private final String projectId;
    private final Context appContext;
    private boolean fcm;

    public ObtainTokenThread(String projectID, Context appContext, PushPluginListener callbacks){
        this(false, projectID, appContext, callbacks);
    }
    public ObtainTokenThread(boolean fcm, String projectID, Context appContext, PushPluginListener callbacks) {
        this.projectId = projectID;
        this.appContext = appContext;
        this.callbacks = callbacks;
        this.fcm = fcm;
    }

    @Override
    public void run() {
        try {
            if(this.fcm)
                token = getTokenFromFCM();
            else
                token = getTokenFromGCM();
        } catch (IOException e) {
            callbacks.error("Error while retrieving a token: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getTokenFromGCM() throws IOException {        
        
        Log.d(TAG, "getTokenFromGCM.");

        InstanceID instanceID = InstanceID.getInstance(this.appContext);
        this.token = instanceID.getToken(this.projectId,
                GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

        Log.d(TAG, this.token.toString());

        if(callbacks != null) {
            Log.d(TAG, "Calling listener callback with token: " + token);
            callbacks.success(token);
        } else {
            Log.d(TAG, "Token call returned, but no callback provided.");
        }

        // TODO: Wrap the whole callback.
        PushPlugin.isActive = true;
        com.telerik.pushplugin.fcm.PushPlugin.isActive = true;
        return this.token;
    }

    private String getTokenFromFCM() throws IOException {

        Log.d(TAG, "getTokenFromFCM.");

        FirebaseInstanceId instanceId = FirebaseInstanceId.getInstance();
        this.token = instanceId.getToken();

        Log.d(TAG, this.token.toString());

        if(callbacks != null) {
            Log.d(TAG, "Calling listener callback with token: " + token);
            callbacks.success(token);
        } else {
            Log.d(TAG, "Token call returned, but no callback provided.");
        }

        // TODO: Wrap the whole callback.
        PushPlugin.isActive = true;
        com.telerik.pushplugin.fcm.PushPlugin.isActive = true;
        return this.token;
    }


}