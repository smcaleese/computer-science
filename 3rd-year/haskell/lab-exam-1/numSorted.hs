numSorted :: Ord a => a -> [a] -> Int
numSorted n [] = 0
numSorted n arr
    | n == (head arr) = 1 + numSorted n (tail arr)
    | n > (head arr) = 0 + numSorted n (tail arr)
    | n < (head arr) = 0

{-
2 [0,1,2,2,3,4,5,6,7]

0 + (1 + (1 + 0)))
-}