//
// Created by morab on 02/01/2022.
//

#include <stdlib.h>
#include "../include/connectionHandler.h"
#include "winsock2.h"


void run(ConnectionHandler &handler) {
    while (1){
        std::string answer;
        handler.getLine();
    }
}


int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    std::thread th(run, std::ref(connectionHandler));
    //From here we will see the rest of the ehco client implementation:
    while (1) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);
        int len=line.length();
        if (!connectionHandler.sendLine(line)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        if (line.substr(0, line.find(" "))=="LOGOUT")
            break;
    }
    th.join();
    return 0;
}
