#ifndef CONNECTION_HANDLER__
#define CONNECTION_HANDLER__
                                           
#include <string>
#include <iostream>
#include <boost/asio.hpp>

using boost::asio::ip::tcp;

class ConnectionHandler {
private:
	const std::string host_;
	const short port_;
	boost::asio::io_service io_service_;   // Provides core I/O functionality
	tcp::socket socket_;
    std::pair<char*, int> toBytes(const std::string &line);
    std::pair<char*, int> RegisterToBytes(const std::string &frame);
    std::pair<char*, int> LoginToBytes(const std::string &frame);
    std::pair<char*, int> LogoutToBytes();
    std::pair<char*, int> UnFollowToBytes(const std::string &frame);
    std::pair<char*, int> PostToBytes(const std::string &frame);
    std::pair<char*, int> PMToBytes(const std::string &frame);
    std::pair<char*, int> LogstatToBytes();
    std::pair<char*, int> BlockToBytes(const std::string &frame);
    std::pair<char*, int> StatToBytes(const std::string &frame);
    void shortToBytes(short num, char* bytesArr);
    short bytesToShort(char *bytesArr);

    void copy(char* dest, char* copy, int len);

    char *insertIntoArray(char *array, int index, int len, char c);



public:
    ConnectionHandler(std::string host, short port);
    virtual ~ConnectionHandler();
 
    // Connect to the remote machine
    bool connect();
 
    // Read a fixed number of bytes from the server - blocking.
    // Returns false in case the connection is closed before bytesToRead bytes can be read.
    bool getBytes(char bytes[], unsigned int bytesToRead);
 
	// Send a fixed number of bytes from the client - blocking.
    // Returns false in case the connection is closed before all the data is sent.
    bool sendBytes(const char bytes[], int bytesToWrite);
	
    // Read an ascii line from the server
    // Returns false in case connection closed before a newline can be read.
    bool getLine();
	
	// Send an ascii line from the server
    // Returns false in case connection closed before all the data is sent.
    bool sendLine(std::string& line);
 
    // Get Ascii data from the server until the delimiter character
    // Returns false in case connection closed before null can be read.
    bool getFrameAscii(char delimiter);
 
    // Send a message to the remote host.
    // Returns false in case connection is closed before all the data is sent.
    bool sendFrameAscii(const std::string& frame, char delimiter);
	
    // Close down the connection properly.
    void close();

    bool getMessage(char *bytes, int index);

    bool getNotification(char *bytes, int index);

    bool getAck(char *bytes, int index);

    bool getError(char *bytes, int index);

}; //class ConnectionHandler
 
#endif