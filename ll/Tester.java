package ll;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Test runner for the LL(1) lexer and parser.
 * <p>
 * {@code Tester} locates every {@code .txt} file inside the {@code LL1/tc/}
 * directory, feeds each one through a {@link Lexer} and {@link Parser}, and
 * reports whether parsing succeeded or failed.  Files are processed in
 * alphabetical order for reproducible output.
 * </p>
 * <p>
 * This class contains only a {@code main} method and is not intended to be
 * instantiated.
 * </p>
 *
 * @author Boxuan Shan
 * @version 03242025
 * @see Lexer
 * @see Parser
 */
public class Tester
{
    /**
     * Entry point for the LL(1) parser test suite.
     * <p>
     * Iterates over all {@code .txt} files in {@code LL1/tc/} (relative to the
     * working directory), parses each one, and prints a {@code SUCCESS} or
     * {@code FAILURE} line to standard output / error respectively.
     * </p>
     *
     * @param args command-line arguments; not used
     */
    public static void main(String[] args)
    {
        System.out.println("--- Starting LL(1) Parser Tester ---");

        File testDir = new File("LL1/tc/");
        File[] testFiles = testDir.listFiles();

        if (testFiles == null || testFiles.length == 0)
        {
            System.out.println("No test files found in LL1/tc/");
            return;
        }

        Arrays.sort(testFiles, Comparator.comparing(File::getName));

        for (File file : testFiles)
        {
            if (file.isFile() && file.getName().endsWith(".txt"))
            {
                System.out.println("\nParsing file: " + file.getName());
                try (InputStream inputStream = new FileInputStream(file))
                {
                    Lexer lexer = new Lexer(inputStream);
                    Parser parser = new Parser(lexer);
                    Program program = parser.parseProgram();
                    System.out.println(
                        "SUCCESS: " + file.getName() + " parsed successfully.");
                    // Optionally print the AST:
                    // System.out.println("AST:\n" + program.toString());
                }
                catch (FileNotFoundException e)
                {
                    System.err.println(
                        "ERROR: File not found - "
                        + file.getName() + ": " + e.getMessage());
                }
                catch (RuntimeException e)
                {
                    System.err.println(
                        "FAILURE: " + file.getName()
                        + " failed to parse. Error: " + e.getMessage());
                }
                catch (Exception e)
                {
                    System.err.println(
                        "UNEXPECTED ERROR: "
                        + file.getName() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        System.out.println("\n--- LL(1) Parser Tester Finished ---");
    }
}
