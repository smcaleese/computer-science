leap :: Int -> Bool
leap y
    | y `mod` 400 == 0 = True
    | y `mod` 4 == 0 && y `mod` 100 /= 0 = True
    | otherwise = False

mLengths :: Int -> [Int]
mLengths y
    | leap y = [31,29,31,30,31,30,31,31,30,31,30,31]
    | otherwise = [31,28,31,30,31,30,31,31,30,31,30,31]