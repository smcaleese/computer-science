type PDA = (Int,[Int],[Transition]) -- start state, accepting states, instructions
data Result = Accept | Reject deriving Show
type Transition = ((Int,String,String),(Int,String)) -- an instruction
-- ((q1, c, S1),(q2, S2))
type Configuration = (Int,String,String) -- current state, remaining input, stack
-- eg (2, "abba", "b") it's the state of the whole machine

-- (q, str, S0) ((q1,c,S1),(q2,S2))
step :: Configuration -> Transition -> [Configuration]

step (a,"",c) ((d,e,f),(g,h))
    | a==d && e=="" && c=="" && f=="" && h=="" = [(g,"","")] -- change state
    | a==d && e=="" && c=="" && f=="" && h/="" = [(g,"",h)] -- push to empty stack
    | a==d && e=="" && c/="" && f=="" && h/="" = [(g,"",h++c)] -- push to existing stack
    | a==d && e=="" && c/="" && f==[head(c)] && h=="" = [(g,"",tail(c))] -- pop from stack
    | a==d && e=="" && c/="" && f==[head(c)] && h/="" = [(g,"", h++tail(c))] -- replace
    | otherwise = []

-- step 1
step (a,b,"") ((d,"",""),(g,""))
    | a==d = [(g, b, "")]
    | otherwise = []

-- step 2
step (a,b,"") ((d,"",""),(g,[h]))
    | a==d = [(g, b, [h])]
    | otherwise = []

-- step 3
step (a,b,"") ((d,"",[f]),(g,"")) = []

-- step 4
step (a,b,"") ((d,"",[f]),(g,[h])) = []

-- step 5
step (a,(b:bs),"") ((d,[e],""),(g,""))
    | a==d && b==e = [(g, bs, "")]
    | otherwise = []

-- step 6
step (a,(b:bs),"") ((d,[e],""),(g,[h]))
    | a==d && b==e = [(g, bs, [h])]
    | otherwise = []

-- step 7
step (a,(b:bs),"") ((d,[e],[f]),(g,"")) = []

-- step 8
step (a,(b:bs),"") ((d,[e],[f]),(g,[h])) = []

-- step 9
step (a,b,(c:cs)) ((d,"",""),(g,""))
    | a==d = [(g, b, c:cs)]
    | otherwise = []

-- step 10
step (a,b,(c:cs)) ((d,"",""),(g,[h]))
    | a==d = [(g, b, h:c:cs)]
    | otherwise = []

-- step 11
step (a,b,(c:cs)) ((d,"",[f]),(g,""))
    | a==d && c==f = [(g, b, cs)]
    | otherwise = []

-- step 12
step (a,b,(c:cs)) ((d,"",[f]),(g,[h]))
    | a==d && c==f = [(g, b, h:cs)]
    | otherwise = []

-- step 13
step (a,(b:bs),(c:cs)) ((d,[e],""),(g,""))
    | a==d && b==e = [(g, bs, c:cs)]
    | otherwise = []

-- step 14
step (a,(b:bs),(c:cs)) ((d,[e],""),(g,[h]))
    | a==d && b==e = [(g, bs, h:c:cs)]
    | otherwise = []

-- step 15
step (a,(b:bs),(c:cs)) ((d,[e],[f]),(g,""))
    | a==d && b==e && c==f = [(g, bs, cs)]
    | otherwise = []

-- step 16
step (a,(b:bs),(c:cs)) ((d,[e],[f]),(g,[h]))
    | a==d && b==e && c==f = [(g, bs, h:cs)]
    | otherwise = []

steps :: Configuration -> [Transition] -> [Configuration]
steps c [] = []
steps c (t:ts) = (step c t) ++ (steps c ts)

nextsteps :: [Configuration] -> [Transition] -> [Configuration]
nextsteps [] t = []
nextsteps (c:cs) t = (steps c t) ++ (nextsteps cs t)

accept :: [Configuration] -> [Int] -> Bool
-- return true if one or more configurations are empty and in the right final state
accept [] intList = False
accept (c:cs) intList
    | s1=="" && s2=="" = elem x intList
    | otherwise = accept cs intList
    where (x,s1,s2) = c

findpath :: [Configuration] -> PDA -> Result
findpath [] pda = Reject
findpath cs pda
    | (accept cs intList) == True = Accept
    | otherwise = findpath (nextsteps cs transitions) pda
    where (_,intList,transitions) = pda

pal :: PDA
pal = (1,[2],[((1,"a",""),(1,"a")),
    ((1,"b",""),(1,"b")),
    ((1,"a",""),(2,"")),
    ((1,"b",""),(2,"")),
    ((1,"",""),(2,"")),
    ((2,"a","a"),(2,"")),
    ((2,"b","b"),(2,""))])

run ::  PDA -> String -> Result
run pda s = let (n,_,t) = pda in (findpath [(n, s, "")]) pda