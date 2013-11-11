while true
    print "Input n > 0: "
    n = gets.chomp.to_i
    break if n > 0 && n.is_a?(Integer)
    puts "Wrong input, try again."
end

puts "your input #{n} belongs to #{n.class} class"

outFile = File.new("Collatz_#{n}.txt", "w")

if outFile
    outFile.write("#{n} -> ")
else raise "File could not be opened"
end

cycle_length = 0

while n != 1
    if n % 2 == 0
        n = n/2
    else
        n = 3 * n + 1
    end
    outFile.write("#{n}")
    outFile.write("-> ") if n > 1
    cycle_length += 1
end

puts "Cycle length = #{cycle_length}"

outFile.close