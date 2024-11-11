Code.require_file("lib/config_resolver.ex")
Code.require_file("lib/input_reader.ex")
Code.require_file("lib/calculator.ex")
Code.require_file("lib/interpolation/linear.ex")
Code.require_file("lib/interpolation/lagrange2.ex")
Code.require_file("lib/interpolation/lagrange3.ex")
Code.require_file("lib/output_printer.ex")

{config_status, config_result} = ConfigResolver.resolve(System.argv())

if config_status == :error do
  IO.puts(config_result)
  System.halt()
end

output_printer_pid = OutputPrinter.start(config_result)
calculator_pid = Calculator.start(output_printer_pid, config_result)
InputReader.start(calculator_pid)
