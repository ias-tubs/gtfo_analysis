// RECEIVE WITHOUT CONNECTION

// SOCKFD TO RECEIVE FROM
/* PROGRAM */ val receiveNoConnectionSockFd = sockFd
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