import API_Request.Request;
import clearTestData.ClearData;
import com.google.gson.Gson;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import constants.BaseURL;
import courier.ResponseBadMessage;
import courier.ResponseSuccessОк;
import io.qameta.allure.Allure;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import randomValues.RandomData;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CreateCourierTest extends Request {
    private String login;
    private String password;
    private String firstName;
    private int statusCode;
    private boolean ok;
    private String message;

    private Map<String, String> mapCourier = new HashMap<>();

    public CreateCourierTest(String login, String password, String firstName, int statusCode, boolean ok, String message) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.statusCode = statusCode;
        this.ok = ok;
        this.message = message;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        List<Object[]> data = new ArrayList<>();
        try (CSVReader reader = new CSVReaderBuilder(
                new InputStreamReader(new FileInputStream(BaseURL.TEST_DATA_CREATE_COURIER), StandardCharsets.UTF_8))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {
            String[] line;
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                data.add(new Object[]{
                        line[0],
                        line[1],
                        line[2],
                        Integer.parseInt(line[3]),
                        Boolean.parseBoolean(line[4]),
                        line[5]
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.toArray(new Object[0][]);
    }

    @Test
    @DisplayName("Проверка API CreateCourier")
    public void testCreateCourier() {
        RandomData randomData = new RandomData();
        ResponseBadMessage responseBadMessage;
        if (Objects.equals(login, "required")) {
            login = randomData.randomValue("login");
        }
        if (Objects.equals(login, "not")) {
            login = "";
        }
        if (Objects.equals(login, "duplicate")) {
            login = randomData.randomValue("login");
            password = randomData.randomValue("password");
            Response response = createCourierPost(login, password, firstName);
            assertEquals(201, response.getStatusCode());
            mapCourier.put(login, password);

        }
        Allure.step("Логин: " + login);
        if (Objects.equals(password, "required")) {
            password = randomData.randomValue("password");
        }
        if (Objects.equals(password, "not")) {
            password = "";
        }
        Allure.step("Пароль: " + password);
        if (Objects.equals(firstName, "required")) {
            firstName = randomData.randomValue("firstName");
        }
        if (Objects.equals(firstName, "not")) {
            firstName = "";
        }
        Allure.step("Имя: " + firstName);
        Response response = createCourierPost(login, password, firstName);
        assertEquals(statusCode, response.getStatusCode());
        String responseBody = response.getBody().asString();
        Gson gson = new Gson();
        switch (statusCode) {
            case 201:
                mapCourier.put(login, password);
                ResponseSuccessОк responseSuccessОк = gson.fromJson(responseBody, ResponseSuccessОк.class);
                assertEquals(ok, responseSuccessОк.getOk());
                break;
            case 400:
                responseBadMessage = gson.fromJson(responseBody, ResponseBadMessage.class);
                assertEquals(message, responseBadMessage.getMessage());
            case 409:
                responseBadMessage = gson.fromJson(responseBody, ResponseBadMessage.class);
                assertEquals(message, responseBadMessage.getMessage());
                break;
            default:
                break;
        }
    }

    @After
    public void tearDown() {
        ClearData clearData = new ClearData();
        clearData.clearCourier(mapCourier);
    }

}



