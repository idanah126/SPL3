# CMAKE generated file: DO NOT EDIT!
# Generated by "NMake Makefiles" Generator, CMake Version 3.20

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:

#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:

.SUFFIXES: .hpux_make_needs_suffix_list

# Command-line flag to silence nested $(MAKE).
$(VERBOSE)MAKESILENT = -s

#Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:
.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

!IF "$(OS)" == "Windows_NT"
NULL=
!ELSE
NULL=nul
!ENDIF
SHELL = cmd.exe

# The CMake executable.
CMAKE_COMMAND = "C:\Program Files\JetBrains\CLion 2021.1.1\bin\cmake\win\bin\cmake.exe"

# The command to remove a file.
RM = "C:\Program Files\JetBrains\CLion 2021.1.1\bin\cmake\win\bin\cmake.exe" -E rm -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = C:\Users\idana\IdeaProjects\SPL3\Boost_Echo_Client

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = C:\Users\idana\IdeaProjects\SPL3\Boost_Echo_Client\cmake-build-debug

# Include any dependencies generated for this target.
include CMakeFiles\Boost_Echo_Client.dir\depend.make
# Include the progress variables for this target.
include CMakeFiles\Boost_Echo_Client.dir\progress.make

# Include the compile flags for this target's objects.
include CMakeFiles\Boost_Echo_Client.dir\flags.make

CMakeFiles\Boost_Echo_Client.dir\src\main.cpp.obj: CMakeFiles\Boost_Echo_Client.dir\flags.make
CMakeFiles\Boost_Echo_Client.dir\src\main.cpp.obj: ..\src\main.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=C:\Users\idana\IdeaProjects\SPL3\Boost_Echo_Client\cmake-build-debug\CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building CXX object CMakeFiles/Boost_Echo_Client.dir/src/main.cpp.obj"
	C:\PROGRA~2\MICROS~2\2019\COMMUN~1\VC\Tools\MSVC\1429~1.301\bin\Hostx86\x86\cl.exe @<<
 /nologo /TP $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) /FoCMakeFiles\Boost_Echo_Client.dir\src\main.cpp.obj /FdCMakeFiles\Boost_Echo_Client.dir\ /FS -c C:\Users\idana\IdeaProjects\SPL3\Boost_Echo_Client\src\main.cpp
<<

CMakeFiles\Boost_Echo_Client.dir\src\main.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/Boost_Echo_Client.dir/src/main.cpp.i"
	C:\PROGRA~2\MICROS~2\2019\COMMUN~1\VC\Tools\MSVC\1429~1.301\bin\Hostx86\x86\cl.exe > CMakeFiles\Boost_Echo_Client.dir\src\main.cpp.i @<<
 /nologo /TP $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E C:\Users\idana\IdeaProjects\SPL3\Boost_Echo_Client\src\main.cpp
<<

CMakeFiles\Boost_Echo_Client.dir\src\main.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/Boost_Echo_Client.dir/src/main.cpp.s"
	C:\PROGRA~2\MICROS~2\2019\COMMUN~1\VC\Tools\MSVC\1429~1.301\bin\Hostx86\x86\cl.exe @<<
 /nologo /TP $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) /FoNUL /FAs /FaCMakeFiles\Boost_Echo_Client.dir\src\main.cpp.s /c C:\Users\idana\IdeaProjects\SPL3\Boost_Echo_Client\src\main.cpp
<<

CMakeFiles\Boost_Echo_Client.dir\src\connectionHandler.cpp.obj: CMakeFiles\Boost_Echo_Client.dir\flags.make
CMakeFiles\Boost_Echo_Client.dir\src\connectionHandler.cpp.obj: ..\src\connectionHandler.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=C:\Users\idana\IdeaProjects\SPL3\Boost_Echo_Client\cmake-build-debug\CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Building CXX object CMakeFiles/Boost_Echo_Client.dir/src/connectionHandler.cpp.obj"
	C:\PROGRA~2\MICROS~2\2019\COMMUN~1\VC\Tools\MSVC\1429~1.301\bin\Hostx86\x86\cl.exe @<<
 /nologo /TP $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) /FoCMakeFiles\Boost_Echo_Client.dir\src\connectionHandler.cpp.obj /FdCMakeFiles\Boost_Echo_Client.dir\ /FS -c C:\Users\idana\IdeaProjects\SPL3\Boost_Echo_Client\src\connectionHandler.cpp
<<

CMakeFiles\Boost_Echo_Client.dir\src\connectionHandler.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/Boost_Echo_Client.dir/src/connectionHandler.cpp.i"
	C:\PROGRA~2\MICROS~2\2019\COMMUN~1\VC\Tools\MSVC\1429~1.301\bin\Hostx86\x86\cl.exe > CMakeFiles\Boost_Echo_Client.dir\src\connectionHandler.cpp.i @<<
 /nologo /TP $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E C:\Users\idana\IdeaProjects\SPL3\Boost_Echo_Client\src\connectionHandler.cpp
<<

CMakeFiles\Boost_Echo_Client.dir\src\connectionHandler.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/Boost_Echo_Client.dir/src/connectionHandler.cpp.s"
	C:\PROGRA~2\MICROS~2\2019\COMMUN~1\VC\Tools\MSVC\1429~1.301\bin\Hostx86\x86\cl.exe @<<
 /nologo /TP $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) /FoNUL /FAs /FaCMakeFiles\Boost_Echo_Client.dir\src\connectionHandler.cpp.s /c C:\Users\idana\IdeaProjects\SPL3\Boost_Echo_Client\src\connectionHandler.cpp
<<

