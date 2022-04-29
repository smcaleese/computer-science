data Tree a = Node a (Tree a) (Tree a)
    | Null deriving(Read, Show)

addNode :: Ord a => a -> Tree a -> Tree a
addNode a (Node b l r)
    | a < b = Node b (addNode a l) r
    | a > b = Node b l (addNode a r)
    | otherwise = Node b l r
addNode a Null = Node a Null Null

makeTree :: Ord a => [a] -> Tree a
makeTree [] = Null
makeTree list =
    let rList = reverse list
        treeRoot = Node (head rList) Null Null
    in foldr addNode treeRoot (reverse (tail rList))

inOrder :: Tree a -> [a]
inOrder Null = []
inOrder (Node v l r) = inOrder l ++ [v] ++ inOrder r

mpSort :: Ord a => [a] -> [a]
mpSort arr = inOrder (makeTree arr)


{-
main = do
    print $ mpSort [4,3,1,7,5]
-}