// WRITE TO FILE

/* PROGRAM */ val content = source
/* PROGRAM */ val fileNameWrite = source

// WRITE TO FILE DESCRIPTOR
// OPEN
/* COMMON */ val openCalls = cpg.call("open").l

/* PROGRAM */ val openCallsWithFlow = getCallsWithArgIdxEqReachableBy(openCalls, 1, fileNameWrite)
/* PROGRAM */ openCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(openCalls, 1, fileNameWrite)

// FILE DESCRIPTOR
/* PROGRAM */ val fileDesc = getCallReturnValueOfType(openCallsWithFlow, "int").l
/* PROGRAM */ fileDesc.length

// WRITE
/* COMMON */ val writeCalls = cpg.call("write").l

/* PROGRAM */ val writeCallsWithFlow = getCallsWithArgIdxEqReachableBy(writeCalls, 2, content)
/* PROGRAM */ writeCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(writeCalls, 2, content)
/* PROGRAM */ val writeCallsToFileWithFlow = getCallsWithArgIdxEqReachableBy(writeCallsWithFlow, 1, fileDesc)
/* PROGRAM */ writeCallsToFileWithFlow.length


// WRITE TO FILE STREAM
// FOPEN
/* COMMON */ val fopenCalls = cpg.call("fopen").l

/* PROGRAM */ val fopenCallsWithFlow = getCallsWithArgIdxEqReachableBy(fopenCalls, 1, fileNameWrite)
/* PROGRAM */ fopenCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fopenCalls, 1, fileNameWrite)

// FREOPEN
/* COMMON */ val freopenCalls = cpg.call("freopen").l

/* PROGRAM */ val freopenCallsWithFlow = getCallsWithArgIdxEqReachableBy(freopenCalls, 1, fileNameWrite)
/* PROGRAM */ freopenCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(freopenCalls, 1, fileNameWrite)

// FDOPEN
/* COMMON */ val fdopenCalls = cpg.call("fdopen").l

/* PROGRAM */ val fdopenCallsWithFlow = getCallsWithArgIdxEqReachableBy(fdopenCalls, 1, fileDesc)
/* PROGRAM */ fdopenCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fdopenCalls, 1, fileDesc)

// FILE POINTER
/* PROGRAM */ val filePtr = getCallReturnValueOfType((fopenCallsWithFlow ::: freopenCallsWithFlow ::: fdopenCallsWithFlow) ,"FILE*")
/* PROGRAM */ filePtr.length

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
/* PROGRAM */ val writeToFileDirectFlows = writeCallsToFileWithFlow ::: fwriteCallsToFileWithFlow ::: fprintfCallsToFileWithFlow ::: vfprintfCallsToFileWithFlow ::: fputcCallsToFileWithFlow ::: fputsCallsToFileWithFlow ::: putcCallsToFileWithFlow ::: putwCallsToFileWithFlow
/* PROGRAM */ writeToFileDirectFlows.length

