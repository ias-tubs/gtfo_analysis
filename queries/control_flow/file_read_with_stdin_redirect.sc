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

// FUNCTIONS FOR CONTROL FLOW
/* COMMON */ val getCallsInCfgForCalls = (calls: List[Call]) => calls.repeat(_.cfgNext)(_.maxDepth(1000).emit(_.isCall)).isCall.l
/* COMMON */ val getCallsInCfgForMethods = (methods: List[Method]) => methods.repeat(_.cfgNext)(_.maxDepth(1000).emit(_.isCall)).isCall.l
/* COMMON */ val getMethodsForCalls = (calls: List[Call]) => calls.map(call => cpg.method(call.name).l(0)).filterNot(_.isExternal).l

/* COMMON */ def processCalls(reachableCalls: List[Call], secondCalls: List[Call]): List[Call] = { val reachableCallsFiltered = reachableCalls.filter(call => secondCalls.contains(call)); val methods = getMethodsForCalls(reachableCalls); if (methods.nonEmpty) { val new_calls = getCallsInCfgForMethods(methods); reachableCallsFiltered ::: processCalls(new_calls, secondCalls) } else { reachableCallsFiltered } }
/* COMMON */ def getCallsAfterCalls(firstCalls: List[Call], secondCalls: List[Call]) = { val firstCallsAndCallSites = firstCalls ::: firstCalls.method.repeat(_.caller)(_.emit(_.isMethod)).callIn.l; val reachableCalls = getCallsInCfgForCalls(firstCallsAndCallSites); processCalls(reachableCalls, secondCalls) }


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


// READ FROM FILE
/* PROGRAM */ val fileNameRead = source


// READ FROM FILE DESCRIPTOR
// OPEN
/* COMMON */ val openCalls = cpg.call("open").l

/* PROGRAM */ val openCallsWithFlow = getCallsWithArgIdxEqReachableBy(openCalls, 1, fileNameRead)
/* PROGRAM */ openCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(openCalls, 1, fileNameRead)

// FILE DESCRIPTOR
/* PROGRAM */ val fileDesc = getCallReturnValueOfType(openCallsWithFlow, "int").l
/* PROGRAM */ fileDesc.length

// READ
/* COMMON */ val readCalls = cpg.call("read").l

/* PROGRAM */ val readCallsWithFlow = getCallsWithArgIdxEqReachableBy(readCalls, 1, fileDesc)
/* PROGRAM */ readCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(readCalls, 1, fileDesc)

/* PROGRAM */ val readCallsFileContent = getCallArgWithIdxEq(readCallsWithFlow, 2)
/* PROGRAM */ readCallsFileContent.length

// FILE CONTENT
/* PROGRAM */ val readFileDecriptorFileContent = readCallsFileContent
/* PROGRAM */ readFileDecriptorFileContent.length


// READ FROM FILE STREAM
// FOPEN
/* COMMON */ val fopenCalls = cpg.call("fopen").l

/* PROGRAM */ val fopenCallsWithFlow = getCallsWithArgIdxEqReachableBy(fopenCalls, 1, fileNameRead)
/* PROGRAM */ fopenCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fopenCalls, 1, fileNameRead)

// FREOPEN
/* COMMON */ val freopenCalls = cpg.call("freopen").l

/* PROGRAM */ val freopenCallsWithFlow = getCallsWithArgIdxEqReachableBy(freopenCalls, 1, fileNameRead)
/* PROGRAM */ freopenCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(freopenCalls, 1, fileNameRead)

// FDOPEN
/* COMMON */ val fdopenCalls = cpg.call("fdopen").l

/* PROGRAM */ val fdopenCallsWithFlow = getCallsWithArgIdxEqReachableBy(fdopenCalls, 1, fileDesc)
/* PROGRAM */ fdopenCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fdopenCalls, 1, fileDesc)

// FILE POINTER
/* PROGRAM */ val filePtr = getCallReturnValueOfType((fopenCallsWithFlow ::: freopenCallsWithFlow ::: fdopenCallsWithFlow) ,"FILE*")
/* PROGRAM */ filePtr.length

