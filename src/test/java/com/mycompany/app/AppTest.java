package com.mycompany.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test for App.
 * 
 */
public class AppTest {

    // Created expected output var so that it is available for all test cases
    private static String expectedOutput = "";

    @BeforeClass
    // Runs only once the first executer
    public static void onlyOnce(){

        // Read the expected output file present in test resorces and store the data in 
        // expected output variable
        File file = new File("src/test/java/com/mycompany/app/resources/JSONL output.jsonl");
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                expectedOutput += sc.nextLine() + "\n";
            }
            sc.close();
        } catch (FileNotFoundException exp) {

            // If file not found
            assertFalse("Expected Result File Not Found", false);
        }
    }

    @Test
    // Test Case 1
    public void dsvFileWithCommaDelimiter() {
        String inputFile = "DSV input 1.txt";
        String outputFilename = "DSV output 1";
        App.readInputAndCreateFile(inputFile, ",", outputFilename);

        // Read the newly created output file and store the data in actualOutput var
        File file = new File("src/main/resources/" + outputFilename + ".jsonl");
        String actualOutput = "";
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                actualOutput += sc.nextLine() + "\n";
            }
            sc.close();
        } catch (FileNotFoundException exp) {

            // If file not found
            assertFalse("Output file not found!", false);
        }

        // Compare both expected and actual result
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    // Test Case 2
    public void dsvFileWithPipeDelimiter() {
        String inputFile = "DSV input 2.txt";
        String outputFilename = "DSV output 2";
        App.readInputAndCreateFile(inputFile, "|", outputFilename);

        // Read the newly created output file and store the data in actualOutput var
        File file = new File("src/main/resources/" + outputFilename + ".jsonl");
        String actualOutput = "";
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                actualOutput += sc.nextLine() + "\n";
            }
            sc.close();
        } catch (FileNotFoundException exp) {

            // If file not found
            assertFalse("Output file not found!", false);
        }

        // Compare both expected and actual result
        assertEquals(expectedOutput, actualOutput);
    }
}
