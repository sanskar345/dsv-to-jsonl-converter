package com.mycompany.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    public static void main(String[] args) {
        
        // Taking input from the user
        Scanner input = new Scanner(System.in); // Create a Scanner object

        System.out.println("Enter the DSV Input Filename (With Extension): ");
        String inputFile = input.nextLine();

        System.out.println("Enter the Delimeter in the File : ");
        String delimiter = input.nextLine();

        System.out.println("Enter the Output Filename (Only name with No Extension): ");
        String outputFile = input.nextLine();

        // To read and convert dsv to jsonl
        readInputAndCreateFile(inputFile, delimiter, outputFile);

        input.close();
    }

    // Function to read input file and create output having converted data in JSONL
    public static void readInputAndCreateFile(String inputFile, String delimiter, String outputFile) {

        File file = new File("src/main/resources/" + inputFile);

        // creating check variable so that we can read file from current directory which
        // is case with running jar file and read file from resources folder when in
        // intelliJ
        boolean readInputFileFromCurrentDirectory = false;

        // Check if file exist or not
        if (!file.exists()) {

            // Check if file exist in current directory if yes then proceed
            // if not then terminate the execution
            file = new File(inputFile);
            if (!file.exists()) {
                System.out.println("The given input File does not exist!");
                System.exit(0);
            }

            // change value to true as file is in current directory.
            readInputFileFromCurrentDirectory = true;
        }

        try {

            // Creating a scanner object for file
            Scanner sc = new Scanner(file);

            // Getting the keys as they exist in the first line--
            String firstLine = sc.nextLine();
            String[] keys = splitString(firstLine, delimiter.charAt(0));

            // condition for creating output file in current folder or the resource folder
            BufferedWriter jsonlFile;
            if (readInputFileFromCurrentDirectory) {

                // If output file with the name already exists then deleting it
                File existingOutputFile = new File(outputFile + ".jsonl");
                if (existingOutputFile.exists()) {
                    existingOutputFile.delete();
                }

                jsonlFile = new BufferedWriter(
                        new FileWriter(outputFile + ".jsonl", true));
            } else {

                // If output file the name already exists then deleting it
                File existingOutputFile = new File("src/main/resources/" + outputFile + ".jsonl");
                if (existingOutputFile.exists()) {
                    existingOutputFile.delete();
                }

                jsonlFile = new BufferedWriter(
                        new FileWriter("src/main/resources/" + outputFile + ".jsonl", true));
            }

            // Reading the input file and writing in output file the converted data that is
            // in JSONL
            while (sc.hasNextLine()) {
                String[] values = splitString(sc.nextLine(), delimiter.charAt(0));
                jsonlFile.write(getObject(keys, values) + "\n");
            }

            // Closing the output file after writing in it
            jsonlFile.close();
            sc.close();
        } catch (IOException exp) {
            System.out.println("IO Exception Occurred");
        }
    }

    // This function splits the string with the given delimiter into an string array
    // it takes care of quotation string and does not split it -- takes it as single
    // word
    public static String[] splitString(String inputString, char delimiter) {
        List<String> result = new ArrayList<>();
        String currentWord = "";
        boolean insideQuotes = false;

        // Looping through the characters of the line and making word out of it and
        // adding them
        // into an array splitting by delimiter
        for (char character : inputString.toCharArray()) {
            if (character == delimiter && !insideQuotes) {
                result.add(currentWord);
                currentWord = "";
            } else {
                if (character == '"') {
                    insideQuotes = !insideQuotes;
                } else {
                    currentWord += character;
                }
            }
        }

        // The last word
        result.add(currentWord.toString());
        return result.toArray(new String[0]);
    }

    // This function converts two arrays into a JSON object
    public static String getObject(String[] keys, String[] values) {

        // Creating an instance of ObjectMapper of JACKSON library and by that
        // instance creating a JSON object using cresteObjectNode() method.
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonNode = mapper.createObjectNode();

        // Looping through keys and values and adding them into JSON object
        // conditionally.
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

        // For JSON string
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(jsonNode);
        } catch (JsonProcessingException exception) {
            System.out.println(exception.getMessage());
        }
        return jsonString;
    }

    // For converting date string into yyyy-MM-dd format
    public static String convertDate(String dateString) {

        // Splitting the string to check whether the first element is year or day and
        // adding formats that string can has
        String[] splittedDate = dateString.split("[-/]");
        List<String> formats = new ArrayList<>();
        if (splittedDate[0].length() > 2) {
            formats.add("yyyy/MM/dd");
            formats.add("yyyy-MM-dd");
        } else {
            formats.add("dd-MM-yyyy");
            formats.add("dd/MM/yyyy");
        }

        String outputFormat = "yyyy-MM-dd"; // The desired format

        // loop throughth formats that string can have parse them and convert it to
        // the desired format
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
