package netz;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameExtractor {
    public static String extractName(String input) throws Exception {
        Pattern pattern = Pattern.compile("<(.+?)>:"); // Regex to capture the text inside <>
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1); // Extract captured group
        }else throw new Exception("No match was found in the Name-String: + " + input);
    }

    public static String extractChannelID(String input)  throws Exception{
        Pattern pattern = Pattern.compile("\\[(-?\\d+)]"); // Regex to capture the text inside []
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1); // Extract captured group
        } else throw new Exception("No match was found in the Channel-ID-String: + " + input);
    }
}
