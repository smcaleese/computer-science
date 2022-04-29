dup (x:[]) = False
dup (x:y:ys)
    | x == y = True
    | dup (x:ys) == True = True
    | otherwise = dup (y:ys)
