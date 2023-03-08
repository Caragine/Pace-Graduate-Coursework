@2
D=A
@i
M=D

(LOOP)
@1
D=M
@i
D=M-D
@notfound
D; JGT

@i
A=M
D=M

@0
D=D-M
@found
D; JEQ

@i
M=M+1

@LOOP
0; JMP

(found)
@0
M=1
@end
0; JMP

(notfound)
@0
M=0
@end
0; JMP

(end)
0; JMP