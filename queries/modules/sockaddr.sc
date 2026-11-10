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