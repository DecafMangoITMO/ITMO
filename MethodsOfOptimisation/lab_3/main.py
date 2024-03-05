def f(x):
    return 5 * x ** 2 - 8 * x ** (5 / 4) - 20 * x


def d(x):
    return 5 * 2 * x - 8 * (5 / 4) * x ** (1 / 4) - 20


def d2(x):
    return 5 * 2 - 8 * (5 / 4) * (1 / 4) * x ** (- 3 / 4)


a, b = 3, 3.5
accuracy = 0.02


# Метод половинного деления
def method_of_dividing_a_segment_in_half(a, b, accuracy):
    left = a
    right = b

    while True:
        x1 = (left + right - accuracy) / 2
        x2 = (left + right + accuracy) / 2
        y1 = f(x1)
        y2 = f(x2)

        if y1 > y2:
            left = x1
        else:
            right = x2

        if right - left <= 2 * accuracy:
            return (left + right) / 2


# Метод золотого сечения
def method_of_golden_ratio(a, b, accuracy):
    left = a
    right = b

    x1 = right - (right - left) / 1.618
    x2 = left + (right - left) / 1.618
    y1 = f(x1)
    y2 = f(x2)

    while True:
        if y1 >= y2:
            left = x1
            x1 = x2
            y1 = y2
            x2 = left + (right - left) / 1.618
            y2 = f(x2)
        else:
            right = x2
            x2 = x1
            y2 = y1
            x1 = right - (right - left) / 1.618
            y1 = f(x1)

        if abs(right - left) < accuracy:
            return (left + right) / 2

# Метод хорд
def method_of_chords(a, b, accuracy):
    left = a
    right = b

    while True:
        x = left - d(left) / (d(left) - d(right)) * (left - right)
        dx = d(x)

        if abs(dx) <= accuracy:
            return x

        if dx > 0:
            right = x
        else:
            left = x

# Метод Ньютона
def method_of_newtons(a, b, accuracy, x0):
    x = x0

    while True:
        if abs(d(x)) <= accuracy:
            return x
        x = x - d(x) / d2(x)


print(method_of_dividing_a_segment_in_half(a, b, accuracy))
print(method_of_golden_ratio(a, b, accuracy))
print(method_of_chords(a, b, accuracy))
print(method_of_newtons(a, b, accuracy, 3.2))
