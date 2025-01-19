package org.example;

import java.util.Scanner;

public class Validation {
    private static final Scanner SCANNER = new Scanner(System.in);

    public String getString() {
        return SCANNER.nextLine();
    }

    public int getPositiveInt() {
        while (true) {
            if (SCANNER.hasNextInt()) {
                int numbers = SCANNER.nextInt();
                if (numbers > 0) {
                    return numbers;
                } else {
                    System.out.println("Нельзя вводить отрицательные числа");
                }
            } else {
                System.out.println("Вы ввели не число");
            }
        }
    }
}
