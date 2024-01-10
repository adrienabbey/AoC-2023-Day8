/*
 * Advent of Code 2023, Day 8: Haunted Wasteland
 * Adrien Abbey, Jan. 2024
 * Part One Solution: 17621
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class AoC2023Day8 {
    /* Global Variables */
    public static String inputFileName = "input.txt";
    public static boolean testing = false;
    public static boolean partTwo = true;
    public static ArrayList<String> inputStrings = new ArrayList<>();
    public static ArrayList<Node> nodeList = new ArrayList<>();
    public static int stepsTaken = 0;

    public static void main(String[] args) throws FileNotFoundException {
        // NOTE: I'm going to always assume valid input, until proven otherwise.

        // Load the input strings:
        loadInputStrings();

        // The first line of the input file contains the directions:
        String directions = inputStrings.get(0);

        // Parse the input:
        parseNodes();

        // Test code:
        if (testing) {
            System.out.println(directions);
            System.out.println();
            for (Node node : nodeList) {
                System.out.println(node);
            }
        }

        // Track the number of steps taken:

        if (!partTwo) {
            // Part one steps:

            // Assumption: The instructions state I need to start at AAA and stop at
            // ZZZ. I'm assuming I follow the directions in a loop, and stop as
            // soon as I hit ZZZ.

            // Find the starting index:
            int startingIndex = findIndexOf("AAA");

            // Loop through the directions until ZZZ is found:
            boolean foundZZZ = false;
            int currentIndex = startingIndex;

            while (!foundZZZ) {
                // For each character in the direction list:
                for (int i = 0; i < directions.length(); i++) {
                    // If left:
                    if (directions.charAt(i) == 'L') {
                        // Find the target node and make that the current index:
                        currentIndex = findIndexOf(nodeList.get(currentIndex).getLeft());
                        stepsTaken++;
                    } else if (directions.charAt(i) == 'R') {
                        currentIndex = findIndexOf(nodeList.get(currentIndex).getRight());
                        stepsTaken++;
                    }
                    // If we found ZZZ, stop:
                    if (nodeList.get(currentIndex).getName().contains("ZZZ")) {
                        foundZZZ = true;
                        break;
                    }
                }
            }
        } else {
            // Part two steps:

            // Add a Runnable to track progress:
            Runnable timerRunnable = new Runnable() {
                public void run() {
                    System.out.println(" Steps taken: " + stepsTaken);
                }
            };

            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
            executorService.scheduleAtFixedRate(timerRunnable, 0, 10, TimeUnit.SECONDS);

            // Find all the valid starting points:
            ArrayList<Integer> currentNodes = new ArrayList<>();
            for (int i = 0; i < nodeList.size(); i++) {
                if (nodeList.get(i).getName().charAt(2) == 'A') {
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
                // For each character in the directions:
                for (int i = 0; i < directions.length(); i++) {
                    // Increment every node appropriately:
                    for (int j = 0; j < currentNodes.size(); j++) {
                        // If left:
                        if (directions.charAt(i) == 'L') {
                            // Adjust the appropriate node accordingly:
                            int targetIndex = findIndexOf(nodeList.get(currentNodes.get(j)).getLeft());
                            currentNodes.set(j, targetIndex);
                        } else if (directions.charAt(i) == 'R') {
                            // Adjust the node index accordingly:
                            int targetIndex = findIndexOf(nodeList.get(currentNodes.get(j)).getRight());
                            currentNodes.set(j, targetIndex);
                        }
                    }
                    // Increment steps taken:
                    stepsTaken++;

                    // Test code:
                    if (testing) {
                        System.out.println(" Current steps taken: " + stepsTaken);
                        for (int index : currentNodes) {
                            System.out.println(nodeList.get(index));
                        }
                    }

                    // Check if every current node has reached xxZ:
                    boolean[] foundZ = new boolean[currentNodes.size()];
                    for (int j = 0; j < currentNodes.size(); j++) {
                        if (nodeList.get(currentNodes.get(j)).getName().charAt(2) == 'Z') {
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

    public static void loadInputStrings() throws FileNotFoundException {
        // Loads strings from the input file to an array list.

        // Open the input file:
        File inputFile = new File(inputFileName);
        Scanner inputScanner = new Scanner(inputFile);

        // Load the input strings into an array list:
        while (inputScanner.hasNextLine()) {
            inputStrings.add(inputScanner.nextLine());
        }

        // Close the input scanner:
        inputScanner.close();
    }

    public static void parseNodes() {
        // Parses the input strings, returning an array list of string arrays.
        // Order: NodeName, LeftNode, RightNode.

        // Start parsing the input strings:
        for (String line : inputStrings) {
            if (line.contains("=")) {
                // Split the string into usable parts:
                String[] firstSplit = line.split("=");
                String[] secondSplit = firstSplit[1].split(",");

                // Parse the node name:
                String nodeName = firstSplit[0];

                // Parse the left branch:
                String leftNode = "";
                for (int i = 0; i < secondSplit[0].length(); i++) {
                    if (Character.isAlphabetic(secondSplit[0].charAt(i))
                            || Character.isDigit(secondSplit[0].charAt(i))) {
                        leftNode += secondSplit[0].charAt(i);
                    }
                }

                // Parse the right branch:
                String rightNode = "";
                for (int i = 0; i < secondSplit[1].length(); i++) {
                    if (Character.isAlphabetic(secondSplit[1].charAt(i))
                            || Character.isDigit(secondSplit[1].charAt(i))) {
                        rightNode += secondSplit[1].charAt(i);
                    }
                }

                Node newNode = new Node(nodeName, leftNode, rightNode);

                // Finally, add the node to the array list:
                nodeList.add(newNode);
            }
        }
    }

    public static int findIndexOf(String nodeName) {
        // Searches the given node array list for the given node name,
        // returning its index.

        for (int i = 0; i < nodeList.size(); i++) {
            if (nodeList.get(i).getName().contains(nodeName)) {
                return i;
            }
        }

        // Something went wrong, intentionally break stuff:
        return -1;
    }
}