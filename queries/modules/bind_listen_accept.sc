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