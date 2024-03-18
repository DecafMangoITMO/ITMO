def y(x):
    return 5 * x ** 2 - 8 * x ** (5 / 4) - 20 * x


def calculate_polynom_x_min(x1, x2, x3):
    y1 = y(x1)
    y2 = y(x2)
    y3 = y(x3)

    a0 = y1
    a1 = (y2 - y1) / (x2 - x1)
    a2 = ((y3 - y1) / (x3 - x1) - (y2 - y1) / (x2 - x1)) / (x3 - x2)

    return (x1 + x2) / 2 - a1 / (2 * a2)


epsilon = 0.0001
dx = 0.1 # шаг

# Определяем начальные три точки
x1 = 1 # левая граница из условия
x3 = 1.5 # правая граница из условия
y1 = y(x1)
y3 = y(x3)
x2 = x1 + dx
y2 = y(x2)
while x2 < x3:
    if y2 < y1 and y2 < y3:
        break
    x2 += dx
    y2 = y(x2)


is_first_approach = True
prev_x_min = 0
while True:
    x_min = calculate_polynom_x_min(x1, x2, x3)

    if is_first_approach:
        prev_x_min = x_min
        is_first_approach = False
    else:
        if abs(x_min - prev_x_min) < epsilon:
            print(x_min)
            break
        prev_x_min = x_min

    y_min = y(x_min)
    if y_min < y2 and x_min > x2:
        x1 = x2
        y1 = y1
        x2 = x_min
        y2 = y_min
    elif y_min < y2 and x_min < x2:
        x3 = x2
        y3 = y2
        x2 = x_min
        y2 = y_min
    elif y_min > y2 and x_min > x2:
        x3 = x_min
        y3 = y_min
    elif y_min > y2 and x_min < x2:
        x1 = x_min
        y1 = y_min
    elif x_min == x2:
        x2 = (x1 + x2) / 2
        y2 = y(x2)