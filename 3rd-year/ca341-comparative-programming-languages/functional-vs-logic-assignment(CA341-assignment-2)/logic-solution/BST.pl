insert(X, nil, t(X, nil, nil)).
insert(X, t(V, L, R), t(V2, L2, R2)) :-
    X < V,
    insert(X, L, N),
    (V2, L2, R2) = (V, N, R) ;
    X > V,
    insert(X, R, N),
    (V2, L2, R2) = (V, L, N) ;
    (V2, L2, R2) = (V, L, R).

makeTree([N|Ns], T0, T) :-
    insert(N, T0, T1), % insert for every element in the list
    makeTree(Ns, T1, T).
makeTree([], T, T).

deleteFromTree(X, T, Z) :-
    inorder(T, NL), % create list of nodes
    delete(NL, X, Y),
    makeTree(Y, nil, Z). % create new tree

search(X, t(X,_,_), Y) :- Y = true.
search(_, nil, Y) :- Y = false.
search(X, t(V,L,_), Y) :- X < V, search(X, L, Y).
search(X, t(V,_,R), Y) :- X > V, search(X, R, Y).

preorder(nil, []).
preorder(t(X, Left, Right), R) :-
    append([[X]], [R1, R2], S),
    preorder(Left, R1),
    preorder(Right, R2),
    append(S, R).

inorder(nil, []).
inorder(t(X, Left, Right), R) :-
    inorder(Left, R1),
    append(R1, [X|R2], R),
    inorder(Right, R2).

postorder(nil, []).
postorder(t(X, Left, Right), R) :-
    postorder(Left, R1),
    postorder(Right, R2),
    append([R1], [R2,[X]], S),
    append(S,R).

main :-
    write('inserting:'), nl,
    makeTree([8,4,12,2,6,10,14], nil, T),
    write(T),
    nl,
    write('preorder traversal:'), nl,
    preorder(T,A),
    write(A),
    nl,
    write('inorder traversal:'), nl,
    inorder(T,B),
    write(B),
    nl,
    write('postorder traversal:'), nl,
    postorder(T,C),
    write(C),
    nl,
    write('deleting 2 from tree:'), nl,
    deleteFromTree(2, T, T2),
    write(T2),
    nl,
    write('searching for 8:'), nl,
    search(8, T, Y),
    write(Y),
    nl,
    write('searching for 99:'), nl, !,
    search(99, T, Y),
    write(Y).
