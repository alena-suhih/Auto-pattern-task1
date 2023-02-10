package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;
import java.util.Locale;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class DeliveryTest {

    private static Faker faker;

    @BeforeAll
    static void setUpAll() {
        faker = new Faker(new Locale("ru"));
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id=\"city\"] input").sendKeys(validUser.getCity());
        $("[data-test-id=\"date\"] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME, Keys.DELETE));
        $("[data-test-id=\"date\"] input").setValue(firstMeetingDate);
        $("[data-test-id=\"name\"] input").sendKeys(validUser.getName());
        $("[data-test-id=\"phone\"] input").sendKeys(validUser.getPhone());
        $("[data-test-id=\"agreement\"] span").click();
        $("button.button").click();
        $("[data-test-id=\"success-notification\"]")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldBe(Condition.text("Встреча успешно запланирована на " + firstMeetingDate));
        $("[data-test-id=\"date\"] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME, Keys.DELETE));
        $("[data-test-id=\"date\"] input").setValue(secondMeetingDate);
        $("button.button").click();
        $("[data-test-id=\"replan-notification\"] .notification__content")
                .shouldBe(Condition.visible)
                .shouldBe(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $("[data-test-id=\"replan-notification\"] button").click();
        $("[data-test-id=\"success-notification\"] .notification__content")
                .shouldBe(Condition.visible)
                .shouldBe(Condition.text("Встреча успешно запланирована на " + secondMeetingDate));
    }
}
