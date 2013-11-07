#!/usr/bin/perl

############################
### DO THE PROCESS STATS ###
############################

$top_output = `top -n 5 -o mem -l 1`;
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

$mail_content = sprintf "-- Process summary -- \n";

foreach $process (@processes[1..5]){
    # Replace every space between two words with an underscore, so we don't break up multiword process names and throw off our array indicies
    # BRO DO YOU EVEN REGEX
    $process =~ s/(?<=[a-z])(\s+)(?=[A-Z])/\_/g;
    @stats = split(/\s+/, $process);
    
    # then put the space back for pretty printing
    $stats[$command_offset] =~ s/\_/\ /g;

    # Format the mem usage up a little
    $stats[$mem_offset] =~ s/M\+//;

    if ($stats[$mem_offset] > 750){
        $alert = " (ALERT: over 750MB!!)";
    } else {
        $alert = "";
    }
    
    $mail_content .= sprintf "Process '%s' (PID %d) is using %s MB RAM, owner: %s%s\n",$stats[$command_offset],$stats[$pid_offset],$stats[$mem_offset],$stats[$user_offset],$alert;
}

############################
### DO THE DISKUSE STATS ###
############################

#print "The 'du' command needs your sudo password. Also, please note that it may take some time to run.\n"; 
#$du_output = `sudo du -hms /Users/*`;
#@diskuses = split("\n", $du_output);

$input_file = "/Users/bjacobel/Documents/sem7/proglang/code/lab4/du";
open INPUT_FILE, "<$input_file"; 
@diskuses = <INPUT_FILE>;

$mail_content .= sprintf "\n-- Disk use summary -- \n";
foreach $diskuse (@diskuses) {
    @stats = split(/(?<=\d)(\s+)(?=\/)/, $diskuse);
    $stats[2] =~ s/\/Users\///;
    $stats[2] =~ s/\n//;
    if ($stats[0] > 10240){
        $alarm = " (ALERT: over 10 GB!!)";
    } else {
        $alarm = "";
    }
    $mail_content .= sprintf "User %s is using %s MB of disk space%s.\n", $stats[2], $stats[0], $alarm;
}

#####################
### MAIL THAT ISH ###
#####################

$to = "bjacobel\@bowdoin.edu";

open(MAIL, "|mail -s 'System Stats' $to");
print MAIL $mail_content;
close(MAIL);

print $mail_content;
print "\nA copy of this report was mailed to $to.", "\n";



