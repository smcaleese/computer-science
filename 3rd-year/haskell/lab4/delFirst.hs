delFirst :: Eq a => a -> [a] -> [a]
delFirst n [] = []
delFirst n (h:t)
    | h == n = t
    | otherwise = h : delFirst n t