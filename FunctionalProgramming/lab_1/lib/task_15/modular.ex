defmodule Task15.Modular do
  @moduledoc "Realization by using modules (generate, filter, reduce + map)"

  defmodule SequenceGenerator do
    @moduledoc "Module-generator"

    @spec generate_sequence(pos_integer()) :: Range.t()
    def generate_sequence(n) when is_integer(n) and n > 1, do: 1..n
  end

  defmodule SequenceFilter do
    @moduledoc "Module-filter"

    @spec filter_sequence(any()) :: list()
    def filter_sequence(sequence), do: Enum.filter(sequence, &is_integer/1)
  end

  defmodule SequenceMapper do
    @moduledoc "Module-mapper"

    @spec map_sequence(any()) :: list()
    def map_sequence(sequence), do: Enum.map(sequence, & &1)
  end

  defmodule SequnceReducer do
    @moduledoc "Module-reducer"

    @spec factorial(any()) :: any()
    def factorial(integer_sequence), do: Enum.reduce(integer_sequence, 1, &(&1 * &2))
  end

  @spec solution(pos_integer()) :: integer()
  def solution(grid_size) when is_integer(grid_size) and grid_size > 0 do
    factorial =
      &(SequenceGenerator.generate_sequence(&1)
        |> SequenceMapper.map_sequence()
        |> SequenceFilter.filter_sequence()
        |> SequnceReducer.factorial())

    trunc(factorial.(2 * grid_size) / (factorial.(grid_size) * factorial.(grid_size)))
  end
end
