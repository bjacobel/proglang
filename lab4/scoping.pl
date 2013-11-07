#!/usr/bin/perl

$x = 5;
$y = 10;
myFunction();

print "x = $x, y = $y", "\n";

sub myFunction {
 	local($x, $y);
 	$x = 100, $y = 200;
 	swap();
	print "After swap: x = $x, y = $y", "\n";
}

sub swap {
	$t = $x;
	$x = $y;
	$y = $t;
}