defmodule Task16.Modular do
  @moduledoc "Realization by using modules (generate, filter, reduce + map)"

  defmodule SequenceGenerator do
    @moduledoc "Module-generator"

    @spec generate_sequence(integer()) :: list()
    def generate_sequence(num) when is_integer(num) and num > 0, do: generate_sequence(num, [])

    defp generate_sequence(0, acc), do: acc

    defp generate_sequence(num, acc) when is_integer(num) and num > 0,
      do: generate_sequence(div(num, 10), [rem(num, 10) | acc])
  end

  defmodule SequenceFilter do
    @moduledoc "Module-filter"

    @spec filter_sequence(list()) :: list()
    def filter_sequence(sequence), do: Enum.filter(sequence, &is_integer/1)
  end

  defmodule SequenceMapper do
    @moduledoc "Module-mapper"

    @spec map_sequence(list()) :: list()
    def map_sequence(sequence), do: Enum.map(sequence, & &1)
  end

  defmodule SequenceReducer do
    @moduledoc "Module-reducer"

    @spec reduce_sequence(list()) :: integer()
    def reduce_sequence(integer_sequence), do: Enum.reduce(integer_sequence, 0, &(&1 + &2))
  end

  @spec solution(integer()) :: integer()
  def solution(pow) when is_integer(pow) and pow > 0 do
    SequenceGenerator.generate_sequence(trunc(:math.pow(2, pow)))
    |> SequenceFilter.filter_sequence()
    |> SequenceMapper.map_sequence()
    |> SequenceReducer.reduce_sequence()
  end
end
