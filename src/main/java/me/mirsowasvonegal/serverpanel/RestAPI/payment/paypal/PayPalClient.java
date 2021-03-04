package me.mirsowasvonegal.serverpanel.RestAPI.payment.paypal;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.sun.xml.internal.ws.util.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * @Projekt: RestAPI
 * @Created: 18.02.2021
 * @By: MirSowasVonEgal | Timo
 */
public class PayPalClient {

    static String clientId = "AekqUpGEFwrOrVoh5sRzY1n2nxVVNWkF5Bs6X4rwdqF3qJxe6Cf_k7hyo2f-RWH6OCreieXoyRQ0u3u3";
    static String secret = "EBtbKrUzF0GyQ9xGuoNwSRAA26VziFwT8DEFZpFfF2xWpMGujSXNi14oVbg6Ud0LPOhN5zDAuM7PigVI";

    // Creating a sandbox environment
    private static final PayPalEnvironment environment = new PayPalEnvironment.Sandbox(clientId, secret);

    // Creating a client for the environment
    static PayPalHttpClient client = new PayPalHttpClient(environment);

    public PayPalHttpClient client() {
        return this.client;
    }

    /**
     * Method to pretty print a response
     *
     * @param jo  JSONObject
     * @param pre prefix (default="")
     * @return String pretty printed JSON
     */
    public String prettyPrint(JSONObject jo, String pre) {
        Iterator<?> keys = jo.keys();
        StringBuilder pretty = new StringBuilder();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            pretty.append(String.format("%s%s: ", pre, StringUtils.capitalize(key)));
            if (jo.get(key) instanceof JSONObject) {
                pretty.append(prettyPrint(jo.getJSONObject(key), pre + "\t"));
            } else if (jo.get(key) instanceof JSONArray) {
                int sno = 1;
                for (Object jsonObject : jo.getJSONArray(key)) {
                    pretty.append(String.format("\n%s\t%d:\n", pre, sno++));
                    pretty.append(prettyPrint((JSONObject) jsonObject, pre + "\t\t"));
                }
            } else {
                pretty.append(String.format("%s\n", jo.getString(key)));
            }
        }
        return pretty.toString();
    }
}
