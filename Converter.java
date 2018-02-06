public class Converter {

    public static String convert(String query, String[] currency) {
        //currency to be converted
        String fromCurrency = "";
        //currency converted to
        String toCurrency = "";
        //amount to be converted
        float amount = 0;
        //rate of the currency to be converted
        float fromRate;
        //rate of the currency to convert to
        float toRate;

        StringBuilder sb = new StringBuilder();
        int i = 0;

        //getting the currency to be converted
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

        if (fromCurrency.isEmpty()) {
            return "Unrecognizable or invalid query!";
        }

        sb = new StringBuilder();

        //extract the amount to be converted
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

        //extract the currency we convert to
        while (i < query.length()) {
            if (Character.isLetter(query.charAt(i))) {
                sb.append(query.charAt(i));
                i++;
            }
            else {
                break;
            }
        }
        toCurrency = sb.toString();
        if (toCurrency.isEmpty()) {
            return "Unrecognizable or invalid query!";
        }

        //find the rate of exchange of the first currency
        fromRate = Converter.findRate(fromCurrency, currency);
        if (fromRate == 0) {
            return "Cant find currency " + fromCurrency;
        }

        //find rate of exchange of the second currency
        toRate = Converter.findRate(toCurrency, currency);
        if (toRate == 0) {
            return "Cant find currency " + toCurrency;
        }

        System.out.println(fromCurrency + " rate " + Float.toString(fromRate));
        System.out.println(toCurrency + " rate " + Float.toString(toRate));

        //converted amount
        float converted = (amount * fromRate) / toRate;

        return String.format("%.2f", amount) + " " + fromCurrency + " is equal to " + String.format("%.2f", converted) + " " + toCurrency;
                //Float.toString(amount) + " " + fromCurrency + " is equal to " + Float.toString(converted) + " " + toCurrency;
     }

     //finds rate of exchange in the file
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
