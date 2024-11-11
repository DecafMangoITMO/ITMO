import numpy as np
import pandas as pd
import math
import random
import matplotlib.pyplot as plt

from sklearn.model_selection import train_test_split
from sklearn.metrics import confusion_matrix


class Attribute:

    def __init__(self, index, unique_values):
        self.index = index
        self.unique_values = unique_values


class Node:

    def __init__(self, examples, attribute, c, is_list):
        self.examples = examples
        self.attribute = attribute
        self.children = {}
        self.is_list = is_list
        self.c = c


class Tree:

    def __init__(self, examples, attributes):
        self.root = None
        self.attributes = attributes

        self.examples = examples

    def train(self, parent_node):
        # if we've just started training tree
        if parent_node == None:
            parent_node = Node(self.examples, None, None, False)
            self.root = parent_node

        # just a check for safety
        if parent_node.is_list:
            return

        examples = parent_node.examples
        info = self.calculate_info(examples)

        # starting to choose the best argument for current node
        gains = []

        for attribute in self.attributes:
            attribute_unique_values = attribute.unique_values
            attribute_unique_values_examples = []

            for attribute_unique_value in attribute_unique_values:
                hit = []
                for example in examples:
                    if example[attribute.index] == attribute_unique_value:
                        hit.append(example)
                attribute_unique_values_examples.append(hit)

            info_x = 0
            split_info_x = 0
            for attribute_unique_values_example in attribute_unique_values_examples:
                if len(attribute_unique_values_example) == 0:
                    continue
                info_x += len(attribute_unique_values_example) / len(examples) * self.calculate_info(
                    attribute_unique_values_example)
                split_info_x -= len(attribute_unique_values_example) / len(examples) * math.log2(
                    len(attribute_unique_values_example) / len(examples))

            if split_info_x == 0:
                continue

            gains.append([attribute, (info - info_x) / split_info_x])

        # when the best choice is found - updating node

        success_examples_count = 0
        failure_examples_count = 0
        for example in examples:
            if example[-1] == 0:
                failure_examples_count += 1
            else:
                success_examples_count += 1

        if len(gains) == 0:
            parent_node.is_list = True
            parent_node.c = 0
            return

        best_gain = max(gains, key=lambda x: x[1])
        best_attribute = best_gain[0]

        parent_node.attribute = best_attribute

        children = {}

        for attribute_unique_value in best_attribute.unique_values:
            hit = []
            for example in examples:
                if example[best_attribute.index] == attribute_unique_value:
                    hit.append(example)

            if len(hit) == 0:
                if success_examples_count > failure_examples_count:
                    child = Node(None, None, 1, True)
                else:
                    child = Node(None, None, 0, True)
            elif len(hit) == 1:
                child = Node(None, None, hit[0][-1], True)
            else:
                child = Node(hit, None, 0, False)

            children[attribute_unique_value] = child

        parent_node.children = children

        for child in children.values():
            self.train(child)

    def test(self, example, node):
        while not node.is_list:
            return self.test(example, node.children[example[node.attribute.index]])
        return node

    def calculate_info(self, examples):
        examples_count = len(examples)
        success_examples_count = 0
        failure_examples_count = 0
        for example in examples:
            if example[-1] == 0:
                failure_examples_count += 1
            else:
                success_examples_count += 1

        if success_examples_count / examples_count == 0 or failure_examples_count / examples_count == 0:
            return 0

        return - (success_examples_count / examples_count) * math.log2(success_examples_count / examples_count) - (
                failure_examples_count / examples_count) * math.log2(failure_examples_count / examples_count)


input_data = pd.read_csv('DATA (1).csv')
examples = input_data.values

min_grade = min(list(map(lambda example: example[-1], examples)))
max_grade = max(list(map(lambda example: example[-1], examples)))
transitional_value = (max_grade - min_grade) / 2

# replacing grade to another metric - success
for i in range(len(examples)):
    if examples[i][-1] < transitional_value:
        examples[i][-1] = 0
    else:
        examples[i][-1] = 1

attributes_count = len(input_data.columns) - 2  # without example's id and grade
attributes_to_take = int(math.sqrt(len(examples)))

attributes_indexes = sorted(random.sample(range(1, attributes_count), attributes_to_take))
attributes = []
for attribute_index in attributes_indexes:
    attributes_unique_values = set(map(lambda example: example[attribute_index], examples))
    attributes.append(Attribute(attribute_index, attributes_unique_values))

examples_train, examples_test = train_test_split(examples, test_size=0.2)

# starting generating solution tree
tree = Tree(examples_train, attributes)

tree.train(None)

y_true = []
y_pred = []
pred_nodes = []
for test_example in examples_test:
    pred_node = tree.test(test_example, tree.root)

    y_true.append(test_example[-1])
    y_pred.append(pred_node.c)
    pred_nodes.append(pred_node)

TP, TN, FP, FN = confusion_matrix(y_true, y_pred).ravel()

accuracy = (TP + TN) / len(examples_test)
precision = TP / (TP + FP)
recall = TP / (TP + FN)

print(f'Accuracy: {accuracy}')
print(f'Precision: {precision}')
print(f'Recall: {recall}')

# Generating AUC-ROC
tested_data = [[y_pred[i], y_true[i]] for i in range(len(y_true))]
tested_data = sorted(tested_data, key=lambda x: x[0], reverse=True)
tested_data = sorted(tested_data, key=lambda x: x[1], reverse=True)

success = len([i[1] for i in tested_data if i[1] == 1])
failure = len(tested_data) - success

step_x = 1 / failure
step_y = 1 / success

x = [0]
y = [0]

for i in range(len(tested_data)):
    if tested_data[i][1] == 1:
        x.append(x[-1])
        y.append(y[-1] + step_y)
    else:
        x.append(x[-1] + step_x)
        y.append(y[-1])

plt.plot(x, y)
plt.title('AUC-ROC')
plt.show()

# generating AUC-PR
tested_data = [[y_pred[i], y_true[i]] for i in range(len(y_true))]
tested_data = sorted(tested_data, key=lambda x: x[0], reverse=True)
tested_data = sorted(tested_data, key=lambda x: x[1], reverse=True)

success = len([i[1] for i in tested_data if i[1] == 1])
failure = len(tested_data) - success

step_x = 1 / failure
step_y = 1 / success

x = [0]
y = [1]

for i in range(len(tested_data)):
    if tested_data[i][1] == 1:
        x.append(x[-1] + step_x)
        y.append(y[-1])
    else:
        x.append(x[-1])
        y.append(y[-1] - step_y)

plt.plot(x, y)
plt.title('AUC-PR')
plt.show()