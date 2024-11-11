defmodule Interpolation.Lagrange3 do
  def calculate(known_points, points_to_calculate) do
    do_calculate(known_points, points_to_calculate, [])
  end

  def get_enough_points_count(), do: 4

  def points_enough?(all_known_points), do: length(all_known_points) >= get_enough_points_count()

  def get_name(), do: "Lagrange3"

  defp do_calculate(_, [], result), do: Enum.reverse(result)

  defp do_calculate(known_points, [x | tail], result) do
    [{x0, y0}, {x1, y1}, {x2, y2}, {x3, y3}] = known_points

    y =
      y0 * ((x - x1) * (x - x2) * (x - x3)) / ((x0 - x1) * (x0 - x2) * (x0 - x3)) +
        y1 * ((x - x0) * (x - x2) * (x - x3)) / ((x1 - x0) * (x1 - x2) * (x1 - x3)) +
        y2 * ((x - x0) * (x - x1) * (x - x3)) / ((x2 - x0) * (x2 - x1) * (x2 - x3)) +
        y3 * ((x - x0) * (x - x1) * (x - x2)) / ((x3 - x0) * (x3 - x1) * (x3 - x2))

    do_calculate(known_points, tail, [{x, y} | result])
  end
end
