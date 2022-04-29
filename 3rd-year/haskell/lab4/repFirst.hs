repFirst :: Eq a => a -> a -> [a] -> [a]
repFirst x y [] = []
repFirst x y (h:t)
    | x == h = y : t
    | otherwise = h : repFirst x y t
