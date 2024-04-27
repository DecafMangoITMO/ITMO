package util;

import java.util.List;

public class TableGenerator {

    public String generate(List<String> headers, List<List<String>> data) {
        int width = 0;
        int maxSize = 0;
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(0).size(); j++) {
                maxSize = Math.max(maxSize, data.get(i).get(j).length());
                maxSize = Math.max(maxSize, headers.get(j).length());
            }
        }
        maxSize += 2;
        if (maxSize % 2 == 1)
            maxSize++;

        width = maxSize * headers.size() + (headers.size() * 2 - headers.size() - 1);
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < headers.size(); i++) {
            buffer.append("|")
                    .append(" ".repeat((maxSize - headers.get(i).length()) / 2))
                    .append(headers.get(i))
                    .append(" ".repeat((maxSize - headers.get(i).length()) / 2 + ((maxSize - headers.get(i).length())) % 2));
        }

        buffer.append("|").append("\n").append(" ").append("‾".repeat(width)).append("\n");
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(0).size(); j++) {
                buffer.append("|")
                        .append(" ".repeat((maxSize - data.get(i).get(j).length()) / 2))
                        .append(data.get(i).get(j))
                        .append(" ".repeat((maxSize - data.get(i).get(j).length()) / 2 + ((maxSize - data.get(i).get(j).length()) % 2)));
            }
            buffer.append("|").append("\n").append(" ").append("‾".repeat(width)).append("\n");
        }
        return buffer.toString();
    }

}