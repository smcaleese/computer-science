data Day = Monday | Tuesday | Wednesday | Thursday | Friday | Saturday | Sunday deriving(Enum, Show)
data Month = Jan | Feb | Mar | Apr | May | Jun | Jul | Aug | Sep | Oct | Nov | Dec deriving(Enum, Read)
type Date = (Int, Month, Int)

leap :: Int -> Bool
leap y
    | y `mod` 400 == 0 = True
    | y `mod` 4 == 0 && y `mod` 100 /= 0 = True
    | otherwise = False

mLengths :: Int -> [Int]
mLengths y
    | leap y = [31,29,31,30,31,30,31,31,30,31,30,31]
    | otherwise = [31,28,31,30,31,30,31,31,30,31,30,31]

yearsToYearList :: Int -> Int -> [Int]
yearsToYearList y1 y2
    | y1+1 == y2 = []
    | otherwise = y1+1 : yearsToYearList (y1 + 1) y2

yearListToTotalDays :: [Int] -> Int
yearListToTotalDays [] = 0
yearListToTotalDays (x:xs) = sum (mLengths(x)) + yearListToTotalDays xs

listSlice :: Int -> [Int] -> [Int]
listSlice 0 list = []
listSlice n list = head(list) : listSlice (n-1) (tail list)

monthToDays :: Int -> Month -> Int
monthToDays y m
    | leap y = sum (listSlice (fromEnum m) [31,29,31,30,31,30,31,31,30,31,30,31])
    | otherwise = sum (listSlice (fromEnum m) [31,28,31,30,31,30,31,31,30,31,30,31])

numDays :: Date -> Int
numDays (d, m, y) = d + monthToDays y m + yearListToTotalDays(yearsToYearList 1752 y)
-- 15 +  [31,29,31] + [[31,29,31,30,31,30,31,31,30,31,30,31], [31,29,31,30,31,30,31,31,30,31,30,31]]