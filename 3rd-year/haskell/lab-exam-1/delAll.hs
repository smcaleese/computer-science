delAll :: Eq a => a -> [a] -> [a]
delAll n [] = []
delAll n arr
    | head(arr) /= n = head(arr) : delAll n (tail arr)
    | otherwise = delAll n (tail arr)

-- [1,2,2,1]
-- [1,1]

-- 1:1:[]