// FREAD
/* COMMON */ val freadCalls = cpg.call("fread").l

/* PROGRAM */ val freadCallsWithFlow = getCallsWithArgIdxEqReachableBy(freadCalls, 4, filePtr)
/* PROGRAM */ freadCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(freadCalls, 4, filePtr)

/* PROGRAM */ val freadCallsFileContent = getCallArgWithIdxEq(freadCallsWithFlow, 1)
/* PROGRAM */ freadCallsFileContent.length

// VFSCANF
/* COMMON */ val vfscanfCalls = cpg.call("vfscanf").l

/* PROGRAM */ val vfscanfCallsWithFlow = getCallsWithArgIdxEqReachableBy(vfscanfCalls, 1, filePtr)
/* PROGRAM */ vfscanfCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(vfscanfCalls, 1, filePtr)

/* PROGRAM */ val vfscanfCallsFileContent = getCallArgWithIdxEq(vfscanfCallsWithFlow, 3)
/* PROGRAM */ vfscanfCallsFileContent.length

// FSCANF
/* COMMON */ val fscanfCalls = cpg.call("fscanf").l

/* PROGRAM */ val fscanfCallsWithFlow = getCallsWithArgIdxEqReachableBy(fscanfCalls, 1, filePtr)
/* PROGRAM */ fscanfCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fscanfCalls, 1, filePtr)

/* PROGRAM */ val fscanfCallsFileContent = getCallArgWithIdxEqOrGr(fscanfCallsWithFlow, 3)
/* PROGRAM */ fscanfCallsFileContent.length

// FGETS
/* COMMON */ val fgetsCalls = cpg.call("fgets").l

/* PROGRAM */ val fgetsCallsWithFlow = getCallsWithArgIdxEqReachableBy(fgetsCalls, 3, filePtr)
/* PROGRAM */ fgetsCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fgetsCalls, 3, filePtr)

/* PROGRAM */ val fgetsCallsFileContent = getCallArgWithIdxEq(fgetsCallsWithFlow, 1)
/* PROGRAM */ fgetsCallsFileContent.length

// GETLINE
/* COMMON */ val getlineCalls = cpg.call("getline").l

/* PROGRAM */ val getlineCallsWithFlow = getCallsWithArgIdxEqReachableBy(getlineCalls, 3, filePtr)
/* PROGRAM */ getlineCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(getlineCalls, 3, filePtr)

/* PROGRAM */ val getlineCallsFileContent = getCallArgWithIdxEq(getlineCallsWithFlow, 1)
/* PROGRAM */ getlineCallsFileContent.length

// GETDELIM
/* COMMON */ val getdelimCalls = cpg.call("getdelim").l

/* PROGRAM */ val getdelimCallsWithFlow = getCallsWithArgIdxEqReachableBy(getdelimCalls, 4, filePtr)
/* PROGRAM */ getdelimCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(getdelimCalls, 4, filePtr)

/* PROGRAM */ val getdelimCallsFileContent = getCallArgWithIdxEq(getdelimCallsWithFlow, 1)
/* PROGRAM */ getdelimCallsFileContent.length

// GETW
/* COMMON */ val getwCalls = cpg.call("getw").l

/* PROGRAM */ val getwCallsWithFlow = getCallsWithArgIdxEqReachableBy(getwCalls, 1, filePtr)
/* PROGRAM */ getwCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(getwCalls, 1, filePtr)

/* PROGRAM */ val getwCallsFileContent = getCallReturnValueOfType(getwCallsWithFlow, "int")
/* PROGRAM */ getwCallsFileContent.length

// FGETC
/* COMMON */ val fgetcCalls = cpg.call("fgetc").l

/* PROGRAM */ val fgetcCallsWithFlow = getCallsWithArgIdxEqReachableBy(fgetcCalls, 1, filePtr)
/* PROGRAM */ fgetcCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fgetcCalls, 1, filePtr)

