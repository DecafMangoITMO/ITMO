def f(x1, x2):
    return 4 * x1 ** 2 + 3 * x2 ** 2 + 16 * x1 - 4 * x2


def gradient(x1, x2):
    return [8 * x1 + 16, 6 * x2 - 4]


def gradient_module(x1, x2):
    dx1, dx2 = gradient(x1, x2)
    return (dx1 ** 2 + dx2 ** 2) ** 0.5


x1 = 2
x2 = 2
step_value = 0.1
accuracy = 0.05


while True:
    f_current = f(x1, x2)
    if gradient_module(x1, x2) <= accuracy:
        break
    step = step_value
    while True:
        dx1, dx2 = gradient(x1, x2)
        x1_new = x1 - step * dx1
        x2_new = x2 - step * dx2
        f_new = f(x1_new, x2_new)
        if f_new < f_current:
            break
        step /= 2
    x1 = x1_new
    x2 = x2_new

print(x1, x2)