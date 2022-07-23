#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <ctype.h>

#define MAX_BYTES 1024

void create_tcp_connection(int port);
void process(char *output, char *input, int size);

int main(int argc, char *argv[]) {
    if (argc != 2) {
        printf("Usage: ./server PortNumber (Example ./server 5500)\n");
        return 1;
    } else {
        create_tcp_connection(atoi(argv[1]));
    }
    return 0;
}

void create_tcp_connection(int port) {
    int sockfd, connfd, len;
    struct sockaddr_in servaddr, cli;

    // socket create and verification
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd == -1) {
        printf("Socket creation failed...\n");
        exit(0);
    }
    else
        printf("Socket successfully created..\n");

    bzero(&servaddr, sizeof(servaddr));

    // assign IP, PORT
    servaddr.sin_family = AF_INET;
    servaddr.sin_addr.s_addr = htonl(INADDR_ANY);
    servaddr.sin_port = htons(port);

    // Binding newly created socket to given IP and verification
    if ((bind(sockfd, (const struct sockaddr *) &servaddr, sizeof(servaddr))) != 0) {
        printf("Socket bind failed...\n");
        exit(0);
    }
    else
        printf("Socket successfully binded..\n");

    // Now server is ready to listen and verification
    if ((listen(sockfd, 5)) != 0) {
        printf("Listen failed...\n");
        exit(0);
    }
    else
        printf("Server listening..\n");
    len = sizeof(cli);

    // Accept the data packet from client and verification
    connfd = accept(sockfd, (struct sockaddr *) &cli, (socklen_t *) &len);
    if (connfd < 0) {
        printf("Server accept failed...\n");
        exit(0);
    }
    else
        printf("Server accept the Client...\n\n");
    
    char buff[MAX_BYTES];
    bzero(buff, MAX_BYTES);
    //read(connfd, buff, MAX_BYTES);
    unsigned short number1;
    recv(connfd,buff,MAX_BYTES,0);
    number1 = ntohs(*(unsigned short *)buff);
    
    printf("%c + %c\n", buff[0], buff[1]);

    printf("%u\n", number1);

    //Send and read 2byte unsigned short
    unsigned short number=34568;

    char result[10];

    *((unsigned short *) result) = htons(number);

    //char * test = "string2";
    send(connfd, result, strlen(result)+1, 0);
    //send(connfd, test, strlen(test)+1, 0);
    printf("Server closed...\n");
    close(sockfd);
}