package org.example;

public class Category {
    private int sum;
    private String name;
    private String type;
    private int budget;

    public Category(int sum, String name, String type, int budget) {
        this.sum = sum;
        this.name = name;
        this.type = type;
        this.budget = budget;
    }

    public int getSum() {
        return sum;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getBudget() {
        return budget;
    }
}
