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


// RECEIVE WITH CONNECTION

// SOCKFD TO RECEIVE FROM
/* PROGRAM */ val receiveConnectionSockFd = connectSockFd ::: newConnectionSockFd
/* PROGRAM */ receiveConnectionSockFd.length


// RECEIVE
/* COMMON */ val recvCalls = cpg.call("recv").l

/* PROGRAM */ val recvCallsFromSockFd = getCallsWithArgIdxEqReachableBy(recvCalls, 1, receiveConnectionSockFd)
/* PROGRAM */ recvCallsFromSockFd.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(recvCalls, 1, receiveConnectionSockFd)

/* PROGRAM */ val recvCallsContent = getCallArgWithIdxEq(recvCallsFromSockFd, 2)
/* PROGRAM */ recvCallsContent.length

// READ
/* COMMON */ val readCalls = cpg.call("read").l

/* PROGRAM */ val readCallsFromSockFd = getCallsWithArgIdxEqReachableBy(readCalls, 1, receiveConnectionSockFd)
/* PROGRAM */ readCallsFromSockFd.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(readCalls, 1, receiveConnectionSockFd)

/* PROGRAM */ val readCallsContent = getCallArgWithIdxEq(readCallsFromSockFd, 2)
/* PROGRAM */ readCallsContent.length

// RECEIVED CONTENT
/* PROGRAM */ val receivedConnectionContent =  recvCallsContent ::: readCallsContent
/* PROGRAM */ receivedConnectionContent.length


// RECEIVE WITHOUT CONNECTION

// SOCKFD TO RECEIVE FROM
/* PROGRAM */ val receiveNoConnectionSockFd = bindSockFd
/* PROGRAM */ receiveNoConnectionSockFd.length


// RECVFROM
/* COMMON */ val recvfromCalls = cpg.call("recvfrom").l

/* PROGRAM */ val recvfromCallsFromSockFd = getCallsWithArgIdxEqReachableBy(recvfromCalls, 1, receiveNoConnectionSockFd)
/* PROGRAM */ recvfromCallsFromSockFd.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(recvfromCalls, 1, receiveNoConnectionSockFd)

/* PROGRAM */ val recvfromCallsContent = getCallArgWithIdxEq(recvfromCallsFromSockFd, 2)
/* PROGRAM */ recvfromCallsContent.length


// RECEIVED CONTENT
/* PROGRAM */ val receivedNoConnectionContent =  recvfromCallsContent
/* PROGRAM */ receivedNoConnectionContent.length

// RECEIVED CONTENT
/* PROGRAM */ val receivedContent =  receivedConnectionContent ::: receivedNoConnectionContent
/* PROGRAM */ receivedContent.length


// WRITE (FILE OR STDOUT)

/* PROGRAM */ val content = receivedContent

// WRITE
/* COMMON */ val writeCalls = cpg.call("write").l

/* PROGRAM */ val writeCallsWithFlow = getCallsWithArgIdxEqReachableBy(writeCalls, 2, content)
/* PROGRAM */ writeCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(writeCalls, 2, content)

// FWRITE
/* COMMON */ val fwriteCalls = cpg.call("fwrite").l

/* PROGRAM */ val fwriteCallsWithFlow = getCallsWithArgIdxEqReachableBy(fwriteCalls, 1, content)
/* PROGRAM */ fwriteCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fwriteCalls, 1, content)

// FPRINTF
/* COMMON */ val fprintfCalls = cpg.call("fprintf").l

/* PROGRAM */ val fprintfCallsWithFlow = getCallsWithArgIdxEqOrGrReachableBy(fprintfCalls, 3, content)
/* PROGRAM */ fprintfCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqOrGrReachableBy(fprintfCalls, 3, content)

// VFPRINTF
/* COMMON */ val vfprintfCalls = cpg.call("vfprintf").l

/* PROGRAM */ val vfprintfCallsWithFlow = getCallsWithArgIdxEqReachableBy(vfprintfCalls, 3, content)
/* PROGRAM */ vfprintfCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(vfprintfCalls, 3, content)

// FPUTC
/* COMMON */ val fputcCalls = cpg.call("fputc").l

/* PROGRAM */ val fputcCallsWithFlow = getCallsWithArgIdxEqReachableBy(fputcCalls, 1, content)
/* PROGRAM */ fputcCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fputcCalls, 1, content)

// FPUTS
/* COMMON */ val fputsCalls = cpg.call("fputs").l

/* PROGRAM */ val fputsCallsWithFlow = getCallsWithArgIdxEqReachableBy(fputsCalls, 1, content)
/* PROGRAM */ fputsCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fputsCalls, 1, content)

// PUTC
/* COMMON */ val putcCalls = cpg.call("putc").l

/* PROGRAM */ val putcCallsWithFlow = getCallsWithArgIdxEqReachableBy(putcCalls, 1, content)
/* PROGRAM */ putcCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(putcCalls, 1, content)

// PUTW
/* COMMON */ val putwCalls = cpg.call("putw").l

/* PROGRAM */ val putwCallsWithFlow = getCallsWithArgIdxEqReachableBy(putwCalls, 1, content)
/* PROGRAM */ putwCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(putwCalls, 1, content)

// FUNCTIONS ALWAYS WRITING TO STDOUT
// PRINTF
/* COMMON */ val printfCalls = cpg.call("printf").l

/* PROGRAM */ val printfCallsWithFlow = getCallsWithArgIdxEqOrGrReachableBy(printfCalls, 2, content)
/* PROGRAM */ printfCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqOrGrReachableBy(printfCalls, 2, content)

// VPRINTF
/* COMMON */ val vprintfCalls = cpg.call("vprintf").l

/* PROGRAM */ val vprintfCallsWithFlow = getCallsWithArgIdxEqReachableBy(vprintfCalls, 2, content)
/* PROGRAM */ vprintfCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(vprintfCalls, 2, content)

// PUTCHAR
/* COMMON */ val putcharCalls = cpg.call("putchar").l

/* PROGRAM */ val putcharCallsWithFlow = getCallsWithArgIdxEqReachableBy(putcharCalls, 1, content)
/* PROGRAM */putcharCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(putcharCalls, 1, content)

// PUTS
/* COMMON */ val putsCalls = cpg.call("puts").l

/* PROGRAM */ val putsCallsWithFlow = getCallsWithArgIdxEqReachableBy(putsCalls, 1, content)
/* PROGRAM */ putsCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(putsCalls, 1, content)


// WRITE FLOWS
/* PROGRAM */ val writeFlows = writeCallsWithFlow ::: fwriteCallsWithFlow ::: fprintfCallsWithFlow ::: vfprintfCallsWithFlow ::: fputcCallsWithFlow ::: fputsCallsWithFlow ::: putcCallsWithFlow ::: putwCallsWithFlow ::: printfCallsWithFlow ::: vprintfCallsWithFlow ::: putcharCallsWithFlow ::: putsCallsWithFlow
/* PROGRAM */ writeFlows.length



/* PROGRAM */ val resultFlows =  writeFlows
/* PROGRAM */ resultFlows.length


