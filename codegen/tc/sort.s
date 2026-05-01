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

# begin stmt proc dec
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
# begin stmt block
# begin stmt assign
# begin expr num
li $v0, 1
# end expr num
sw $v0, 4($sp)
# end stmt assign
# begin stmt while
while1:
# begin to lbl binop
# begin expr var
lw $v0, 4($sp)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr binop
# begin expr var
lw $v0, 24($sp)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
# end expr binop
lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endwhile1
# end to lbl binop
# begin stmt block
# begin stmt assign
# begin expr binop
# begin expr var
lw $v0, 20($sp)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
lw $v0, 8($sp)
# end expr var
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
# end expr binop
sw $v0, 8($sp)
# end stmt assign
# begin stmt assign
# begin expr num
li $v0, 1
# end expr num
sw $v0, 0($sp)
# end stmt assign
# begin stmt while
while2:
# begin to lbl binop
# begin expr var
lw $v0, 0($sp)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
lw $v0, 12($sp)
# end expr var
lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endwhile2
# end to lbl binop
# begin stmt block
# begin stmt if
# begin to lbl binop
# begin expr array elem
# begin expr var
lw $v0, 0($sp)
# end expr var
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __vararr
addu $t0, $t0, $v0
lw $v0, ($t0)
# end expr array elem
subu $sp $sp 4
sw $v0 ($sp)
# begin expr array elem
# begin expr binop
# begin expr var
lw $v0, 4($sp)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
# end expr binop
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __vararr
addu $t0, $t0, $v0
lw $v0, ($t0)
# end expr array elem
lw $t0 ($sp)
addu $sp $sp 4
ble $t0, $v0, endif3
# end to lbl binop
# begin stmt block
# begin stmt assign
# begin expr array elem
# begin expr var
lw $v0, 0($sp)
# end expr var
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __vararr
addu $t0, $t0, $v0
lw $v0, ($t0)
# end expr array elem
sw $v0, 12($sp)
# end stmt assign
# begin stmt arr assign
# begin expr array elem
# begin expr binop
# begin expr var
lw $v0, 0($sp)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
# end expr binop
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __vararr
addu $t0, $t0, $v0
lw $v0, ($t0)
# end expr array elem
subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
lw $v0, 4($sp)
# end expr var
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vararr
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign
# begin stmt arr assign
# begin expr var
lw $v0, 12($sp)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr binop
# begin expr var
lw $v0, 4($sp)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
# end expr binop
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vararr
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign
# end stmt block
endif3:
# end stmt if
# begin stmt assign
# begin expr binop
# begin expr var
lw $v0, 0($sp)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
# end expr binop
sw $v0, 0($sp)
# end stmt assign
# end stmt block
j while2
endwhile2:
# end stmt while
# begin stmt assign
# begin expr binop
# begin expr var
lw $v0, 4($sp)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
# end expr binop
sw $v0, 4($sp)
# end stmt assign
# end stmt block
j while1
endwhile1:
# end stmt while
# end stmt block
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
# end stmt proc dev

.globl main
main:

# begin stmt block
# begin stmt assign
# begin expr num
li $v0, 5
# end expr num
la $t0, __varsize
sw $v0, ($t0)
# end stmt assign
# begin stmt assign
la $t0, __vararr
sw $v0, ($t0)
# end stmt assign
# begin stmt arr assign
# begin expr num
li $v0, 64
# end expr num
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vararr
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign
# begin stmt arr assign
# begin expr num
li $v0, 34
# end expr num
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 2
# end expr num
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vararr
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign
# begin stmt arr assign
# begin expr num
li $v0, 25
# end expr num
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 3
# end expr num
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vararr
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign
# begin stmt arr assign
# begin expr num
li $v0, 12
# end expr num
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 4
# end expr num
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vararr
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign
# begin stmt arr assign
# begin expr num
li $v0, 22
# end expr num
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 5
# end expr num
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vararr
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign
# begin stmt writeln
# begin expr string
la $v0, __strliteral0
# end expr string
move $a0, $v0
li $v0, 4
syscall
li $v0, 11
li $a0, 10
syscall
# end stmt writeln
# begin stmt for
# begin stmt assign
# begin expr num
li $v0, 1
# end expr num
la $t0, __vari
sw $v0, ($t0)
# end stmt assign
for4:
# begin to lbl binop
# begin expr var
la $t0, __vari
lw $v0 ($t0)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
la $t0, __varsize
lw $v0 ($t0)
# end expr var
lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endfor4
# end to lbl binop
# begin stmt writeln
# begin expr array elem
# begin expr var
la $t0, __vari
lw $v0 ($t0)
# end expr var
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __vararr
addu $t0, $t0, $v0
lw $v0, ($t0)
# end expr array elem
move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall
# end stmt writeln
contfor4:
lw $t0, __vari
addi $t0, $t0, 1
sw $t0, __vari
j for4
endfor4:
# end stmt for
# begin stmt assign
# begin proc call
subu $sp $sp 4
sw $ra ($sp)
# begin expr var
la $t0, __varsize
lw $v0 ($t0)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
jal procbubbleSort
lw $t0 ($sp)
addu $sp $sp 4
lw $ra ($sp)
addu $sp $sp 4
# endproc call
la $t0, __varignore
sw $v0, ($t0)
# end stmt assign
# begin stmt writeln
# begin expr string
la $v0, __strliteral1
# end expr string
move $a0, $v0
li $v0, 4
syscall
li $v0, 11
li $a0, 10
syscall
# end stmt writeln
# begin stmt for
# begin stmt assign
# begin expr num
li $v0, 1
# end expr num
la $t0, __vari
sw $v0, ($t0)
# end stmt assign
for5:
# begin to lbl binop
# begin expr var
la $t0, __vari
lw $v0 ($t0)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
la $t0, __varsize
lw $v0 ($t0)
# end expr var
lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endfor5
# end to lbl binop
# begin stmt writeln
# begin expr array elem
# begin expr var
la $t0, __vari
lw $v0 ($t0)
# end expr var
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __vararr
addu $t0, $t0, $v0
lw $v0, ($t0)
# end expr array elem
move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall
# end stmt writeln
contfor5:
lw $t0, __vari
addi $t0, $t0, 1
sw $t0, __vari
j for5
endfor5:
# end stmt for
# end stmt block

# termination
li $v0 10
syscall