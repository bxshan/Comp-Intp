.text 0x00400000
.globl main 
main:
li $t0, 2 # load 2 into $to
li $t1, 3 # load 3 into $t1
addu $t2, $t0, $t1 # store $t0 + $t1 in $t2
li $v0, 10 # normal termination
syscall
