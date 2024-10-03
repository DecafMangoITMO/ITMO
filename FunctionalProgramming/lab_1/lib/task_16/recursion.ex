defmodule Task16.Recursion do
  @moduledoc "Realization by using recursion"

  defp calculate(0), do: 0

  defp calculate(num) when is_integer(num) and num > 0, do: rem(num, 10) + calculate(div(num, 10))

  @spec solution(non_neg_integer()) :: non_neg_integer()
  def solution(pow) when is_integer(pow) and pow > 0, do: calculate(trunc(:math.pow(2, pow)))
end
