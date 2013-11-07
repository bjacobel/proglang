#!/usr/bin/perl

open (INFILE, $ARGV[0]);

open (OUTFILE, ">tokens.txt");

local @clite_array = <INFILE>;

$clite = join(" ", @clite_array);

$clite =~ s/\n//g;

$type = qr/int|souble|void/;
$main = qr/main/;
$lbr = qr/\{/;
$rbr = qr/\}/;	
$lpn = qr/\(/;
$rpn = qr/\)/;
$semi = qr/;/;
$plus = qr/\+/;
$eq = qr/=/;
$id = qr/[A-za-z]\w*/;
$double = qr/\d*\.\d+/;
$int = qr/\d+/;

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
	return printToFile(";", $1) if $clite =~ s/^($semi)//;
	return printToFile("+", $1) if $clite =~ s/^($plus)//;
	return printToFile("=", $1) if $clite =~ s/^($eq)//;
	return printToFile("Identifier", $1) if $clite =~ s/^($id)//;
	return printToFile("DoubleLiteral", $1) if $clite =~ s/^($double)//;
	return printToFile("IntegerLiteral", $1) if $clite =~ s/^($int)//;
	return printToFile("Undefined", $clite);
	$clite = "";
}

sub printToFile {
	print OUTFILE shift (@_), "\t", shift (@_), "\n";
}