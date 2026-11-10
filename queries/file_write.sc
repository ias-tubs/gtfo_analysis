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
/* PROGRAM */ val fileContent = readFileDecriptorFileContent ::: readFilePtrFileContent
/* PROGRAM */ fileContent.length


// FILE CONTENT
/* PROGRAM */ val writeContent = fileContent ::: source
/* PROGRAM */ writeContent.length


// WRITE TO FILE

/* PROGRAM */ val content = writeContent

// WRITE TO FILE DESCRIPTOR
// open and file descriptor in read file part

// WRITE
/* COMMON */ val writeCalls = cpg.call("write").l

/* PROGRAM */ val writeCallsWithFlow = getCallsWithArgIdxEqReachableBy(writeCalls, 2, content)
/* PROGRAM */ writeCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(writeCalls, 2, content)
/* PROGRAM */ val writeCallsToFileWithFlow = getCallsWithArgIdxEqReachableBy(writeCallsWithFlow, 1, fileDesc)
/* PROGRAM */ writeCallsToFileWithFlow.length


// WRITE TO FILE STREAM
// fopen and file pointer in read file part

// FWRITE
/* COMMON */ val fwriteCalls = cpg.call("fwrite").l

/* PROGRAM */ val fwriteCallsWithFlow = getCallsWithArgIdxEqReachableBy(fwriteCalls, 1, content)
/* PROGRAM */ fwriteCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fwriteCalls, 1, content)
/* PROGRAM */ val fwriteCallsToFileWithFlow = getCallsWithArgIdxEqReachableBy(fwriteCallsWithFlow, 4, filePtr)
/* PROGRAM */ fwriteCallsToFileWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fwriteCalls, 4, filePtr)

// FPRINTF
/* COMMON */ val fprintfCalls = cpg.call("fprintf").l

/* PROGRAM */ val fprintfCallsWithFlow = getCallsWithArgIdxEqOrGrReachableBy(fprintfCalls, 3, content)
/* PROGRAM */ fprintfCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqOrGrReachableBy(fprintfCalls, 3, content)
/* PROGRAM */ val fprintfCallsToFileWithFlow = getCallsWithArgIdxEqReachableBy(fprintfCallsWithFlow, 1, filePtr)
/* PROGRAM */ fprintfCallsToFileWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fprintfCallsWithFlow, 1, filePtr)

// VFPRINTF
/* COMMON */ val vfprintfCalls = cpg.call("vfprintf").l

/* PROGRAM */ val vfprintfCallsWithFlow = getCallsWithArgIdxEqReachableBy(vfprintfCalls, 3, content)
/* PROGRAM */ vfprintfCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(vfprintfCalls, 3, content)
/* PROGRAM */ val vfprintfCallsToFileWithFlow = getCallsWithArgIdxEqReachableBy(vfprintfCallsWithFlow, 1, filePtr)
/* PROGRAM */ vfprintfCallsToFileWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(vfprintfCallsWithFlow, 1, filePtr)

// FPUTC
/* COMMON */ val fputcCalls = cpg.call("fputc").l

/* PROGRAM */ val fputcCallsWithFlow = getCallsWithArgIdxEqReachableBy(fputcCalls, 1, content)
/* PROGRAM */ fputcCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fputcCalls, 1, content)
/* PROGRAM */ val fputcCallsToFileWithFlow = getCallsWithArgIdxEqReachableBy(fputcCallsWithFlow, 2, filePtr)
/* PROGRAM */ fputcCallsToFileWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fputcCallsWithFlow, 2, filePtr)

// FPUTS
/* COMMON */ val fputsCalls = cpg.call("fputs").l

/* PROGRAM */ val fputsCallsWithFlow = getCallsWithArgIdxEqReachableBy(fputsCalls, 1, content)
/* PROGRAM */ fputsCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fputsCalls, 1, content)
/* PROGRAM */ val fputsCallsToFileWithFlow = getCallsWithArgIdxEqReachableBy(fputsCallsWithFlow, 2, filePtr)
/* PROGRAM */ fputsCallsToFileWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fputsCallsWithFlow, 2, filePtr)

// PUTC
/* COMMON */ val putcCalls = cpg.call("putc").l

/* PROGRAM */ val putcCallsWithFlow = getCallsWithArgIdxEqReachableBy(putcCalls, 1, content)
/* PROGRAM */ putcCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(putcCalls, 1, content)
/* PROGRAM */ val putcCallsToFileWithFlow = getCallsWithArgIdxEqReachableBy(putcCallsWithFlow, 2, filePtr)
/* PROGRAM */ putcCallsToFileWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(putcCallsWithFlow, 2, filePtr)

// PUTW
/* COMMON */ val putwCalls = cpg.call("putw").l

/* PROGRAM */ val putwCallsWithFlow = getCallsWithArgIdxEqReachableBy(putwCalls, 1, content)
/* PROGRAM */ putwCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(putwCalls, 1, content)
/* PROGRAM */ val putwCallsToFileWithFlow = getCallsWithArgIdxEqReachableBy(putwCallsWithFlow, 2, filePtr)
/* PROGRAM */ putwCallsToFileWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(putwCallsWithFlow, 2, filePtr)

// WRITE FLOWS
/* PROGRAM */ val writeToFileFlows = writeCallsToFileWithFlow ::: fwriteCallsToFileWithFlow ::: fprintfCallsToFileWithFlow ::: vfprintfCallsToFileWithFlow ::: fputcCallsToFileWithFlow ::: fputsCallsToFileWithFlow ::: putcCallsToFileWithFlow ::: putwCallsToFileWithFlow
/* PROGRAM */ writeToFileFlows.length


// RESULT FLOWS
/* PROGRAM */ val resultFlows = writeToFileFlows
/* PROGRAM */ resultFlows.length