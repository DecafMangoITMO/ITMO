package SolvingNonlinearEquation.functions;

public class Solution {

    private final double value;
    private final double left;
    private final double right;
    private final boolean hasLeft;
    private final boolean hasRight;

    public Solution(double value, double left, double right, boolean hasLeft, boolean hasRight) {
        this.value = value;
        this.left = left;
        this.right = right;
        this.hasLeft = hasLeft;
        this.hasRight = hasRight;
    }

    public double getValue() {
        return value;
    }

    public double getLeft() {
        return left;
    }

    public double getRight() {
        return right;
    }

    public boolean isHasLeft() {
        return hasLeft;
    }

    public boolean isHasRight() {
        return hasRight;
    }
}
