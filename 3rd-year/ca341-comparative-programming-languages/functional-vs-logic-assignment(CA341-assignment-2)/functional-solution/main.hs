import BST

main = do
    let nodes = [8, 4, 12, 2, 6, 10, 14]
    -- recursively insert nodes into tree
    let tree = makeTree nodes
    putStrLn "printing tree:"
    print tree
    putStrLn "searching for 4:"
    print $ search tree 4
    putStrLn "preorder traversal:"
    print $ preorder tree
    putStrLn "inorder traversal:"
    print $ inorder tree
    putStrLn "postorder traversal:"
    print $ postorder tree
    putStrLn "deleting 8 from tree:"
    print $ deleteFromTree tree 8