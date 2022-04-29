data BMI = VSUnderweight | SUnderweight | Underweight | Normal | Overweight | MObese | SObese | VSObese deriving Show
type Person = (Float, Float)

bmi :: Person -> Float
bmi (w, h) = w / (h**2)

bmiCalculator :: Person -> BMI
bmiCalculator p
    | bmi(p) < 15 = VSUnderweight
    | bmi(p) >= 15 && bmi(p) < 16 = SUnderweight
    | bmi(p) >= 16 && bmi(p) < 18.5 = Underweight
    | bmi(p) >= 18.5 && bmi(p) < 25 = Normal
    | bmi(p) >= 25 && bmi(p) < 30 = Overweight
    | bmi(p) >= 30 && bmi(p) < 35 = MObese
    | bmi(p) >= 35 && bmi(p) < 40 = SObese
    | bmi(p) > 40 = VSObese
