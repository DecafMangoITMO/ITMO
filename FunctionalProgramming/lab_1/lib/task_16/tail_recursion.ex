defmodule Task16.TailRecursion do
  @moduledoc "Realization by using tail recursion"

  defp calculate(0, acc), do: acc

  defp calculate(num, acc) when is_integer(num) and num > 0,
    do: calculate(div(num, 10), acc + rem(num, 10))

  @spec solution(integer()) :: integer()
  def solution(pow) when is_integer(pow) and pow > 0, do: calculate(trunc(:math.pow(2, pow)), 0)
end
