/*
 * Node class for Advent of Code, Day 8: Haunted Wasteland
 * Adrien Abbey, Jan. 2024
 */

public class Node {

    /* Fields */

    private String nodeName;
    private String leftNode;
    private String rightNode;

    /* Constructor */

    public Node(String nodeName, String leftNode, String rightNode) {
        this.nodeName = nodeName;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    /* Methods */

    public String getName() {
        return nodeName;
    }

    public String getLeft() {
        return leftNode;
    }

    public String getRight() {
        return rightNode;
    }

    @Override
    public String toString() {
        return nodeName + " = (" + leftNode + ", " + rightNode + ")";
    }
}
