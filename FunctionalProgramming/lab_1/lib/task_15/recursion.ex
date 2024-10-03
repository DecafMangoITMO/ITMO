defmodule Task15.Recursion do
  @moduledoc "Realization by using recursion"

  defp factorial(1), do: 1

  defp factorial(n) when is_integer(n) and n > 1, do: n * factorial(n - 1)

  @spec solution(pos_integer()) :: integer()
  def solution(grid_size) when is_integer(grid_size) and grid_size > 0 do
    trunc(factorial(2 * grid_size) / (factorial(grid_size) * factorial(grid_size)))
  end
end
