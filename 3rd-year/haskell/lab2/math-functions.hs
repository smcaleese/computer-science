-- write the functions for square, cube, volume and surfaceArea

square :: Integer -> Integer
square x = x * x

cube :: Integer -> Integer
cube x = x * x * x

-- find the volume of a sphere given its radius
volume :: Float -> Float
volume r = (4.0 * pi * cube r) / 3.0

-- find the surface area of a circle given its radius
surfaceArea :: Float -> Float
surfaceArea r = 4.0 * pi * square r