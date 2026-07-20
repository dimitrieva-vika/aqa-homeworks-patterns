package ru.netology.delivery.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    private DataGenerator() {
    }

    /**
     * Генерация даты со сдвигом от текущей
     * @param shift количество дней для сдвига
     * @return дата в формате dd.MM.yyyy
     */
    public static String generateDate(int shift) {
        LocalDate date = LocalDate.now().plusDays(shift);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return date.format(formatter);
    }

    /**
     * Генерация валидного города из списка административных центров РФ
     */
    public static String generateCity(Faker faker) {
        String[] cities = {
                "Москва", "Санкт-Петербург", "Новосибирск", "Екатеринбург",
                "Казань", "Нижний Новгород", "Челябинск", "Самара",
                "Омск", "Ростов-на-Дону", "Уфа", "Красноярск",
                "Воронеж", "Пермь", "Волгоград", "Краснодар"
        };
        return cities[new Random().nextInt(cities.length)];
    }

    /**
     * Генерация имени (Фамилия Имя) на русском языке
     */
    public static String generateName(Faker faker) {
        return faker.name().lastName() + " " + faker.name().firstName();
    }

    /**
     * Генерация номера телефона в формате +7XXXXXXXXXX
     */
    public static String generatePhone(Faker faker) {
        return "+7" + (9000000000L + new Random().nextInt(999999999));
    }

    public static class Registration {
        private Registration() {
        }

        /**
         * Генерация пользователя с валидными данными
         * @param locale локаль (например, "ru")
         * @return UserInfo с городом, именем и телефоном
         */
        public static UserInfo generateUser(String locale) {
            Faker faker = new Faker(new Locale(locale));
            return new UserInfo(
                    generateCity(faker),
                    generateName(faker),
                    generatePhone(faker)
            );
        }
    }

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }
}