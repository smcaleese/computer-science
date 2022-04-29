getP :: Float -> Float -> Float -> Float
getP a b c = (a + b + c) / 2

isValid :: Float -> Float -> Float -> Bool
isValid a b c = a < b + c && b < a + c && c < a + b

triangleArea :: Float -> Float -> Float -> Float
triangleArea a b c | isValid a b c = let p = getP a b c in sqrt(p * (p - a) * (p - b) * (p - c))
                   | otherwise = error "Not a triangle!"
