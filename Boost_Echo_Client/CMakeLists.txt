cmake_minimum_required(VERSION 3.20)
project(Boost_Echo_Client)

set(CMAKE_CXX_STANDARD 11)

add_executable(Boost_Echo_Client src/main.cpp src/connectionHandler.cpp src/echoClient.cpp include/connectionHandler.h)

find_package(Boost)
IF (Boost_FOUND)
    include_directories(${src})
endif()

set(Boost_INCLUDE_DIR C:/cygwin64/usr/include)

find_package(boost REQUIRED COMPONENTS system)
include_directories(${Boost_INCLUDE_DIRS})

target_link_libraries(${PROJECT_NAME} ${Boost_LIBRARIES})