package clearTestData;

import API_Request.Request;
import com.google.gson.Gson;
import courier.ResponseLoginSuccessId;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.restassured.response.Response;
import java.util.Map;

public class ClearData extends Request {
    public void clearCourier(Map<String, String> mapCourier) {
        Gson gson = new Gson();
        for (Map.Entry<String, String> entry : mapCourier.entrySet()) {
            Response response = loginCourierPost(entry.getKey(), entry.getValue());
            if (response.getStatusCode() == 200) {
                String responseBody = response.getBody().asString();
                ResponseLoginSuccessId responseLoginSuccessId = gson.fromJson(responseBody, ResponseLoginSuccessId.class);
                response = deleteCourier(responseLoginSuccessId.getId());
                Response finalResponse = response;
                Allure.step("Удаление учетной записи:", () -> {
                    if (finalResponse.getStatusCode() == 200) {
                        Allure.step("Учётная запись с логин:  " + entry.getKey() + ", пароль: " + entry.getValue() + ", удалена", Status.PASSED);
                    } else {
                        Allure.step("Учётная запись с логин:  " + entry.getKey() + ", пароль: " + entry.getValue() + ", не удалена" + finalResponse.getStatusCode(), Status.FAILED);
                    }
                });
            }
        }
    }

    public void clearOrder(Integer track) {
        Response response = cancelOrderPut(track);
        Allure.step("Отмена заказа с треком: " + track, () -> {
            if (response.getStatusCode() == 200) {
                Allure.step("Заказ с треком: " + track + " успешно отменён.", Status.PASSED);
            } else {
                Allure.step("Не удалось отменить заказ с треком: " + track + ". Код ответа: " + response.getStatusCode(), Status.FAILED);
            }
        });
    }
}
