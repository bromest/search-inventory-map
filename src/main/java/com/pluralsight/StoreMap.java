package com.pluralsight;

import java.io.*;
import java.util.*;

public class StoreMap {
    public static void main(String[] args) {
        Scanner sr = new Scanner(System.in);
        HashMap<Integer, Product> inventory = loadInventory();

        while (true) {
            System.out.println("\n1- List | 2- Lookup ID | 3- Price Range | 4- Add | 5- Quit\nEnter choice: ");
            String choice = sr.nextLine().trim();

            if (choice.equals("5")) break;

            if (choice.equals("1")) {
                inventory.values().stream()
                        .sorted(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER))
                        .forEach(p -> System.out.printf("%d|%s|%.2f%n", p.getId(), p.getName(), p.getPrice()));
            }
            else if (choice.equals("2")) {
                System.out.print("Enter ID: ");
                int id = Integer.parseInt(sr.nextLine().trim());
                Product p = inventory.get(id); // Direct O(1) lookup using Map key
                if (p != null) {
                    System.out.printf("Found: %d|%s|%.2f%n", p.getId(), p.getName(), p.getPrice());
                } else {
                    System.out.println("Product not found with ID: " + id);
                }
            }
            else if (choice.equals("3")) {
                System.out.print("Min price: "); double min = Double.parseDouble(sr.nextLine().trim());
                System.out.print("Max price: "); double max = Double.parseDouble(sr.nextLine().trim());
                inventory.values().stream()
                        .filter(p -> p.getPrice() >= min && p.getPrice() <= max)
                        .forEach(p -> System.out.printf("%d|%s|%.2f%n", p.getId(), p.getName(), p.getPrice()));
            }
            else if (choice.equals("4")) {
                System.out.print("Enter ID: ");
                int id = Integer.parseInt(sr.nextLine().trim());
                System.out.print("Enter Name: ");
                String name = sr.nextLine().trim();
                System.out.print("Enter Price: ");
                double price = Double.parseDouble(sr.nextLine().trim());

                Product newProduct = new Product(id, name, price);
                inventory.put(id, newProduct); // Store in HashMap

                try (PrintWriter pw = new PrintWriter(new FileWriter("inventory.csv", true))) {
                    pw.println(id + "|" + name + "|" + price);
                    System.out.println("Product added successfully!");
                } catch (IOException e) {
                    System.out.println("Write error: " + e.getMessage());
                }
            }
        }
        sr.close();
    }

    public static HashMap<Integer, Product> loadInventory() {
        HashMap<Integer, Product> inventoryMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("inventory.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] file = line.split("\\|");
                if (file.length == 3) {
                    int id = Integer.parseInt(file[0].trim());
                    String name = file[1].trim();
                    double price = Double.parseDouble(file[2].trim());
                    inventoryMap.put(id, new Product(id, name, price)); // ID as Map key
                }
            }
        } catch (IOException e) {
            System.err.println("Inventory file not found");
        }
        return inventoryMap;
    }
}


