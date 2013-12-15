% Only way to express that the graph is bidirectional s to double these facts, unfortunately
edge(a,b). edge(b,a).
edge(a,c). edge(c,a).
edge(b,c). edge(c,b).
edge(d,e). edge(e,d).
edge(e,c). edge(c,e).
edge(e,a). edge(a,e).
edge(f,b). edge(b,f).

% the allowed colors
color(red).
color(blue).
color(green).

% is node1 adjacent to node2? there must be an edge between each if this is to be true
adjacent(Node1, Node2) :- 
    edge(Node1, Node2),
    edge(Node2, Node1).

% If N1 and N2 are djacent and havee the same colors, we have a problem
conflict(Node1, Node2) :- 
    adjacent(Node1, Node2),
    color(Node1) == color(Node2).

coloring([Vertex-Color]) :-
    color(Color),
    \+ edge(Vertex,_).

coloring([Vertex-Color,Vertex1-Color1|Coloring]) :-
    color(Color),
    %conflict(Vertex,Vertex1),
    coloring([Vertex1-Color1|Coloring]).

colors(X) :-
    coloring(X),
    findall(V,edge(V,_),List),
    length(List,Len),
    length(X,Len).