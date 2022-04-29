delOdds :: [Int] -> [Int]
delOdds [] = []
delOdds (h:t)
    | h `mod` 2 /= 0 = delOdds t
    | otherwise = h : delOdds t

-- [2, 3, 4]