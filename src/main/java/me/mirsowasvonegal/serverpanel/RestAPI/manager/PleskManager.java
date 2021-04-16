package me.mirsowasvonegal.serverpanel.RestAPI.manager;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import me.mirsowasvonegal.serverpanel.RestAPI.model.Status;
import okhttp3.*;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PleskManager {


    JSONObject config = new JSONObject(SettingsManager.getPlesk()).getJSONObject("value");
    JSONObject auth = config.getJSONObject("auth");

    public PleskManager() {
    }

    public Object getLoginLink(String username) {
        if(!config.getBoolean("enabled")) return new Status("Plesk ist deaktivert!", 500);
        JSONArray Params = new JSONArray()
                .put("--get-login-link")
                .put("-user")
                .put(username);
        JSONObject Body = new JSONObject()
                .put("params", Params);
        return getURLObject("https://" + auth.getString("hostname")  + ":8443/api/v2/cli/admin/call", "POST", Body.toString());
    }

    public Object createUser(String username, String password, String email, String type, String plan) {
        if(!config.getBoolean("enabled")) return new Status("Plesk ist deaktivert!", 500);
        JSONObject User = new JSONObject() {
            {
                put("name", username);
                put("login", username.toLowerCase());
                put("password", password);
                put("email", email);
                put("type", type);
            }};
        JSONObject userObject = new JSONObject(getURLObject("https://" + auth.getString("hostname")  + ":8443/api/v2/clients", "POST", User.toString()).toString());

        if(!userObject.isNull("code")) return null;
        JSONObject FTP = new JSONObject() {
            {
                put("ftp_login", username.toLowerCase());
                put("ftp_password", password);
            }};
        User.put("id", userObject.getInt("id")).put("guid", userObject.getString("guid"));
        JSONArray IPv6 = new JSONArray()
                .put("2a01:4f8:140:605e::210");
        JSONArray IPv4 = new JSONArray();

        JSONObject Plan = new JSONObject()
                .put("name", plan);
        JSONObject Domains = new JSONObject() {
            {
                put("name", username.toLowerCase()+"." + auth.getString("hostname"));
                put("hosting_type", "virtual");
                put("hosting_settings", FTP);
                put("owner_client", User);
                put("ipv6", IPv6);
                put("ipv4", IPv4);
                put("plan", Plan);
            }};
        getURLObject("https://" + auth.getString("hostname") + ":8443/api/v2/domains", "POST", Domains.toString());
        return userObject;
    }

    public Object getURLObject(String url, String method, String body) {
        if(!config.getBoolean("enabled")) return new Status("Plesk ist deaktivert!", 500);
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            RequestBody requestBody = null;
            MediaType mediaType = MediaType.parse("application/json");
            if(!(body == null || method == "GET"))
                requestBody = RequestBody.create(mediaType, body);
            Request request = new Request.Builder()
                    .url(url)
                    .method(method, requestBody)
                    .addHeader("X-API-Key", auth.getString("key"))
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Cookie", "__cfduid=d24a5cca5b1851eb6d4103a19f03fabe81616691955")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setTrustAllSslCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }


}
