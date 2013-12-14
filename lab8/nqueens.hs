queens n = solve n
    where
    solve k
        |   k <= 0 = [ [] ]
        |   otherwise = [ h:partial | partial <- solve (k-1), h <- [0..(n-1)], safe h partial]
    safe h partial = and [ not (checks h partial i) | i <- [0..(length partial - 1)]]
    checks h partial i = h == partial!!i || abs(h - partial!!i) == i+1