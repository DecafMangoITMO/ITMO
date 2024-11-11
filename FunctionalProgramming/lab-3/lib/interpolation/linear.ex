defmodule Interpolation.Linear do
  def calculate(known_points, points_to_calculate) do
    do_calculate(known_points, points_to_calculate, [])
  end

  def get_enough_points_count(), do: 2

  def points_enough?(all_known_points), do: length(all_known_points) >= get_enough_points_count()

  def get_name(), do: "Linear"

  defp do_calculate(_, [], result), do: Enum.reverse(result)

  defp do_calculate(known_points, [x | tail], result) do
    [{x0, y0}, {x1, y1}] = known_points

    a = (y1 - y0) / (x1 - x0)
    b = y0 - a * x0

    do_calculate(known_points, tail, [{x, a * x + b} | result])
  end
end
