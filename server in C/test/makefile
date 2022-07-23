CFLAGS = -c -Wall
CC = gcc
LIBS =  -lm 

all: server

server: server.o
	${CC} server.o -o server
	
server.o : server.c
	${CC} ${CFLAGS} server.c

clean:
	rm -f *.o *~


