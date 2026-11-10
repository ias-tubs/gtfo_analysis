// FUNCTIONS
/* COMMON */ val getCallsWithArgIdxEqReachableBy = (calls: List[Call], argIndex: Int, source: List[StoredNode]) => calls.where(call => call.argument.filter(_.argumentIndex == argIndex).where(_.reachableBy(source))).l
/* COMMON */ val printCallsWithArgIdxEqReachableBy = (calls: List[Call], argIndex: Int, source: List[StoredNode]) => calls.argument.filter(_.argumentIndex == argIndex).reachableByFlows(source).p

/* COMMON */ val getCallsWithArgIdxEqOrGrReachableBy = (calls: List[Call], argIndex: Int, source: List[StoredNode]) => calls.where(call => call.argument.filter(_.argumentIndex >= argIndex).where(_.reachableBy(source))).l
/* COMMON */ val printCallsWithArgIdxEqOrGrReachableBy = (calls: List[Call], argIndex: Int, source: List[StoredNode]) => calls.argument.filter(_.argumentIndex >= argIndex).reachableByFlows(source).p

/* COMMON */ val getCallsWithArgIdxEqOrLoReachableBy = (calls: List[Call], argIndex: Int, source: List[StoredNode]) => calls.where(call => call.argument.filter(_.argumentIndex <= argIndex).where(_.reachableBy(source))).l
/* COMMON */ val printCallsWithArgIdxEqOrLoReachableBy = (calls: List[Call], argIndex: Int, source: List[StoredNode]) => calls.argument.filter(_.argumentIndex <= argIndex).reachableByFlows(source).p

/* COMMON */ val getCallsWithArgReachableBy = (calls: List[Call], source: List[StoredNode]) => calls.where(call => call.argument.where(_.reachableBy(source))).l
/* COMMON */ val printCallsWithArgReachableBy = (calls: List[Call], source: List[StoredNode]) => calls.argument.reachableByFlows(source).p

/* COMMON */ val getParentAssignmentCallAst = (calls: List[Call]) => calls.repeat(_.astParent)(_.maxDepth(2).until(_.isCallTo("<operator>.assignment"))).l
/* COMMON */ val getParentAssignmentCallCfg = (calls: List[Call]) => calls.repeat(_.cfgNext)(_.maxDepth(5).until(_.isCallTo("<operator>.assignment"))).l
/* COMMON */ val getCallReturnValueOfType = (calls: List[Call], returnType: String) => (getParentAssignmentCallAst(calls) ::: getParentAssignmentCallCfg(calls)).dedup.isCall.argument.filter(_.argumentIndex == 1).isIdentifier.filter(_.typeFullName == returnType).l

/* COMMON */ val getCallArgWithIdxEq = (calls: List[Call], argIndex: Int) => calls.argument.filter(_.argumentIndex == argIndex).l 
/* COMMON */ val getCallArgWithIdxEqOrGr = (calls: List[Call], argIndex: Int) => calls.argument.filter(_.argumentIndex >= argIndex).l 


// SOURCE
// ARGV
/* COMMON */ val argv = cpg.method("main").parameter.order(2).l
/* COMMON */ val getArgvInProgram = (progPath: String) => argv.filter(_.file.name.contains(progPath))

/* PROGRAM */ val argvInProgram = getArgvInProgram(progPath)
/* PROGRAM */ argvInProgram.l.length

// GETOPT
/* COMMON */ val getoptCalls = cpg.call("getopt(_long)?").l
/* PROGRAM */ val getoptCallsWithFlow = getCallsWithArgIdxEqReachableBy(getoptCalls, 2, argvInProgram)
/* PROGRAM */ getoptCallsWithFlow.length

/* PROGRAM */ val optarg = if (getoptCallsWithFlow.length > 0) cpg.identifier("optarg").filter(_.file.name.contains(progPath)).l else List()
/* PROGRAM */ optarg.length

// SOURCE
/* PROGRAM */ val source = argvInProgram ::: optarg
/* PROGRAM */ source.length


// SOCKADDR
/* PROGRAM */ val address = source

// GETADDRINFO
/* COMMON */ val getaddrinfoCalls = cpg.call("getaddrinfo").l

/* PROGRAM */ val getaddrinfoCallsWithFlow = getCallsWithArgIdxEqOrLoReachableBy(getaddrinfoCalls, 2, address)
/* PROGRAM */ getaddrinfoCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqOrLoReachableBy(getaddrinfoCalls, 2, address)

/* PROGRAM */ val ipInAddrInfoStruct = getCallArgWithIdxEq(getaddrinfoCallsWithFlow, 4)
/* PROGRAM */ val ipInAddrInfoStructArg = ipInAddrInfoStruct.isCall.argument.l

