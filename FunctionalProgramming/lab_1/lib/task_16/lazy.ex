defmodule Task16.Lazy do
  @moduledoc "Realization by using lasy components"

  def solution(pow) when is_integer(pow) and pow > 0 do
    Stream.iterate(trunc(:math.pow(2, pow)), &div(&1, 10))
    |> Stream.take(pow)
    |> Stream.map(&rem(&1, 10))
    |> Enum.reduce(0, &(&1 + &2))
  end
end
