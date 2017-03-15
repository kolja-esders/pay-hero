package edu.pietro.team.payhero.helper;

import android.hardware.camera2.params.Face;
import android.util.JsonReader;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by david on 26.11.16.
 */


public class PostHelper {

    public static String LING_KEY = "e75ee2412fc74adb901a33398ec07696";
    public static String VISION_KEY = "8c7bb9fea8c14560a4d3c000a5f775e9";
    public static String FACE_KEY = "9b2ecf2bc1a24ed8ac7c059deb5fab7c";

    public static String KOLJA_KEY = "2f154220-39bf-4b64-8b36-b2cce3a68492";
    public static String DAVID_KEY = "0704a7f3-dda2-4001-b064-e082f9ee036c";
    public static String MAXIM_KEY = "829e847e-29d4-45aa-aba2-4704981d27f1";

    public static String postKey = "485431330021fc2e";

    


    public static void transfer(String IBAN, String Name, String amount) throws Exception{

        JSONObject loginJ = loginPB();
        String token = loginJ.getString("token");

        JSONObject finStatus = getFinanceStatus(token);
        JSONObject devInfo = getAuthDevice(token);

        JSONObject template = getTransferTemplate(token);

        String ownIban = "DE20100100100005579147";//finStatus.getJSONArray("accounts").getJSONObject(0).getString("iban");
        devInfo.getJSONObject("bestSign").getJSONArray("devices").getJSONObject(0).put("authorizationState", "SELECTED");

        template.put("authorizationDevice", devInfo.getJSONObject("bestSign").getJSONArray("devices").getJSONObject(0));

        template.getJSONObject("creditTransfer").put("amount", amount);
        template.getJSONObject("creditTransfer").getJSONObject("recipient").put("iban", IBAN);
        template.getJSONObject("creditTransfer").getJSONObject("recipient").put("paymentName" , Name);
        template.getJSONObject("creditTransfer").getJSONObject("recipient").put("accountHolder", Name);
        template.getJSONObject("creditTransfer").getJSONObject("sender").put("iban", ownIban);

        JSONObject trans = creditTransfer(template, token);

        String finLink = trans.getJSONArray("links").getJSONObject(0).getString("href");

        boolean success = commitTransfer(finLink, token);
        if (!success) {
            Log.w("PAYMENT", "Unable to pay!");
        }
    }


    public static JSONObject loginPB() throws Exception{

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=---011000010111000001101001");
        RequestBody body = RequestBody.create(mediaType, "-----011000010111000001101001\r\nContent-Disposition: form-data; name=\"username\"\r\n\r\nHackathonNov01_10\r\n-----011000010111000001101001\r\nContent-Disposition: form-data; name=\"password\"\r\n\r\nhat1116\r\n-----011000010111000001101001--");
        Request request = new Request.Builder()
                .url("https://hackathon.postbank.de/bank-api/gold/postbankid/token")
                .post(body)
                .addHeader("api-key", postKey)
                .addHeader("device-signatur", "1234567891234567")
                .addHeader("content-type", "multipart/form-data; boundary=---011000010111000001101001")
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();
        String resp = response.body().string();
        JSONObject loginJ = new JSONObject(resp);

        return loginJ;

    }



    public static JSONObject getFinanceStatus(String token) throws Exception{

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://hackathon.postbank.de:443/bank-api/gold/postbankid/?refreshCache=true")
                .get()
                .addHeader("x-auth", token)
                .addHeader("api-key", postKey)
                .addHeader("device-signatur", "1234567891234567")
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();
        String resp = response.body().string();
        JSONObject finStatus = new JSONObject(resp);

        return finStatus;

    }




    public static JSONObject getAuthDevice(String token) throws Exception{

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://hackathon.postbank.de:443/bank-api/gold/postbankid/authorizations")
                .get()
                .addHeader("x-auth", token)
                .addHeader("api-key", postKey)
                .addHeader("device-signatur", "1234567891234567")
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();
        String resp = response.body().string();
        JSONObject devInfo = new JSONObject(resp);

        return devInfo;

    }

    public static JSONObject getTransferTemplate(String token) throws Exception{

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://hackathon.postbank.de:443/bank-api/gold/postbankid/credittransfer")
                .get()
                .addHeader("x-auth", token)
                .addHeader("api-key", postKey)
                .addHeader("device-signatur", "1234567891234567")
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();


        String resp = response.body().string();
        JSONObject template = new JSONObject(resp);

        return template;

    }

