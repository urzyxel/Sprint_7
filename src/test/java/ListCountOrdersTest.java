import API_Request.Request;
import com.google.gson.Gson;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import orders.responceListOrders.Order;
import orders.responceListOrders.ResponseListOrders;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ListCountOrdersTest extends Request {

    @Test
    @DisplayName("Проверка API List Count Orders")
    public void testCreateCourier(){
        Response response = listOrders("limit", "2");
        assertEquals(200, response.getStatusCode());
        String responseBody = response.getBody().asString();
        Gson gson = new Gson();
        ResponseListOrders responseListOrders  = gson.fromJson(responseBody, ResponseListOrders.class);
        List<Order> orders = responseListOrders.getOrders();
        assertEquals(2, orders.size());
    }
}
