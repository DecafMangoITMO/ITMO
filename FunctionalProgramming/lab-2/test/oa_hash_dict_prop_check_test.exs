defmodule OAHashDictQuixirTest do
  use ExUnit.Case
  use Quixir

  doctest OAHashDict

  test "check monoid" do
    ptest numbers: list(of: positive_int(), size: 10) do
      dict = OAHashDict.new(length(numbers))
      dict = fill_dict(dict, numbers)

      emp = OAHashDict.empty()

      new_dict = OAHashDict.combine(dict, emp)

      assert dict.size == new_dict.size
      assert dict.capacity == new_dict.capacity
      assert equal_dicts(dict, new_dict) == true

      new_dict = OAHashDict.combine(emp, dict)
      assert dict.size == new_dict.size
      assert dict.capacity == new_dict.capacity
      assert equal_dicts(dict, new_dict) == true
    end
  end

  test "check insert" do
    ptest values: list(size: 10) do
      dict = OAHashDict.new(length(values))

      Enum.each(values, fn value ->
        dict = OAHashDict.insert(dict, value, value)

        assert OAHashDict.get(dict, value) == value
      end)
    end
  end

  test "check associativity" do
    ptest values1: list(10), values2: list(10) do
      dict1 = OAHashDict.new(length(values1))
      dict2 = OAHashDict.new(length(values2))

      dict1 = fill_dict(dict1, values1)
      dict2 = fill_dict(dict2, values2)

      new_dict1 = OAHashDict.combine(dict1, dict2)
      new_dict2 = OAHashDict.combine(dict2, dict1)

      assert equal_dicts(new_dict1, new_dict2)
    end
  end

  defp fill_dict(dict, []), do: dict

  defp fill_dict(dict, [head | tail]) do
    fill_dict(OAHashDict.insert(dict, head, head), tail)
  end

  defp equal_dicts(%OAHashDict{table: table1}, %OAHashDict{} = dict2) do
    Enum.reduce(table1, true, fn {key, value}, acc ->
      case OAHashDict.get(dict2, key) do
        ^value -> acc
        _ -> false
      end
    end)
  end

end
