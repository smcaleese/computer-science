longest :: [[Int]] -> [Int]
longest arr
    | arr == [[]] || arr == [] = []
    | tail arr == [] = head arr
    | length(head arr) > length(longest (tail arr)) = head arr
    | otherwise = longest(tail arr)
