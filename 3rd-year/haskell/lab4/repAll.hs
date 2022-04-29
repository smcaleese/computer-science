repAll :: Eq a => a -> a -> [a] -> [a]
repAll x y [] = []
repAll x y (h:t)
    | h == x = y : repAll x y t
    | otherwise = h : repAll x y t
