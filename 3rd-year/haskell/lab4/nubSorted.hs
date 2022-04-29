nubSorted :: Eq a => [a] -> [a]
nubSorted [] = []
nubSorted [a] = [a]
nubSorted (h:t)
    | h == head(t) = nubSorted t
    | otherwise = h : nubSorted t