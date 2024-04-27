package functions;

import lombok.Data;

@Data
public abstract class Function {

    private final Double leftLimit;
    private final Double rightLimit;

    public abstract double calculate(double x);
}
