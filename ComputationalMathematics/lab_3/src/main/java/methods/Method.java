package methods;

import functions.Function;
import util.Result;

public abstract class Method {

    protected final long START_PARTITION = 4;

    public abstract Result compute(Function function, double a, double b, double accuracy);

    abstract double computeRes(Function function, double a, double b, long partition);

}
