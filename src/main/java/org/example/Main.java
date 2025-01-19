package org.example;

import java.util.ArrayList;

public class Main {
    private static final String POSTGRES_URL = "jdbc:postgresql://localhost/postgres?user=postgres&password=postgres";
    private static final Database DATABASE = new Database(POSTGRES_URL);
    private static int saveUserId;
    private static final Validation VALIDATION = new Validation();

    public static void main(String[] args) {
        if (!DATABASE.initDatabase()) {
            System.exit(0);
        }

        System.out.println("create user - создать пользователя\n" +
                "login - зайти на аккаунт\n" +
                "create wallet - создать кошелек\n" +
                "add revenue - добавить доходы\n" +
                "add expenses - добавить траты\n" +
                "create a category - создать категорию\n" +
                "set a budget - установить бюджет\n" +
                "output data - вывести данные\n" +
                "exit - выйти из приложения");

        while (true) {
            String input = VALIDATION.getString();

            if (input.equals("create user")) {
                createUser();
            } else if (input.equals("login")) {
                login();
            } else if (input.equals("create wallet")) {
                createWallet();
            } else if (input.equals("add revenue")) {
                addRevenue();
            } else if (input.equals("add expenses")) {
                addExpenses();
            } else if (input.equals("create a category")) {
                createCategory();
            } else if (input.equals("set a budget")) {
                setBudget();
            } else if (input.equals("output data")) {
                outputData();
            } else if (input.equals("exit")) {
                System.exit(0);
            } else if (input.isEmpty()) {
                System.out.println();
            } else {
                System.out.println("Моя твоя не понимать");
            }
        }
    }

    private static void createUser() {
        System.out.println("Введите логин");
        String login = VALIDATION.getString();
        System.out.println("Введите пароль");
        String password = VALIDATION.getString();
        int userId = DATABASE.createUser(password, login);
        if (userId == 0) {
            System.out.println("Непредвиденная ошибка");
        } else {
            System.out.println("Пользователь успешно создан");
        }
    }

    private static void login() {
        System.out.println("Введите логин");
        String login = VALIDATION.getString();
        System.out.println("Введите пароль");
        String password = VALIDATION.getString();
        int userId = DATABASE.getUSer(password, login);
        if (userId == 0) {
            System.out.println("Неправильный логин или пароль");
        } else {
            System.out.println("Вы вошли в аккаунт");
            saveUserId = userId;
        }
    }

    private static void createWallet() {
        int walletId = DATABASE.createWallet(saveUserId);
        if (walletId == 0) {
            System.out.println("Что-то пошло не так...");
        } else {
            System.out.println("Кошелек создан. Id кошелька: " + walletId);
        }
    }

    private static void addRevenue() {
        System.out.println("Напишите Id кошелька");
        int walletId = VALIDATION.getPositiveInt();
        System.out.println("Напишите Id категории");
        int categoryId = VALIDATION.getPositiveInt();
        System.out.println("Напишите сумму дохода");
        int sum = VALIDATION.getPositiveInt();
        int userTransaction = DATABASE.createTransaction("+", sum, walletId, categoryId);
        if (userTransaction == 0) {
            System.out.println("Счет заблокирован по 115-ФЗ, ну, или произошла ошибка...");
        } else {
            System.out.println("Мани зачислены");
        }
    }

    private static void addExpenses() {
        System.out.println("Напишите Id кошелька");
        int walletId = VALIDATION.getPositiveInt();
        System.out.println("Напишите Id категории");
        int categoryId = VALIDATION.getPositiveInt();
        System.out.println("Напишите сумму расхода");
        int sum = VALIDATION.getPositiveInt();
        int userTransaction = DATABASE.createTransaction("-", sum, walletId, categoryId);
        if (userTransaction == 0) {
            System.out.println("Аеее. Вы ничего не потратили, потому что что-то сломалось");
        } else {
            System.out.println("Давай трать, мы же миллионеры");
        }
    }

    private static void createCategory() {
        System.out.println("Создайте категорию");
        String category = VALIDATION.getString();
        int userCategory = DATABASE.createCategory(category);
        if (userCategory == 0) {
            System.out.println("Что-то пошло не так...");
        } else {
            System.out.println("Категория успешно создана. Id категории: " + userCategory);
        }
    }

    private static void setBudget() {
        System.out.println("Напишите Id категории");
        int categoryId = VALIDATION.getPositiveInt();
        System.out.println("Установите бюджет");
        int budget = VALIDATION.getPositiveInt();
        int budgetCategory = DATABASE.createBudget(categoryId, saveUserId, budget);
        if (budgetCategory == 0) {
            System.out.println("Ни в чем себе не ограничивайте! Или что-то пошло не так...");
        } else {
            System.out.println("Бюджет установлен");
        }
    }

    private static void outputData() {
        ArrayList<Category> caterogies = DATABASE.getCategories(saveUserId);
        int revenue = 0;
        int expenses = 0;
        for (Category category: caterogies) {
            System.out.println(category.getName() + ": " + category.getType() + category.getSum());
            if (category.getType().equals("+")){
                revenue += category.getSum();
            } else {
                expenses -= category.getSum();
                int result = category.getBudget() - category.getSum();
                System.out.println("Оставшийся бюджет: " + result);
            }
        }
        System.out.println("Итого доходы: " + revenue);
        System.out.println("Итого расходы: " + expenses);
    }
}
