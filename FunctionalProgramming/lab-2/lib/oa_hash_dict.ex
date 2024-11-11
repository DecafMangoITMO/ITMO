defmodule OAHashDict do
  @default_capacity 10

  defstruct table: [], size: 0, capacity: @default_capacity

  def new(capacity \\ @default_capacity) do
    %OAHashDict{table: List.duplicate({nil, nil}, capacity), capacity: capacity}
  end

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

  def empty(), do: new(0)

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

  defp has_key?(%OAHashDict{size: size, capacity: capacity} = dict, key) do
    if size == 0 || capacity == 0 do
      nil
    else
      index = rem(hash(key), capacity)
      search_key(dict, key, index, 0)
    end
  end

  defp search_key(%OAHashDict{table: table, capacity: capacity} = dict, key, index, probe_count) do
    if probe_count < capacity do
      case Enum.at(table, index) do
        {^key, _} -> index
        _ -> search_key(dict, key, rem(index + 1, capacity), probe_count + 1)
      end
    else
      nil
    end
  end

  defp hash(key) do
    :erlang.phash2(key)
  end

  defp linear_probe(%OAHashDict{table: table, capacity: capacity} = dict, key, index) do
    case Enum.at(table, index) do
      {nil, _} -> index
      {^key, _} -> index
      _ -> linear_probe(dict, key, rem(index + 1, capacity))
    end
  end
end
