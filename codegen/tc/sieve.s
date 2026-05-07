.data
__ignore: .space 1024
__vari: .space 1024
__varj: .space 1024
__varsieve: .space 1024
__varn: .space 1024
.text
j main


.globl main
main:

li $v0, 100
la $t0, __varn
sw $v0, ($t0)
la $t0, __varsieve
sw $v0, ($t0)
li $v0, 1
la $t0, __vari
sw $v0, ($t0)
for1:
la $t0, __vari
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __varn
lw $v0 ($t0)
lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endfor1
li $v0, 1
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __vari
lw $v0 ($t0)
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __varsieve
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
contfor1:
lw $t0, __vari
addi $t0, $t0, 1
sw $t0, __vari
j for1
endfor1:
li $v0, 2
la $t0, __vari
sw $v0, ($t0)
for2:
la $t0, __vari
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __varn
lw $v0 ($t0)
lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endfor2
la $t0, __vari
lw $v0 ($t0)
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __varsieve
addu $t0, $t0, $v0
lw $v0, ($t0)
beq $v0, $zero, endif3
la $t0, __vari
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __vari
lw $v0 ($t0)
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
la $t0, __varj
sw $v0, ($t0)
while4:
la $t0, __varj
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __varn
lw $v0 ($t0)
lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endwhile4
li $v0, 0
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __varj
lw $v0 ($t0)
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __varsieve
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
la $t0, __varj
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __vari
lw $v0 ($t0)
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
la $t0, __varj
sw $v0, ($t0)
j while4
endwhile4:
endif3:
contfor2:
lw $t0, __vari
addi $t0, $t0, 1
sw $t0, __vari
j for2
endfor2:
li $v0, 2
la $t0, __vari
sw $v0, ($t0)
for5:
la $t0, __vari
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __varn
lw $v0 ($t0)
lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endfor5
la $t0, __vari
lw $v0 ($t0)
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __varsieve
addu $t0, $t0, $v0
lw $v0, ($t0)
beq $v0, $zero, endif6
la $t0, __vari
lw $v0 ($t0)
move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall
endif6:
contfor5:
lw $t0, __vari
addi $t0, $t0, 1
sw $t0, __vari
j for5
endfor5:

# termination
li $v0 10
syscall