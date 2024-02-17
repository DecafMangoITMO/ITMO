import math

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
    min = -round(abs(min), 2)
    max = round(max, 2)
    return [min, max]


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


#todo реализовать эту функцию
def print_polygone(elements):
    print("")


def calculate_distribution_function(elements):
    intervals = calculate_interval_series(elements)
    x = [interval.x for interval in intervals]
    p = [interval.p for interval in intervals]

    values = []

    for i in range (len(x) + 1):
        if i == 0:
            values.append(0)
        elif i == len(x):
            values.append(1)
        else:
            values.append(values[-1] + p[i - 1])

    return values


def calculate_gist(elements):
    intervals = calculate_interval_series(elements)
    p = [interval.p for interval in intervals]
    interval_length = calculate_interval_length(elements)

    values = []

    for i in range(len(p)):
        values.append(p[i] / interval_length)

    return values


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


