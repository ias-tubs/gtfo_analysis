// REDIRECT STDIN AND STDOUT TO SOCKFD
/* PROGRAM */ val sockFd = newConnectionSockFd 

// DUP
/* COMMON */ val dupCalls = cpg.call("dup(2|3)").l

/* PROGRAM */ val dupCallsWithFlow = getCallsWithArgReachableBy(dupCalls, sockFd)
/* PROGRAM */ dupCallsWithFlow.length
/* PROGRAM */ printCallsWithArgReachableBy(dupCalls, sockFd)

/* PROGRAM */ val redirect = dupCallsWithFlow.length > 0