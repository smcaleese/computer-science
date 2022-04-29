dupSorted :: Eq a => [a] -> Bool
dupSorted [] = False
dupSorted [a] = False
dupSorted (h:t)
    | h == head(t) = True
    | otherwise = dupSorted t