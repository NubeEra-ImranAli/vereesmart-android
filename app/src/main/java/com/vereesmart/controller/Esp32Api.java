package com.vereesmart.controller;

import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/** The board address is intentionally fixed: Wi-Fi is selected in Android Settings. */
public final class Esp32Api {
    private static final String BASE_URL = "http://192.168.4.1";
    public static JSONObject status() throws Exception { return request("/api/status", "GET", null); }
    public static JSONObject home() throws Exception { return request("/api/home", "POST", null); }
    public static JSONObject back() throws Exception { return request("/api/back", "POST", null); }
    public static JSONObject execute() throws Exception { return request("/api/execute", "POST", null); }
    public static JSONObject stop() throws Exception { return request("/api/stop", "POST", null); }
    public static JSONObject select(int grade, int project) throws Exception {
        return request("/api/select", "POST", new JSONObject().put("grade", grade).put("project", project).toString());
    }
    private static JSONObject request(String route, String method, String payload) throws Exception {
        HttpURLConnection c = (HttpURLConnection) new URL(BASE_URL + route).openConnection();
        c.setRequestMethod(method); c.setConnectTimeout(4500); c.setReadTimeout(4500); c.setRequestProperty("Accept", "application/json");
        if (payload != null) { c.setRequestProperty("Content-Type", "application/json"); c.setDoOutput(true); try (OutputStream out = c.getOutputStream()) { out.write(payload.getBytes("UTF-8")); } }
        int code = c.getResponseCode(); if (code < 200 || code >= 300) throw new IOException("ESP32 returned HTTP " + code);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()))) { StringBuilder body = new StringBuilder(); for (String line; (line = in.readLine()) != null;) body.append(line); return new JSONObject(body.toString()); }
        finally { c.disconnect(); }
    }
}
