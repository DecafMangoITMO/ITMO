import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import random

from sklearn.model_selection import train_test_split
from sklearn.metrics import confusion_matrix


def read_data(datapath):
    return pd.read_csv(datapath).values


def get_feature_name(index):
    if index == 0:
        return 'Беременность'
    elif index == 1:
        return 'Глюкоза'
    elif index == 2:
        return 'Кровяное давление'
    elif index == 3:
        return 'Слабость кожи'
    elif index == 4:
        return 'Инсулин'
    elif index == 5:
        return 'Индекс Массы Тела'
    elif index == 6:
        return 'Родословная'
    elif index == 7:
        return 'Возраст'
    else:
        raise 'Unknown index'


def show_feature(feature_data, feature_name):
    feature_data = np.array(feature_data)

    mean = np.mean(feature_data)
    std_dev = np.std(feature_data, ddof=1)
    min_val = np.min(feature_data)
    max_val = np.max(feature_data)
    q1 = np.percentile(feature_data, 25)
    q3 = np.percentile(feature_data, 75)

    labels = ['Мат. Ожидание', 'СКО', 'Минимум', 'Максимум', 'Квантиль 25', 'Квантиль 75']
    values = [mean, std_dev, min_val, max_val, q1, q3]

    plt.figure(figsize=(10, 6))
    plt.bar(labels, values)
    plt.xticks(rotation=45)
    plt.tight_layout()
    plt.title(f'{feature_name} (кол-во: {len(feature_data)})')
    plt.show()


def normalize(feature_data):
    feature_data = np.array(feature_data)

    mean = np.mean(feature_data)
    std_dev = np.std(feature_data)

    return list(map(lambda value: (value - mean) / std_dev, feature_data))


def calculate_distance(x_train_row, x_test_row, column_indexes):
    distance = 0
    for column_index in column_indexes:
        distance += (x_train_row[column_index] - x_test_row[column_index]) ** 2
    return distance ** 0.5


def predict(x_train, x_test_row, y_train, k, column_indexes):
    results = []
    for i in range(len(x_train)):
        results.append([
            calculate_distance(x_train[i], x_test_row, column_indexes),
            y_train[i],
        ])
    results.sort(key=lambda result: result[0])
    closest = results[:k:]
    closest = list(map(lambda result: result[1], closest))
    print(len(closest))

    return round(sum(closest) / len(closest))


def test_model(x_train, x_test, y_train, y_test, k, column_indexes):
    results = []

    for i in range(len(x_test)):
        results.append([
            predict(x_train, x_test[i], y_train, k, column_indexes),
            y_test[i],
        ])

    y_true = list(map(lambda result: result[1], results))
    y_pred = list(map(lambda result: result[0], results))

    return confusion_matrix(y_true, y_pred)


def compare_models(x_train, x_test, y_train, y_test, k, column_indexes):
    random_result = test_model(x_train, x_test, y_train, y_test, k, random_column_indexes)
    full_result = test_model(x_train, x_test, y_train, y_test, k, [0, 1, 2, 3, 4, 5, 6, 7])

    values = [
        full_result[0][0],
        random_result[0][0],
        full_result[0][1],
        random_result[0][1],
        full_result[1][0],
        random_result[1][0],
        full_result[1][1],
        random_result[1][1],
    ]
    labels = [
        'TP Full',
        'TP Random',
        'TN Full',
        'TN Random',
        'FP Full',
        'FP Random',
        'FN Full',
        'FN Random',
    ]

    random_column_names = list(map(lambda column_index: get_feature_name(column_index), column_indexes))
    random_column_names = ''.join(random_column_names)

    plt.figure(figsize=(10, 6))
    plt.bar(labels, values)
    plt.xticks(rotation=45)
    plt.tight_layout()
    plt.title(f'Сравнение полной модели с моделью из свойств: {random_column_names} (k={k})')
    plt.show()




data = read_data('./diabetes.xls')

pregnancy = list(map(lambda row: row[0], data))
glucose = list(map(lambda row: row[1], data))
blood_pressure = list(map(lambda row: row[2], data))
skin_thickness = list(map(lambda row: row[3], data))
insulin = list(map(lambda row: row[4], data))
bmi = list(map(lambda row: row[5], data))
pedigree = list(map(lambda row: row[6], data))
age = list(map(lambda row: row[7], data))
outcome = list(map(lambda row: row[8], data))

show_feature(pregnancy, 'Беременность')
show_feature(glucose, 'Глюкоза')
show_feature(blood_pressure, 'Кровяное давление')
show_feature(skin_thickness, 'Слабость кожи')
show_feature(insulin, 'Инсулин')
show_feature(bmi, 'Индекс Массы Тела')
show_feature(pedigree, 'Родословная')
show_feature(age, 'Возраст')
show_feature(outcome, 'Результат')

normalized_pregnancy = normalize(pregnancy)
normalized_glucose = normalize(glucose)
normalized_blood_pressure = normalize(blood_pressure)
normalized_skin_thickness = normalize(skin_thickness)
normalized_insulin = normalize(insulin)
normalized_bmi = normalize(bmi)
normalized_pedigree = normalize(pedigree)
normalized_age = normalize(age)
normalized_outcome = normalize(outcome)

x = []
y = outcome[::]

for i in range(len(normalized_pregnancy)):
    x.append([
        normalized_pregnancy[i],
        normalized_glucose[i],
        normalized_blood_pressure[i],
        normalized_skin_thickness[i],
        normalized_insulin[i],
        normalized_bmi[i],
        normalized_pedigree[i],
        normalized_age[i],
    ])

x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.2, random_state=0)

n = random.randint(1, 8)
random_column_indexes = random.sample([0, 1, 2, 3, 4, 5, 6, 7], n)

compare_models(x_train, x_test, y_train, y_test, 10, random_column_indexes)
compare_models(x_train, x_test, y_train, y_test, 100, random_column_indexes)
compare_models(x_train, x_test, y_train, y_test, 1000, random_column_indexes)