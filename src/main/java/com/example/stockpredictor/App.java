package com.example.stockpredictor;
import java.util.*;

public class App {
    static class Stock {
        String name;
        double price;

        public Stock(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public void updatePrice() {
            Random randomNumber = new Random();
            double percentageChange = (randomNumber.nextDouble() * 20) - 10;
            price += price * (percentageChange / 100);
            price = Math.round(price * 100.0) / 100.0;
        }

        @Override
        public String toString() {
            return name + ": $" + price;
        }

        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);

            List<Stock> stocks = new ArrayList<>();
            stocks.add(new Stock("Apple", 150.00));
            stocks.add(new Stock("Google", 200.00));
            stocks.add(new Stock("Microsoft", 300.00));
            stocks.add(new Stock("Nvidia", 450.00));

            System.out.println("Welcome to the Stock Market Simulator!");

            while (true) {
                System.out.println("\nAvailable stocks:");

                for (int i = 0; i < stocks.size(); i++) {
                    System.out.println((i + 1) + ". " + stocks.get(i));
                }

                System.out.println("\n Choose an action: ");
                System.out.println("1. Buy Stock");
                System.out.println("2. Sell Stock");
                System.out.println("3. Update Stock Prices");
                System.out.println("4. Exit");
                System.out.println("Enter your choice: ");
                int choice = scanner.nextInt();
                int stockNumber;

                switch (choice) {
                    case 1:
                        System.out.println("Enter stock number to buy: ");
                        stockNumber = scanner.nextInt();

                        if (stockNumber > 0 && stockNumber <= stocks.size()) {
                            System.out.println("You bought: " + stocks.get(stockNumber - 1));
                        } else {
                            System.out.println("Invalid stock number.");
                        }
                    case 2:
                        System.out.println("Enter stock number to sell: ");
                        stockNumber = scanner.nextInt();

                        if (stockNumber > 0 && stockNumber <= stocks.size()) {
                            System.out.println("You sold: " + stocks.get(stockNumber - 1));
                        } else {
                            System.out.println("Invalid stock number.");
                        }
                    case 3:
                        System.out.println("Updating stock prices...");
                        for (Stock stock : stocks) {
                            stock.updatePrice();
                        }
                        System.out.println("Stock prices updated.");
                        break;
                    case 4:
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
                scanner.close();
            }
        }
    }
}
