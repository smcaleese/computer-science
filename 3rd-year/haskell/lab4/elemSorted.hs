elemSorted :: Ord a => a -> [a] -> Bool
elemSorted n [] = False
elemSorted n (h:t)
    | n == h = True
    | h < n = elemSorted n t
    | h > n = False
