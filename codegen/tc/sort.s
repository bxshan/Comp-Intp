.data
__ignore: .space 1024
__vararr: .space 1024
__vari: .space 1024
__varignore: .space 1024
__varsize: .space 1024
__strliteral0: .asciiz "Before Sort:"
__strliteral1: .asciiz "After Sort:"
.text
j main

procbubbleSort:
subu $sp $sp 4
sw $zero ($sp)
subu $sp $sp 4
sw $zero ($sp)
subu $sp $sp 4
sw $zero ($sp)
subu $sp $sp 4
sw $zero ($sp)
subu $sp $sp 4
sw $zero ($sp)
li $v0, 1
sw $v0, 4($sp)
while1:
lw $v0, 4($sp)
subu $sp $sp 4
sw $v0 ($sp)
lw $v0, 24($sp)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endwhile1
lw $v0, 20($sp)
subu $sp $sp 4
sw $v0 ($sp)
lw $v0, 8($sp)
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
sw $v0, 8($sp)
li $v0, 1
sw $v0, 0($sp)
while2:
lw $v0, 0($sp)
subu $sp $sp 4
sw $v0 ($sp)
lw $v0, 12($sp)
lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endwhile2
lw $v0, 0($sp)
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __vararr
addu $t0, $t0, $v0
lw $v0, ($t0)
subu $sp $sp 4
sw $v0 ($sp)
lw $v0, 4($sp)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __vararr
addu $t0, $t0, $v0
lw $v0, ($t0)
lw $t0 ($sp)
addu $sp $sp 4
ble $t0, $v0, endif3
lw $v0, 0($sp)
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __vararr
addu $t0, $t0, $v0
lw $v0, ($t0)
sw $v0, 12($sp)
lw $v0, 0($sp)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __vararr
addu $t0, $t0, $v0
lw $v0, ($t0)
subu $sp $sp 4
sw $v0 ($sp)
lw $v0, 4($sp)
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vararr
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
lw $v0, 12($sp)
subu $sp $sp 4
sw $v0 ($sp)
lw $v0, 4($sp)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vararr
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
endif3:
lw $v0, 0($sp)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
sw $v0, 0($sp)
j while2
endwhile2:
lw $v0, 4($sp)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
sw $v0, 4($sp)
j while1
endwhile1:
lw $t0 ($sp)
addu $sp $sp 4
lw $t0 ($sp)
addu $sp $sp 4
lw $t0 ($sp)
addu $sp $sp 4
lw $t0 ($sp)
addu $sp $sp 4
lw $v0 ($sp)
addu $sp $sp 4
jr $ra

.globl main
main:

li $v0, 5
la $t0, __varsize
sw $v0, ($t0)
la $t0, __vararr
sw $v0, ($t0)
li $v0, 64
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vararr
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
li $v0, 34
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 2
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vararr
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
li $v0, 25
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 3
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vararr
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
li $v0, 12
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 4
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vararr
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
li $v0, 22
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 5
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vararr
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
la $v0, __strliteral0
move $a0, $v0
li $v0, 4
syscall
li $v0, 11
li $a0, 10
syscall
li $v0, 1
la $t0, __vari
sw $v0, ($t0)
for4:
la $t0, __vari
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __varsize
lw $v0 ($t0)
lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endfor4
la $t0, __vari
lw $v0 ($t0)
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __vararr
addu $t0, $t0, $v0
lw $v0, ($t0)
move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall
contfor4:
lw $t0, __vari
addi $t0, $t0, 1
sw $t0, __vari
j for4
endfor4:
subu $sp $sp 4
sw $ra ($sp)
la $t0, __varsize
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
jal procbubbleSort
lw $t0 ($sp)
addu $sp $sp 4
lw $ra ($sp)
addu $sp $sp 4
la $t0, __varignore
sw $v0, ($t0)
la $v0, __strliteral1
move $a0, $v0
li $v0, 4
syscall
li $v0, 11
li $a0, 10
syscall
li $v0, 1
la $t0, __vari
sw $v0, ($t0)
for5:
la $t0, __vari
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __varsize
lw $v0 ($t0)
lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endfor5
la $t0, __vari
lw $v0 ($t0)
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __vararr
addu $t0, $t0, $v0
lw $v0, ($t0)
move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall
contfor5:
lw $t0, __vari
addi $t0, $t0, 1
sw $t0, __vari
j for5
endfor5:

# termination
li $v0 10
syscall