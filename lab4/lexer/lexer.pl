#!/usr/bin/perl

open (INFILE, $ARGV[0]);

open (OUTFILE, ">tokens.txt");

local @clite_array = <INFILE>;

$clite = join(" ", @clite_array);

$clite =~ s/\n//g;

$type = qr/int|double|void|bool|char/;
$main = qr/main/;
$lbr = qr/\{/;
$rbr = qr/\}/;	
$lpn = qr/\(/;
$rpn = qr/\)/;
$semi = qr/;/;
$plus = qr/\+/;
$eq = qr/==/;  # had to modify this one
$id = qr/[A-za-z]\w*/;
$double = qr/\d*\.\d+/;
$int = qr/\d+/;

# added by me
$assign = qr/=(?=[^=])/;
$bool = qr/true|false/;
$comma = qr/,/;
$rbrace = qr/\]/;
$lbrace = qr/\[/;
$lt = qr/</;
$leq = qr/<=/;
$gt = qr/>/;
$geq = qr/>=/;
$neq = qr/!=/;
$not = qr/!(?=[^=])/;
$and = qr/&&/;
$or = qr/\|\|/;
$minus = qr/\-/;
$multiply = qr/\*/;
$divide = qr/\//;
$else = qr/else/;
$if = qr/if/;
$while = qr/while/;

while ($clite ne ""){
	match();
}

sub match {
	return if $clite =~ s/\s+//;
	return printToFile("Type", $1) if $clite =~ s/^($type)//;
	return printToFile("main", $1) if $clite =~ s/^($main)//;
	return printToFile("(", $1) if $clite =~ s/^($lpn)//;
	return printToFile(")", $1) if $clite =~ s/^($rpn)//;
	return printToFile("{", $1) if $clite =~ s/^($lbr)//;
	return printToFile("}", $1) if $clite =~ s/^($rbr)//;
	return printToFile("[", $1) if $clite =~ s/^($lbrace)//;
	return printToFile("]", $1) if $clite =~ s/^($rbrace)//;
	return printToFile(";", $1) if $clite =~ s/^($semi)//;
	return printToFile(",", $1) if $clite =~ s/^($comma)//;
	return printToFile("+", $1) if $clite =~ s/^($plus)//;
	return printToFile("-", $1) if $clite =~ s/^($minus)//;
	return printToFile("*", $1) if $clite =~ s/^($multiply)//;
	return printToFile("/", $1) if $clite =~ s/^(divide)//;
	return printToFile("==", $1) if $clite =~ s/^($eq)//;
	return printToFile("=", $1) if $clite =~ s/^($assign)//;
	return printToFile("And", $1) if $clite =~ s/^($and)//;
	return printToFile("Or", $1) if $clite =~ s/^($or)//;
	return printToFile("Else", $1) if $clite =~ s/^($else)//;
	return printToFile("If", $1) if $clite =~ s/^($if)//;
	return printToFile("While", $1) if $clite =~ s/^($while)//;
	return printToFile("LessThan", $1) if $clite =~ s/^($lt)//;
	return printToFile("LessEqual", $1) if $clite =~ s/^($leq)//;
	return printToFile("GreaterThan", $1) if $clite =~ s/^($gt)//;
	return printToFile("GreaterEqual", $1) if $clite =~ s/^($geq)//;
	return printToFile("NotEqual", $1) if $clite =~ s/^($neq)//;
	return printToFile("Not", $1) if $clite =~ s/^($not)//;
	return printToFile("Identifier", $1) if $clite =~ s/^($id)//;
	return printToFile("DoubleLiteral", $1) if $clite =~ s/^($double)//;
	return printToFile("IntegerLiteral", $1) if $clite =~ s/^($int)//;
	return printToFile("BooleanLiteral", $1) if $clite =~ s/^($bool)//;
	return printToFile("Undefined", $clite);
	$clite = "";
}

sub printToFile {
	print OUTFILE shift (@_), "\t", shift (@_), "\n";
}