CMakeFiles\Boost_Echo_Client.dir\src\echoClient.cpp.obj: CMakeFiles\Boost_Echo_Client.dir\flags.make
CMakeFiles\Boost_Echo_Client.dir\src\echoClient.cpp.obj: ..\src\echoClient.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=C:\Users\idana\IdeaProjects\SPL3\Boost_Echo_Client\cmake-build-debug\CMakeFiles --progress-num=$(CMAKE_PROGRESS_3) "Building CXX object CMakeFiles/Boost_Echo_Client.dir/src/echoClient.cpp.obj"
	C:\PROGRA~2\MICROS~2\2019\COMMUN~1\VC\Tools\MSVC\1429~1.301\bin\Hostx86\x86\cl.exe @<<
 /nologo /TP $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) /FoCMakeFiles\Boost_Echo_Client.dir\src\echoClient.cpp.obj /FdCMakeFiles\Boost_Echo_Client.dir\ /FS -c C:\Users\idana\IdeaProjects\SPL3\Boost_Echo_Client\src\echoClient.cpp
<<

CMakeFiles\Boost_Echo_Client.dir\src\echoClient.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/Boost_Echo_Client.dir/src/echoClient.cpp.i"
	C:\PROGRA~2\MICROS~2\2019\COMMUN~1\VC\Tools\MSVC\1429~1.301\bin\Hostx86\x86\cl.exe > CMakeFiles\Boost_Echo_Client.dir\src\echoClient.cpp.i @<<
 /nologo /TP $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E C:\Users\idana\IdeaProjects\SPL3\Boost_Echo_Client\src\echoClient.cpp
<<

CMakeFiles\Boost_Echo_Client.dir\src\echoClient.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/Boost_Echo_Client.dir/src/echoClient.cpp.s"
	C:\PROGRA~2\MICROS~2\2019\COMMUN~1\VC\Tools\MSVC\1429~1.301\bin\Hostx86\x86\cl.exe @<<
 /nologo /TP $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) /FoNUL /FAs /FaCMakeFiles\Boost_Echo_Client.dir\src\echoClient.cpp.s /c C:\Users\idana\IdeaProjects\SPL3\Boost_Echo_Client\src\echoClient.cpp
<<

# Object files for target Boost_Echo_Client
Boost_Echo_Client_OBJECTS = \
"CMakeFiles\Boost_Echo_Client.dir\src\main.cpp.obj" \
"CMakeFiles\Boost_Echo_Client.dir\src\connectionHandler.cpp.obj" \
"CMakeFiles\Boost_Echo_Client.dir\src\echoClient.cpp.obj"

# External object files for target Boost_Echo_Client
Boost_Echo_Client_EXTERNAL_OBJECTS =

Boost_Echo_Client.exe: CMakeFiles\Boost_Echo_Client.dir\src\main.cpp.obj
Boost_Echo_Client.exe: CMakeFiles\Boost_Echo_Client.dir\src\connectionHandler.cpp.obj
Boost_Echo_Client.exe: CMakeFiles\Boost_Echo_Client.dir\src\echoClient.cpp.obj
Boost_Echo_Client.exe: CMakeFiles\Boost_Echo_Client.dir\build.make
Boost_Echo_Client.exe: CMakeFiles\Boost_Echo_Client.dir\objects1.rsp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=C:\Users\idana\IdeaProjects\SPL3\Boost_Echo_Client\cmake-build-debug\CMakeFiles --progress-num=$(CMAKE_PROGRESS_4) "Linking CXX executable Boost_Echo_Client.exe"
	"C:\Program Files\JetBrains\CLion 2021.1.1\bin\cmake\win\bin\cmake.exe" -E vs_link_exe --intdir=CMakeFiles\Boost_Echo_Client.dir --rc=C:\PROGRA~2\WI3CF2~1\10\bin\100190~1.0\x86\rc.exe --mt=C:\PROGRA~2\WI3CF2~1\10\bin\100190~1.0\x86\mt.exe --manifests -- C:\PROGRA~2\MICROS~2\2019\COMMUN~1\VC\Tools\MSVC\1429~1.301\bin\Hostx86\x86\link.exe /nologo @CMakeFiles\Boost_Echo_Client.dir\objects1.rsp @<<
 /out:Boost_Echo_Client.exe /implib:Boost_Echo_Client.lib /pdb:C:\Users\idana\IdeaProjects\SPL3\Boost_Echo_Client\cmake-build-debug\Boost_Echo_Client.pdb /version:0.0 /machine:X86 /debug /INCREMENTAL /subsystem:console  kernel32.lib user32.lib gdi32.lib winspool.lib shell32.lib ole32.lib oleaut32.lib uuid.lib comdlg32.lib advapi32.lib 
<<

# Rule to build all files generated by this target.
CMakeFiles\Boost_Echo_Client.dir\build: Boost_Echo_Client.exe
.PHONY : CMakeFiles\Boost_Echo_Client.dir\build

CMakeFiles\Boost_Echo_Client.dir\clean:
	$(CMAKE_COMMAND) -P CMakeFiles\Boost_Echo_Client.dir\cmake_clean.cmake
.PHONY : CMakeFiles\Boost_Echo_Client.dir\clean

CMakeFiles\Boost_Echo_Client.dir\depend:
	$(CMAKE_COMMAND) -E cmake_depends "NMake Makefiles" C:\Users\idana\IdeaProjects\SPL3\Boost_Echo_Client C:\Users\idana\IdeaProjects\SPL3\Boost_Echo_Client C:\Users\idana\IdeaProjects\SPL3\Boost_Echo_Client\cmake-build-debug C:\Users\idana\IdeaProjects\SPL3\Boost_Echo_Client\cmake-build-debug C:\Users\idana\IdeaProjects\SPL3\Boost_Echo_Client\cmake-build-debug\CMakeFiles\Boost_Echo_Client.dir\DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles\Boost_Echo_Client.dir\depend

