/*
 * Copyright (C) 2012 ParanoidAndroid Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.paranoid.ota;

import android.os.AsyncTask;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class FetchOnlineData extends AsyncTask<Integer, String, String>{
    
    protected static final String HTTP_HEADER = "http://paranoidandroid.d4net.org/";
    protected static final String DEVICE_NAME_PROPERTY = "ro.cm.device";
    protected static final String REQUEST_VERSION = "webtools/ota.php?device=";
    protected static final String REQUEST_FILENAME = "&request=1";
    protected static String mDevice;
    public String mResult;
    
    @Override
    protected String doInBackground(Integer... paramss) {
        mDevice = Utils.getProp(DEVICE_NAME_PROPERTY) + File.separator;
        try {
            String url = paramss[0] == 1 ? HTTP_HEADER+REQUEST_VERSION+mDevice+REQUEST_FILENAME : HTTP_HEADER+REQUEST_VERSION+mDevice;
            InputStream is = openHttpConnection(url);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    protected void onPostExecute(String result) {
          mResult = result;
    }
    
    void release(){
        this.cancel(true);
    }
    
    private InputStream openHttpConnection(String strURL) throws IOException {
        URLConnection conn;
        InputStream inputStream = null;
        URL url = new URL(strURL);
        conn = url.openConnection();
        HttpURLConnection httpConn = (HttpURLConnection) conn;
        httpConn.setRequestMethod("GET");
        httpConn.connect();
        if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            inputStream = httpConn.getInputStream();
        }
        return inputStream;
    }
}
