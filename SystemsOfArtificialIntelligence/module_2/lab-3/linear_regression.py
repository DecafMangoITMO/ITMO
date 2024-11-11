import numpy as np

def calculate_linear_regression(data, columns_indexes):
    X = []
    Y = []
    for i in range(len(data)):
        row = [1]
        for j in range(len(columns_indexes)):
            row.append(data[i][columns_indexes[j]])
        X.append(row)
        Y.append(data[i][6])

    X = np.array(X)
    Y = np.array(Y)
    print(Y)

    B = np.dot(np.dot(np.linalg.inv(np.dot(np.transpose(X), X)), np.transpose(X)), Y)
    return B