#include "../include/connectionHandler.h"

using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;
 
ConnectionHandler::ConnectionHandler(string host, short port): host_(host), port_(port), io_service_(), socket_(io_service_){}
    
ConnectionHandler::~ConnectionHandler() {
    close();
}
 
bool ConnectionHandler::connect() {
    std::cout << "Starting connect to " 
        << host_ << ":" << port_ << std::endl;
    try {
		tcp::endpoint endpoint(boost::asio::ip::address::from_string(host_), port_); // the server endpoint
		boost::system::error_code error;
		socket_.connect(endpoint, error);
		if (error)
			throw boost::system::system_error(error);
    }
    catch (std::exception& e) {
        std::cerr << "Connection failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp ) {
			tmp += socket_.read_some(boost::asio::buffer(bytes+tmp, bytesToRead-tmp), error);			
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToWrite > tmp ) {
			tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getLine(std::string& line) {
    return getFrameAscii(line, ';');
}

bool ConnectionHandler::sendLine(std::string& line) {
    return sendFrameAscii(line, ';');
}
 
bool ConnectionHandler::getFrameAscii(std::string& frame, char delimiter) {
    char ch;
    // Stop when we encounter the null character. 
    // Notice that the null character is not appended to the frame string.
    try {
		do{
			getBytes(&ch, 1);
            frame.append(1, ch);
        }while (delimiter != ch);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::sendFrameAscii(const std::string& frame, char delimiter) {
    char *bytes = toBytes(frame);
    bool result = sendBytes(bytes, sizeof(bytes));
	if(!result) return false;
	return sendBytes(&delimiter,1);
}
 
// Close down the connection properly.
void ConnectionHandler::close() {
    try{
        socket_.close();
    } catch (...) {
        std::cout << "closing failed: connection already closed" << std::endl;
    }
}

char* ConnectionHandler::toBytes(const std::string &line) {
    int index = line.find(" ");
    if (line.substr(0, index) == "REGISTER") return RegisterToBytes(line.substr(index+2));
    if (line.substr(0, index) == "LOGIN") return LoginToBytes(line.substr(index+2));
    if (line.substr(0, index) == "LOGOUT") return LogoutToBytes();
    if (line.substr(0, index) == "FOLLOW") return UnFollowToBytes(line.substr(index+2));
    if (line.substr(0, index) == "POST") return PostToBytes(line.substr(index+2));
    if (line.substr(0, index) == "PM") return PMToBytes(line.substr(index+2));
    if (line.substr(0, index) == "LOGSTAT") return LogstatToBytes();
    if (line.substr(0, index) == "STAT") return StatToBytes(line.substr(index+2));
    else return nullptr;
}

void ConnectionHandler::shortToBytes(short num, char* bytesArr)
{
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}

char* ConnectionHandler::LoginToBytes(const std::string &frame){
    char ans[4+frame.length()];
    shortToBytes(2, ans);
    int frameIndex=0;
    int ArrayIndex=2;
    while (frameIndex!=frame.length()){
        int index = frame.find(" ");
        for (; frameIndex<index; frameIndex++, ArrayIndex++)
            ans[ArrayIndex]=frame[frameIndex];
        ans[ArrayIndex]='\0';
    }
    ans[ArrayIndex+1]='1';
    return ans;
}

char* ConnectionHandler::LogoutToBytes(){
    char ans[2];
    shortToBytes(3, ans);
    return ans;
}

char* ConnectionHandler::UnFollowToBytes(const std::string &frame){
    char ans[2+frame.length()];
    shortToBytes(4, ans);
    int frameIndex=0;
    int ArrayIndex=2;
    while (frameIndex!=frame.length()){
        int index = frame.find(" ");
        for (; frameIndex<index; frameIndex++, ArrayIndex++)
            ans[ArrayIndex]=frame[frameIndex];
        ans[ArrayIndex]='\0';
    }
    return ans;
}

char* ConnectionHandler::PostToBytes(const std::string &frame){
    char ans[3+frame.length()];
    shortToBytes(5, ans);
    int i=0;
    for (; i<frame.length(); i++)
        ans[i+2]=frame[i];
    ans[i]='\0';
    return ans;
}

char* ConnectionHandler::PMToBytes(const std::string &frame){
    char ans[20+frame.length()];
    shortToBytes(6, ans);
    int frameIndex=0;
    int ArrayIndex=2;
    while (frameIndex!=frame.length()){
        int index = frame.find(" ");
        for (; frameIndex<index; frameIndex++, ArrayIndex++)
            ans[ArrayIndex]=frame[frameIndex];
        ans[ArrayIndex]='\0';
    }
    time_t now= time(0);
    tm *ltm= localtime(&now);
    int day = ltm->tm_mday;
    ArrayIndex++;
    if (day<10)
        {ans[ArrayIndex++]='0'; ans[ArrayIndex++]=day;}
    else
        {ans[ArrayIndex++]=(day/10); ans[ArrayIndex++]=(day%10);}
    ans[ArrayIndex++]='-';
    int month= ltm->tm_mon;
    if(month<10)
    {ans[ArrayIndex++]='0'; ans[ArrayIndex++]=month;}
    else
    {ans[ArrayIndex++]=(month/10); ans[ArrayIndex++]=(month%10);}
    ans[ArrayIndex++]='-';
    int year= 1900+ltm->tm_year;
    ans[ArrayIndex++]=year/1000;
    ans[ArrayIndex++]=(year/100)%10;
    ans[ArrayIndex++]=(year/10)%10;
    ans[ArrayIndex++]= year%10;
    ans[ArrayIndex++]=' ';
    int hour= ltm->tm_hour;
    if(hour<10)
    {ans[ArrayIndex++]='0'; ans[ArrayIndex++]=hour;}
    else
    {ans[ArrayIndex++]=(hour/10); ans[ArrayIndex++]=(hour%10);}
    ans[ArrayIndex++]=':';
    int minutes= ltm->tm_min;
    if(minutes<10)
    {ans[ArrayIndex++]='0'; ans[ArrayIndex++]=minutes;}
    else
    {ans[ArrayIndex++]=(minutes/10); ans[ArrayIndex++]=(minutes%10);}
    ans[ArrayIndex]='\0';
    return ans;
}







