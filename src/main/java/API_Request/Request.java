package API_Request;

import constants.BaseURL;
import courier.CreateCourierRequest;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import orders.orderRequest.CancelOrderTrack;
import orders.orderRequest.CreateOrderRequest;

import java.util.List;

import static io.restassured.RestAssured.given;

public class Request {
    static {
        RestAssured.baseURI = BaseURL.BASE_URI;
    }

    public Response postRequest(Object json, String urlMethod) {
        Response response = given()
                .filter(new AllureRestAssured())
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post(urlMethod);
        return response;
    }

    public Response deleteRequest(String id, String urlMethod) {
        Response response = given()
                .filter(new AllureRestAssured())
                .and()
                .when()
                .delete(urlMethod + id);
        return response;
    }

    public Response putRequest(Object json, String urlMethod) {
        Response response = given()
                .filter(new AllureRestAssured())
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .put(urlMethod);
        return response;
    }

    public Response getRequestParameter(String parameter, String urlMethod, String count) {
        Response response = given()
                .filter(new AllureRestAssured())
                .and()
                .when()
                .queryParam(parameter, count)
                .get(urlMethod);
        return response;
    }

    // Создание курьера
    @Step("Создание курьера")
    public Response createCourierPost(String login, String password, String firstName) {
        CreateCourierRequest createCourierRequest = new CreateCourierRequest(login, password, firstName);
        return postRequest(createCourierRequest, BaseURL.URL_CREATE_COURIER);
    }

    // Получение ID курьера
    @Step("Получение ID курьера")
    public Response loginCourierPost(String login, String password) {
        CreateCourierRequest createCourierRequest = new CreateCourierRequest(login, password);
        return postRequest(createCourierRequest, BaseURL.URL_LOGIN_COURIER);
    }

    // Удаление курьера
    @Step("Удаление курьера")
    public Response deleteCourier(String id) {
        return deleteRequest(id, BaseURL.URL_DELETE_COURIER);
    }

    @Step("Создание заказа")
    public Response createOrderPost(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, List<String> color) {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        return postRequest(createOrderRequest, BaseURL.URL_CREATE_ORDER);
    }

    @Step("Отмена заказа")
    public Response cancelOrderPut(Integer track) {
        CancelOrderTrack cancelOrderTrack = new CancelOrderTrack(track);
        return putRequest(cancelOrderTrack, BaseURL.URL_CANCEL_ORDER);
    }

    @Step("Запрос списка заказов по количеству")
    public Response listOrders(String parameter, String count) {
        return getRequestParameter(parameter, BaseURL.URL_LIST_ORDERS, count);
    }


}
