from random import (random, randint)
from bisect import bisect
from itertools import permutations

graph = [
    [0, 1, 1, 5, 3],
    [1, 0, 3, 1, 5],
    [1, 3, 0, 11, 1],
    [5, 1, 11, 0, 1],
    [3, 5, 1, 1, 0]
]
cities_count = len(graph)
population_size = 4
mutation_probability = 0.5


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
    for i in range(int(population_size / 2)):
        non_chosen_ways = []
        for way in ways:
            is_non_taken = True
            for parent in parents:
                if way.sequence == parent.sequence:
                    is_non_taken = False
            if is_non_taken:
                non_chosen_ways.append(way)

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
    return parents


def create_next_population(parents):
    pairs = []
    for i in range(0, len(parents), 2):
        pairs.append([parents[i], parents[i + 1]])

    next_population = []

    for pair in pairs:
        parent_1_sequence = pair[0].sequence
        parent_2_sequence = pair[1].sequence

        print("Скрешиваем:", "".join(map(str, parent_1_sequence)), "".join(map(str, parent_2_sequence)))

        break_point_1 = randint(1, cities_count - 1)
        break_point_2 = randint(1, cities_count - 1)

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

        print("Получены потомки:", "".join(map(str, child_1)), "".join(map(str, child_2)))
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


population_count = 5
cities = range(cities_count)

population = []
for sequence in permutations(cities):
    if len(population) < population_size:
        population.append(Way(sequence))
    else:
        break

current_population_number = 1

while current_population_number <= population_size:
    print(f"Популяция №{current_population_number}:")
    for way in population:
        print("".join(map(str, way.sequence)), way.f())
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
    population.sort(key=lambda x: x.f())
    population = population[:population_count]

    current_population_number += 1

print()

optimal_way = population[0]
print("Оптимальный путь:", "".join(map(str, optimal_way.sequence)), "Расстояние:", optimal_way.f())
