myProduct :: [Int] -> Int
myProduct [] = 1
myProduct (h:t) = h * myProduct t

{-
main = do
    let answer = myProduct [1,2,3,4]
    putStrLn "answer: "
    print(answer)
-}