.data
__ignore: .space 1024
__vararr: .space 1024
__varres: .space 1024
__varfound: .space 1024
__varsize: .space 1024
__vari: .space 1024
__vartarget2: .space 1024
__vartarget: .space 1024
__strliteral0: .asciiz "Linear Searching for:"
__strliteral1: .asciiz "Found at Index:"
__strliteral2: .asciiz "Binary Searching for:"
__strliteral3: .asciiz "Found at Index:"
.text
j main

procbinarySearch:
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
lw $v0, 20($sp)
sw $v0, 12($sp)
li $v0, 0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
sw $v0, 8($sp)
while1:
lw $v0, 4($sp)
subu $sp $sp 4
sw $v0 ($sp)
lw $v0, 16($sp)
lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endwhile1
lw $v0, 4($sp)
subu $sp $sp 4
sw $v0 ($sp)
lw $v0, 16($sp)
subu $sp $sp 4
sw $v0 ($sp)
lw $v0, 12($sp)
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 2
lw $t0 ($sp)
addu $sp $sp 4
divu $t0, $v0
mflo $v0
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
sw $v0, 0($sp)
lw $v0, 0($sp)
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __vararr
addu $t0, $t0, $v0
lw $v0, ($t0)
subu $sp $sp 4
sw $v0 ($sp)
lw $v0, 28($sp)
lw $t0 ($sp)
addu $sp $sp 4
bne $t0, $v0, endif2
lw $v0, 0($sp)
sw $v0, 8($sp)
j endwhile1
endif2:
lw $v0, 0($sp)
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __vararr
addu $t0, $t0, $v0
lw $v0, ($t0)
subu $sp $sp 4
sw $v0 ($sp)
lw $v0, 28($sp)
lw $t0 ($sp)
addu $sp $sp 4
bge $t0, $v0, else3
lw $v0, 0($sp)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
sw $v0, 4($sp)
j endif3
else3:
lw $v0, 0($sp)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
sw $v0, 12($sp)
endif3:
j while1
endwhile1:
lw $v0, 8($sp)
sw $v0, 16($sp)
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

li $v0, 10
la $t0, __varsize
sw $v0, ($t0)
la $t0, __vararr
sw $v0, ($t0)
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
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 10
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __vari
lw $v0 ($t0)
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vararr
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
contfor4:
lw $t0, __vari
addi $t0, $t0, 1
sw $t0, __vari
j for4
endfor4:
li $v0, 70
la $t0, __vartarget
sw $v0, ($t0)
la $v0, __strliteral0
move $a0, $v0
li $v0, 4
syscall
li $v0, 11
li $a0, 10
syscall
la $t0, __vartarget
lw $v0 ($t0)
move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall
li $v0, 0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
la $t0, __varfound
sw $v0, ($t0)
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
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __vartarget
lw $v0 ($t0)
lw $t0 ($sp)
addu $sp $sp 4
bne $t0, $v0, endif6
la $t0, __vari
lw $v0 ($t0)
la $t0, __varfound
sw $v0, ($t0)
j endfor5
endif6:
contfor5:
lw $t0, __vari
addi $t0, $t0, 1
sw $t0, __vari
j for5
endfor5:
la $v0, __strliteral1
move $a0, $v0
li $v0, 4
syscall
li $v0, 11
li $a0, 10
syscall
la $t0, __varfound
lw $v0 ($t0)
move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall
li $v0, 30
la $t0, __vartarget2
sw $v0, ($t0)
la $v0, __strliteral2
move $a0, $v0
li $v0, 4
syscall
li $v0, 11
li $a0, 10
syscall
la $t0, __vartarget2
lw $v0 ($t0)
move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall
subu $sp $sp 4
sw $ra ($sp)
la $t0, __vartarget2
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __varsize
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
jal procbinarySearch
lw $t0 ($sp)
addu $sp $sp 4
lw $t0 ($sp)
addu $sp $sp 4
lw $ra ($sp)
addu $sp $sp 4
la $t0, __varres
sw $v0, ($t0)
la $v0, __strliteral1
move $a0, $v0
li $v0, 4
syscall
li $v0, 11
li $a0, 10
syscall
la $t0, __varres
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