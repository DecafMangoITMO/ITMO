from random import (random, randint)
from bisect import bisect
from itertools import permutations

graph = [
    [0, 2, 5, 9, 6],
    [2, 0, 7, 9, 10],
    [5, 7, 0, 6, 10],
    [9, 9, 6, 0, 3],
    [6, 10, 10, 3, 0]
]
cities_count = len(graph)
population_size = 4
mutation_probability = 0.01


class Way:
    def __init__(self, sequence):
        self.sequence = sequence

    def f(self):
        distance = 0
        for i in range(1, cities_count):
            distance += graph[self.sequence[i - 1]][self.sequence[i]]
        distance += graph[self.sequence[-1]][self.sequence[0]]
        return distance


def choose_parents(ways):
    parents = []

    non_chosen_ways = [way for way in ways]
    for i in range(population_size):
        sum_of_distances = 0
        for way in non_chosen_ways:
            sum_of_distances += way.f()
        probabilities = []
        for way in non_chosen_ways:
            probabilities.append(way.f() / sum_of_distances)

        cumulative_probabilities = []
        cumulative_sum = 0
        for probability in probabilities:
            cumulative_sum += probability
            cumulative_probabilities.append(cumulative_sum)

        r = random()

        chosen_way_index = bisect(cumulative_probabilities, r)
        parents.append(non_chosen_ways[chosen_way_index])
        non_chosen_ways.remove(non_chosen_ways[chosen_way_index])
    return parents


def get_sequence_with_break_points(sequence, break_point_1, break_point_2):
    res = ""
    for i in range(cities_count):
        if i in (break_point_1, break_point_2):
            res += "|"
        res += str(sequence[i])
    return res


def create_next_population(parents):
    pairs = []
    for i in range(0, len(parents), 2):
        pairs.append([parents[i], parents[i + 1]])

    next_population = []

    for pair in pairs:
        parent_1_sequence = pair[0].sequence
        parent_2_sequence = pair[1].sequence

        break_point_1 = randint(1, cities_count - 1)
        break_point_2 = randint(1, cities_count - 1)

        print(f"Скрешиваем: {get_sequence_with_break_points(parent_1_sequence, break_point_1, break_point_2)} {get_sequence_with_break_points(parent_2_sequence, break_point_1, break_point_2)}")

        while break_point_2 == break_point_1:
            break_point_2 = randint(1, cities_count - 1)

        if break_point_2 < break_point_1:
            break_point_1, break_point_2 = break_point_2, break_point_1

        taken_cities = []

        child_1 = [0 for i in range(cities_count)]
        for i in range(break_point_1, break_point_2):
            child_1[i] = parent_2_sequence[i]
            taken_cities.append(parent_2_sequence[i])
        i = 0
        j = break_point_1
        while i < break_point_1:
            if parent_1_sequence[j] not in taken_cities:
                child_1[i] = parent_1_sequence[j]
                taken_cities.append(parent_1_sequence[j])
                i += 1
            if j == cities_count - 1:
                j = 0
            else:
                j += 1
        i = break_point_2
        while i < cities_count:
            if parent_1_sequence[j] not in taken_cities:
                child_1[i] = parent_1_sequence[j]
                taken_cities.append(parent_1_sequence[j])
                i += 1
            if j == cities_count - 1:
                j = 0
            else:
                j += 1

        taken_cities.clear()

        child_2 = [0 for i in range(cities_count)]
        for i in range(break_point_1, break_point_2):
            child_2[i] = parent_1_sequence[i]
            taken_cities.append(parent_1_sequence[i])
        i = 0
        j = break_point_1
        while i < break_point_1:
            if parent_2_sequence[j] not in taken_cities:
                child_2[i] = parent_2_sequence[j]
                taken_cities.append(parent_2_sequence[j])
                i += 1
            if j == cities_count - 1:
                j = 0
            else:
                j += 1
        i = break_point_2
        while i < cities_count:
            if parent_2_sequence[j] not in taken_cities:
                child_2[i] = parent_2_sequence[j]
                taken_cities.append(parent_2_sequence[j])
                i += 1
            if j == cities_count - 1:
                j = 0
            else:
                j += 1

        print(f"Получены потомки:{get_sequence_with_break_points(child_1, break_point_1, break_point_2)} {get_sequence_with_break_points(child_2, break_point_1, break_point_2)}")
        print("")

        next_population.append(Way(child_1))
        next_population.append(Way(child_2))

    return next_population


def mutation(way):
    random_number = random()
    if random_number <= mutation_probability:
        index_1 = randint(0, cities_count - 1)
        index_2 = index_1
        while index_2 == index_1:
            index_2 = randint(0, cities_count - 1)

        mutated_way_sequence = way.sequence

        mutated_way_sequence[index_1], mutated_way_sequence[index_2] = mutated_way_sequence[index_2], \
        mutated_way_sequence[index_1]

        print("Произошла мутация:", "".join(map(str, way.sequence)), "-->", "".join(map(str, mutated_way_sequence)))

        return Way(mutated_way_sequence)

    return None


population_count = 3
cities = range(cities_count)

population = []
for sequence in permutations(cities):
    if len(population) < population_size:
        population.append(Way(sequence))
    else:
        break

current_population_number = 1

while True:
    print(f"Популяция №{current_population_number}:")
    print("Путь | Значение целевой функции | Вероятность участия в процессе размножения")
    sum_of_distances = 0
    for way in population:
        sum_of_distances += way.f()
    for way in population:
        print(f"{"".join(map(str, way.sequence))} | {way.f()} | {way.f()}/{sum_of_distances}")

    if current_population_number == population_count:
        break

    print("")

    parents = choose_parents(population)
    print("В качестве родителей выбраны:")
    for parent in parents:
        print("".join(map(str, parent.sequence)))
    print("")

    next_population = create_next_population(parents)

    for i in range(len(next_population)):
        child = mutation(next_population[i])
        if child is not None:
            next_population[i] = child

    population += next_population
    print("Полученная расширенная популяция:")
    print("Путь | Значение целевой функции")
    for way in population:
        print(f"{"".join(map(str, way.sequence))} | {way.f()}")
    population.sort(key=lambda x: x.f())
    population = population[:population_size]

    current_population_number += 1
    print("")

print("")

optimal_way = population[0]
print("Оптимальный путь:", "".join(map(str, optimal_way.sequence)), "Расстояние:", optimal_way.f())