/* PROGRAM */ val fgetcCallsFileContent = getCallReturnValueOfType(fgetcCallsWithFlow, "int")
/* PROGRAM */ fgetcCallsFileContent.length

// GETC
/* COMMON */ val getcCalls = cpg.call("getc").l

/* PROGRAM */ val getcCallsWithFlow = getCallsWithArgIdxEqReachableBy(getcCalls, 1, filePtr)
/* PROGRAM */ getcCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(getcCalls, 1, filePtr)

/* PROGRAM */ val getcCallsFileContent = getCallReturnValueOfType(getcCallsWithFlow, "int")
/* PROGRAM */ getcCallsFileContent.length

// FILE CONTENT
/* PROGRAM */ val readFilePtrFileContent = freadCallsFileContent ::: vfscanfCallsFileContent ::: fscanfCallsFileContent ::: fgetsCallsFileContent ::: getlineCallsFileContent ::: getdelimCallsFileContent ::: getwCallsFileContent ::: fgetcCallsFileContent ::: getcCallsFileContent
/* PROGRAM */ readFilePtrFileContent.length

// FILE CONTENT
/* PROGRAM */ val fileContentDirect = readFileDecriptorFileContent ::: readFilePtrFileContent
/* PROGRAM */ fileContentDirect.length


// READ FROM FILE WITH STDIN REDIRECT

/* COMMON */ val stdinFd = cpg.expression.filter(_.code == "STDIN_FILENO").l
/* COMMON */ val stdin = cpg.expression.filter(_.code == "stdin").l

// FREOPEN
/* PROGRAM */ val freopenCallsFromStdinWithFlow = getCallsWithArgIdxEqReachableBy(freopenCallsWithFlow, 3, stdin)
/* PROGRAM */ freopenCallsFromStdinWithFlow.length

// READ
/* PROGRAM */ val readCallsAfterFreopen = getCallsAfterCalls(freopenCallsFromStdinWithFlow, readCalls)
/* PROGRAM */ readCallsAfterFreopen.length
/* PROGRAM */ val readCallsFromStdinAfterFreopen = getCallsWithArgIdxEqReachableBy(readCallsAfterFreopen, 1, stdinFd) ::: readCallsAfterFreopen.where(_.argument.filter(exp => (exp.argumentIndex == 1 && exp.code == "0"))).l
/* PROGRAM */ readCallsFromStdinAfterFreopen.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(readCallsAfterFreopen, 1, stdinFd)

/* PROGRAM */ val readCallsStdinFileContent = getCallArgWithIdxEq(readCallsFromStdinAfterFreopen, 2)
/* PROGRAM */ readCallsStdinFileContent.length

// FREAD
/* PROGRAM */ val freadCallsAfterFreopen = getCallsAfterCalls(freopenCallsFromStdinWithFlow, freadCalls)
/* PROGRAM */ freadCallsAfterFreopen.length
/* PROGRAM */ val freadCallsFromStdinAfterFreopen = getCallsWithArgIdxEqReachableBy(freadCallsAfterFreopen, 4, stdin)
/* PROGRAM */ freadCallsFromStdinAfterFreopen.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(freadCallsAfterFreopen, 4, stdin)

/* PROGRAM */ val freadCallsStdinFileContent = getCallArgWithIdxEq(freadCallsFromStdinAfterFreopen, 1)
/* PROGRAM */ freadCallsStdinFileContent.length

// VFSCANF
/* PROGRAM */ val vfscanfCallsAfterFreopen = getCallsAfterCalls(freopenCallsFromStdinWithFlow, vfscanfCalls)
/* PROGRAM */ vfscanfCallsAfterFreopen.length
/* PROGRAM */ val vfscanfCallsFromStdinAfterFreopen = getCallsWithArgIdxEqReachableBy(vfscanfCallsAfterFreopen, 1, stdin)
/* PROGRAM */ vfscanfCallsFromStdinAfterFreopen.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(vfscanfCallsAfterFreopen, 1, stdin)

