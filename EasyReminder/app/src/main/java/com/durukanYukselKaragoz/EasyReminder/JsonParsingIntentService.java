package com.durukanYukselKaragoz.EasyReminder;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonParsingIntentService extends IntentService {
    //JsonRelated variables
    String jsonStr;
    JSONObject eventJSONObject;
    JSONArray eventTypes;

    //Tag variables
    public static final String TAG_EVENTTYPES = "eventTypes";
    public JsonParsingIntentService() {
        super("JsonParsingIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        readEventTypesFromAssets();
    }
    //Reads Json Objects from a previously created Json File
    public void readEventTypesFromAssets(){
        jsonStr = loadFileFromAssets("eventTypes.json");
        if (jsonStr != null) {
            try {
                eventJSONObject = new JSONObject(jsonStr);
                eventTypes = eventJSONObject.getJSONArray(TAG_EVENTTYPES);
                ArrayList<String> listist = new ArrayList<String>();

                // looping through all eventTypes
                for (int i = 0; i < eventTypes.length(); i++) {
                    listist.add("" + eventTypes.get(i));
                }

                // Send the Bitmap using a broadcast to inform the activity about the data
                Intent broadcastIntent = new Intent();
                broadcastIntent.putStringArrayListExtra("listeventtype", listist);
                broadcastIntent.setAction("LIST_SENDING_ACTION");
                getBaseContext().sendBroadcast(broadcastIntent);

            } catch (JSONException ee) {
                ee.printStackTrace();
            }
        }
    }
    //Retrieves the file from the assets folder
    private String loadFileFromAssets(String fileName) {
        String file = null;
        try {

            InputStream is = getBaseContext().getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            file = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return file;
    }

    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
