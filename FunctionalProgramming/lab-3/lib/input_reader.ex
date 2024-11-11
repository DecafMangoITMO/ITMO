defmodule InputReader do
  def start(calculator_pid) do
    loop(calculator_pid, nil)
  end

  defp loop(calculator_pid, prev_x) do
    input = String.trim(IO.gets(""))

    if input == "exit" do
      IO.puts("Shutdown system")
      System.halt()
    end

    {validation_status, validation_result} = validate_input(input)

    if validation_status == :error do
      IO.puts(validation_result)
      loop(calculator_pid, prev_x)
    else
      {x, y} = validation_result

      if prev_x == nil do
        send(calculator_pid, {:point, {x, y}})
        loop(calculator_pid, x)
      else
        if prev_x >= x do
          IO.puts("Coordinate x value must stand after previous")
          loop(calculator_pid, prev_x)
        else
          send(calculator_pid, {:point, {x, y}})
          loop(calculator_pid, x)
        end
      end
    end
  end

  defp validate_input(input) do
    input = String.split(String.replace(input, ~r"\s{2,}", " "), " ")

    if length(input) != 2 do
      {:error, "Point must have two coordinates"}
    else
      x_result = Float.parse(Enum.at(input, 0))
      y_result = Float.parse(Enum.at(input, 1))

      if x_result == :error or y_result == :error do
        {:error, "Coordinates must be numbers"}
      else
        {x_value, _} = x_result
        {y_value, _} = y_result
        {:ok, {x_value, y_value}}
      end
    end
  end
end