/* PROGRAM */ val vfscanfCallsStdinFileContent = getCallArgWithIdxEq(vfscanfCallsFromStdinAfterFreopen, 3)
/* PROGRAM */ vfscanfCallsStdinFileContent.length

// FSCANF
/* PROGRAM */ val fscanfCallsAfterFreopen = getCallsAfterCalls(freopenCallsFromStdinWithFlow, fscanfCalls)
/* PROGRAM */ fscanfCallsAfterFreopen.length
/* PROGRAM */ val fscanfCallsFromStdinAfterFreopen = getCallsWithArgIdxEqReachableBy(fscanfCallsAfterFreopen, 1, stdin)
/* PROGRAM */ fscanfCallsFromStdinAfterFreopen.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fscanfCallsAfterFreopen, 1, stdin)

/* PROGRAM */ val fscanfCallsStdinFileContent = getCallArgWithIdxEqOrGr(fscanfCallsFromStdinAfterFreopen, 3)
/* PROGRAM */ fscanfCallsStdinFileContent.length

// FGETS
/* PROGRAM */ val fgetsCallsAfterFreopen = getCallsAfterCalls(freopenCallsFromStdinWithFlow, fgetsCalls)
/* PROGRAM */ fgetsCallsAfterFreopen.length
/* PROGRAM */ val fgetsCallsFromStdinAfterFreopen = getCallsWithArgIdxEqReachableBy(fgetsCallsAfterFreopen, 3, stdin)
/* PROGRAM */ fgetsCallsFromStdinAfterFreopen.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fgetsCallsAfterFreopen, 3, stdin)

/* PROGRAM */ val fgetsCallsStdinFileContent = getCallArgWithIdxEqOrGr(fgetsCallsFromStdinAfterFreopen, 1)
/* PROGRAM */ fgetsCallsStdinFileContent.length

// GETLINE
/* PROGRAM */ val getlineCallsAfterFreopen = getCallsAfterCalls(freopenCallsFromStdinWithFlow, getlineCalls)
/* PROGRAM */ getlineCallsAfterFreopen.length
/* PROGRAM */ val getlineCallsFromStdinAfterFreopen = getCallsWithArgIdxEqReachableBy(getlineCallsAfterFreopen, 3, stdin)
/* PROGRAM */ getlineCallsFromStdinAfterFreopen.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(getlineCallsAfterFreopen, 3, stdin)

/* PROGRAM */ val getlineCallsStdinFileContent = getCallArgWithIdxEqOrGr(getlineCallsFromStdinAfterFreopen, 1)
/* PROGRAM */ getlineCallsStdinFileContent.length

// GETDELIM
/* PROGRAM */ val getdelimCallsAfterFreopen = getCallsAfterCalls(freopenCallsFromStdinWithFlow, getdelimCalls)
/* PROGRAM */ getdelimCallsAfterFreopen.length
/* PROGRAM */ val getdelimCallsFromStdinAfterFreopen = getCallsWithArgIdxEqReachableBy(getdelimCallsAfterFreopen, 4, stdin)
/* PROGRAM */ getdelimCallsFromStdinAfterFreopen.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(getdelimCallsAfterFreopen, 4, stdin)

/* PROGRAM */ val getdelimCallsStdinFileContent = getCallArgWithIdxEqOrGr(getdelimCallsFromStdinAfterFreopen, 1)
/* PROGRAM */ getdelimCallsStdinFileContent.length

// GETW
/* PROGRAM */ val getwCallsAfterFreopen = getCallsAfterCalls(freopenCallsFromStdinWithFlow, getwCalls)
/* PROGRAM */ getwCallsAfterFreopen.length
/* PROGRAM */ val getwCallsFromStdinAfterFreopen = getCallsWithArgIdxEqReachableBy(getwCallsAfterFreopen, 1, stdin)
/* PROGRAM */ getwCallsFromStdinAfterFreopen.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(getwCallsAfterFreopen, 1, stdin)

