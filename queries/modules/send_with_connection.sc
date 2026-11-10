// SEND WITH CONNECTION

// CONTENT TO SEND
/* PROGRAM */ val sendContent = content
/* PROGRAM */ sendContent.length

// SOCKFD TO SEND OVER
/* PROGRAM */ val sendConnectionSockFd = sockFd
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
