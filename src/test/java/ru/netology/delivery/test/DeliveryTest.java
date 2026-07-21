package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should have correct default values on page load")
    void shouldHaveCorrectDefaultValues() {
        $("[data-test-id='city'] input").shouldHave(Condition.value(""));
        $("[data-test-id='city'] .input__sub").shouldHave(text("Выберите ваш город"));

        String expectedDate = DataGenerator.generateDate(3);
        $("[data-test-id='date'] input").shouldHave(Condition.value(expectedDate));
        $("[data-test-id='date'] .input__sub").shouldHave(text("Выберите дату встречи с представителем банка"));

        $("[data-test-id='name'] .input__sub").shouldHave(text("Укажите точно как в паспорте"));
        $("[data-test-id='phone'] .input__sub").shouldHave(text("На указанный номер моб. тел. будет отправлен смс-код для подтверждения заявки на карту. Проверьте, что номер ваш и введен корректно."));
        $("[data-test-id='agreement'] .checkbox__control").shouldNotBe(checked);

        $(".button__text").shouldHave(text("Запланировать"));
    }

    @Test
    @DisplayName("Should successfully plan a meeting")
    void shouldSuccessfulPlanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var meetingDate = DataGenerator.generateDate(4);

        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(meetingDate);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();

        $(".button__text").click();

        // Исправлено: проверяем видимость + текст в одной цепочке
        $("[data-test-id='success-notification']")
                .shouldBe(visible, Duration.ofSeconds(15))
                .$(".notification__title").shouldHave(text("Успешно!"));

        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(text("Встреча успешно запланирована на " + meetingDate));

        $("[data-test-id='replan-notification']").shouldNotBe(visible);
    }

    @Test
    @DisplayName("Should successfully replan meeting with same user data and new date")
    void shouldSuccessfulReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var firstDate = DataGenerator.generateDate(4);
        var secondDate = DataGenerator.generateDate(7);

        // Первое планирование
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(firstDate);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(".button__text").click();

        // Исправлено: проверяем видимость + текст
        $("[data-test-id='success-notification']")
                .shouldBe(visible, Duration.ofSeconds(15))
                .$(".notification__content").shouldHave(text("Встреча успешно запланирована на " + firstDate));

        // Второе планирование с новой датой
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(secondDate);
        $(".button__text").click();

        // Исправлено: проверяем видимость уведомления о перепланировании
        $("[data-test-id='replan-notification']")
                .shouldBe(visible, Duration.ofSeconds(15))
                .$(".notification__title").shouldHave(text("Необходимо подтверждение"));

        $("[data-test-id='replan-notification'] .button__text").click();

        // Исправлено: проверяем видимость + текст
        $("[data-test-id='success-notification']")
                .shouldBe(visible, Duration.ofSeconds(15))
                .$(".notification__content").shouldHave(text("Встреча успешно запланирована на " + secondDate));

        $("[data-test-id='replan-notification']").shouldNotBe(visible);
    }

    @Test
    @DisplayName("Should replan meeting with different user data")
    void shouldReplanMeetingWithDifferentUserData() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var anotherUser = DataGenerator.Registration.generateUser("ru");
        var firstDate = DataGenerator.generateDate(4);
        var secondDate = DataGenerator.generateDate(5);

        // Первое планирование
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(firstDate);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(".button__text").click();

        $("[data-test-id='success-notification']").shouldBe(visible, Duration.ofSeconds(15));

        // Второе планирование с другими данными
        $("[data-test-id='city'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='city'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='city'] input").setValue(anotherUser.getCity());

        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(secondDate);

        $("[data-test-id='name'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='name'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='name'] input").setValue(anotherUser.getName());

        $("[data-test-id='phone'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='phone'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='phone'] input").setValue(anotherUser.getPhone());

        $(".button__text").click();

        $("[data-test-id='success-notification']").shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should show error when city is empty")
    void shouldShowErrorWhenCityIsEmpty() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var meetingDate = DataGenerator.generateDate(4);

        $("[data-test-id='city'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='city'] input").sendKeys(Keys.DELETE);

        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(meetingDate);

        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();

        $(".button__text").click();

        // Исправлено: комбинированный селектор с классом input_invalid
        $("[data-test-id='city'].input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    @DisplayName("Should show error when city is invalid")
    void shouldShowErrorWhenCityIsInvalid() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var meetingDate = DataGenerator.generateDate(4);

        $("[data-test-id='city'] input").setValue("New York");

        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(meetingDate);

        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();

        $(".button__text").click();

        $("[data-test-id='city'].input_invalid .input__sub").shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    @DisplayName("Should show error when date is empty")
    void shouldShowErrorWhenDateIsEmpty() {
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id='city'] input").setValue(validUser.getCity());

        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);

        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();

        $(".button__text").click();

        // Для даты класс не добавляется, проверяем текст
        $("[data-test-id='date'] .input__sub").shouldHave(text("Неверно введена дата"));
    }

    @Test
    @DisplayName("Should show error when name is empty")
    void shouldShowErrorWhenNameIsEmpty() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var meetingDate = DataGenerator.generateDate(4);

        $("[data-test-id='city'] input").setValue(validUser.getCity());

        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(meetingDate);

        $("[data-test-id='name'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='name'] input").sendKeys(Keys.DELETE);

        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();

        $(".button__text").click();

        $("[data-test-id='name'].input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    @DisplayName("Should show error when name contains digits")
    void shouldShowErrorWhenNameContainsDigits() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var meetingDate = DataGenerator.generateDate(4);

        $("[data-test-id='city'] input").setValue(validUser.getCity());

        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(meetingDate);

        $("[data-test-id='name'] input").setValue("Иван123");

        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();

        $(".button__text").click();

        String expectedError = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        $("[data-test-id='name'].input_invalid .input__sub").shouldHave(text(expectedError));
    }

    @Test
    @DisplayName("Should show error when phone is empty")
    void shouldShowErrorWhenPhoneIsEmpty() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var meetingDate = DataGenerator.generateDate(4);

        $("[data-test-id='city'] input").setValue(validUser.getCity());

        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(meetingDate);

        $("[data-test-id='name'] input").setValue(validUser.getName());

        $("[data-test-id='phone'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='phone'] input").sendKeys(Keys.DELETE);

        $("[data-test-id='agreement'] .checkbox__box").click();

        $(".button__text").click();

        $("[data-test-id='phone'] .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    @DisplayName("Should show error when agreement not checked")
    void shouldShowErrorWhenAgreementNotChecked() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var meetingDate = DataGenerator.generateDate(4);

        $("[data-test-id='city'] input").setValue(validUser.getCity());

        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(meetingDate);

        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());

        if ($("[data-test-id='agreement'] .checkbox__control").isSelected()) {
            $("[data-test-id='agreement'] .checkbox__box").click();
        }

        $(".button__text").click();

        // Исправлено: проверяем видимость элемента с классом input_invalid
        $("[data-test-id='agreement'].input_invalid").shouldBe(visible);
    }

    @Test
    @DisplayName("Should show error only for city when all fields empty")
    void shouldShowErrorOnlyForCityWhenAllFieldsEmpty() {
        $("[data-test-id='city'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='city'] input").sendKeys(Keys.DELETE);

        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);

        $("[data-test-id='name'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='name'] input").sendKeys(Keys.DELETE);

        $("[data-test-id='phone'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='phone'] input").sendKeys(Keys.DELETE);

        $(".button__text").click();

        $("[data-test-id='city'].input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения"));

        $("[data-test-id='date']").shouldNotHave(cssClass("input_invalid"));
        $("[data-test-id='name']").shouldNotHave(cssClass("input_invalid"));
        $("[data-test-id='phone']").shouldNotHave(cssClass("input_invalid"));
    }
}