/* PROGRAM */ val getwCallsStdinFileContent = getCallReturnValueOfType(getwCallsFromStdinAfterFreopen, "int")
/* PROGRAM */ getwCallsStdinFileContent.length

// FGETC
/* PROGRAM */ val fgetcCallsAfterFreopen = getCallsAfterCalls(freopenCallsFromStdinWithFlow, fgetcCalls)
/* PROGRAM */ fgetcCallsAfterFreopen.length
/* PROGRAM */ val fgetcCallsFromStdinAfterFreopen = getCallsWithArgIdxEqReachableBy(fgetcCallsAfterFreopen, 1, stdin)
/* PROGRAM */ fgetcCallsFromStdinAfterFreopen.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fgetcCallsAfterFreopen, 1, stdin)

/* PROGRAM */ val fgetcCallsStdinFileContent = getCallReturnValueOfType(fgetcCallsFromStdinAfterFreopen, "int")
/* PROGRAM */ fgetcCallsStdinFileContent.length

// GETC
/* PROGRAM */ val getcCallsAfterFreopen = getCallsAfterCalls(freopenCallsFromStdinWithFlow, getcCalls)
/* PROGRAM */ getcCallsAfterFreopen.length
/* PROGRAM */ val getcCallsFromStdinAfterFreopen = getCallsWithArgIdxEqReachableBy(getcCallsAfterFreopen, 1, stdin)
/* PROGRAM */ getcCallsFromStdinAfterFreopen.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(getcCallsAfterFreopen, 1, stdin)

/* PROGRAM */ val getcCallsStdinFileContent = getCallReturnValueOfType(getcCallsFromStdinAfterFreopen, "int")
/* PROGRAM */ getcCallsStdinFileContent.length

// FUNCTIONS ALWAYS READING FROM STDIN
// SCANF
/* COMMON */ val scanfCalls = cpg.call("scanf").l

/* PROGRAM */ val scanfCallsAfterFreopen = getCallsAfterCalls(freopenCallsFromStdinWithFlow, scanfCalls)
/* PROGRAM */ scanfCallsAfterFreopen.length

/* PROGRAM */ val scanfCallsFileContent = getCallArgWithIdxEqOrGr(scanfCallsAfterFreopen, 2)
/* PROGRAM */ scanfCallsFileContent.length

// GETS
/* COMMON */ val getsCalls = cpg.call("gets").l

/* PROGRAM */ val getsCallsAfterFreopen = getCallsAfterCalls(freopenCallsFromStdinWithFlow, getsCalls)
/* PROGRAM */ getsCallsAfterFreopen.length

/* PROGRAM */ val getsCallsFileContent = getCallArgWithIdxEq(getsCallsAfterFreopen, 1)
/* PROGRAM */ getsCallsFileContent.length

// VSCANF
/* COMMON */ val vscanfCalls = cpg.call("vscanf").l

/* PROGRAM */ val vscanfCallsAfterFreopen = getCallsAfterCalls(freopenCallsFromStdinWithFlow, vscanfCalls)
/* PROGRAM */ vscanfCallsAfterFreopen.length

/* PROGRAM */ val vscanfCallsFileContent = getCallArgWithIdxEq(vscanfCallsAfterFreopen, 2)
/* PROGRAM */ vscanfCallsFileContent.length

// GETCHAR
/* COMMON */ val getcharCalls = cpg.call("getchar").l

/* PROGRAM */ val getcharCallsAfterFreopen = getCallsAfterCalls(freopenCallsFromStdinWithFlow, getcharCalls)
/* PROGRAM */ getcharCallsAfterFreopen.length

/* PROGRAM */ val getcharCallsFileContent = getCallReturnValueOfType(getcharCallsAfterFreopen, "int")
/* PROGRAM */ getcharCallsFileContent.length

