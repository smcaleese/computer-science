let (a,b) = (10,12) in a * 2
-- result: 20

let (a:b:c:[]) = "xyz" in a
-- result: 'x'

let (a:_:_:_) = "xyz" in a
-- result: 'x'

let (a:_) = "xyz" in a
-- result: 'x'

let (_,(a:_)) = (10,"abc") in a
-- result: 'a'

let _:_:c:_ = "abcd" in c
-- result: 'c'

let [a,b,c] = "cat" in (a,b,c)
-- result: ('c', 'a', 't')

let abc@(a,b,c) = (10,20,30) in (abc,a,b,c)
-- result: ((10,20,30),10,20,30)