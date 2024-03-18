def y(x):
    return 5 * x ** 2 - 8 * x ** (5 / 4) - 20 * x


def calculate(x1, dx, epsilon):
    while True:
        x2 = x1 + dx

        y1 = y(x1)
        y2 = y(x2)

        if y1 > y2:
            x3 = x1 + 2 * dx
        else:
            x3 = x1 - dx

        y3 = y(x3)

        while True:
            x_min = x1
            y_min = y1

            if y2 < y_min:
                x_min = x2
                y_min = y2
            if y3 < y_min:
                x_min = x3
                y_min = y3

            if (x2 - x3) * y1 + (x3 - x1) * y2 + (x1 - x2) * y3 == 0:
                x1 = x_min
                break

            polynom_x_min = 0.5 * ((x2 ** 2 - x3 ** 2) * y1 + (x3 ** 2 - x1 ** 2) * y2 + (x1 ** 2 - x2 ** 2) * y3) / (
                    (x2 - x3) * y1 + (x3 - x1) * y2 + (x1 - x2) * y3)
            polynom_y_min = y(polynom_x_min)

            if abs((y_min - polynom_y_min) / polynom_y_min) < epsilon and abs(
                    (x_min - polynom_x_min) / polynom_x_min) < epsilon:
                return polynom_x_min
            elif x1 <= polynom_x_min <= x3:
                if polynom_y_min < y2:
                    if polynom_x_min < x2:
                        x3 = x2
                        x2 = polynom_x_min
                        y3 = y2
                        y2 = polynom_y_min
                    else:
                        x1 = x2
                        x2 = polynom_x_min
                        y1 = y2
                        y2 = polynom_y_min
                else:
                    if polynom_x_min < x2:
                        x1 = polynom_x_min
                        y1 = polynom_y_min
                    else:
                        x3 = polynom_x_min
                        y3 = polynom_y_min
            else:
                x1 = polynom_x_min
                break


print(calculate(3, 0.1, 0.0001))
