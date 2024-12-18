import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn.preprocessing import MinMaxScaler
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, precision_score, recall_score, f1_score

# Reading dataset from file
input_data = pd.read_csv('diabetes.csv')

columns = input_data.columns
rows = input_data.values

data = {}
for column_index, column in enumerate(columns):
    data[column] = list(map(lambda row: row[column_index], rows))

# Calculating statistics
df = pd.DataFrame(data)
statistics = df.describe(percentiles=[0.25, 0.5, 0.75])

# Printing statistics
print(statistics)

sns.set_style('whitegrid')

plt.figure(figsize=(15, 10))

for i, column in enumerate(df.columns[:-1]):
    plt.subplot(4, 2, i + 1)
    sns.histplot(df[column], bins=10, kde=True)
    plt.title(column + ' Distribution')
    plt.xlabel(column)
    plt.ylabel('frequency')

plt.tight_layout()
plt.show()

# Normalization 
min_max_scaler = MinMaxScaler()

df[list(df.columns[:-1])] = min_max_scaler.fit_transform(df[list(df.columns[:-1])])

# Logistic regression
def sigmoid(z):
    return 1 / (1 + np.exp(-z))

def log_loss(y_act, y_pred):
    return -np.mean(y_act * np.log(y_pred) + (1 - y_act) * np.log(1 - y_pred))

def gradient_descent(x, y, learning_rate = 0.2, iterations = 1000):
    m = len(y)
    theta = np.zeros(x.shape[1])

    for i in range(iterations):
        z = np.dot(x, theta)
        pred_y = sigmoid(z)
        errors = pred_y - y

        theta -= (learning_rate / m) * np.dot(x.T, errors)

        if i % 100 == 0:
            print(f'Iteration {i + 1}, logistic loss {log_loss(y, pred_y)}')

    return theta

def newton_method(x, y, iterations=1000):
    m, n = x.shape
    theta = np.zeros(n)

    for _ in range(iterations):
        z = np.dot(x, theta)
        p = sigmoid(z)

        gradient = np.dot(x.T, (p - y)) / m

        D = np.diag(p * (1 - p))
        hessian = -np.dot(np.dot(x.T, D), x) / m

        theta -= np.dot(hessian.T, gradient)

    return theta

# Partitioning of dataset 
x = df[list(df.columns[:-1])].values
y = df[df.columns[-1]].values

x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.2)

# Testing 
is_gradient = True
learning_rate = 0.2
iterations = 10000

weights = None
if is_gradient:
    weights = gradient_descent(x_train, y_train, learning_rate=learning_rate, iterations=iterations)
else:
    weights = newton_method(x_train, y_train, iterations = iterations)

y_pred = np.dot(x_test, weights) >= 0.5

print(f'Accuracy: {accuracy_score(y_test, y_pred)}')
print(f'Precision: {precision_score(y_test, y_pred)}')
print(f'Recall: {recall_score(y_test, y_pred)}')
print(f'F1: {f1_score(y_test, y_pred)}')
