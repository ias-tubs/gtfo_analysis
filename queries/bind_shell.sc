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


// REDIRECT STDIN AND STDOUT TO SOCKFD
/* PROGRAM */ val sockFd = newConnectionSockFd 

// DUP
/* COMMON */ val dupCalls = cpg.call("dup(2|3)").l

/* PROGRAM */ val dupCallsWithFlow = getCallsWithArgReachableBy(dupCalls, sockFd)
/* PROGRAM */ dupCallsWithFlow.length
/* PROGRAM */ printCallsWithArgReachableBy(dupCalls, sockFd)

/* PROGRAM */ val redirect = dupCallsWithFlow.length > 0


// SHELL PROGRAM
/* COMMON */ val shellProgram = cpg.literal("\"/bin/(ba|da|z)*sh\"").l

// EXECUTE PROGRAM
/* PROGRAM */ val program = source ::: shellProgram

// EXECUTE FILE DESCRIPTOR
// OPEN
/* COMMON */ val openCalls = cpg.call("open").l

/* PROGRAM */ val openCallWithFlow = getCallsWithArgIdxEqReachableBy(openCalls, 1, program)
/* PROGRAM */ openCallWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(openCalls, 1, program)

// FILE DESCRIPTOR
/* PROGRAM */ val fileDesc = getCallReturnValueOfType(openCallWithFlow, "int").l
/* PROGRAM */ fileDesc.length

// FEXECVE
/* COMMON */ val fexecveCalls = cpg.call("fexecve").l

/* PROGRAM */ val fexecveCallsWithFlow = getCallsWithArgIdxEqReachableBy(fexecveCalls, 1, program)
/* PROGRAM */ fexecveCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fexecveCalls, 1, program)

/* PROGRAM */ val executeFileDescFlows = fexecveCallsWithFlow
/* PROGRAM */ executeFileDescFlows.length

// EXECUTE FILE (PATH)
// EXEC FUNCTIONS
/* COMMON */ val execCalls = cpg.call("exec(l|v)(e|p)?").l
/* PROGRAM */ val execCallsWithFlow = getCallsWithArgIdxEqReachableBy(execCalls, 1, program)
/* PROGRAM */ execCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(execCalls, 1, program)

// SYSTEM
/* COMMON */ val systemCalls = cpg.call("system").l
/* PROGRAM */ val systemCallsWithFlow = getCallsWithArgIdxEqReachableBy(systemCalls, 1, program)
/* PROGRAM */ systemCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(systemCalls, 1, program)

// POPEN
/* COMMON */ val popenCalls = cpg.call("popen").l
/* PROGRAM */ val popenCallsWithFlow = getCallsWithArgIdxEqReachableBy(popenCalls, 1, program)
/* PROGRAM */ popenCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(popenCalls, 1, program)

/* PROGRAM */ val executeFileFlows = execCallsWithFlow ::: systemCallsWithFlow ::: popenCallsWithFlow
/* PROGRAM */ executeFileFlows.length

// EXECUTE FLOW
/* PROGRAM */ val executeFlows = executeFileDescFlows ::: executeFileFlows
/* PROGRAM */ executeFlows.length

// RESULT FLOW
/* PROGRAM */ val resultFlows = if (redirect) executeFlows.l else List()
/* PROGRAM */ resultFlows.length