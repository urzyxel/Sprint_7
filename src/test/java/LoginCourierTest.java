import API_Request.Request;
import clearTestData.ClearData;
import com.google.gson.Gson;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import constants.BaseURL;
import courier.ResponseBadMessage;
import courier.ResponseLoginSuccessId;
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

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class LoginCourierTest extends Request {
    private String login;
    private String password;
    private int statusCode;
    private String message;

    private Map<String, String> mapCourier = new HashMap<>();

    public LoginCourierTest(String login, String password, int statusCode, String message) {
        this.login = login;
        this.password = password;
        this.statusCode = statusCode;
        this.message = message;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        List<Object[]> data = new ArrayList<>();
        try (CSVReader reader = new CSVReaderBuilder(
                new InputStreamReader(new FileInputStream(BaseURL.TEST_DATA_LOGIN_COURIER), StandardCharsets.UTF_8))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {
            String[] line;
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                data.add(new Object[]{
                        line[0],
                        line[1],
                        Integer.parseInt(line[2]),
                        line[3]
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.toArray(new Object[0][]);
    }

    @Test
    @DisplayName("Проверка API LoginCourier")
    public void testLoginCourier() {
        RandomData randomData = new RandomData();
        ResponseBadMessage responseBadMessage;
        Gson gson = new Gson();
        String createLogin = randomData.randomValue("login");
        String createPassword = randomData.randomValue("password");
        String createFirstName = randomData.randomValue("firstName");
        Response response = createCourierPost(createLogin, createPassword, createFirstName);
        Allure.step("Создание тестового пользователя", () -> {
            assertEquals(201, response.getStatusCode());
        });
        mapCourier.put(login, password);
        if (Objects.equals(login, "required")) {
            login = createLogin;
        }
        if (Objects.equals(login, "not")) {
            login = "";
        }
        if (Objects.equals(password, "required")) {
            password = createPassword;
        }
        if (Objects.equals(password, "not")) {
            password = "";
        }
        Response responseLogin = loginCourierPost(login, password);
        String responseBody = responseLogin.getBody().asString();
        switch (statusCode) {
            case 200:
                ResponseLoginSuccessId responseLoginSuccessId = gson.fromJson(responseBody, ResponseLoginSuccessId.class);
                assertNotNull(responseLoginSuccessId.getId());
                break;
            case 400:
            case 404:
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
