// EXECUTE PROGRAM
/* PROGRAM */ val program = source

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