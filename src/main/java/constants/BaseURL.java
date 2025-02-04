package constants;

public class BaseURL {
    public static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    public static final String URL_CREATE_COURIER = "/api/v1/courier";
    public static final String URL_LOGIN_COURIER = "/api/v1/courier/login";
    public static final String URL_DELETE_COURIER = "/api/v1/courier/";
    public static final String URL_CREATE_ORDER = "/api/v1/orders";
    public static final String URL_CANCEL_ORDER = "/api/v1/orders/cancel";
    public static final String URL_LIST_ORDERS = "/api/v1/orders";

    public static final String TEST_DATA_CREATE_COURIER = "src/main/resources/testDataCreateCourier.csv";
    public static final String TEST_DATA_LOGIN_COURIER = "src/main/resources/testDataLoginCourier.csv";
}
