num :: Eq a => a -> [a] -> Int
num n [] = 0
num n arr
    | (head arr) == n = 1 + num n (tail arr)
    | otherwise = 0 + num n (tail arr)

{-
count the number of occurances
> num 2 [1,2,3,2,1]
2

increment count if head == n, otherwise continue
-}