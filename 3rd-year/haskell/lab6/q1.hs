type PDA = (Int,[Int],[Transition]) -- start state, accepting states, instructions
data Result = Accept | Reject deriving Show
type Transition = ((Int,String,String),(Int,String)) -- an instruction
-- ((q1, c, S1),(q2, S2))
type Configuration = (Int,String,String) -- current state, remaining input, stack
-- eg (2, "abba", "b") it's the state of the whole machine

-- String is the input string
run :: PDA -> String -> Result

step :: Configuration -> Transition -> [Configuration]
-- if stack empty
-- step (1,"abbabba","") ((1,"a",""),(1,"a"))
-- above should output [(1,"bbabba","a")]
step (a,(b:bs),es) ((d,[e],[f]),(g,[h]))
    -- if character will be consumed and state is empty
    | e/="" && b==e && f=="" && es==f && a==d && h/="" = [(a, bs, h)]
    | otherwise = []

step (1, "abbabba", "") ((1, "a", ""), (1, "a"))

-- if stack not empty
step (a,(b:bs),(c:cs)) ((d,[e],[f]),(g,[h]))
    | b==e && e/="" = [(1,bs,"")]
    | b==e && a==d && c==f = [(g,bs,(h:cs))]
    | otherwise = []

{-
     cs   reI     stk ((q1, c, S1),(q2, S2))
step (1,"abbabba","") ((1,"a",""),(1,"a") )
-- above should output [(1,"bbabba","a")]
-}

pal = (1,[2],[((1,"a",""),(1,"a")),
              ((1,"b",""),(1,"b")),
              ((1,"a",""),(2,"")),
              ((1,"b",""),(2,"")),
              ((1,"",""),(2,"")),
              ((2,"a","a"),(2,"")),
              ((2,"b","b"),(2,""))])

-- loop:
-- configuration + transition -> [list of next possible configurations]
