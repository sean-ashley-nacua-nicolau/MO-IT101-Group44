import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.awt.Desktop;

public class QuickCartItems {

    public static ArrayList<String> readItemsFromFile() {
        ArrayList<String> items = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("items.txt"));
            String line;

            while ((line = reader.readLine()) != null) {
                items.add(line);
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading items.txt");
        }

        return items;
    }

    public static void writeReceiptToFile(String message) {
        try {
            String fileName = "receipt.txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

            writer.write(message);
            writer.close();

            System.out.println("Receipt successfully written to receipt.txt");

            // Automatically open the file
            File file = new File(fileName);

            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (file.exists()) {
                    desktop.open(file);
                }
            }

        } catch (IOException e) {
            System.out.println("Error writing or opening receipt.txt");
        }
    }

    public static double parsePrice(String line) {
        String[] parts = line.split(",");
        return Double.parseDouble(parts[1]);
    }

    public static double getDiscount(double total) {
        if (total > 1000) {
            return total * 0.10;
        } else if (total > 500) {
            return total * 0.05;
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {

        ArrayList<String> items = readItemsFromFile();
        double subtotal = 0;

        System.out.println("Checkout Summary:");

        StringBuilder itemList = new StringBuilder();

        for (String item : items) {
            double price = parsePrice(item);
            System.out.println(item);
            subtotal += price;
            itemList.append(item).append("\n");
        }

        double discount = getDiscount(subtotal);
        double finalTotal = subtotal - discount;

        // Format values to 2 decimal places
        String subtotalStr = String.format("%.2f", subtotal);
        String discountStr = String.format("%.2f", discount);
        String totalStr = String.format("%.2f", finalTotal);

        System.out.println("Subtotal: " + subtotalStr);
        System.out.println("Discount: " + discountStr);
        System.out.println("Total: " + totalStr);

        // Build formatted receipt
        String receipt = "QuickCart Receipt\n"
                + "-------------------\n"
                + "Items:\n"
                + itemList.toString()
                + "\n"
                + "Subtotal: " + subtotalStr + "\n"
                + "Discount: " + discountStr + "\n"
                + "Total: " + totalStr + "\n";

        writeReceiptToFile(receipt);
    }
}