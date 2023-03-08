@0
D=M
@1
D=D-M
@1greaterthan0
D; JLT
@0
D=M
@2
D=D-M
@2greaterthan01
D; JLT
@0
D=M
@3
D=D-M
@3greaterthan012
D; JLT
@0
D=M
@0
M=D
0; JMP

(1greaterthan0)
@1
D=M
@2
D=D-M
@2greaterthan01
D; JLT
@1
D=M
@3
D=D-M
@3greaterthan012
D; JLT
@1
D=M
@0
M=D
0; JMP

(2greaterthan01)
@2
D=M
@3
D=D-M
@3greaterthan012
D; JLT
@2
D=M
@0
M=D
0; JMP

(3greaterthan012)
@3
D=M
@0
M=D
0; JMP
