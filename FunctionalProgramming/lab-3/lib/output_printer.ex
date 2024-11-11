defmodule OutputPrinter do
  def start(config) do
    spawn(fn -> loop(config) end)
  end

  defp loop(config) do
    receive do
      {method, result} -> print(method, result, config.step)
    end

    loop(config)
  end

  defp print(method, result, step) do
    IO.puts("\nResult:")

    {start_x, _} = List.first(result)

    IO.puts("#{method.get_name()} (go from #{start_x} with step #{step})")
    Enum.each(result, fn {x, _} -> IO.write("#{Float.round(x, 2)}  ") end)
    IO.write("\n")
    Enum.each(result, fn {_, y} -> IO.write("#{Float.round(y, 2)}  ") end)
    IO.write("\n\n")
  end
end