/* PROGRAM */ val sockAddr = address ::: ipInAddrInfoStruct ::: ipInAddrInfoStructArg
/* PROGRAM */ sockAddr.length


// CONNECT

// CONNECT
/* COMMON */ val connectCalls = cpg.call("connect").l

/* PROGRAM */ val connectCallsWithFlow = getCallsWithArgIdxEqReachableBy(connectCalls, 2, sockAddr)
/* PROGRAM */ connectCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(connectCalls, 2, sockAddr)

/* PROGRAM */ val connectSockFd = getCallArgWithIdxEq(connectCallsWithFlow, 1)
/* PROGRAM */ connectSockFd.length


// BIND LISTEN ACCEPT

// BIND
/* COMMON */ val bindCalls = cpg.call("bind").l

/* PROGRAM */ val bindCallsWithFlow = getCallsWithArgIdxEqReachableBy(bindCalls, 2, sockAddr)
/* PROGRAM */ bindCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(bindCalls, 2, sockAddr)

/* PROGRAM */ val bindSockFd = getCallArgWithIdxEq(bindCallsWithFlow, 1)
/* PROGRAM */ bindSockFd.length

// LISTEN
/* COMMON */ val listenCalls = cpg.call("listen").l

/* PROGRAM */ val listenCallsWithFlow = getCallsWithArgIdxEqReachableBy(listenCalls, 1, bindSockFd)
/* PROGRAM */ listenCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(listenCalls, 1, bindSockFd)

// ACCEPT
/* COMMON */ val acceptCalls = cpg.call("accept").l

/* PROGRAM */ val acceptCallsWithFlow = getCallsWithArgIdxEqReachableBy(acceptCalls, 1, bindSockFd)
/* PROGRAM */ acceptCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(acceptCalls, 1, bindSockFd)

/* PROGRAM */ val acceptSockFd = getCallReturnValueOfType(acceptCallsWithFlow, "int").l
/* PROGRAM */ acceptSockFd.length

/* PROGRAM */ val newConnectionSockFd =  if (listenCallsWithFlow.length > 0) acceptSockFd else List()
/* PROGRAM */ newConnectionSockFd.length

// READ (FROM FILE OR STDIN)

// READ
/* COMMON */ val readCalls = cpg.call("read").l

/* PROGRAM */ val readCallsFileContent = getCallArgWithIdxEq(readCalls, 2)
/* PROGRAM */ readCallsFileContent.length


// FREAD
/* COMMON */ val freadCalls = cpg.call("fread").l

/* PROGRAM */ val freadCallsFileContent = getCallArgWithIdxEq(freadCalls, 1)
/* PROGRAM */ freadCallsFileContent.length


// VFSCANF
/* COMMON */ val vfscanfCalls = cpg.call("vfscanf").l

/* PROGRAM */ val vfscanfCallsFileContent = getCallArgWithIdxEq(vfscanfCalls, 3)
/* PROGRAM */ vfscanfCallsFileContent.length


// FSCANF
/* COMMON */ val fscanfCalls = cpg.call("fscanf").l

/* PROGRAM */ val fscanfCallsFileContent = getCallArgWithIdxEqOrGr(fscanfCalls, 3)
/* PROGRAM */ fscanfCallsFileContent.length


// FGETS
/* COMMON */ val fgetsCalls = cpg.call("fgets").l

/* PROGRAM */ val fgetsCallsFileContent = getCallArgWithIdxEqOrGr(fgetsCalls, 1)
/* PROGRAM */ fgetsCallsFileContent.length


// GETLINE
/* COMMON */ val getlineCalls = cpg.call("getline").l

/* PROGRAM */ val getlineCallsFileContent = getCallArgWithIdxEqOrGr(getlineCalls, 1)
/* PROGRAM */ getlineCallsFileContent.length


// GETDELIM
/* COMMON */ val getdelimCalls = cpg.call("getdelim").l

/* PROGRAM */ val getdelimCallsFileContent = getCallArgWithIdxEqOrGr(getdelimCalls, 1)
/* PROGRAM */ getdelimCallsFileContent.length


// GETW
/* COMMON */ val getwCalls = cpg.call("getw").l

/* PROGRAM */ val getwCallsFileContent = getCallReturnValueOfType(getwCalls, "int")
/* PROGRAM */ getwCallsFileContent.length


// FGETC
/* COMMON */ val fgetcCalls = cpg.call("fgetc").l

/* PROGRAM */ val fgetcCallsFileContent = getCallReturnValueOfType(fgetcCalls, "int")
/* PROGRAM */ fgetcCallsFileContent.length


// GETC
/* COMMON */ val getcCalls = cpg.call("getc").l

/* PROGRAM */ val getcCallsFileContent = getCallReturnValueOfType(getcCalls, "int")
/* PROGRAM */ getcCallsFileContent.length


