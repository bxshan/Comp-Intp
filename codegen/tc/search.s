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

# begin stmt proc dec
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
# begin stmt block
# begin stmt assign
# begin expr num
li $v0, 1
# end expr num
sw $v0, 4($sp)
# end stmt assign
# begin stmt assign
# begin expr var
lw $v0, 20($sp)
# end expr var
sw $v0, 12($sp)
# end stmt assign
# begin stmt assign
# begin expr binop
# begin expr num
li $v0, 0
# end expr num
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
# end expr binop
sw $v0, 8($sp)
# end stmt assign
# begin stmt while
while1:
# begin to lbl binop
# begin expr var
lw $v0, 4($sp)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
lw $v0, 16($sp)
# end expr var
lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endwhile1
# end to lbl binop
# begin stmt block
# begin stmt assign
# begin expr binop
# begin expr var
lw $v0, 4($sp)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr binop
# begin expr binop
# begin expr var
lw $v0, 16($sp)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
lw $v0, 12($sp)
# end expr var
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
# end expr binop
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 2
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
divu $t0, $v0
mflo $v0
# end expr binop
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
# end expr binop
sw $v0, 0($sp)
# end stmt assign
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
# begin expr var
lw $v0, 28($sp)
# end expr var
lw $t0 ($sp)
addu $sp $sp 4
bne $t0, $v0, endif2
# end to lbl binop
# begin stmt block
# begin stmt assign
# begin expr var
lw $v0, 0($sp)
# end expr var
sw $v0, 8($sp)
# end stmt assign
j endwhile1
# end stmt block
endif2:
# end stmt if
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
# begin expr var
lw $v0, 28($sp)
# end expr var
lw $t0 ($sp)
addu $sp $sp 4
bge $t0, $v0, else3
# end to lbl binop
# begin stmt block
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
sw $v0, 4($sp)
# end stmt assign
# end stmt block
j endif3
else3:
# begin stmt block
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
subu $v0, $t0, $v0
# end expr binop
sw $v0, 12($sp)
# end stmt assign
# end stmt block
endif3:
# end stmt if
# end stmt block
j while1
endwhile1:
# end stmt while
# begin stmt assign
# begin expr var
lw $v0, 8($sp)
# end expr var
sw $v0, 16($sp)
# end stmt assign
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
li $v0, 10
# end expr num
la $t0, __varsize
sw $v0, ($t0)
# end stmt assign
# begin stmt assign
la $t0, __vararr
sw $v0, ($t0)
# end stmt assign
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
# begin stmt arr assign
# begin expr binop
# begin expr var
la $t0, __vari
lw $v0 ($t0)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 10
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
# end expr binop
subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
la $t0, __vari
lw $v0 ($t0)
# end expr var
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vararr
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign
contfor4:
lw $t0, __vari
addi $t0, $t0, 1
sw $t0, __vari
j for4
endfor4:
# end stmt for
# begin stmt assign
# begin expr num
li $v0, 70
# end expr num
la $t0, __vartarget
sw $v0, ($t0)
# end stmt assign
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
# begin stmt writeln
# begin expr var
la $t0, __vartarget
lw $v0 ($t0)
# end expr var
move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall
# end stmt writeln
# begin stmt assign
# begin expr binop
# begin expr num
li $v0, 0
# end expr num
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
# end expr binop
la $t0, __varfound
sw $v0, ($t0)
# end stmt assign
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
# begin stmt block
# begin stmt if
# begin to lbl binop
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
subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
la $t0, __vartarget
lw $v0 ($t0)
# end expr var
lw $t0 ($sp)
addu $sp $sp 4
bne $t0, $v0, endif6
# end to lbl binop
# begin stmt block
# begin stmt assign
# begin expr var
la $t0, __vari
lw $v0 ($t0)
# end expr var
la $t0, __varfound
sw $v0, ($t0)
# end stmt assign
j endfor5
# end stmt block
endif6:
# end stmt if
# end stmt block
contfor5:
lw $t0, __vari
addi $t0, $t0, 1
sw $t0, __vari
j for5
endfor5:
# end stmt for
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
# begin stmt writeln
# begin expr var
la $t0, __varfound
lw $v0 ($t0)
# end expr var
move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall
# end stmt writeln
# begin stmt assign
# begin expr num
li $v0, 30
# end expr num
la $t0, __vartarget2
sw $v0, ($t0)
# end stmt assign
# begin stmt writeln
# begin expr string
la $v0, __strliteral2
# end expr string
move $a0, $v0
li $v0, 4
syscall
li $v0, 11
li $a0, 10
syscall
# end stmt writeln
# begin stmt writeln
# begin expr var
la $t0, __vartarget2
lw $v0 ($t0)
# end expr var
move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall
# end stmt writeln
# begin stmt assign
# begin proc call
subu $sp $sp 4
sw $ra ($sp)
# begin expr var
la $t0, __vartarget2
lw $v0 ($t0)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
la $t0, __varsize
lw $v0 ($t0)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
jal procbinarySearch
lw $t0 ($sp)
addu $sp $sp 4
lw $t0 ($sp)
addu $sp $sp 4
lw $ra ($sp)
addu $sp $sp 4
# endproc call
la $t0, __varres
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
# begin stmt writeln
# begin expr var
la $t0, __varres
lw $v0 ($t0)
# end expr var
move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall
# end stmt writeln
# end stmt block

# termination
li $v0 10
syscall