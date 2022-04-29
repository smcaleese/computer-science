reverseIt :: [Int] -> [Int]
reverseIt [] = []
reverseIt arr = last arr : reverseIt(init arr)
