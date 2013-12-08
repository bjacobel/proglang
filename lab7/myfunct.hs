doubleMe x = x + x

addSquares x y = x*x + y*y

fibonacci n =
    if n == 0 then 1
    else if n == 1 then 1
    else if n > 1
        then fibonacci (n-1) + fibonacci (n-2)
    else 0

fib n
    |   n == 0 = 1
    |   n == 1 = 1
    |   n > 1 = fib (n-1) + fib (n-2)
    |   otherwise = 0

factorial n
    |   n == 0 = 1
    |   n > 0 = n * factorial (n-1)

sort [] = []

sort (head:remainingList) = sort [b | b <- remainingList, b < head]
    ++ [head] ++
    sort [b | b <- remainingList, b >= head]