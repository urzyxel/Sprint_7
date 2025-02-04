import API_Request.Request;
import clearTestData.ClearData;
import com.google.gson.Gson;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import orders.orderResponse.ResponseSuccessTrack;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CreateOrderTest extends Request {
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private List<String> color;
    private int statusCode;

    private Integer track;

    public CreateOrderTest(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, List<String> color, int statusCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
        this.statusCode = statusCode;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][] {
                {"Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", List.of("BLACK"), 201},
                {"Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", List.of("GREY"), 201},
                {"Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", List.of("BLACK","GREY"), 201},
                {"Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", List.of(), 201}

        };
    }

    @Test
    @DisplayName("Проверка API CreateOrder")
    public void testCreateOrder(){
        Response response = createOrderPost(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        assertEquals(statusCode, response.getStatusCode());
        String responseBody = response.getBody().asString();
        Gson gson = new Gson();
        switch (statusCode){
            case 201:
                ResponseSuccessTrack responseSuccessTrack = gson.fromJson(responseBody, ResponseSuccessTrack.class);
                assertThat(responseSuccessTrack.getTrack()).isPositive();
                track = responseSuccessTrack.getTrack();
                break;
            default:
                break;
        }
    }

    @After
    public void tearDown(){
        ClearData clearData = new ClearData();
        clearData.clearOrder(track);
    }

}
