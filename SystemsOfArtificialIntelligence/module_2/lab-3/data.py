import math

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

class Data:

    def __init__(self, dataset_path):
        dataset = pd.read_csv(dataset_path)
        values = dataset.values

        self.hours_studied = list(map(lambda row: self._replace_nan_to_int(row[0]), values))
        self.previous_scores = list(map(lambda row: self._replace_nan_to_int(row[1]), values))
        self.extracurricular_activities = list(map(lambda row: self._replace_empty(row[2]), values))
        self.sleep_hours = list(map(lambda row: self._replace_nan_to_int(row[3]), values))
        self.sample_question_papers_practised = list(map(lambda row: self._replace_nan_to_int(row[4]), values))
        self.hours_rest = list(map(lambda row: 24 - self._replace_nan_to_int(row[0]) - self._replace_nan_to_int(row[3]) , values))
        self.performance_index = list(map(lambda row: self._replace_nan_to_float(row[5]), values))

        self.mean_hours_studied = None
        self.standard_deviation_hours_studied = None

        self.mean_previous_scores = None
        self.standard_deviation_previous_scores = None

        self.mean_sleep_hours = None
        self.standard_deviation_sleep_hours = None

        self.mean_sample_question_papers_practised = None
        self.standard_deviation_sample_question_papers_practised = None

        self.mean_hours_rest = None
        self.standard_deviation_hours_rest = None

        self.mean_performance_index = None
        self.standard_deviation_performance_index = None

    def encode_categorical_features(self):
        self.extracurricular_activities = list(map(lambda value : 1 if value == "Yes" else 0, self.extracurricular_activities))

    def normalize_features(self):
        self.mean_hours_studied = np.mean(self.hours_studied)
        self.standard_deviation_hours_studied = np.std(self.hours_studied)

        self.mean_previous_scores = np.mean(self.previous_scores)
        self.standard_deviation_previous_scores = np.std(self.previous_scores)

        self.mean_sleep_hours = np.mean(self.sleep_hours)
        self.standard_deviation_sleep_hours = np.std(self.sleep_hours)

        self.mean_sample_question_papers_practised = np.mean(self.sample_question_papers_practised)
        self.standard_deviation_sample_question_papers_practised = np.std(self.sample_question_papers_practised)

        self.mean_sleep_hours = np.mean(self.sleep_hours)
        self.standard_deviation_sleep_hours = np.std(self.sleep_hours)

        self.mean_hours_rest = np.mean(self.hours_studied)
        self.standard_deviation_hours_rest = np.std(self.hours_studied)

        self.mean_performance_index = np.mean(self.performance_index)
        self.standard_deviation_performance_index = np.std(self.performance_index)

        self._do_normalization()

    def _do_normalization(self):
        self.hours_studied = list(map(lambda hours : (hours - self.mean_hours_studied) / self.standard_deviation_hours_studied, self.hours_studied))
        self.previous_scores = list(map(lambda previous_scores : (previous_scores - self.mean_previous_scores) / self.standard_deviation_previous_scores, self.previous_scores))
        self.sleep_hours = list(map(lambda sleep_hours : (sleep_hours - self.mean_sleep_hours) / self.standard_deviation_sleep_hours, self.sleep_hours))
        self.sample_question_papers_practised = list(map(lambda sample_question_papers_practised : (sample_question_papers_practised - self.mean_sample_question_papers_practised) / self.standard_deviation_sample_question_papers_practised, self.sample_question_papers_practised))
        self.hours_rest = list(map(lambda hours_rest: (hours_rest - self.mean_hours_rest) / self.standard_deviation_hours_rest, self.hours_rest))
        self.performance_index = list(map(lambda performance_index : (performance_index - self.mean_performance_index) / self.standard_deviation_performance_index, self.performance_index))

    def get_feature_by_index(self, index):
        if index == 0:
            return self.hours_studied
        elif index == 1:
            return self.previous_scores
        elif index == 2:
            return self.extracurricular_activities
        elif index == 3:
            return self.sleep_hours
        elif index == 4:
            return self.sample_question_papers_practised
        elif index == 5:
            return self.sleep_hours
        elif index == 6:
            return self.performance_index
        else:
            raise 'Unknown index'

    def get_data(self):
        x = []
        y = []
        for i in range(len(self.hours_studied)):
            x.append([
                self.hours_studied[i],
                self.previous_scores[i],
                self.extracurricular_activities[i],
                self.sleep_hours[i],
                self.sample_question_papers_practised[i],
                self.hours_rest[i],
            ])
            y.append(self.performance_index[i])

        return x, y

    @staticmethod
    def show_histogram(values, name):
        values = np.array(values)

        min_val = np.min(values)
        max_val = np.max(values)
        mean_val = np.mean(values)
        deviation_val = np.std(values)
        q1_val = np.percentile(values, 25)
        q3_val = np.percentile(values, 75)

        labels = ['Минимум', 'Максимум', 'Мат. Ожидание', 'СКО', 'Квантиль 0.25', 'Квантиль 0.75']
        values = [min_val, max_val, mean_val, deviation_val, q1_val, q3_val]

        plt.figure(figsize=(10, 6))
        plt.bar(labels, values)
        plt.title(name)
        plt.xticks(rotation=45)
        plt.tight_layout()
        plt.show()

    @staticmethod
    def _replace_nan_to_int(value):
        return int(value) if not math.isnan(value) else 0

    @staticmethod
    def _replace_nan_to_float(value):
        return float(value) if not math.isnan(value) else 0

    @staticmethod
    def _replace_empty(value):
        return value if value else 'No'
