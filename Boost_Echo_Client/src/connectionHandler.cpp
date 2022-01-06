#include "../include/connectionHandler.h"
#include <algorithm>

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
 
bool ConnectionHandler::getLine() {
    return getFrameAscii(';');
}

bool ConnectionHandler::sendLine(std::string& line) {
    return sendFrameAscii(line, ';');
}
 
bool ConnectionHandler::getFrameAscii(char delimiter) {
    char ch;
    char *bytes= new char[30];
    int len =30;
    int index=0;
    bool ans = true;
    // Stop when we encounter the null character.
    // Notice that the null character is not appended to the frame string.
    try {
		do{
			getBytes(&ch, 1);
            bytes= insertIntoArray(bytes,index++, len, ch);
            if (len<=index-1) len=len*2;
        }while (delimiter != ch);
        ans = getMessage(bytes, index);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return ans;
    }
    return ans;
}

char* ConnectionHandler::insertIntoArray(char array[], int index, int len, char c){
    if(index>=len){
        char *ans = new char[2*len];
        copy(ans, array, len);
        ans[index]=c;
        delete(array);
        return ans;
    }
    array[index]=c;
    return array;

}



bool ConnectionHandler::sendFrameAscii(const std::string& frame, char delimiter) {
    std::pair<char*, int> bytes = toBytes(frame);
    bool result = sendBytes(bytes.first, bytes.second);
    delete(bytes.first);
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

std::pair<char*, int> ConnectionHandler::toBytes(const std::string &line) {
    int index = line.find(" ");
    if (line.substr(0, index) == "REGISTER") return RegisterToBytes(line.substr(index + 1));
    if (line.substr(0, index) == "LOGIN") return LoginToBytes(line.substr(index+1));
    if (line.substr(0, index) == "LOGOUT") return LogoutToBytes();
    if (line.substr(0, index) == "FOLLOW") return UnFollowToBytes(line.substr(index+1));
    if (line.substr(0, index) == "POST") return PostToBytes(line.substr(index+1));
    if (line.substr(0, index) == "PM") return PMToBytes(line.substr(index+1));
    if (line.substr(0, index) == "LOGSTAT") return LogstatToBytes();
    if (line.substr(0, index) == "STAT") return StatToBytes(line.substr(index+1));
    return BlockToBytes(line.substr(index+1));
}

void ConnectionHandler::shortToBytes(short num, char* bytesArr)
{
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}

std::pair<char*, int> ConnectionHandler::RegisterToBytes(const string &frame) {
    char *ans = new char[3 + frame.length()];
    shortToBytes(1, ans);
    int frameIndex = 0;
    int ArrayIndex = 2;
    std::string changeable = frame;
    while (frameIndex < frame.length()){
        int index = changeable.find(" ");
        if (index == -1) index = changeable.length();
        for (int i = 0; i < index; frameIndex++, ArrayIndex++, i++) 
            ans[ArrayIndex] = changeable[i];
        ans[ArrayIndex++] = '\0';
        changeable = changeable.substr(std::min(index+1, (int)changeable.length()));
        frameIndex++;
    }
    return std::pair<char*, int>(ans, 3 + frame.length());
}


std::pair<char*, int> ConnectionHandler::LoginToBytes(const std::string &frame){
    char *ans = new char[3+frame.length()];
    shortToBytes(2, ans);
    int frameIndex=0;
    int ArrayIndex=2;
    std::string changeable = frame;
    while (frameIndex<frame.length()){
        int index = changeable.find(" ");
        if (index == -1) index = changeable.length();
        for (int i = 0; i < index; frameIndex++, ArrayIndex++, i++)
            ans[ArrayIndex]= changeable[i];
        ans[ArrayIndex++]='\0';
        changeable = changeable.substr(std::min(index + 1, (int)changeable.length()));
        frameIndex++;
    }
    return std::pair<char*, int>(ans, 2 + frame.length());
}

std::pair<char*, int> ConnectionHandler::LogoutToBytes(){
    char *ans = new char[2];
    shortToBytes(3, ans);
    return std::pair<char*, int>(ans, 2);
}

std::pair<char*, int> ConnectionHandler::UnFollowToBytes(const std::string &frame){
    char *ans = new char[2+frame.length()];
    shortToBytes(4, ans);
    ans[2] = frame[0];
    for (int i = 2; i < frame.length(); i++) {
        ans[i + 1] = frame[i];
    }
    return std::pair<char*, int>(ans, 2 + frame.length());
}

std::pair<char*, int> ConnectionHandler::PostToBytes(const std::string &frame){
    char *ans = new char[3+frame.length()];
    shortToBytes(5, ans);
    int i=0;
    for (; i<frame.length(); i++)
        ans[i+2]=frame[i];
    ans[i]='\0';
    return std::pair<char*, int>(ans, 3 + frame.length());
}

std::pair<char*, int> ConnectionHandler::PMToBytes(const std::string &frame){
    char *ans = new char[20+frame.length()];
    shortToBytes(6, ans);
    int frameIndex=0;
    int ArrayIndex=2;
    std::string changeable = frame;
    while (frameIndex<frame.length()) {
        int index = changeable.find(" ");
        if (index == -1) index = changeable.length();
        for (int i = 0; i < index; frameIndex++, ArrayIndex++, i++)
            ans[ArrayIndex] = changeable[i];
        ans[ArrayIndex++] = '\0';
        changeable = changeable.substr(std::min(index + 1, (int)changeable.length()));
        frameIndex++;
    }
    time_t now= time(0);
    /*
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
    */

    return std::pair<char*, int>(ans, 20 + frame.length());
}

std::pair<char*, int> ConnectionHandler::LogstatToBytes() {
    char *ans = new char[2];
    shortToBytes(7, ans);
    return std::pair<char*, int>(ans, 2);
}

std::pair<char*, int> ConnectionHandler::StatToBytes(const string &frame) {
    char *ans = new char[frame.length() + 3];
    shortToBytes(8, ans);
    int frameIndex = 0;
    int ArrayIndex = 2;
    std::string temp = frame;
    for (int i = 0; i < temp.length(); i++) {
        if (temp[i] == ' '){
            temp[i] = '|';
        }
    }
    while (frameIndex != temp.length()){
        int index = temp.find(" ");
        for (; frameIndex < index; frameIndex++, ArrayIndex++)
            ans[ArrayIndex] = temp[frameIndex];
        ans[ArrayIndex] = 0;
    }
    return std::pair<char*, int>(ans, 3 + frame.length());
}

std::pair<char*, int> ConnectionHandler::BlockToBytes(const std::string &frame){
    char *ans = new char[3+frame.length()];
    shortToBytes(12, ans);
    int i=0;
    for (; i<frame.length(); i++)
        ans[i+2]=frame[i];
    ans[i]='\0';
    return std::pair<char*, int>(ans, 3 + frame.length());
}


bool ConnectionHandler::getMessage(char *bytes, int len) {
    char opcode[2];
    opcode[0]=bytes[0];
    opcode[1]=bytes[1];
    short result = bytesToShort(opcode);
    if (result==9) return getNotification(bytes, len);
    if (result==10) return getAck(bytes, len);
    if (result==11) return getError(bytes, len);
    return true;
}
short ConnectionHandler::bytesToShort(char* bytesArr)
{
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}

bool ConnectionHandler::getNotification(char *bytes, int len) {
    char messageOpcode[1];
    messageOpcode[0]=bytes[2];
    short mo= bytesToShort(messageOpcode);
    std::string ans="";
    if (mo==0) ans="PM";
    else ans="Public";
    for(int i=3; i<len; i++) {
        if (bytes[i] == '\0') ans = ans + " ";
        else ans = ans + bytes[i];
    }
    std::cout<<"NOTIFICATION "<<mo<<ans.substr(0, ans.length()-2)<<std::endl;
    return false;
}

bool ConnectionHandler::getAck(char *bytes, int len) {
    char messageOpcode[2];
    messageOpcode[0]=bytes[2];
    messageOpcode[1]=bytes[3];
    short mo= bytesToShort(messageOpcode);
    std::string ans="";
    for(int i=4; i<len; i++)
        ans=ans+bytes[i];
    std::cout<<"ACK "<<mo<<" "<<ans.substr(0, ans.length()-1)<<std::endl;
    if (mo == 3)
    {
        return true;
    }
    return false;
}

bool ConnectionHandler::getError(char *bytes, int len) {
    char messageOpcode[2];
    messageOpcode[0]=bytes[2];
    messageOpcode[1]=bytes[3];
    short mo= bytesToShort(messageOpcode);
    std::cout<<"ERROR "<<mo<<std::endl;
    return false;

}

void ConnectionHandler::copy(char *dest, char *copy, int len) {
    for (int i=0; i<len; i++)
        dest[i]=copy[i];

}
