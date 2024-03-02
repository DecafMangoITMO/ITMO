package SolvingNonlinearEquation.methods;

import java.util.List;

public class Result {

    private final List<String> headers;
    private final List<List<String>> data;

    public Result(List<String> headers, List<List<String>> data) {
        this.headers = headers;
        this.data = data;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public List<List<String>> getData() {
        return data;
    }

}
