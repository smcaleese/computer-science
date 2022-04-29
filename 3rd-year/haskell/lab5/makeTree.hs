data Tree a = Node a (Tree a) (Tree a)
    | Null deriving(Read, Show)

addNode :: Ord a => a -> Tree a -> Tree a
addNode a (Node b l r)
    | a < b = Node b (addNode a l) r
    | a > b = Node b l (addNode a r)
    | otherwise = Node b l r
addNode a Null = Node a Null Null

makeTree :: [Int] -> Tree Int
makeTree [] = Null
makeTree list =
    let rList = reverse list
        treeRoot = Node (head rList) Null Null
    in foldr addNode treeRoot (reverse (tail rList))
-- let bst = foldl insert treeRoot nodes

{-
main = do
    --print $ makeTree (reverse [4,3,1,7,5])
    print $ makeTree [1,2,3,4,5]
-}

--print $ foldl addNode treeRoot [2,3] where treeRoot = Node 1 Null Null

-- Add Example:
-- Node 8 (Node 4 N N) (Node 12 N N)
-- Node 8 l (addNode a (Node 12 N N))
-- Node 8 l (Node 12 N (addNode a N))
-- Node 8 l (Node 12 N (Node a N N))

-- Node 8 (Node 4 (2 Null Null) (6 Null Null)) (12 (Node 10 Null Null) (14 Null Null))
