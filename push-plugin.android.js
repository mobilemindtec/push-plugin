module.exports = (function () {
    var app = require('application');
    var context = app.android.context;

    (function() {
        //debugger;
        // Hook on the application events
        com.telerik.pushplugin.PushLifecycleCallbacks.registerCallbacks(app.android.nativeApp);
    })();

    var pluginObject = {

        register: function (options, successCallback, errorCallback) {
            com.telerik.pushplugin.fcm.PushPlugin.register(context, options.senderID,
                //Success
                new com.telerik.pushplugin.PushPluginListener(
                    {
                        success: successCallback,
                        error: errorCallback
                    })
            );
        },

        unregister: function (onSuccessCallback, onErrorCallback, options) {
            com.telerik.pushplugin.fcm.PushPlugin.unregister(context, options.senderID, new com.telerik.pushplugin.PushPluginListener(
                {
                    success: onSuccessCallback
                }
            ));
        },

        fcmRegister: function (options, successCallback, errorCallback) {
            com.telerik.pushplugin.fcm.PushPlugin.register(context, options.senderID,
                //Success
                new com.telerik.pushplugin.PushPluginListener(
                    {
                        success: successCallback,
                        error: errorCallback
                    })
            );
        },

        fcmUnregister: function (onSuccessCallback, onErrorCallback, options) {
            com.telerik.pushplugin.fcm.PushPlugin.unregister(context, options.senderID, new com.telerik.pushplugin.PushPluginListener(
                {
                    success: onSuccessCallback
                }
            ));
        },

        onMessageReceived: function (callback) {
            com.telerik.pushplugin.fcm.PushPlugin.setOnMessageReceivedCallback(
                new com.telerik.pushplugin.PushPluginListener(
                    {
                        success: callback
                    })
            );
        },

        onTokenRefresh : function (callback) {
            com.telerik.pushplugin.fcm.PushPlugin.setOnTokenRefreshCallback(
                new com.telerik.pushplugin.PushPluginListener(
                    {
                        success: callback
                    })
            );
        },

        areNotificationsEnabled : function (callback) {
            var bool = com.telerik.pushplugin.fcm.PushPlugin.areNotificationsEnabled();
            callback(bool);
        },

        fcmOnMessageReceived: function (callback) {
            com.telerik.pushplugin.fcm.PushPlugin.setOnMessageReceivedCallback(
                new com.telerik.pushplugin.PushPluginListener(
                    {
                        success: callback
                    })
            );
        },

        fcmOnTokenRefresh : function (callback) {
            com.telerik.pushplugin.fcm.PushPlugin.setOnTokenRefreshCallback(
                new com.telerik.pushplugin.PushPluginListener(
                    {
                        success: callback
                    })
            );
        },

        fcmAreNotificationsEnabled : function (callback) {
            var bool = com.telerik.pushplugin.fcm.PushPlugin.areNotificationsEnabled();
            callback(bool);
        }        

    };
    return pluginObject;
})();
