-- checks if a list is empty or not
check :: [a] -> String
check [] = "Empty"
check (x:xs) = "Contains Elements"

-- same function using guards instead of pattern matching
check_ :: [a] -> String
check_ lst
    | length lst < 1 = "Empty"
    | otherwise = "Contains elements"