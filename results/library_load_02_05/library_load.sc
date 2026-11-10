// FUNCTIONS
/* COMMON */ val getCallsWithArgIdxEqReachableBy = (calls: List[Call], argIndex: Int, source: List[StoredNode]) => calls.where(call => call.argument.filter(_.argumentIndex == argIndex).where(_.reachableBy(source))).l
/* COMMON */ val printCallsWithArgIdxEqReachableBy = (calls: List[Call], argIndex: Int, source: List[StoredNode]) => calls.argument.filter(_.argumentIndex == argIndex).reachableByFlows(source).p

/* COMMON */ val getCallsWithArgIdxEqOrGrReachableBy = (calls: List[Call], argIndex: Int, source: List[StoredNode]) => calls.where(call => call.argument.filter(_.argumentIndex >= argIndex).where(_.reachableBy(source))).l
/* COMMON */ val printCallsWithArgIdxEqOrGrReachableBy = (calls: List[Call], argIndex: Int, source: List[StoredNode]) => calls.argument.filter(_.argumentIndex >= argIndex).reachableByFlows(source).p

/* COMMON */ val getCallsWithArgReachableBy = (calls: List[Call], source: List[StoredNode]) => calls.where(call => call.argument.where(_.reachableBy(source))).l
/* COMMON */ val printCallsWithArgReachableBy = (calls: List[Call], source: List[StoredNode]) => calls.argument.reachableByFlows(source).p

/* COMMON */ val getParentAssignmentCallAst = (calls: List[Call]) => calls.repeat(_.astParent)(_.maxDepth(2).until(_.isCallTo("<operator>.assignment"))).l
/* COMMON */ val getParentAssignmentCallCfg = (calls: List[Call]) => calls.repeat(_.cfgNext)(_.maxDepth(5).until(_.isCallTo("<operator>.assignment"))).l
/* COMMON */ val getCallReturnValueOfType = (calls: List[Call], returnType: String) => (getParentAssignmentCallAst(calls) ::: getParentAssignmentCallCfg(calls)).dedup.isCall.argument.filter(_.argumentIndex == 1).isIdentifier.filter(_.typeFullName == returnType).l

/* COMMON */ val getCallArgWithIdxEq = (calls: List[Call], argIndex: Int) => calls.argument.filter(_.argumentIndex == argIndex).l 
/* COMMON */ val getCallArgWithIdxEqOrGr = (calls: List[Call], argIndex: Int) => calls.argument.filter(_.argumentIndex >= argIndex).l 


// SOURCE
// ARGV
/* COMMON */ val argv = cpg.method("main").parameter.order(2).l
/* COMMON */ val getArgvInProgram = (progPath: String) => argv.filter(_.file.name.contains(progPath))

/* PROGRAM */ val argvInProgram = getArgvInProgram(progPath)
/* PROGRAM */ argvInProgram.l.length

// GETOPT
/* COMMON */ val getoptCalls = cpg.call("getopt(_long)?").l
/* PROGRAM */ val getoptCallsWithFlow = getCallsWithArgIdxEqReachableBy(getoptCalls, 2, argvInProgram)
/* PROGRAM */ getoptCallsWithFlow.length

/* PROGRAM */ val optarg = if (getoptCallsWithFlow.length > 0) cpg.identifier("optarg").filter(_.file.name.contains(progPath)).l else List()
/* PROGRAM */ optarg.length

// SOURCE
/* PROGRAM */ val source = argvInProgram ::: optarg
/* PROGRAM */ source.length


// DLOPEN
/* COMMON */ val dlopenCalls = cpg.call("dlopen").l
/* PROGRAM */ val dlopenCallsWithFlow =  getCallsWithArgIdxEqReachableBy(dlopenCalls, 1, source)
/* PROGRAM */ dlopenCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(dlopenCalls, 1, source)

/* PROGRAM */ val resultFlows = dlopenCallsWithFlow
/* PROGRAM */ resultFlows.length