package me.mirsowasvonegal.serverpanel.RestAPI.payment.paypal;

import java.io.IOException;

import com.paypal.orders.*;
import org.json.JSONObject;

import com.paypal.http.HttpResponse;
import com.paypal.http.serializer.Json;

/**
 * @Projekt: RestAPI
 * @Created: 19.02.2021
 * @By: MirSowasVonEgal | Timo
 */
public class PayPalAuthorizeOrder extends PayPalClient {

    private OrderRequest buildRequestBody() {
        return new OrderRequest();
    }

    public HttpResponse<Order> authorizeOrder(String orderId, boolean debug) throws IOException {
        OrdersAuthorizeRequest request = new OrdersAuthorizeRequest(orderId);
        request.requestBody(buildRequestBody());
        HttpResponse<Order> response = client().execute(request);
        if (debug) {
            System.out.println("Authorization Ids:");
            response.result().purchaseUnits().forEach(purchaseUnit -> purchaseUnit.payments().authorizations().stream()
                    .map(authorization -> authorization.id()).forEach(System.out::println));
            System.out.println("Link Descriptions: ");
            for (LinkDescription link : response.result().links()) {
                System.out.println("\t" + link.rel() + ": " + link.href());
            }
            System.out.println("Full response body:");
            System.out.println(new JSONObject(new Json().serialize(response.result())).toString(4));
        }
        return response;
    }

    /**
     * This is the driver function which invokes the authorizeOrder function to
     * create an sample order.
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            new PayPalAuthorizeOrder().authorizeOrder("5UK9626823223505F", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
