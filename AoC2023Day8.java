/*
 * Advent of Code 2023, Day 8: Haunted Wasteland
 * Adrien Abbey, Jan. 2024
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

class AoC2023Day8 {
    /* Global Variables */
    public static String inputFileName = "example1-input.txt";
    public static boolean testing = true;

    public static void main(String[] args) throws FileNotFoundException {
        // NOTE: I'm going to always assume valid input, until proven otherwise.

        // Load the input strings:
        ArrayList<String> inputStrings = loadInputStrings();

        // The first line of the input file contains the directions:
        String directions = inputStrings.get(0);

        // Parse the input:
        ArrayList<String[]> nodeList = parseNodes(inputStrings);

        // Test code:
        if (testing) {
            for (String[] line : nodeList) {
                System.out.println(line[0] + " = (" + line[1] + ", " + line[2] + ")");
            }
        }
    }

    public static ArrayList<String> loadInputStrings() throws FileNotFoundException {
        // Loads strings from the input file to an array list.

        // Open the input file:
        File inputFile = new File(inputFileName);
        Scanner inputScanner = new Scanner(inputFile);

        // Load the input strings into an array list:
        ArrayList<String> inputStrings = new ArrayList<>();
        while (inputScanner.hasNextLine()) {
            inputStrings.add(inputScanner.nextLine());
        }

        // Close the input scanner:
        inputScanner.close();

        // Return the input string array list:
        return inputStrings;
    }

    public static ArrayList<String[]> parseNodes(ArrayList<String> inputStrings) {
        // Parses the input strings, returning an array list of string arrays.
        // Order: NodeName, LeftNode, RightNode.

        // Start parsing the input strings:
        ArrayList<String[]> nodeList = new ArrayList<>();
        for (String line : inputStrings) {
            if (line.contains("=")) {
                // Create a string array to hold our node:
                String[] nodeStrings = new String[3];

                // Split the string into usable parts:
                String[] firstSplit = line.split("=");
                String[] secondSplit = firstSplit[1].split(",");

                // Parse the node name:
                nodeStrings[0] = firstSplit[0];

                // Parse the left branch:
                String leftString = "";
                for (int i = 0; i < secondSplit[0].length(); i++) {
                    if (Character.isAlphabetic(secondSplit[0].charAt(i))) {
                        leftString += secondSplit[0].charAt(i);
                    }
                }
                nodeStrings[1] = leftString;

                // Parse the right branch:
                String rightString = "";
                for (int i = 0; i < secondSplit[1].length(); i++) {
                    if (Character.isAlphabetic(secondSplit[1].charAt(i))) {
                        rightString += secondSplit[1].charAt(i);
                    }
                }
                nodeStrings[2] = rightString;

                // Finally, add the node to the array list:
                nodeList.add(nodeStrings);
            }
        }

        // Return the array list of nodes:
        return nodeList;
    }
}