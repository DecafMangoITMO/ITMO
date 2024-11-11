import numpy as np

from data import Data
from sklearn.model_selection import train_test_split

from linear_regression import calculate_linear_regression
from tester import test

import matplotlib.pyplot as plt

data = Data('./Student_Performance.csv')

Data.show_histogram(data.hours_studied, 'Часы учебы')
Data.show_histogram(data.previous_scores, 'Предыдущие результаты')
Data.show_histogram(data.sleep_hours, 'Часы сна')
Data.show_histogram(data.sample_question_papers_practised, 'Решено вопросов')
Data.show_histogram(data.hours_rest, 'Часы отдыха')
Data.show_histogram(data.performance_index, 'Эффективность')

data.encode_categorical_features()
data.normalize_features()

data_x, data_y = data.get_data()
x_train, x_test, y_train, y_test = train_test_split(data_x, data_y,  test_size=0.2)

raw_data_train = []
raw_data_test = []

for i in range(len(x_train)):
    row = []
    row.extend(x_train[i])
    row.append(y_train[i])
    raw_data_train.append(row)

for i in range(len(x_test)):
    row = []
    row.extend(x_test[i])
    row.append(y_test[i])
    raw_data_test.append(row)

model_1 = calculate_linear_regression(raw_data_train, [0, 1, 2, 3, 4, 5])
result_1 = test(model_1, raw_data_test, [0, 1, 2, 3, 4, 5])

model_2 = calculate_linear_regression(raw_data_train, [0, 1, 5])
result_2 = test(model_2, raw_data_test, [0, 1, 5])

model_3 = calculate_linear_regression(raw_data_train, [0, 2, 3, 4])
result_3 = test(model_3, raw_data_test, [0, 2, 3, 4])

labels = ['Модель 1', 'Модель 2', 'Модель 3']
values = [result_1, result_2, result_3]

plt.figure(figsize=(10, 6))
plt.bar(labels, values)
plt.title('Результаты')
plt.xticks(rotation=45)
plt.yticks(np.arange(0, 1.05, 0.05))
plt.tight_layout()
plt.show()
