cmake_minimum_required(VERSION 3.20)
project(Boost_Echo_Client)
include_directories(include)
set(CMAKE_CXX_STANDARD 11)

add_executable(Boost_Echo_Client src/connectionHandler.cpp src/MessageClient.cpp)

include_directories(C:\\boost\\include\\boost-1_66)
link_directories(C:\\boost\\lib)
add_definitions(-DBOOST_ERROR_CODE_HEADER_ONLY)
link_libraries(ws2_32 wsock32)

