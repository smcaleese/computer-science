-- find the shortest list in the list
-- eg. [[1,2,3], [1,2], [1]] -> [1]
shortest :: [[Int]] -> [Int]
shortest arr
    | arr == [[]] || arr == [] = []
    | tail arr == [] = head arr
    | length(head arr) < length(shortest(tail arr)) = head arr
    | otherwise = shortest(tail arr)
