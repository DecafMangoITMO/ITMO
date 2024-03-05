import math
import matplotlib.pyplot as plt


class Interval:
    def __init__(self, start, end, x, m, p):
        self.start = start
        self.end = end
        self.x = x
        self.m = m
        self.p = p


def read_sample():
    file = open("sample.txt")
    lines = file.readlines()
    file.close()
    elements = []
    for line in lines:
        elements.append(float(line.replace('\n', '')))
    return elements


def calculate_intervals_count(elements):
    return math.ceil(math.sqrt(len(elements)))


def calculate_scope(elements):
    elements = sorted(elements)
    min = elements[0]
    max = elements[-1]

    return [-3.0, 2.5]


def calculate_interval_length(elements):
    min, max = calculate_scope(elements)
    intervals_count = calculate_intervals_count(elements)
    return (max - min) / intervals_count


def calculate_interval_series(elements):
    min, max = calculate_scope(elements)
    intervals_count = calculate_intervals_count(elements)
    interval_length = calculate_interval_length(elements)

    intervals = []

    current = min
    for i in range(intervals_count):
        start = current
        end = current + interval_length
        intervals.append(Interval(start, end, (start + end) / 2, 0, 0))
        current = end

    for element in elements:
        for interval in intervals:
            if element >= interval.start and element < interval.end:
                interval.m = interval.m + 1
                break

    for interval in intervals:
        interval.p = interval.m / len(elements)

    return intervals


def calculate_distribution_function(elements):
    intervals = calculate_interval_series(elements)
    x = [interval.x for interval in intervals]
    p = [interval.p for interval in intervals]

    values = []

    for i in range(len(x) + 1):
        if i == 0:
            values.append(0)
        elif i == len(x):
            values.append(1)
        else:
            values.append(values[-1] + p[i - 1])

    return values


def calculate_histogram(elements):
    intervals = calculate_interval_series(elements)
    p = [interval.p for interval in intervals]
    interval_length = calculate_interval_length(elements)

    values = []

    for i in range(len(p)):
        values.append(p[i] / interval_length)

    return values


def print_polygone(elements):
    intervals = calculate_interval_series(elements)
    x = [interval.x for interval in intervals]
    p = [interval.p for interval in intervals]

    plt.xlabel('Представитель интервала')
    plt.ylabel('Относительная частота')
    plt.title('Полигон частот')
    plt.plot(x, p, '-')  # Plot with points

    for xi, yi in zip(x, p):
        plt.scatter(xi, yi, color='purple')  # Point marker
        plt.annotate(f'({round(xi, 3)}, {round(yi, 3)})', (xi, yi), textcoords='offset points', xytext=(0, 5), ha='center')

    plt.show()


def print_histogram(elements):
    intervals = calculate_interval_series(elements)
    values = calculate_histogram(elements)

    fig, ax = plt.subplots()
    for i in range(len(values)):
        interval = intervals[i]
        value = values[i]
        ax.bar(interval.start, value, interval.end - interval.start, align='edge', color='blue')

    ax.set_title('Гистограмма')
    plt.show()


def print_distribution_function(elements):
    intervals = calculate_interval_series(elements)
    x = [interval.x for interval in intervals]
    values = calculate_distribution_function(elements)

    arrow_properties = {
        'head_width': 0.02,
        'head_length': 0.1
    }

    for i in range(len(x) + 1):
        if i == 0:
            plt.arrow(x[i] - 1, 0, 1 - 0.1, 0, **arrow_properties)
        elif i == len(x):
            plt.arrow(x[-1] + 0.05, 1, 1 - 0.05, 0, **arrow_properties)
            plt.plot(x[-1], 1, marker='o', color='blue', markerfacecolor='none', markersize='5')
        else:
            plt.arrow(x[i - 1] + 0.05, values[i], x[i] - x[i - 1] - 0.1 - 0.05, 0, **arrow_properties)
            plt.plot(x[i-1], values[i], marker='o', color='blue', markerfacecolor='none', markersize='5')

    plt.xlabel('X-axis')
    plt.ylabel('Y-axis')
    plt.title('Plot with Arrow and Pierced Circle')

    plt.show()


def calculate_expected_value_point_estimate(elements):
    sum = 0

    for element in elements:
        sum += element

    return sum / len(elements)


def calculate_dispersion_point_estimate(elements):
    expected_value_point_estimate = calculate_expected_value_point_estimate(elements)
    sum_of_deviation_squares = 0

    for element in elements:
        sum_of_deviation_squares += (element - expected_value_point_estimate) ** 2

    return sum_of_deviation_squares / len(elements)


def calculate_deviation_for_expected_value_interval(elements):
    t = 1.6604
    dispersion_point_estimate = calculate_dispersion_point_estimate(elements)
    return t * math.sqrt(dispersion_point_estimate) / math.sqrt(len(elements))


print("start end x m p")
for interval in calculate_interval_series(read_sample()):
    print(round(interval.start, 3),round(interval.end, 3), round(interval.x, 3), interval.m, interval.p)

print_polygone(read_sample())

print_distribution_function(read_sample())

print_histogram(read_sample())

print("Оценка математического ожидания:", round(calculate_expected_value_point_estimate(read_sample()), 3))
print("Оценка дисперсии:", round(calculate_dispersion_point_estimate(read_sample()), 3))
print("Доверительный интервал для математического ожидания:")
print(round(calculate_expected_value_point_estimate(read_sample()) - calculate_deviation_for_expected_value_interval(read_sample()), 3), "< m <", round(calculate_expected_value_point_estimate(read_sample()) + calculate_deviation_for_expected_value_interval(read_sample()), 3))
h1 = 128.42
h2 = 73.36
n = len(read_sample())
d = calculate_dispersion_point_estimate(read_sample())

print(round((n - 1) * d / h1, 3), "< D <", round((n - 1) * d / h2, 3))