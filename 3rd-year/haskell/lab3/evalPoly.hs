evalPoly :: Int -> [Int] -> Int
evalPoly x a = sum [fst(t) * x ^ snd(t) | t <- zip a [0..]]

-- evalPoly 3 [1,7,5,2]:
-- 1*x^0 + 7*x^1 + 5*x^2 + 2*x^3
-- [(1,0),(7,1),(5,2),(2,3)]
