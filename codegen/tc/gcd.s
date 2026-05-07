.data
__ignore: .space 1024
__varn1: .space 1024
__varn2: .space 1024
__varresRec: .space 1024
__varresIter: .space 1024
__strliteral0: .asciiz "Finding GCD of 48 and 18"
__strliteral1: .asciiz "Iterative:"
__strliteral2: .asciiz "Recursive:"
.text
j main

procgcd:
subu $sp $sp 4
sw $zero ($sp)
subu $sp $sp 4
sw $zero ($sp)
while1:
lw $v0, 8($sp)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 0
lw $t0 ($sp)
addu $sp $sp 4
beq $t0, $v0, endwhile1
lw $v0, 8($sp)
sw $v0, 0($sp)
lw $v0, 12($sp)
subu $sp $sp 4
sw $v0 ($sp)
lw $v0, 12($sp)
lw $t0 ($sp)
addu $sp $sp 4
divu $t0, $v0
mfhi $v0
sw $v0, 8($sp)
lw $v0, 0($sp)
sw $v0, 12($sp)
j while1
endwhile1:
lw $v0, 12($sp)
sw $v0, 4($sp)
lw $t0 ($sp)
addu $sp $sp 4
lw $v0 ($sp)
addu $sp $sp 4
jr $ra
procgcdRec:
subu $sp $sp 4
sw $zero ($sp)
lw $v0, 4($sp)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 0
lw $t0 ($sp)
addu $sp $sp 4
bne $t0, $v0, else2
lw $v0, 8($sp)
sw $v0, 0($sp)
j endif2
else2:
subu $sp $sp 4
sw $ra ($sp)
lw $v0, 8($sp)
subu $sp $sp 4
sw $v0 ($sp)
lw $v0, 16($sp)
subu $sp $sp 4
sw $v0 ($sp)
lw $v0, 16($sp)
lw $t0 ($sp)
addu $sp $sp 4
divu $t0, $v0
mfhi $v0
subu $sp $sp 4
sw $v0 ($sp)
jal procgcdRec
lw $t0 ($sp)
addu $sp $sp 4
lw $t0 ($sp)
addu $sp $sp 4
lw $ra ($sp)
addu $sp $sp 4
sw $v0, 0($sp)
endif2:
lw $v0 ($sp)
addu $sp $sp 4
jr $ra

.globl main
main:

li $v0, 48
la $t0, __varn1
sw $v0, ($t0)
li $v0, 18
la $t0, __varn2
sw $v0, ($t0)
la $v0, __strliteral0
move $a0, $v0
li $v0, 4
syscall
li $v0, 11
li $a0, 10
syscall
la $v0, __strliteral1
move $a0, $v0
li $v0, 4
syscall
li $v0, 11
li $a0, 10
syscall
subu $sp $sp 4
sw $ra ($sp)
la $t0, __varn1
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __varn2
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
jal procgcd
lw $t0 ($sp)
addu $sp $sp 4
lw $t0 ($sp)
addu $sp $sp 4
lw $ra ($sp)
addu $sp $sp 4
la $t0, __varresIter
sw $v0, ($t0)
la $t0, __varresIter
lw $v0 ($t0)
move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall
la $v0, __strliteral2
move $a0, $v0
li $v0, 4
syscall
li $v0, 11
li $a0, 10
syscall
subu $sp $sp 4
sw $ra ($sp)
la $t0, __varn1
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __varn2
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
jal procgcdRec
lw $t0 ($sp)
addu $sp $sp 4
lw $t0 ($sp)
addu $sp $sp 4
lw $ra ($sp)
addu $sp $sp 4
la $t0, __varresRec
sw $v0, ($t0)
la $t0, __varresRec
lw $v0 ($t0)
move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall

# termination
li $v0 10
syscall