    public static JSONObject creditTransfer(JSONObject json, String token) throws Exception{

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        Request request = new Request.Builder()
                .url("https://hackathon.postbank.de:443/bank-api/gold/postbankid/credittransfer")
                .post(body)
                .addHeader("x-auth", token)
                .addHeader("api-key", postKey)
                .addHeader("content-type", "application/json")
                .addHeader("device-signatur", "1234567891234567")
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();

        String resp = response.body().string();
        JSONObject trans = new JSONObject(resp);

        return trans;

    }

    public static boolean commitTransfer(String link, String token) throws Exception{

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(link)
                .get()
                .addHeader("x-auth", token)
                .addHeader("api-key", postKey)
                .addHeader("content-type", "application/json")
                .addHeader("device-signatur", "1234567891234567")
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();

        return response.isSuccessful();
    }


    public static String sendMSPost(byte[] data, String apiKey) throws Exception{

        String url = "https://api.projectoxford.ai/linguistics/v1.0/analyze";
        URL obj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

        conn.setConnectTimeout(5000);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Ocp-Apim-Subscription-Key", apiKey);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");

        OutputStream os = conn.getOutputStream();
        os.write(data);
        os.close();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        return response.toString();

    }


    public static String sendMSText(String text) throws Exception{

        String json = "{\n" +
                "\t\"language\" : \"en\",\n" +
                "\t\"analyzerIds\" : [\"4fa79af1-f22c-408d-98bb-b7d7aeef7f04\"],\n" +
                "\t\"text\" : \""+ text +"\" \n" +
                "}";

        return sendMSPost(json.getBytes("UTF-8"), LING_KEY);

    }

    public static String sendOcrPost(String contentType, byte[] data, String apiKey) throws Exception{

        String url = "https://api.projectoxford.ai/vision/v1.0/ocr?language=de&detectOrientation=true";
        //String url = "https://api.projectoxford.ai/vision/v1.0/ocr";
        URL obj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

        conn.setConnectTimeout(5000);
        conn.setRequestProperty("Content-Type", contentType);
        conn.setRequestProperty("Ocp-Apim-Subscription-Key", apiKey);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");

        OutputStream os = conn.getOutputStream();
        os.write(data);
        os.close();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        return response.toString();

    }


    public static String sendOcrText(String contentType, String data) throws Exception {

        if (contentType.equals("application/json")) {
            data = "{\n" +
                    "\t\"url\" : \"" + data + "\" \n" +
                    "}";
        }

        return sendOcrPost(contentType, data.getBytes("UTF-8"), VISION_KEY);

    }

    public static String detectFace(byte[] image) throws Exception {
        String url = "https://api.projectoxford.ai/face/v1.0/detect";
        URL obj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

        conn.setConnectTimeout(5000);
        conn.setRequestProperty("Content-Type", "application/octet-stream");
        //conn.setRequestProperty("Content-Type", "application/json");
        //image = "{\"url\": \"http://pngsammlung.com/thumbs/people/face/face-02.png\"}".getBytes("UTF-8");
        conn.setRequestProperty("Ocp-Apim-Subscription-Key", FACE_KEY);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");

        OutputStream os = conn.getOutputStream();
        os.write(image);
        os.close();

        int status = conn.getResponseCode();
        if (status >= 400 && status < 600) {
            Log.e("CONNECTION", status + ": " +  IOUtils.toString(conn.getErrorStream(), "UTF-8"));
            return "";
        }

        /*BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();*/

        //print result
        //String res = response.toString();
        String res = IOUtils.toString(conn.getInputStream(), "UTF-8");
        try {
            JSONArray ja = new JSONArray(res);
            return ja.getJSONObject(0).getString("faceId");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public static String identifyFace(String faceId) throws Exception {
        String url = "https://api.projectoxford.ai/face/v1.0/identify";
        URL obj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

        conn.setConnectTimeout(5000);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Ocp-Apim-Subscription-Key", FACE_KEY);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");

        String data = "{    \n" +
                "    \"personGroupId\":\"12345\",\n" +
                "    \"faceIds\":[\n" +
                "        \"" + faceId + "\",\n" +
                "    ],\n" +
                "    \"maxNumOfCandidatesReturned\":1,\n" +
                "    \"confidenceThreshold\": 0.5\n" +
                "}";

        OutputStream os = conn.getOutputStream();
        os.write(data.getBytes("UTF-8"));
        os.close();

        int status = conn.getResponseCode();
        if (status >= 400 && status < 600) {
            Log.e("CONNECTION", status + ": " + IOUtils.toString(conn.getErrorStream(), "UTF-8"));
            return "";
        }

        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        try {
            JSONArray ja = new JSONArray(response.toString());
            return ja.getJSONObject(0).getJSONArray("candidates").getJSONObject(0).getString("personId");
        } catch (Exception e) {
            return "";
        }
    }

}
