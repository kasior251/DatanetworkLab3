import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Converter {

    public static String convert(String query, String[] currency) {
        String fromCurrency = "";
        String toCurrency = "";
        float amount = 0;
        float fromRate;
        float toRate;

        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < query.length()) {
            if (Character.isLetter(query.charAt(i))) {
                sb.append(query.charAt(i));
                i++;
            }
            else {
                fromCurrency = sb.toString();
                break;
            }
        }

        sb = new StringBuilder();
        while (i < query.length()) {
            if (Character.isDigit(query.charAt(i)) || query.charAt(i) == '.' ) {
                sb.append(query.charAt(i));
                i++;
            }
            else {
                try {
                    amount = Float.parseFloat(sb.toString());
                }
                catch (NumberFormatException e) {
                    return ("Wrong amount: " + sb.toString() + " Please try again");

                }
                break;
            }
        }

        sb = new StringBuilder();
        while (i < query.length()) {
            if (Character.isLetter(query.charAt(i))) {
                sb.append(query.charAt(i));
                i++;
            }
            else {
                break;
            }
            toCurrency = sb.toString();
        }

        fromRate = Converter.findRate(fromCurrency, currency);
        if (fromRate == 0) {
            return "Cant find currency " + fromCurrency;
        }
        toRate = Converter.findRate(toCurrency, currency);
        if (toRate == 0) {
            return "Cant find currency " + toCurrency;
        }

        System.out.println(fromCurrency + " rate " + Float.toString(fromRate));
        System.out.println(toCurrency + " rate " + Float.toString(toRate));

        float converted = (amount * fromRate) / toRate;

        return Float.toString(amount) + " " + fromCurrency + " is equal to " + Float.toString(converted) + " " + toCurrency;
     }

     //finner rate of exchange for en gitt valuta
     private static float findRate(String name, String[] currency) {
        int i = 0;
        System.out.println("Checking " + name);

        while (i < currency.length) {

            if (currency[i].equals(name)) {
                System.out.println("Found! " + name);
                return Float.parseFloat(currency[i+1]);}
            i = i+2;
        }
        return 0;
     }
}
