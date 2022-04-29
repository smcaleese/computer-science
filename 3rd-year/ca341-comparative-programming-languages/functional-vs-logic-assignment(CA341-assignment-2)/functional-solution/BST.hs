module BST (insert, makeTree, search, preorder, inorder, postorder, deleteFromList, deleteFromTree) where

data Tree a = Node a (Tree a) (Tree a)
    | Null deriving(Read, Show)

insert :: Ord a => Tree a -> a -> Tree a
insert (Node v l r) x
    | x < v      = Node v (insert l x) r
    | x > v      = Node v l (insert r x)
    | otherwise  = Node v l r
insert _ x = Node x Null Null

makeTree :: Ord a => [a] -> Tree a
makeTree [] = Null
makeTree list =
    let treeRoot = Node (head list) Null Null
    in foldl insert treeRoot (tail list)

deleteFromList :: Eq a => a -> [a] -> [a]
deleteFromList a [] = []
deleteFromList a (x:xs)
    | a == x = deleteFromList a xs
    | otherwise = x : deleteFromList a xs

deleteFromTree :: Ord a => Tree a -> a -> Tree a
deleteFromTree (Node v l r) a =
    makeTree nodes
    where nodes = deleteFromList a (inorder (Node v l r))

search :: Ord a => Tree a -> a -> Bool
search (Node b l r) a
    | a < b      = search l a
    | a > b      = search r a
    | otherwise  = True
search Null a = False

preorder :: Tree a -> [a]
preorder Null = []
preorder (Node v l r) = [v] ++ preorder l ++ preorder r

inorder :: Tree a -> [a]
inorder Null = []
inorder (Node v l r) = inorder l ++ [v] ++ inorder r

postorder :: Tree a -> [a]
postorder Null = []
postorder (Node v l r) = postorder l ++ postorder r ++ [v]
