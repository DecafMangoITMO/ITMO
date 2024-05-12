package methods;

import lombok.Data;

import java.util.List;

@Data
public class Result {

    private final List<Double> x;
    private final List<Double> y;
    private final List<String> headers;
    private final List<List<String>> data;

}
