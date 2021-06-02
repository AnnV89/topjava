package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMeal> userMealFiltered = new ArrayList<>();
        Map<LocalDate, List<UserMeal>> userMealMap = new HashMap<>();
        List<UserMealWithExcess> userMealWithExcessList = new ArrayList<>();

        for (UserMeal meal : meals) {
            if (meal.getDateTime().toLocalTime().isAfter(startTime) && meal.getDateTime().toLocalTime().isBefore(endTime)) {
                userMealFiltered.add(meal);
            }
        }
        for (UserMeal meal : userMealFiltered) {
            LocalDate localDate = meal.getDateTime().toLocalDate();
            if (!userMealMap.containsKey(localDate)) {
                List<UserMeal> list = new ArrayList<>();
                list.add(meal);
                userMealMap.put(localDate, list);
            } else {
                List<UserMeal> list = userMealMap.get(localDate);
                list.add(meal);
            }
        }

        for (Map.Entry<LocalDate, List<UserMeal>> entry : userMealMap.entrySet()) {
            List<UserMeal> list = entry.getValue();
            int sumCalories = 0;
            for (UserMeal userMeal : list) {
                sumCalories += userMeal.getCalories();
            }
            if (sumCalories > caloriesPerDay) {
                for (UserMeal userMeal : list) {
                    userMealWithExcessList.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), true));
                }
            } else {
                for (UserMeal userMeal : list) {
                    userMealWithExcessList.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), false));
                }
            }
        }
        return userMealWithExcessList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDay = meals.stream()
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)));

        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        caloriesSumByDay.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}