// FILE CONTENT
/* PROGRAM */ val fileContentStdin = readCallsStdinFileContent ::: freadCallsStdinFileContent ::: vfscanfCallsStdinFileContent ::: fscanfCallsStdinFileContent ::: fgetsCallsStdinFileContent ::: getlineCallsStdinFileContent ::: getdelimCallsStdinFileContent ::: getwCallsStdinFileContent ::: fgetcCallsStdinFileContent ::: getcCallsStdinFileContent ::: scanfCallsFileContent ::: getsCallsFileContent ::: vscanfCallsFileContent ::: getcharCallsFileContent
/* PROGRAM */ fileContentStdin.length

// FILE CONTENT
/* PROGRAM */ val fileContent = fileContentDirect ::: fileContentStdin
/* PROGRAM */ fileContent.length


// WRITE STDOUT

/* PROGRAM */ val content = fileContent

// WRITE FILE POINTER OR FILE DESC
/* COMMON */ val stdoutStderrFd = cpg.expression.filter(exp => exp.code == "STDOUT_FILENO" || exp.code == "STDERR_FILENO").l
/* COMMON */ val stdoutStderr = cpg.expression.filter(exp => exp.code == "stdout" || exp.code == "stderr").l

// WRITE
/* COMMON */ val writeCalls = cpg.call("write").l

/* PROGRAM */ val writeCallsWithFlow = getCallsWithArgIdxEqReachableBy(writeCalls, 2, content)
/* PROGRAM */ writeCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(writeCalls, 2, content)
/* PROGRAM */ val writeCallsToStdoutWithFlow = getCallsWithArgIdxEqReachableBy(writeCallsWithFlow, 1, stdoutStderrFd) ::: writeCallsWithFlow.where(_.argument.filter(exp => exp.argumentIndex == 1 && (exp.code == "1" || exp.code == "2"))).l
/* PROGRAM */ writeCallsToStdoutWithFlow.length

// FWRITE
/* COMMON */ val fwriteCalls = cpg.call("fwrite").l

/* PROGRAM */ val fwriteCallsWithFlow = getCallsWithArgIdxEqReachableBy(fwriteCalls, 1, content)
/* PROGRAM */ fwriteCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fwriteCalls, 1, content)
/* PROGRAM */ val fwriteCallsToStdoutWithFlow = getCallsWithArgIdxEqReachableBy(fwriteCallsWithFlow, 4, stdoutStderr)
/* PROGRAM */ fwriteCallsToStdoutWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fwriteCalls, 4, stdoutStderr)

// FPRINTF
/* COMMON */ val fprintfCalls = cpg.call("fprintf").l

/* PROGRAM */ val fprintfCallsWithFlow = getCallsWithArgIdxEqOrGrReachableBy(fprintfCalls, 3, content)
/* PROGRAM */ fprintfCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqOrGrReachableBy(fprintfCalls, 3, content)
/* PROGRAM */ val fprintfCallsToStdoutWithFlow = getCallsWithArgIdxEqReachableBy(fprintfCallsWithFlow, 1, stdoutStderr)
/* PROGRAM */ fprintfCallsToStdoutWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fprintfCallsWithFlow, 1, stdoutStderr)

// VFPRINTF
/* COMMON */ val vfprintfCalls = cpg.call("vfprintf").l

/* PROGRAM */ val vfprintfCallsWithFlow = getCallsWithArgIdxEqReachableBy(vfprintfCalls, 3, content)
/* PROGRAM */ vfprintfCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(vfprintfCalls, 3, content)
/* PROGRAM */ val vfprintfCallsToStdoutWithFlow = getCallsWithArgIdxEqReachableBy(vfprintfCallsWithFlow, 1, stdoutStderr)
/* PROGRAM */ vfprintfCallsToStdoutWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(vfprintfCallsWithFlow, 1, stdoutStderr)

// FPUTC
/* COMMON */ val fputcCalls = cpg.call("fputc").l

/* PROGRAM */ val fputcCallsWithFlow = getCallsWithArgIdxEqReachableBy(fputcCalls, 1, content)
/* PROGRAM */ fputcCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fputcCalls, 1, content)
/* PROGRAM */ val fputcCallsToStdoutWithFlow = getCallsWithArgIdxEqReachableBy(fputcCallsWithFlow, 2, stdoutStderr)
/* PROGRAM */ fputcCallsToStdoutWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fputcCallsWithFlow, 2, stdoutStderr)

