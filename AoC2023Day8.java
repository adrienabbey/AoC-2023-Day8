/*
 * Advent of Code 2023, Day 8: Haunted Wasteland
 * Adrien Abbey, Jan. 2024
 * Part One Solution: 17621
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

class AoC2023Day8 {
    /* Global Variables */
    public static String inputFileName = "input.txt";
    public static boolean testing = false;
    public static boolean partTwo = true;

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
            System.out.println(directions);
            System.out.println();
            for (String[] line : nodeList) {
                System.out.println(line[0] + " = (" + line[1] + ", " + line[2] + ")");
            }
        }

        // Track the number of steps taken:
        int stepsTaken = 0;

        if (!partTwo) {
            // Part one steps:

            // Assumption: The instructions state I need to start at AAA and stop at
            // ZZZ. I'm assuming I follow the directions in a loop, and stop as
            // soon as I hit ZZZ.

            // Find the starting index:
            int startingIndex = findIndexOf("AAA", nodeList);

            // Loop through the directions until ZZZ is found:
            boolean foundZZZ = false;
            int currentIndex = startingIndex;

            while (!foundZZZ) {
                // For each character in the direction list:
                for (int i = 0; i < directions.length(); i++) {
                    // If left:
                    if (directions.charAt(i) == 'L') {
                        // Find the target node and make that the current index:
                        currentIndex = findIndexOf(nodeList.get(currentIndex)[1], nodeList);
                        stepsTaken++;
                    } else if (directions.charAt(i) == 'R') {
                        currentIndex = findIndexOf(nodeList.get(currentIndex)[2], nodeList);
                        stepsTaken++;
                    }
                    // If we found ZZZ, stop:
                    if (nodeList.get(currentIndex)[0].contains("ZZZ")) {
                        foundZZZ = true;
                        break;
                    }
                }
            }
        } else {
            // Part two steps:

            // Find all the valid starting points:
            ArrayList<Integer> currentNodes = new ArrayList<>();
            for (int i = 0; i < nodeList.size(); i++) {
                if (nodeList.get(i)[0].charAt(2) == 'A') {
                    currentNodes.add(i);
                }
            }

            // Test code:
            if (testing) {
                System.out.println(" Starting nodes: " + currentNodes.toString());
            }

            // Start looping through the nodes:
            boolean foundAllZ = false;
            while (!foundAllZ) {
                // Test code:
                if (testing) {
                    System.out.println(" Current steps taken: " + stepsTaken);
                    for (int index : currentNodes) {
                        System.out.println(nodeList.get(index)[0] + " = ("
                                + nodeList.get(index)[1] + ", "
                                + nodeList.get(index)[2] + ")");
                    }
                }

                // For each character in the directions:
                for (int i = 0; i < directions.length(); i++) {
                    // Increment every node appropriately:
                    for (int j = 0; j < currentNodes.size(); j++) {
                        // If left:
                        if (directions.charAt(i) == 'L') {
                            // Adjust the appropriate node accordingly:
                            int targetIndex = findIndexOf(nodeList.get(currentNodes.get(j))[1], nodeList);
                            currentNodes.set(j, targetIndex);
                        } else if (directions.charAt(i) == 'R') {
                            // Adjust the node index accordingly:
                            int targetIndex = findIndexOf(nodeList.get(currentNodes.get(j))[2], nodeList);
                            currentNodes.set(j, targetIndex);
                        }
                    }
                    // Increment steps taken:
                    stepsTaken++;

                    // Check if every current node has reached xxZ:
                    boolean[] foundZ = new boolean[currentNodes.size()];
                    for (int j = 0; j < currentNodes.size(); j++) {
                        if (nodeList.get(currentNodes.get(j))[0].charAt(2) == 'Z') {
                            foundZ[j] = true;
                        }
                    }
                    // https://stackoverflow.com/a/8260897
                    foundAllZ = true;
                    for (boolean found : foundZ) {
                        if (!found) {
                            foundAllZ = false;
                        }
                    }
                    if (foundAllZ) {
                        break;
                    }
                }
            }
        }

        // Print the number of steps required to reach ZZZ:
        System.out.println("Steps taken: " + stepsTaken);
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
                    if (Character.isAlphabetic(secondSplit[0].charAt(i))
                            || Character.isDigit(secondSplit[0].charAt(i))) {
                        leftString += secondSplit[0].charAt(i);
                    }
                }
                nodeStrings[1] = leftString;

                // Parse the right branch:
                String rightString = "";
                for (int i = 0; i < secondSplit[1].length(); i++) {
                    if (Character.isAlphabetic(secondSplit[1].charAt(i))
                            || Character.isDigit(secondSplit[1].charAt(i))) {
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

    public static int findIndexOf(String nodeName, ArrayList<String[]> nodeList) {
        // Searches the given node array list for the given node name,
        // returning its index.

        for (int i = 0; i < nodeList.size(); i++) {
            if (nodeList.get(i)[0].contains(nodeName)) {
                return i;
            }
        }

        // Something went wrong, intentionally break stuff:
        return -1;
    }
}