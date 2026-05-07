.data
__ignore: .space 1024
__varvisited: .space 1024
__varmax_nodes: .space 1024
__vari: .space 1024
__varignore: .space 1024
__vargraph: .space 1024
__strliteral0: .asciiz "Starting DFS:"
__strliteral1: .asciiz "Visiting Node "
.text
j main

procdfs:
subu $sp $sp 4
sw $zero ($sp)
subu $sp $sp 4
sw $zero ($sp)
subu $sp $sp 4
sw $zero ($sp)
li $v0, 1
subu $sp $sp 4
sw $v0 ($sp)
lw $v0, 16($sp)
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __varvisited
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
la $v0, __strliteral1
move $a0, $v0
li $v0, 4
syscall
li $v0, 11
li $a0, 10
syscall
lw $v0, 12($sp)
move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall
li $v0, 1
sw $v0, 0($sp)
while1:
lw $v0, 0($sp)
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __varmax_nodes
lw $v0 ($t0)
lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endwhile1
lw $v0, 12($sp)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __varmax_nodes
lw $v0 ($t0)
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
subu $sp $sp 4
sw $v0 ($sp)
lw $v0, 4($sp)
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
sw $v0, 4($sp)
lw $v0, 4($sp)
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __vargraph
addu $t0, $t0, $v0
lw $v0, ($t0)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
bne $t0, $v0, endif2
lw $v0, 0($sp)
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __varvisited
addu $t0, $t0, $v0
lw $v0, ($t0)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
beq $t0, $v0, endif3
subu $sp $sp 4
sw $ra ($sp)
lw $v0, 4($sp)
subu $sp $sp 4
sw $v0 ($sp)
jal procdfs
lw $t0 ($sp)
addu $sp $sp 4
lw $ra ($sp)
addu $sp $sp 4
la $t0, __varignore
sw $v0, ($t0)
endif3:
endif2:
lw $v0, 0($sp)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
sw $v0, 0($sp)
j while1
endwhile1:
lw $t0 ($sp)
addu $sp $sp 4
lw $t0 ($sp)
addu $sp $sp 4
lw $v0 ($sp)
addu $sp $sp 4
jr $ra

.globl main
main:

li $v0, 4
la $t0, __varmax_nodes
sw $v0, ($t0)
la $t0, __vargraph
sw $v0, ($t0)
la $t0, __varvisited
sw $v0, ($t0)
li $v0, 1
la $t0, __vari
sw $v0, ($t0)
for4:
la $t0, __vari
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 4
lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endfor4
li $v0, 0
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __vari
lw $v0 ($t0)
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __varvisited
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
li $v0, 1
la $t0, __vari
sw $v0, ($t0)
for5:
la $t0, __vari
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 16
lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endfor5
li $v0, 0
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __vari
lw $v0 ($t0)
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vargraph
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
contfor5:
lw $t0, __vari
addi $t0, $t0, 1
sw $t0, __vari
j for5
endfor5:
li $v0, 1
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 4
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 2
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vargraph
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
li $v0, 1
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 2
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 4
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vargraph
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
li $v0, 1
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 4
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 3
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vargraph
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
li $v0, 1
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 3
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 4
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vargraph
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
li $v0, 1
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 2
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 4
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 4
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vargraph
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
li $v0, 1
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 4
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 4
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 2
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vargraph
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
li $v0, 1
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 3
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 4
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 4
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vargraph
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
li $v0, 1
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 4
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 4
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 3
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vargraph
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
subu $sp $sp 4
sw $ra ($sp)
li $v0, 1
subu $sp $sp 4
sw $v0 ($sp)
jal procdfs
lw $t0 ($sp)
addu $sp $sp 4
lw $ra ($sp)
addu $sp $sp 4
la $t0, __varignore
sw $v0, ($t0)

# termination
li $v0 10
syscall