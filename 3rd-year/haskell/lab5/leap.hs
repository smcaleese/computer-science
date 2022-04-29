data Day = Monday | Tuesday | Wednesday | Thursday | Friday | Saturday | Sunday deriving(Enum, Show)
data Month = Jan | Feb | Mar | Apr | May | Jun | Jul | Aug | Sep | Oct | Nov | Dec deriving(Enum, Read)
--monthDays :: Int
--years :: Int
type Date = (Int, Int, Int)

leap :: Int -> Bool
leap y
    | y `mod` 400 == 0 = True
    | y `mod` 4 == 0 && y `mod` 100 /= 0 = True
    | otherwise = False
