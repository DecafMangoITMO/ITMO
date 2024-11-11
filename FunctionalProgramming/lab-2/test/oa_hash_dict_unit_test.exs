defmodule OAHashDictUnetTest do
  use ExUnit.Case
  doctest OAHashDict

  test "new/0 creates an empty dictionary" do
    dict = OAHashDict.new()
    assert dict.size == 0
    assert dict.capacity == 10
  end
  

  test "new/1 creates a dictionary with specified capacity" do
    dict = OAHashDict.new(20)
    assert dict.size == 0
    assert dict.capacity == 20
  end

  test "insert/3 adds a new key-value pair" do
    dict = OAHashDict.new() |> OAHashDict.insert(:key, "value")
    assert dict.size == 1
    assert Enum.any?(dict.table, fn pair -> pair == {:key, "value"} end)
  end

  test "insert/3 updates an existing key" do
    dict = OAHashDict.new()
           |> OAHashDict.insert(:key, "value1")
           |> OAHashDict.insert(:key, "value2")
    assert dict.size == 1
    assert Enum.count(dict.table, fn pair -> pair == {:key, "value2"} end) == 1
    assert Enum.count(dict.table, fn pair -> pair == {:key, "value1"} end) == 0
  end

  test "insert/3 raises error when dictionary is full" do
    dict = Enum.reduce(1..10, OAHashDict.new(10), fn i, acc -> OAHashDict.insert(acc, i, i) end)
    assert_raise RuntimeError, "Dictionary is full", fn -> OAHashDict.insert(dict, 11, 11) end
  end

  test "get/2 returns value for existing key" do
    dict = OAHashDict.new() |> OAHashDict.insert(:key, "value")
    assert OAHashDict.get(dict, :key) == "value"
  end

  test "get/2 returns nil for non-existing key" do
    dict = OAHashDict.new()
    assert OAHashDict.get(dict, :key) == nil
  end

  test "remove/2 removes existing key-value pair" do
    dict = OAHashDict.new()
           |> OAHashDict.insert(:key1, "value1")
           |> OAHashDict.insert(:key2, "value2")
           |> OAHashDict.remove(:key1)
    assert dict.size == 1
    assert Enum.count(dict.table, fn pair -> pair == {:key1, "value1"} end) == 0
    assert Enum.count(dict.table, fn pair -> pair == {:key2, "value2"} end) == 1
  end

  test "remove/2 does nothing for non-existing key" do
    dict = OAHashDict.new()
           |> OAHashDict.insert(:key1, "value1")
    dict_after_remove = OAHashDict.remove(dict, :non_existing_key)
    assert dict_after_remove.size == dict.size
    assert dict_after_remove.table == dict.table
  end

  test "filter/2 keeps only elements satisfying predicate" do
    dict = Enum.reduce(1..10, OAHashDict.new(), fn i, acc -> OAHashDict.insert(acc, i, i) end)
    filtered = OAHashDict.filter(dict, fn {k, _v} -> rem(k, 2) == 0 end)
    assert filtered.size == 5
    Enum.each(1..5, fn i -> assert OAHashDict.get(filtered, i*2) == i*2 end)
  end

  test "map/2 applies function to all elements" do
    dict = Enum.reduce(1..5, OAHashDict.new(), fn i, acc -> OAHashDict.insert(acc, i, i) end)
    mapped = OAHashDict.map(dict, fn {k, v} -> {k, v * 2} end)
    Enum.each(1..5, fn i -> assert OAHashDict.get(mapped, i) == i * 2 end)
  end

  test "foldl/3 folds from left to right" do
    dict = OAHashDict.new(2)
      |> OAHashDict.insert(1, "Hello")
      |>OAHashDict.insert(2, "World")
    result = OAHashDict.foldl(dict, "", fn acc, {_k, v} -> acc <> v end)
    assert result == "HelloWorld"
  end

  test "foldr/3 folds from right to left" do
    dict = OAHashDict.new(2)
      |> OAHashDict.insert(1, "Hello")
      |>OAHashDict.insert(2, "World")
    result = OAHashDict.foldr(dict, "", fn acc, {_k, v} -> acc <> v end)
    assert result == "WorldHello"
  end

  test "combine/2 merges two dictionaries" do
    dict1 = Enum.reduce(1..3, OAHashDict.new(3), fn i, acc -> OAHashDict.insert(acc, i, i) end)
    dict2 = Enum.reduce(1..3, OAHashDict.new(3), fn i, acc -> OAHashDict.insert(acc, i, i) end)
    combined = OAHashDict.combine(dict1, dict2)
    assert combined.size == 3
    assert OAHashDict.get(combined, 1) == 2
    assert OAHashDict.get(combined, 2) == 4
    assert OAHashDict.get(combined, 3) == 6
  end


end
