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

inOrder :: Tree a -> [a]
inOrder Null = []
inOrder (Node v l r) = inOrder l ++ [v] ++ inOrder r

{-
main = do
    let tree = makeTree [1,2,3]
    print $ inOrder tree
-}