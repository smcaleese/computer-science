num :: Eq a => a -> [a] -> Int
num e [] = 0
num e (h:t)
    | h == e = 1 + num e t
    | otherwise = 0 + num e t

-- 1 [1, 2, 1, 1]