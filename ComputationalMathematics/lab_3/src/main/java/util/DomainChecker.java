package util;

import functions.Function;

import java.util.List;

public class DomainChecker {

    public boolean checkDomain(Function function, double a, double b) {
        List<double[]> domain = function.getDomain();

        double leftBorder = domain.get(0)[0];
        double rightBorder = domain.get(domain.size() - 1)[1];

        boolean aInDomain = a >= leftBorder && a <= rightBorder;
        boolean bInDomain = b >= leftBorder && b <= rightBorder;

        return aInDomain && bInDomain;
    }

}
