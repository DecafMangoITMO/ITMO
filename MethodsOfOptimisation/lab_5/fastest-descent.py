def f(x1, x2):
    return 4 * x1 ** 2 + 3 * x2 ** 2 + 16 * x1 - 4 * x2


def gradient(x1, x2):
    return [8 * x1 + 16, 6 * x2 - 4]


def gradient_module(x1, x2):
    dx1, dx2 = gradient(x1, x2)
    return (dx1 ** 2 + dx2 ** 2) ** 0.5


# f = 4(x1 - hdx1)^2 + 3(x2 - hdx2)^2 + 16(x1 - hdx1) - 4(x2 - hdx2)
# f' = -8dx1(x1 - hdx1) - 6dx2(x2 - hdx2) - 16dx1 + 4dx2 = -8x1dx1 + 8h(dx1)^2 - 6x2dx2 + 6h(dx2)^2 0 - 16dx1 + 4dx2
# f' = 0 => h = (8x1dx1 + 6x2dx2 + 16dx1 - 4dx2) / (8(dx1)^2 + 6(dx2)^2)
def calculate_step(x1, x2):
    dx1, dx2 = gradient(x1, x2)
    return (8 * x1 * dx1 + 6 * x2 * dx2 + 16 * dx1 - 4 * dx2) / (8 * dx1 ** 2 + 6 * dx2 ** 2)


x1 = 0
x2 = 0
accuracy = 0.05


while True:
    if gradient_module(x1, x2) <= accuracy:
        break
    step = calculate_step(x1, x2)

    dx1, dx2 = gradient(x1, x2)
    x1 = x1 - step * dx1
    x2 = x2 - step * dx2


print(x1, x2)




