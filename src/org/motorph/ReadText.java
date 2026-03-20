package org.motorph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadText {

    // SSS RANGE CLASS
    static class SSSRange {
        double min;
        double max;
        double contribution;

        SSSRange(double min, double max, double contribution) {
            this.min = min;
            this.max = max;
            this.contribution = contribution;
        }
    }

    // SSS TABLE
    static SSSRange[] sssTable = {
        new SSSRange(0, 3249.99, 135.00),
        new SSSRange(3250, 3749.99, 157.50),
        new SSSRange(3750, 4249.99, 180.00),
        new SSSRange(4250, 4749.99, 202.50),
        new SSSRange(4750, 5249.99, 225.00),
        new SSSRange(5250, 5749.99, 247.50),
        new SSSRange(5750, 6249.99, 270.00),
        new SSSRange(6250, 6749.99, 292.50),
        new SSSRange(6750, 7249.99, 315.00),
        new SSSRange(7250, 7749.99, 337.50),
        new SSSRange(7750, 8249.99, 360.00),
        new SSSRange(8250, 8749.99, 382.50),
        new SSSRange(8750, 9249.99, 405.00),
        new SSSRange(9250, 9749.99, 427.50),
        new SSSRange(9750, 10249.99, 450.00),
        new SSSRange(10250, 10749.99, 472.50),
        new SSSRange(10750, 11249.99, 495.00),
        new SSSRange(11250, 11749.99, 517.50),
        new SSSRange(11750, 12249.99, 540.00),
        new SSSRange(12250, 12749.99, 562.50),
        new SSSRange(12750, 13249.99, 585.00),
        new SSSRange(13250, 13749.99, 607.50),
        new SSSRange(13750, 14249.99, 630.00),
        new SSSRange(14250, 14749.99, 652.50),
        new SSSRange(14750, 15249.99, 675.00),
        new SSSRange(15250, 15749.99, 697.50),
        new SSSRange(15750, 16249.99, 720.00),
        new SSSRange(16250, 16749.99, 742.50),
        new SSSRange(16750, 17249.99, 765.00),
        new SSSRange(17250, 17749.99, 787.50),
        new SSSRange(17750, 18249.99, 810.00),
        new SSSRange(18250, 18749.99, 832.50),
        new SSSRange(18750, 19249.99, 855.00),
        new SSSRange(19250, 19749.99, 877.50),
        new SSSRange(19750, 20249.99, 900.00),
        new SSSRange(20250, 20749.99, 922.50),
        new SSSRange(20750, 21249.99, 945.00),
        new SSSRange(21250, 21749.99, 967.50),
        new SSSRange(21750, 22249.99, 990.00),
        new SSSRange(22250, 22749.99, 1012.50),
        new SSSRange(22750, 23249.99, 1035.00),
        new SSSRange(23250, 23749.99, 1057.50),
        new SSSRange(23750, 24249.99, 1080.00),
        new SSSRange(24250, 24749.99, 1102.50),
        new SSSRange(24750, Double.MAX_VALUE, 1125.00)
    };

    public static void main(String[] args) {

        String filePath = "src/org/motorph/employee_data.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;

            while ((line = br.readLine()) != null) {

                try {
                    String[] data = line.split(",");

                    if (data.length != 2) {
                        System.out.println("Invalid format: " + line);
                        continue;
                    }

                    String name = data[0].trim();
                    double grossSalary = Double.parseDouble(data[1].trim());

                    if (grossSalary <= 0) {
                        System.out.println("Invalid salary for: " + name);
                        continue;
                    }

                    String firstName = name.trim().split("\\s+")[0].toUpperCase();

                    // Deductions
                    double sss = getSSS(grossSalary);
                    double philhealth = getPhilHealth(grossSalary);
                    double pagibig = getPagIbig(grossSalary);

                    // Taxable income
                    double taxableIncome = grossSalary - (sss + philhealth + pagibig);

                    // Tax
                    double tax = getTax(taxableIncome);

                    double totalDeductions = sss + philhealth + pagibig + tax;
                    double netPay = grossSalary - totalDeductions;

                    // OUTPUT
                    System.out.println("\n--- " + firstName + " PAYROLL SUMMARY ---");
                    System.out.println("Employee:       " + name);
                    System.out.printf("Gross Salary:   PHP %,.2f\n", grossSalary);
                    System.out.printf("SSS:            PHP %,.2f\n", sss);
                    System.out.printf("PhilHealth:     PHP %,.2f\n", philhealth);
                    System.out.printf("Pag-IBIG:       PHP %,.2f\n", pagibig);
                    System.out.printf("Taxable Income: PHP %,.2f\n", taxableIncome);
                    System.out.printf("Income Tax:     PHP %,.2f\n", tax);
                    System.out.println();
                    System.out.printf("NET PAY:        PHP %,.2f\n", netPay);

                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format in line: " + line);
                }
            }

        } catch (IOException e) {
            System.out.println("Error: File not found or cannot be read.");
        }
    }

    // SSS
    public static double getSSS(double salary) {
        for (SSSRange range : sssTable) {
            if (salary >= range.min && salary <= range.max) {
                return range.contribution;
            }
        }
        return 0;
    }

    // PHILHEALTH
    public static double getPhilHealth(double salary) {
        double premiumRate = 0.03;
        double minSalary = 10000;
        double maxSalary = 60000;

        if (salary < minSalary) {
            salary = minSalary;
        } else if (salary > maxSalary) {
            salary = maxSalary;
        }

        double premium = salary * premiumRate;
        return premium / 2;
    }

    // PAG-IBIG
    public static double getPagIbig(double salary) {
        double contribution;

        if (salary >= 1000 && salary <= 1500) {
            contribution = salary * 0.01;
        } else if (salary > 1500) {
            contribution = salary * 0.02;
        } else {
            contribution = 0;
        }

        if (contribution > 100) {
            contribution = 100;
        }

        return contribution;
    }

    // TAX (STRICT TRAIN TABLE)
    public static double getTax(double taxableIncome) {

        if (taxableIncome <= 20833) {
            return 0;

        } else if (taxableIncome <= 33333) {
            return (taxableIncome - 20833) * 0.20;

        } else if (taxableIncome <= 66667) {
            return 2500 + (taxableIncome - 33333) * 0.25;

        } else if (taxableIncome <= 166667) {
            return 10833 + (taxableIncome - 66667) * 0.30;

        } else if (taxableIncome <= 666667) {
            return 40833.33 + (taxableIncome - 166667) * 0.32;

        } else {
            return 200833.33 + (taxableIncome - 666667) * 0.35;
        }
    }
}