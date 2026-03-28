package LL1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class Tester {

    /**
     * Main method to test the LL(1) Parser.
     * It iterates through test files in LL1/tc/ and attempts to parse them.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("--- Starting LL(1) Parser Tester ---");

        File testDir = new File("LL1/tc/");
        File[] testFiles = testDir.listFiles();

        if (testFiles == null || testFiles.length == 0) {
            System.out.println("No test files found in LL1/tc/");
            return;
        }

        // Sort files for consistent order
        Arrays.sort(testFiles, Comparator.comparing(File::getName));

        for (File file : testFiles) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                System.out.println("\nParsing file: " + file.getName());
                try (InputStream inputStream = new FileInputStream(file)) {
                    Lexer lexer = new Lexer(inputStream); // Create Lexer with InputStream
                    Parser parser = new Parser(lexer);    // Pass Lexer to Parser
                    Program program = parser.parseProgram();
                    System.out.println("SUCCESS: " + file.getName() + " parsed successfully.");
                    // Optionally, print the AST. Ensure your AST nodes have a meaningful toString()
                    // System.out.println("AST:\n" + program.toString()); 
                } catch (FileNotFoundException e) {
                    System.err.println("ERROR: File not found - " + file.getName() + ": " + e.getMessage());
                } catch (RuntimeException e) {
                    System.err.println("FAILURE: " + file.getName() + " failed to parse. Error: " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("UNEXPECTED ERROR: " + file.getName() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        System.out.println("\n--- LL(1) Parser Tester Finished ---");
    }
}