// FUNCTIONS ALWAYS READING FROM STDIN
// SCANF
/* COMMON */ val scanfCalls = cpg.call("scanf").l

/* PROGRAM */ val scanfCallsFileContent = getCallArgWithIdxEqOrGr(scanfCalls, 2)
/* PROGRAM */ scanfCallsFileContent.length

// GETS
/* COMMON */ val getsCalls = cpg.call("gets").l

/* PROGRAM */ val getsCallsFileContent = getCallArgWithIdxEq(getsCalls, 1)
/* PROGRAM */ getsCallsFileContent.length

// VSCANF
/* COMMON */ val vscanfCalls = cpg.call("vscanf").l

/* PROGRAM */ val vscanfCallsFileContent = getCallArgWithIdxEq(vscanfCalls, 2)
/* PROGRAM */ vscanfCallsFileContent.length

// GETCHAR
/* COMMON */ val getcharCalls = cpg.call("getchar").l

/* PROGRAM */ val getcharCallsFileContent = getCallReturnValueOfType(getcharCalls, "int")
/* PROGRAM */ getcharCallsFileContent.length

// READ CONTENT
/* PROGRAM */ val readContent = readCallsFileContent ::: freadCallsFileContent ::: vfscanfCallsFileContent ::: fscanfCallsFileContent ::: fgetsCallsFileContent ::: getlineCallsFileContent ::: getdelimCallsFileContent ::: getwCallsFileContent ::: fgetcCallsFileContent ::: getcCallsFileContent ::: scanfCallsFileContent ::: getsCallsFileContent ::: vscanfCallsFileContent ::: getcharCallsFileContent
/* PROGRAM */ readContent.length

// SEND WITH CONNECTION

// CONTENT TO SEND
/* PROGRAM */ val sendContent = readContent ::: source
/* PROGRAM */ sendContent.length

// SOCKFD TO SEND OVER
/* PROGRAM */ val sendConnectionSockFd = connectSockFd ::: newConnectionSockFd
/* PROGRAM */ sendConnectionSockFd.length

// SEND
/* COMMON */ val sendCalls = cpg.call("send").l

/* PROGRAM */ val sendCallsToSockFd = getCallsWithArgIdxEqReachableBy(sendCalls, 1, sendConnectionSockFd)
/* PROGRAM */ sendCallsToSockFd.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(sendCalls, 1, sendConnectionSockFd)

/* PROGRAM */ val sendCallsToSockFdWithFlow = getCallsWithArgIdxEqReachableBy(sendCallsToSockFd, 2, sendContent)
/* PROGRAM */ sendCallsToSockFdWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(sendCallsToSockFd, 2, sendContent)

// WRITE
/* COMMON */ val writeCalls = cpg.call("write").l

/* PROGRAM */ val writeCallsToSockFd = getCallsWithArgIdxEqReachableBy(writeCalls, 1, sendConnectionSockFd)
/* PROGRAM */ writeCallsToSockFd.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(writeCalls, 1, sendConnectionSockFd)

/* PROGRAM */ val writeCallsToSockFdWithFlow = getCallsWithArgIdxEqReachableBy(writeCallsToSockFd, 2, sendContent)
/* PROGRAM */ writeCallsToSockFdWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(writeCallsToSockFd, 2, sendContent)

// SEND FLOW
/* PROGRAM */ val sendConnectionFlows =  sendCallsToSockFdWithFlow ::: writeCallsToSockFdWithFlow
/* PROGRAM */ sendConnectionFlows.length



// SEND WITHOUT CONNECTION

// ADDRESS TO SEND TO
/* PROGRAM */ val sendtoAddr = sockAddr
/* PROGRAM */ sendtoAddr.length

// SEND_TO
/* COMMON */ val sendToCalls = cpg.call("sendto").l

/* PROGRAM */ val sendToCallsToAddr = getCallsWithArgIdxEqReachableBy(sendToCalls, 5, sendtoAddr)
/* PROGRAM */ sendToCallsToAddr.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(sendToCalls, 5, sendtoAddr)

/* PROGRAM */ val sendToCallsToAddrWithFlow = getCallsWithArgIdxEqReachableBy(sendToCallsToAddr, 2, sendContent)
/* PROGRAM */ sendToCallsToAddrWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(sendToCallsToAddr, 2, sendContent)

// SEND FLOW
/* PROGRAM */ val sendNoConnectionFlows =  sendToCallsToAddrWithFlow
/* PROGRAM */ sendNoConnectionFlows.length

// SEND FLOW
/* PROGRAM */ val sendFlows =  sendConnectionFlows ::: sendNoConnectionFlows
/* PROGRAM */ sendFlows.length


/* PROGRAM */ val resultFlows =  sendFlows
/* PROGRAM */ resultFlows.length


