fibonacci :: Int -> Int
fibonacci n
    | n == 1 = 0
    | n < 4 = 1
    | otherwise = fibonacci(n - 1) + fibonacci(n - 2)

