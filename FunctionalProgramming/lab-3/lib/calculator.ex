defmodule Calculator do
  def start(output_printer_pid, config) do
    spawn(fn -> loop(output_printer_pid, [], config) end)
  end

  defp loop(output_printer_pid, known_points, config) do
    known_points =
      receive do
        {:point, point} -> Enum.reverse([point | Enum.reverse(known_points)])
      end

    handle_add_point_request(output_printer_pid, known_points, config)

    loop(output_printer_pid, known_points, config)
  end

  defp handle_add_point_request(output_printer_pid, known_points, config) do
    method1 = config.method1

    if method1.points_enough?(known_points) do
      enough_points1 =
        Enum.slice(
          known_points,
          (length(known_points) - method1.get_enough_points_count())..(length(known_points) - 1)
        )

      generated_points1 = PointGenerator.generate(enough_points1, config.step)
      send(output_printer_pid, {method1, method1.calculate(enough_points1, generated_points1)})
    end

    method2 = config.method2

    if method2.points_enough?(known_points) do
      enough_points2 =
        Enum.slice(
          known_points,
          (length(known_points) - method2.get_enough_points_count())..(length(known_points) - 1)
        )

      generated_points2 = PointGenerator.generate(enough_points2, config.step)
      send(output_printer_pid, {method2, method2.calculate(enough_points2, generated_points2)})
    end
  end
end

defmodule PointGenerator do
  def generate(known_points, step) do
    {x_start, _} = List.first(known_points)
    {x_finish, _} = List.last(known_points)

    do_generate(x_start, x_finish, step, [])
  end

  defp do_generate(current_point, finish_point, step, result) do
    result = [current_point | result]

    if current_point > finish_point do
      Enum.reverse(result)
    else
      do_generate(current_point + step, finish_point, step, result)
    end
  end
end
