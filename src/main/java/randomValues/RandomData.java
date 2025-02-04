package randomValues;

import com.github.javafaker.Faker;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

public class RandomData {

    @Step("Получение случайных тестовых данных для поля:")
    public String randomValue(String nameField) {
        Faker faker = new Faker();
        String[] result = {""}; // Используем массив для обхода ограничения

        Allure.step("Формирование случайных тестовых данных", () -> {
            switch (nameField) {
                case "login":
                    result[0] = faker.name().username();
                    break;
                case "password":
                    result[0] = faker.internet().password();
                    break;
                case "firstName":
                    result[0] = faker.name().firstName();
                    break;
                default:
                    throw new IllegalArgumentException("Неизвестное поле: " + nameField);
            }
        });
        return result[0];
    }
}
