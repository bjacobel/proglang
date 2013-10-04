public class lab2(){

	public static void main(String[] args) {
		
	}

	private Assignment assignment() {
		//Assignment -> Identifier = Expression ;
		Variable target = new Variable(match(Token.identifier));
		match(Token.Assign);
		Expression source = expression();
		match(Token.Semicolon);
		return new Assignment();
	}


	private String match (TokenType t) {
		String value = token.value();
		if (token.type().equals(t)) {
			token = token.lexer.next()
		}
	}

	private void error(TokenType tok) {
		System.err.println("Syntax error: expecting: " + tok + ", saw: " + token);
		system.exit(1);
	}


}