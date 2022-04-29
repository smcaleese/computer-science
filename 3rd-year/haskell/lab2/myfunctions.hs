factorial1 :: Integer -> Integer
factorial1 n = if n < 2 then 1 else factorial1(n - 1)

factorial2 :: Integer -> Integer
factorial2 0 = 1
factorial2 n = n * factorial2(n - 1)

addTwoNums :: Int -> Int -> Int
addTwoNums a b = a + b

revList [] = []
revList (x:xs) = (revList xs) ++ [x]

sumList [] = 0
sumList (x:xs) = x + (sumList xs)

listLength [] = 0
listLength (_:xs) = 1 + (listLength xs)

let x = 8 * 10 in x + x
map (+1) [1..5]

let square x = x * x in square 3
let square x = x * x in map square [1..10]
let take5s = filter (==5) in take5s [1,5,2,5,3,5]
map toUpper "Chris"

main = do
    let answer = addTwoNums 5 3
    putStrLn "answer:"
    print(answer)

