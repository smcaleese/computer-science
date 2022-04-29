isProduct :: Float -> Float -> Float -> Bool
isProduct a b c = b * c == a || a * c == b || a * b == c
