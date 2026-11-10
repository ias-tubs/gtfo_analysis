// RECEIVE WITH CONNECTION

// SOCKFD TO RECEIVE FROM
/* PROGRAM */ val receiveConnectionSockFd = sockFd
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