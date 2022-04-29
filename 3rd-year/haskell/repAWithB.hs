repAWithB :: [Char] -> [Char]
repAWithB [] = []
repAWithB (h:t)
    | h == 'a' = 'b' : repAWithB t
    | otherwise = h : repAWithB t

{-
repAWithB [a,b,a,a]
> [b,b,b,b]
-}