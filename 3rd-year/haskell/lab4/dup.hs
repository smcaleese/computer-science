dup :: Eq a => [a] -> Bool
dup [] = False
dup (h:t)
    | elem h t = True
    | otherwise = dup t
