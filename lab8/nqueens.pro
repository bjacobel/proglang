valid(TrialRow, TrialDiag1, TrialDiag2, RowList, Diag1List, Diag2List) :-
    not(member(TrialRow, RowList)),
    not(member(TrialDiag1, Diag1List)),
    not(member(TrialDiag2, Diag2List)).

getDiag(Row, Col, Diag1, Diag2) :-
    Diag1 is Row + Col, Diag2 is Row - Col.

place(N, Row, Col, RowList, Diag1List, Diag2List, Row) :-
    Row < N,
    getDiag(Row, Col, Diag1, Diag2),
    valid(Row, Diag1, Diag2, RowList, Diag1List, Diag2List).

place(N, Row, Col, RowList, Diag1List, Diag2List, Answer) :-
    NextRow is Row + 1,
    NextRow < N,
    place(N, NextRow, Col, RowList, Diag1List, Diag2List, Answer).

solve(N, Col, RowList, _, _, RowList) :-
    Col >= N.

solve(N, Col, RowList, Diag1List, Diag2List, Answer) :-
    Col < N,
    place(N, 0, Col, RowList, Diag1List, Diag2List, Row),
    getDiag(Row, Col, Diag1, Diag2),
    NextCol is Col + 1,
    solve(N, NextCol, [Row | RowList], [Diag1 | Diag1List], [Diag2 | Diag2List], Answer).

queens(N, Answer) :-
    solve(N, 0, [], [], [], Answer).