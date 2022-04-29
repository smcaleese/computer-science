import Data.Char(toUpper)

myProduct :: [Int] -> Int
myProduct [] = 1
myProduct (h:t) = h * myProduct t

factorial2 :: Integer -> Integer
factorial2 0 = 1
factorial2 n = n * factorial2(n - 1)

revList [] = []
revList (x:xs) = (revList xs) ++ [x]

stringToUpper :: String -> String
stringToUpper s = map toUpper s

square :: Int -> Int
square x = x * x

max :: [Int] -> Int
max [h] = h
max (h:t) | h >= max(t) = h
          | otherwise   = max(t)

-- list comprehension:
double x = [x*2| x<-[1..10]

map (+1) [1,2,3]

twoCombinations :: [Int] -> [Int] -> [(Int,Int)]
twoCombinations x y = [(i,j) | i <- x, j <- y]

zip [1..] [1..10] -- => (1,1), (2,2), (3,3)...

zipWith (+) [1,2,3] [1,2,3] -- => [2,4,6]

triangleArea :: Float -> Float -> Float -> Float
triangleArea a b c | isValid a b c = let p = getP a b c in sqrt(p * (p - a) * (p - b) * (p - c))
                   | otherwise = error "Not a triangle!"

main = do
    let myList = [1,2,3,4]
    let answer = myProduct myList
    let squared = map square myList
    putStrLn "answer: "
    print(answer)