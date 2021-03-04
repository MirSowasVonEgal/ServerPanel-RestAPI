package me.mirsowasvonegal.serverpanel.RestAPI.payment.paypal;

import com.paypal.http.HttpResponse;
import com.paypal.http.exceptions.SerializeException;
import com.paypal.http.serializer.Json;
import com.paypal.orders.Order;
import me.mirsowasvonegal.serverpanel.RestAPI.utils.RandomString;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @Projekt: RestAPI
 * @Created: 19.02.2021
 * @By: MirSowasVonEgal | Timo
 */
public class PayPalManager {

    public PayPalManager() {
    }

    public Order createOrder(String invoiceId, Double price) {
        try {
            return new PayPalCreateOrder().createOrder(invoiceId, price).result();
        } catch (IOException | NullPointerException e) {
            return null;
        }
    }

    public Order captureOrder(String orderId) {
        try {
            return new PayPalCaptureOrder().captureOrder(orderId);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public Order getOrder(String orderId) {
        try {
            return new PayPalGetOrder().getOrder(orderId);
        } catch (IOException | NullPointerException e) {
            return null;
        }
    }

    public static void main(String[] args) throws SerializeException {
        //System.out.println(new JSONObject(new Json().serialize(new PayPalManager().createOrder(RandomString.generateInt(16), 10.0))).toString(4));
        System.out.println(new PayPalManager().captureOrder("4UL96222KL1448849"));
        //System.out.println(new JSONObject(new Json().serialize(new PayPalManager().getOrder("6NN718025H0811414"))).toString(4));
    }

}
