getP :: Float -> Float -> Float -> Float
getP a b c = (a + b + c) / 2

triangleArea :: Float -> Float -> Float -> Float
triangleArea a b c = sqrt(p * (p - a) * (p - b) * (p - c)) where p = getP a b c