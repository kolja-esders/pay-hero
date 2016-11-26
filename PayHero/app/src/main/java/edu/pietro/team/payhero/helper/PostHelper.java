package edu.pietro.team.payhero.helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by david on 26.11.16.
 */


public class PostHelper {

    public static String lingKey = "e75ee2412fc74adb901a33398ec07696";
    public static String VISION_KEY = "8c7bb9fea8c14560a4d3c000a5f775e9";

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

        return sendMSPost(json.getBytes("UTF-8"), lingKey);

    }

    public static String sendOcrPost(String contentType, byte[] data, String apiKey) throws Exception{

        String url = "https://api.projectoxford.ai/vision/v1.0/ocr";
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

        return sendOcrPost(contentType, data.getBytes("UTF-8"), visionKey);

    }


}
