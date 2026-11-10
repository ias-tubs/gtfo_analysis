// FUNCTIONS FOR CONTROL FLOW
/* COMMON */ val getCallsInCfgForCalls = (calls: List[Call]) => calls.repeat(_.cfgNext)(_.maxDepth(1000).emit(_.isCall)).isCall.l
/* COMMON */ val getCallsInCfgForMethods = (methods: List[Method]) => methods.repeat(_.cfgNext)(_.maxDepth(1000).emit(_.isCall)).isCall.l
/* COMMON */ val getMethodsForCalls = (calls: List[Call]) => calls.map(call => cpg.method(call.name).l(0)).filterNot(_.isExternal).l

/* COMMON */ def processCalls(reachableCalls: List[Call], secondCalls: List[Call]): List[Call] = { val reachableCallsFiltered = reachableCalls.filter(call => secondCalls.contains(call)); val methods = getMethodsForCalls(reachableCalls); if (methods.nonEmpty) { val new_calls = getCallsInCfgForMethods(methods); reachableCallsFiltered ::: processCalls(new_calls, secondCalls) } else { reachableCallsFiltered } }
/* COMMON */ def getCallsAfterCalls(firstCalls: List[Call], secondCalls: List[Call]) = { val firstCallsAndCallSites = firstCalls ::: firstCalls.method.repeat(_.caller)(_.emit(_.isMethod)).callIn.l; val reachableCalls = getCallsInCfgForCalls(firstCallsAndCallSites); processCalls(reachableCalls, secondCalls) }


// Same as above but formatted

// if reachableCalls are methods defined in source code, go through their CFG also until no internal methods remain
// filter reachable Calls for the given secondCalls
// e.g. reachableCalls - methods in CFG after freopen, secondCalls - calls to fgets
def processCalls(reachableCalls: List[Call], secondCalls: List[Call]): List[Call] = {
    val reachableCallsFiltered = reachableCalls.filter(call => secondCalls.contains(call))
    val methods = getMethodsForCalls(reachableCalls)
    if (methods.nonEmpty) {
        val new_calls = getCallsInCfgForMethods(methods)
        reachableCallsFiltered ::: processCalls(new_calls, secondCalls)
    } else {
        reachableCallsFiltered
    }
}

def getCallsAfterCalls(firstCalls: List[Call], secondCalls: List[Call]) = {
    // e.g. freopen and all calls to methods which call freopen (should be repeated until main)
    val firstCallsAndCallSites = firstCalls ::: firstCalls.method.repeat(_.caller)(_.emit(_.isMethod)).callIn.l
    // all calls in CFG
    val reachableCalls = getCallsInCfgForCalls(firstCallsAndCallSites)

    processCalls(reachableCalls, secondCalls)
}// FUNCTIONS FOR CONTROL FLOW
/* COMMON */ val getCallsInCfgForCalls = (calls: List[Call]) => calls.repeat(_.cfgNext)(_.maxDepth(1000).emit(_.isCall)).isCall.l
/* COMMON */ val getCallsInCfgForMethods = (methods: List[Method]) => methods.repeat(_.cfgNext)(_.maxDepth(1000).emit(_.isCall)).isCall.l
/* COMMON */ val getMethodsForCalls = (calls: List[Call]) => calls.map(call => cpg.method(call.name).l(0)).filterNot(_.isExternal).l

/* COMMON */ def processCalls(reachableCalls: List[Call], secondCalls: List[Call]): List[Call] = { val reachableCallsFiltered = reachableCalls.filter(call => secondCalls.contains(call)); val methods = getMethodsForCalls(reachableCalls); if (methods.nonEmpty) { val new_calls = getCallsInCfgForMethods(methods); reachableCallsFiltered ::: processCalls(new_calls, secondCalls) } else { reachableCallsFiltered } }
/* COMMON */ def getCallsAfterCalls(firstCalls: List[Call], secondCalls: List[Call]) = { val firstCallsAndCallSites = firstCalls ::: firstCalls.method.repeat(_.caller)(_.emit(_.isMethod)).callIn.l; val reachableCalls = getCallsInCfgForCalls(firstCallsAndCallSites); processCalls(reachableCalls, secondCalls) }


// Same as above but formatted

// if reachableCalls are methods defined in source code, go through their CFG also until no internal methods remain
// filter reachable Calls for the given secondCalls
// e.g. reachableCalls - methods in CFG after freopen, secondCalls - calls to fgets
def processCalls(reachableCalls: List[Call], secondCalls: List[Call]): List[Call] = {
    val reachableCallsFiltered = reachableCalls.filter(call => secondCalls.contains(call))
    val methods = getMethodsForCalls(reachableCalls)
    if (methods.nonEmpty) {
        val new_calls = getCallsInCfgForMethods(methods)
        reachableCallsFiltered ::: processCalls(new_calls, secondCalls)
    } else {
        reachableCallsFiltered
    }
}

def getCallsAfterCalls(firstCalls: List[Call], secondCalls: List[Call]) = {
    // e.g. freopen and all calls to methods which call freopen (should be repeated until main)
    val firstCallsAndCallSites = firstCalls ::: firstCalls.method.repeat(_.caller)(_.emit(_.isMethod)).callIn.l
    // all calls in CFG
    val reachableCalls = getCallsInCfgForCalls(firstCallsAndCallSites)

    processCalls(reachableCalls, secondCalls)
}