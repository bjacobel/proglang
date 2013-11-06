#!/usr/bin/perl

$top_output = `top -n 5 -o mem -l 1`;
#$du_output = `du -shc /Users/*`

@to = split("\n\n", $top_output);
@processes = split("\n", $to[1]);
@column_heads = split(/\s+/, $processes[0]);

$i = $pid_offset = $command_offset = $mem_offset = $user_offset = 0;

foreach $column_header (@column_heads) {
    if($column_header =~ /^pid/i){
        $pid_offset = $i;
    } elsif ($column_header =~ /^command/i) {
        $command_offset = $i;
    } elsif ($column_header =~ /^mem/i) {
        $mem_offset = $i;
    } elsif ($column_header =~ /^user/i) {
        $user_offset = $i;
    }
    $i += 1;
}

print "-- Process summary -- ", "\n";

foreach $process (@processes[1..5]){
    # Replace every space between two words with an underscore, so we don't break up multiword process names and throw off our array indicies
    # BRO DO YOU EVEN REGEX
    $process =~ s/(?<=[a-z])(\s+)(?=[A-Z])/\_/;
    @stats = split(/\s+/, $process);
    
    # then put the space back for pretty printing
    $stats[$command_offset] =~ s/\_/\ /;
    
    printf"%s: process '%s' (PID %d), started by user %s\n",$stats[$mem_offset],$stats[$command_offset],$stats[$pid_offset],$stats[$user_offset];
}