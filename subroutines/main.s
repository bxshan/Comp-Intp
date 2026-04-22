.data
list: .space 1024 # reserve 1024 bytes for listnodes
                  # 8 bytes per node, enough for 128 nodes
next: .word 8 # offset on list for addr of next free 8 byte loc
.text 0x00400000
.globl main 
main:
# init next
la $t0, list
sw $t0, next

# set up subroutine call
li $a0, 111
li $a1, 0
# li $a1, 10000
# li $a2, 200
jal newlistnode
move $t0, $v0

# listnode only
li $a0, 222
move $a1, $t0
jal newlistnode
move $t0, $v0 # now $t0 sto address of 222, which points to 111
# now sum the list
move $a0, $t0
jal sumlist
move $t0, $v0

# write output
li $v0, 1
move $a0, $t0
syscall

# termination 
li $v0, 10
syscall


square: # sample provided
multu $a0, $a0
mflo $v0
jr $ra


max2:
bge $a0, $a1, a0max 
a1max:
  move $v0, $a1
  j return
a0max:
  move $v0, $a0
return:
jr $ra


max3:
subu $sp, $sp, 8
sw $ra, 4($sp) # push ra 
sw $a2, 0($sp) # push third num

jal max2 

lw $a0, 0($sp) # pop third num
move $a1, $v0

jal max2 
lw $ra, 4($sp) # pop ra
addu $sp, $sp, 8

jr $ra


fact:
bne $a0, $zero, factgoon
li $v0, 1
jr $ra

factgoon:
subu $sp, $sp, 8
sw $ra, 4($sp) # push ra
sw $a0, 0($sp) # push n
subu $a0, $a0, 1
jal fact # fact(n-1) in $v0
lw $a0, 0($sp) # pop n 
lw $ra, 4($sp) # pop ra
addu $sp, $sp, 8
mult $v0, $a0
mflo $v0
jr $ra


fib:
bgt $a0, 1, fibgoon
move $v0, $a0
jr $ra

fibgoon:
subu $sp, $sp, 12
sw $ra, 8($sp) # push ra
sw $a0, 4($sp) # push n
subu $a0, $a0, 1
jal fib
sw $v0, 0($sp) # push fib(n-1)
lw $a0, 4($sp) # pop n
subu $a0, $a0, 2
jal fib
lw $a0, 0($sp) # pop fib(n-1)
add $v0, $a0, $v0
lw $ra, 8($sp) # pop ra
addu $sp, $sp, 12
jr $ra


newlistnode: # $a0 is val, $a1 is next
lw $t0, next # address of next free 8 bytes 
sw $a0, 0($t0) # put val in first 4
sw $a1, 4($t0) # next in next 4

move $v0, $t0
addu $t0, $t0, 8
sw $t0, next

jr $ra


sumlist:
bne $a0, $zero, sumlistgoon 
# if address is 0 (null)
move $v0, $zero
jr $ra

sumlistgoon:
subu $sp, $sp, 8
sw $ra, 4($sp) # push ra
sw $a0, 0($sp) # push listnode addr
# next is in 4($a0)
lw $a0, 4($a0)
jal sumlist
lw $t0, 0($sp) # pop listnode addr 
lw $t1, 0($t0)
addu $v0, $v0, $t1
lw $ra, 4($sp) # pop ra
addu $sp, $sp, 8
jr $ra

