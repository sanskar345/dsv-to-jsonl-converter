package com.mycompany.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class App {
    public static Scanner input = new Scanner(System.in); // Create a Scanner object

    public static void main(String[] args) {
        System.out.println("Enter the DSV Input Filename : ");
        String inputFile = input.nextLine();

        System.out.println("Enter the Delimeter in the File : ");
        String delimiter = input.nextLine();

        System.out.println("Enter the Output Filename : ");
        String outputFile = input.nextLine();

        File file = new File("src/main/resources/" + inputFile);

        if (!file.exists()) {
            System.out.println("The given input File does not exist!");
            System.exit(0);
        }

        try {

            Scanner sc = new Scanner(file);

            // Getting the keys --
            String firstLine = sc.nextLine();
            String[] keys = splitString(firstLine, delimiter.charAt(0));

            BufferedWriter jsonlFile = new BufferedWriter(
                    new FileWriter("src/main/resources/" + outputFile + ".jsonl", true));

            while (sc.hasNextLine()) {

                String[] values = splitString(sc.nextLine(), delimiter.charAt(0));
                jsonlFile.write(getObject(keys, values) + "\n");
            }

            jsonlFile.close();

        } catch (Exception exp) {

        }

    }

    public static String[] splitString(String inputString, char delimiter) {
        List<String> result = new ArrayList<>();
        String currentWord = "";
        boolean insideQuotes = false;

        for (char character : inputString.toCharArray()) {
            if (character == delimiter && !insideQuotes) {
                result.add(currentWord.toString());
                currentWord = "";
            } else {
                if (character == '"') {
                    insideQuotes = !insideQuotes;
                } else {
                    currentWord += character;
                }
            }
        }

        result.add(currentWord.toString());
        return result.toArray(new String[0]);
    }

    public static String getObject(String[] keys, String[] values) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonNode = mapper.createObjectNode();

        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            String value = values[i];
            if (!value.isEmpty()) {
                if ("dateOfBirth".equals(key)) {
                    value = convertDate(value);
                }
                if ("salary".equals(key)) {
                    int intValue = Integer.parseInt(value);
                    jsonNode.put(key, intValue);
                    continue;
                }
                jsonNode.put(key, value);
            }
        }

        return mapper.writeValueAsString(jsonNode);
    }

    public static String convertDate(String dateString) {

        String[] splittedDate = dateString.split("[-/]");
        List<String> formats = new ArrayList<>();
        if (splittedDate[0].length() > 2) {
            formats.add("yyyy/MM/dd");
            formats.add("yyyy-MM-dd");
        } else {
            formats.add("dd-MM-yyyy");
            formats.add("dd/MM/yyyy");
        }

        String outputFormat = "yyyy-MM-dd";

        for (String format : formats) {
            try {
                DateFormat inputDateFormat = new SimpleDateFormat(format);
                java.util.Date date = inputDateFormat.parse(dateString);
                DateFormat outputDateFormat = new SimpleDateFormat(outputFormat);
                return outputDateFormat.format(date);
            } catch (ParseException e) {
                // ignore and try next format
            }
        }

        // no matching format found, return input string
        return dateString;
    }

}
