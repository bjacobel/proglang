% Usage: color() is called with three parameters
% - a list of adjacencies, eg. [[a, b], [a, c], [b, c], [d, e], [e, c], [e, a], [f, b]]
% - a list of colors, eg [red, green, blue]
% - a variable in which to store the result, eg. X

% Example: color([[a, b], [a, c], [b, c], [d, e], [e, c], [e, a], [f, b]], [red, green, blue], X)

% if colors() returns false, you need more color options.

% Two nodes are adjacent if there is an adjacency from one to the other, and vice versa
adjacent(Node1, Node2, Adj) :-  
    member([Node1, Node2], Adj); 
    member([Node2, Node1], Adj).

% Two nodes are in conflict if they have the same color in Coloring and are adjacent in Adj
conflict(Adj, Coloring) :- 
    member([Node1, Color], Coloring), 
    member([Node2, Color], Coloring), 
    adjacent(Node1, Node2, Adj). 

% assign a color to all of the nodes recursively. Store result in All_Nodes.
color_all([], _, []).
 
color_all([Node|Nodes], Colors, [[Node,Color]|All_Nodes]) :- 
    member(Color, Colors), 
    color_all(Nodes, Colors, All_Nodes).   

% From the array of adjancencies, determine the nodes present
find_nodes([], Nodes, Nodes).

find_nodes([[Node1, Node2]|Remaining_Nodes], Nodes, All_Nodes) :- 
    (member(Node1, Nodes) ->  
        (member(Node2, Nodes) -> 
            find_nodes(Remaining_Nodes, Nodes, All_Nodes);
            find_nodes(Remaining_Nodes, [Node2|Nodes], All_Nodes)); 
        (member(Node2, Nodes) -> 
            find_nodes(Remaining_Nodes, [Node1|Nodes], All_Nodes) ;
            find_nodes(Remaining_Nodes, [Node1, Node2|Nodes], All_Nodes))). 

% entry point: takes an array of adjacencies, options for colors, array to hold result
% returns the last parameter filled with an assignment of colors to nodes
color(Adj, Colors, Result) :-
    find_nodes(Adj, [], Nodes), 
    color_all(Nodes, Colors, Result), 
    \+ conflict(Adj, Result). 

% I based my algorithm for this problem on the one outlined here:
% http://www.csupomona.edu/~jrfisher/www/prolog_tutorial/2_9.html