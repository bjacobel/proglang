x = ["NFL", "NBA"]

class Array
    def summarize
        self.each do |x|
            print x, " "
        end
        puts
    end
end

x.summarize

class Array
    def addNumbers
        total = 0
        self.each do |x|
            total += x
        end
        total
    end
end

y = [10, 20, 30]
puts y.addNumbers