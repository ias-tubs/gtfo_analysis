// SEND WITHOUT CONNECTION

// CONTENT TO SEND
/* PROGRAM */ val sendContent = content
/* PROGRAM */ sendContent.length

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
