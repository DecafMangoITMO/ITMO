
import java.util.List;

public class Data {
    private List<List<Double>> A;
    private List<Double> B;
    private double accuracy;
    private int iterations = -1;

    public Data(List<List<Double>> A, List<Double> B, double accuracy) {
        this.A = A;
        this.B = B;
        this.accuracy = accuracy;
    }

    public List<List<Double>> getA() {
        return A;
    }

    public void setA(List<List<Double>> a) {
        A = a;
    }

    public List<Double> getB() {
        return B;
    }

    public void setB(List<Double> b) {
        B = b;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }
}