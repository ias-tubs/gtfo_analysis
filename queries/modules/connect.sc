// CONNECT

// CONNECT
/* COMMON */ val connectCalls = cpg.call("connect").l

/* PROGRAM */ val connectCallsWithFlow = getCallsWithArgIdxEqReachableBy(connectCalls, 2, sockAddr)
/* PROGRAM */ connectCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(connectCalls, 2, sockAddr)

/* PROGRAM */ val connectSockFd = getCallArgWithIdxEq(connectCallsWithFlow, 1)
/* PROGRAM */ connectSockFd.length
