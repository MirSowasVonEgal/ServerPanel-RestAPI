package me.mirsowasvonegal.serverpanel.RestAPI.payment.paypal;

import java.io.IOException;

import org.json.JSONObject;

import com.paypal.http.HttpResponse;
import com.paypal.http.serializer.Json;
import com.paypal.orders.Order;
import com.paypal.orders.OrdersGetRequest;
/**
 * @Projekt: RestAPI
 * @Created: 19.02.2021
 * @By: MirSowasVonEgal | Timo
 */
public class PayPalGetOrder extends PayPalClient {

    public Order getOrder(String orderId) throws IOException {
        OrdersGetRequest request = new OrdersGetRequest(orderId);
        HttpResponse<Order> response = client.execute(request);
        return response.result();
    }

}
