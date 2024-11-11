def test(model, raw_test_data, columns):
    y_actual = [row[6] for row in raw_test_data]
    y_actual_mean = sum(y_actual) / len(y_actual)

    SStot = 0
    for i in range(len(y_actual)):
        SStot += (y_actual[i] - y_actual_mean) ** 2

    SSres = 0
    for i in range(len(y_actual)):
        y_predict = model[0]
        for j in range(len(columns)):
            y_predict += model[j + 1] * raw_test_data[i][columns[j]]

        SSres += (y_actual[i] - y_predict) ** 2

    return 1 - SSres / SStot