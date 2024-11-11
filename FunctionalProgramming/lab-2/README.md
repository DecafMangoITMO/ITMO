# Лабораторная работа №2 (OpenAddress HashMap Dict)

---

* Студент: `Разинкин Александр Владимирович`
* Группа: `P3307`
* ИСУ: `368712`
* Функциональный язык: `Elixir`

--- 

## Требования

1. Функции:
    - добавление и удаление элементов;
    - фильтрация;
    - отображение (map);
    - свертки (левая и правая);
    - структура должна быть [моноидом](https://ru.m.wikipedia.org/wiki/Моноид).
2. Структуры данных должны быть неизменяемыми.
3. Библиотека должна быть протестирована в рамках unit testing.
4. Библиотека должна быть протестирована в рамках property-based тестирования (как минимум 3 свойства, включая свойства моноида).
5. Структура должна быть полиморфной.
6. Требуется использовать идиоматичный для технологии стиль программирования. Примечание: некоторые языки позволяют получить большую часть API через реализацию небольшого интерфейса. Так как лабораторная работа про ФП, а не про экосистему языка -- необходимо реализовать их вручную и по возможности -- обеспечить совместимость.

--- 

## Ключевые элементы реализации

Добавление, получение и удаление элементов:

```elixir
def insert(%OAHashDict{table: table, size: size, capacity: capacity} = dict, key, value) do
    if is_nil(key) do
      dict
    else
      if size == capacity && !has_key?(dict, key) do
        raise "Dictionary is full"
      end

      index = rem(hash(key), capacity)
      index = linear_probe(dict, key, index)

      case Enum.at(table, index) do
        {^key, _} -> %{dict | table: List.replace_at(table, index, {key, value})}
        _ -> %{dict | table: List.replace_at(table, index, {key, value}), size: size + 1}
      end
    end
  end

  def get(%OAHashDict{table: table} = dict, key) do
    if is_nil(key) do
      nil
    else
      index = has_key?(dict, key)

      if index do
        {_, value} = Enum.at(table, index)
        value
      else
        nil
      end
    end
  end

  def remove(%OAHashDict{table: table, size: size} = dict, key) do
    if is_nil(key) do
      dict
    else
      index = has_key?(dict, key)

      if index do
        table = List.replace_at(table, index, {nil, nil})
        %{dict | table: table, size: size - 1}
      else
        dict
      end
    end
   end
```

Фильтрация:

```elixir
def filter(%OAHashDict{table: table} = dict, predicate) do
    {table, counter} = do_filter(table, [], predicate, 0)
    table = Enum.reverse(table)
    %{dict | table: table, size: counter}
  end

  defp do_filter([], matchingPairs, _, counter), do: {matchingPairs, counter}

  defp do_filter([{headKey, headValue} | tail], matchingPairs, predicate, counter) do
    if !is_nil(headKey) && predicate.({headKey, headValue}) do
      do_filter(tail, [{headKey, headValue} | matchingPairs], predicate, counter + 1)
    else
      do_filter(tail, [{nil, nil} | matchingPairs], predicate, counter)
    end
  end
```

Отображение (map):

```elixir
def map(%OAHashDict{table: table} = dict, mapper) do
    table = do_map(table, [], mapper)
    table = Enum.reverse(table)
    %{dict | table: table}
  end

  defp do_map([], changedPairs, _), do: changedPairs

  defp do_map([{headKey, headValue} | tail], changedPairs, mapper) do
    if !is_nil(headKey) do
      headPair = mapper.({headKey, headValue})
      do_map(tail, [headPair | changedPairs], mapper)
    else
      do_map(tail, [{nil, nil} | changedPairs], mapper)
    end
  end
```

Свертки (левая и правая):

```elixir
def foldl(%OAHashDict{table: table}, initAcc, reducer) do
    do_foldl(table, initAcc, reducer)
  end

  defp do_foldl([], acc, _), do: acc

  defp do_foldl([{headKey, headValue} | tail], acc, reducer) do
    if !is_nil(headKey) do
      acc = reducer.(acc, {headKey, headValue})
      do_foldl(tail, acc, reducer)
    else
      do_foldl(tail, acc, reducer)
    end
  end

  def foldr(%OAHashDict{table: table}, initAcc, reducer) do
    table = Enum.reverse(table)
    do_foldl(table, initAcc, reducer)
  end
```

---

## Соответствие свойству моноида

Определили пустой элемент (размер и вместимость равны нулю):

```elixir
def empty(), do: new(0)
```

Определили бинарную операцию combine:

```elixir
  def combine(
        %OAHashDict{table: table1, size: size1, capacity: capacity1},
        %OAHashDict{table: table2, size: size2, capacity: capacity2} = dict2
      ) do

    crossing_keys_count = Enum.reduce(table1, 0, fn  {key, _}, acc ->
      if has_key?(dict2, key) do
        acc + 1
      else
        acc
      end
    end)

    new_capacity = max(max(capacity1, capacity2), size1  + size2 - crossing_keys_count)
    new_dict = new(new_capacity)

    new_dict = do_combine_with_crossing_keys(table1, dict2, new_dict)
    do_combine_without_crossing_keys(table2, new_dict, new_dict)
  end

  defp do_combine_with_crossing_keys([], _, new_dict), do: new_dict

  defp do_combine_with_crossing_keys([{headKey, headValue} | tail], another_dict, new_dict) do
    case get(another_dict, headKey) do
      nil -> do_combine_with_crossing_keys(tail, another_dict, insert(new_dict, headKey, headValue))
      another_value -> do_combine_with_crossing_keys(tail, another_dict, insert(new_dict, headKey, headValue + another_value))
    end
  end

  defp do_combine_without_crossing_keys([], _, new_dict), do: new_dict

  defp do_combine_without_crossing_keys([{headKey, headValue} | tail], another_dict, new_dict) do
    case get(another_dict, headKey) do
      nil -> do_combine_without_crossing_keys(tail, another_dict, insert(new_dict, headKey, headValue))
      another_value -> do_combine_without_crossing_keys(tail, another_dict, insert(new_dict, headKey, another_value))
    end
  end
```

Мысль данной операции следующая: при пересечении ключей из обеих структур в итоговую структуру кладется сумма значений,
которые лежат под данными ключами в данных структурах, остальные значения просто кладутся в итоговую структуру.

---

## Тестирование

В рамках данной работы были применены два инструмента:

- ExUnit - для модульного тестирования
- Quixir - для тестирования свойств (property-based)

---

## Выводы

При реализации данной структуры данных было достаточно просто применять те приемы, которые надо было применить
в предыдущей работе (лабораторная работа №1). В общем было приятно и вполне очевидно писать исходный код.
