package app.validators;

public class CityValidator {

    public static String capitalizeCustom(String str) {
        String[] words = str.split(" ");
        StringBuilder formattedString = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0) {
                if (word.length() > 0) {
                    formattedString.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1).toLowerCase());
                }
            } else {
                formattedString.append(word.toUpperCase());
            }
            if (i < words.length - 1) {
                formattedString.append(" ");
            }
        }
        return formattedString.toString();
    }
}
