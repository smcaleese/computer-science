data Tree a = Node a (Tree a) (Tree a)
    | Null deriving(Read, Show)

addNode :: Ord a => a -> Tree a -> Tree a
addNode a (Node b l r)
    | a < b = Node b (addNode a l) r
    | a > b = Node b l (addNode a r)
    | otherwise = Node b l r
addNode a Null = Node a Null Null

{-
main = do
    print $ makeTree [4,2,6]
-}

--print $ foldl addNode treeRoot [2,3] where treeRoot = Node 1 Null Null

-- Add Example:
-- Node 8 (Node 4 N N) (Node 12 N N)
-- Node 8 l (addNode a (Node 12 N N))
-- Node 8 l (Node 12 N (addNode a N))
-- Node 8 l (Node 12 N (Node a N N))

-- Node 8 (Node 4 (2 Null Null) (6 Null Null)) (12 (Node 10 Null Null) (14 Null Null))
