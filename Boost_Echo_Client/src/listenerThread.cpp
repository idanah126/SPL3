//
// Created by morab on 03/01/2022.
//

#include "../include/listenerThread.h"

void listenerThread::run(ConnectionHandler &handler) {
    while (1){
        std::string answer;
        handler.getLine();
    }
}
