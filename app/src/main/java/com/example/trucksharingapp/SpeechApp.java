package com.example.trucksharingapp;

import android.app.Application;

import com.iflytek.cloud.Setting;
import com.iflytek.cloud.SpeechUtility;

public class SpeechApp extends Application {

    @Override
    public void onCreate() {
        // Called at the entrance of the application to avoid the phone memory is too small, after killing the background process through the history intent into the Activity caused SpeechUtility object is null
        // If you call the initialization in the Application, you need to register the applicaiton in Mainifest
        // Note: This interface will return null object when called in non-main process, if you want to use speech function in non-main process, please add parameter: SpeechConstant.FORCE_LOGIN+"=true"
        // Use half-cent "," to separate between parameters.
        // Set the appid of your application, please do not add spaces and empty escapes between '=' and appid

        // Note: The appid must be the same as the downloaded SDK, otherwise there will be a 10407 error.

        SpeechUtility.createUtility(SpeechApp.this, "appid=f1b2fc03");

        // The following statement is used to set the logging switch (default on), set to false to turn off the voice cloud SDK log printing
        Setting.setShowLog(true);
        super.onCreate();
    }

}
