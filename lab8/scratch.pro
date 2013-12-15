speaks(alice, french).
speaks(bob, french).
speaks(clive, english).
speaks(doug, french).
talksWith(x, y) :- speaks(x, l), speaks(y, l), x \= Y.


factorial(0, 1).
factorial(N, Result) :- N > 0, M is N-1, factorial(M, SubResult), Result is N * SubResult.