// FPUTS
/* COMMON */ val fputsCalls = cpg.call("fputs").l

/* PROGRAM */ val fputsCallsWithFlow = getCallsWithArgIdxEqReachableBy(fputsCalls, 1, content)
/* PROGRAM */ fputsCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fputsCalls, 1, content)
/* PROGRAM */ val fputsCallsToStdoutWithFlow = getCallsWithArgIdxEqReachableBy(fputsCallsWithFlow, 2, stdoutStderr)
/* PROGRAM */ fputsCallsToStdoutWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fputsCallsWithFlow, 2, stdoutStderr)

// PUTC
/* COMMON */ val putcCalls = cpg.call("putc").l

/* PROGRAM */ val putcCallsWithFlow = getCallsWithArgIdxEqReachableBy(putcCalls, 1, content)
/* PROGRAM */ putcCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(putcCalls, 1, content)
/* PROGRAM */ val putcCallsToStdoutWithFlow = getCallsWithArgIdxEqReachableBy(putcCallsWithFlow, 2, stdoutStderr)
/* PROGRAM */ putcCallsToStdoutWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(putcCallsWithFlow, 2, stdoutStderr)

// PUTW
/* COMMON */ val putwCalls = cpg.call("putw").l

/* PROGRAM */ val putwCallsWithFlow = getCallsWithArgIdxEqReachableBy(putwCalls, 1, content)
/* PROGRAM */ putwCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(putwCalls, 1, content)
/* PROGRAM */ val putwCallsToStdoutWithFlow = getCallsWithArgIdxEqReachableBy(putwCallsWithFlow, 2, stdoutStderr)
/* PROGRAM */ putwCallsToStdoutWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(putwCallsWithFlow, 2, stdoutStderr)

// FUNCTIONS ALWAYS WRITING TO STDOUT
// PRINTF
/* COMMON */ val printfCalls = cpg.call("printf").l

/* PROGRAM */ val printfCallsWithFlow = getCallsWithArgIdxEqOrGrReachableBy(printfCalls, 2, content)
/* PROGRAM */ printfCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqOrGrReachableBy(printfCalls, 2, content)

// VPRINTF
/* COMMON */ val vprintfCalls = cpg.call("vprintf").l

/* PROGRAM */ val vprintfCallsWithFlow = getCallsWithArgIdxEqReachableBy(vprintfCalls, 2, content)
/* PROGRAM */ vprintfCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(vprintfCalls, 2, content)

// PUTCHAR
/* COMMON */ val putcharCalls = cpg.call("putchar").l

/* PROGRAM */ val putcharCallsWithFlow = getCallsWithArgIdxEqReachableBy(putcharCalls, 1, content)
/* PROGRAM */putcharCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(putcharCalls, 1, content)

// PUTS
/* COMMON */ val putsCalls = cpg.call("puts").l

/* PROGRAM */ val putsCallsWithFlow = getCallsWithArgIdxEqReachableBy(putsCalls, 1, content)
/* PROGRAM */ putsCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(putsCalls, 1, content)

// WRITE FLOWS
/* PROGRAM */ val writeToStdoutFlows = writeCallsToStdoutWithFlow ::: fwriteCallsToStdoutWithFlow ::: fprintfCallsToStdoutWithFlow ::: vfprintfCallsToStdoutWithFlow ::: fputcCallsToStdoutWithFlow ::: fputsCallsToStdoutWithFlow ::: putcCallsToStdoutWithFlow ::: putwCallsToStdoutWithFlow ::: printfCallsWithFlow ::: vprintfCallsWithFlow ::: putcharCallsWithFlow ::: putsCallsWithFlow
/* PROGRAM */ writeToStdoutFlows.length

// RESULT FLOWS
/* PROGRAM */ val resultFlows = writeToStdoutFlows
/* PROGRAM */ resultFlows.length