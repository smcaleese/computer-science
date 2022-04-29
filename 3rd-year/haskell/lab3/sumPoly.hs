sumPoly :: [Int] -> [Int] -> [Int]
sumPoly [] [] = []
sumPoly (x:xs) [] = x + 0 : sumPoly xs []
sumPoly [] (y:ys) = 0 + y : sumPoly [] ys
sumPoly (x:xs) (y:ys) = x + y : sumPoly xs ys
