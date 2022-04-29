-- 1 2 3
-- is 2 + 3 = 1?
-- is 1 + 3 = 2?
-- is 1 + 2 = 3?

isSum :: Int -> Int -> Int -> Bool
isSum a b c = a == b + c || b == a + c || c == a + b