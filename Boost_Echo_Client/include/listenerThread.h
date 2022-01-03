//
// Created by morab on 03/01/2022.
//

#ifndef BOOST_ECHO_CLIENT_LISTENERTHREAD_H
#define BOOST_ECHO_CLIENT_LISTENERTHREAD_H


#include "connectionHandler.h"

class listenerThread {
public:
    void run(ConnectionHandler &handler);
};


#endif //BOOST_ECHO_CLIENT_LISTENERTHREAD_H
