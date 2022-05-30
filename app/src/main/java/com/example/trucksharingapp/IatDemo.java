package com.example.trucksharingapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trucksharingapp.util.JsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class IatDemo extends Activity implements OnClickListener {
    private static String TAG = IatDemo.class.getSimpleName();
    private SpeechRecognizer mIat;
    private RecognizerDialog mIatDialog;
    private HashMap<String, String> mIatResults = new LinkedHashMap<>();

    private EditText mResultText;
    private Toast mToast;
    private SharedPreferences mSharedPreferences;
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private String[] languageEntries;
    private String[] languageValues;
    private String language = "us_en";
    private int selectedNum = 0;

    private String resultType = "json";

    private StringBuffer buffer = new StringBuffer();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.iatdemo);

        languageEntries = getResources().getStringArray(R.array.iat_language_entries);
        languageValues = getResources().getStringArray(R.array.iat_language_value);
        initLayout();
        mIat = SpeechRecognizer.createRecognizer(IatDemo.this, mInitListener);
        mIatDialog = new RecognizerDialog(IatDemo.this, mInitListener);
        mSharedPreferences = getSharedPreferences(IatSettings.PREFER_NAME,
                Activity.MODE_PRIVATE);
        mResultText = ((EditText) findViewById(R.id.iat_text));
//        showContacts = (EditText) findViewById(R.id.iat_contacts);
    }

    private void initLayout() {
        findViewById(R.id.iat_recognize).setOnClickListener(IatDemo.this);
        findViewById(R.id.iat_stop).setOnClickListener(IatDemo.this);
//        findViewById(R.id.languageText).setOnClickListener(IatDemo.this);
    }

    int ret = 0;

    @Override
    public void onClick(View view) {
        if (null == mIat) {
            this.showTip("Failed to create the object. Make sure libmsc.so is correctly placed and createUtility has been called to initialize it.");
            return;
        }

        switch (view.getId()) {
            // Start dictation
            // How to determine the end of a dictation: OnResult isLast=true or onError
            case R.id.iat_recognize:
                buffer.setLength(0);
                // Clear the display
                mResultText.setText(null);
                mIatResults.clear();
                //  Setting parameters
                setParam();
                boolean isShowDialog = mSharedPreferences.getBoolean(
                        getString(R.string.pref_key_iat_show), true);
                if (isShowDialog) {
                    // Show Dictation Dialog
                    mIatDialog.setListener(mRecognizerDialogListener);
                    mIatDialog.show();
                    showTip(getString(R.string.text_begin));
                } else {
                    // Do not show the dictation dialog
                    ret = mIat.startListening(mRecognizerListener);
                    if (ret != ErrorCode.SUCCESS) {
                        showTip("Dictation failed, error code：" + ret + ",Please click on the websiteh ttps://www.xfyun.cn/document/error-code Query Solution");
                    } else {
                        showTip(getString(R.string.text_begin));
                    }
                }
                break;
//            // Audio stream recognition
//            case R.id.languageText:
//                setLanguage(view);
//                break;
            // Stop Dictation
            case R.id.iat_stop:
                closeActivity();
                break;
            default:
                break;
        }
    }

    /**
     * Initialize the listener.
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("Initialization failed with error code：" + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            }
        }
    };

    private void setLanguage(View v) {
        new AlertDialog.Builder(v.getContext()).setTitle("Language Language Type")
                .setSingleChoiceItems(languageEntries,
                        1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                language = languageValues[which];
                                selectedNum = which;
                                dialog.dismiss();
                            }
                        }).show();
        mIat.setParameter(SpeechConstant.LANGUAGE, language);
    }


    /**
     * Dictation listener.
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            //This callback indicates that the sdk internal recorder is ready and the user can start voice input
            showTip("Start talking");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // Error code: 10118 (You are not talking), it may be that the recorder permission is disabled and the user needs to be prompted to open the recording permission of the application.
            showTip(error.getPlainDescription(true));
        }

        @Override
        public void onEndOfSpeech() {
            // This callback indicates that the endpoint of the speech has been detected, the recognition process has been entered, and the speech input is no longer accepted
            showTip("End of speech");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, results.getResultString());
            if (isLast) {
                Log.d(TAG, "onResult End");
            }
            if (resultType.equals("json")) {
                printResult(results);
                return;
            }
            if (resultType.equals("plain")) {
                buffer.append(results.getResultString());
                mResultText.setText(buffer.toString());
                mResultText.setSelection(mResultText.length());
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("Currently talking, volume level = " + volume + " Return audio data = " + data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };

    /**
     * Show results
     */
    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());
        String sn = null;
        // Read the sn field in the json result
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        mResultText.setText(resultBuffer.toString());
        mResultText.setSelection(mResultText.length());
    }

    /**
     * Dictating UI listener
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        // return result
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);
        }

        // Identifying callback errors
        public void onError(SpeechError error) {
            showTip(error.getPlainDescription(true));
        }

    };

    private void closeActivity() {
        Intent intent = new Intent();
        intent.putExtra("voice_text", mResultText.getText().toString());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void showTip(final String str) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * Parameter Setting
     *
     * @return
     */
    public void setParam() {
        //Clear parameters
        mIat.setParameter(SpeechConstant.PARAMS, null);
        //Setting up the dictation engine
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        //Set the return result format
        mIat.setParameter(SpeechConstant.RESULT_TYPE, resultType);

//        if (language.equals("zh_cn")) {
//            String lag = mSharedPreferences.getString("iat_language_preference",
//                    "mandarin");
//            // 设置语言
//            Log.e(TAG, "language = " + language);
//            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
//            // 设置语言区域
//            mIat.setParameter(SpeechConstant.ACCENT, lag);
//        } else {
//            mIat.setParameter(SpeechConstant.LANGUAGE, language);
//        }
        mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        Log.e(TAG, "last language:" + mIat.getParameter(SpeechConstant.LANGUAGE));

        //  This is used to set the dialog not to display error code information
        mIat.setParameter("view_tips_plain", "false");

        // Set the front-end point of voice: mute timeout time, that is, how long the user does not speak is treated as a timeout
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

        // Set voice back-end point: back-end point mute detection time, that is, how long the user stops talking that is no longer input, automatically stop recording
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));

        // Set punctuation, set to "0" to return results without punctuation, set to "1" to return results with punctuation
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));

        // Set the audio save path, save audio format support pcm, wav.
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
                getExternalFilesDir("msc").getAbsolutePath() + "/iat.wav");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIat != null) {
            // Release connection on exit
            mIat.cancel();
            mIat.destroy();
        }
    }
}
