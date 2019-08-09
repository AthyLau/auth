package com.bizseer.auth.util.http;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class HttpRequestHelper {

    public static String makeRequest(String urlString, String requestMethod, String data) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setRequestProperty("Content-Length", Integer.toString(data.getBytes("utf-8").length));
            connection.setRequestProperty("Content-Language", "zh-CN");
            connection.setRequestProperty("Authorization", "RYZ2TmkPPfiBcmWI713hWhiE4AQfAdcF");
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            if (!requestMethod.equals("GET") && !data.isEmpty()) {
                PrintWriter wr = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "utf-8"));
                wr.print(data);
                wr.close();
            }

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "utf-8"));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {
            log.error("Cannot send request with content '{}' by {} method to '{}', stack trace is: ", data, requestMethod, urlString, e);
            return "{}";
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
