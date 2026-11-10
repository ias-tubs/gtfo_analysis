// WRITE TO FILE WITH STDOUT REDIRECT

// ALREADY IN write_to_file.sc ------------------------------------------------------------------------------------------
/* PROGRAM */ val fileNameWrite = source
/* PROGRAM */ val content = source

// FREOPEN
/* COMMON */ val freopenCalls = cpg.call("freopen").l

/* PROGRAM */ val freopenCallsWithFlow = getCallsWithArgIdxEqReachableBy(freopenCalls, 1, fileNameWrite)
/* PROGRAM */ freopenCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(freopenCalls, 1, fileNameWrite)

/* COMMON */ val writeCalls = cpg.call("write").l
/* COMMON */ val fwriteCalls = cpg.call("fwrite").l
/* COMMON */ val fprintfCalls = cpg.call("fprintf").l
/* COMMON */ val vfprintfCalls = cpg.call("vfprintf").l
/* COMMON */ val fputcCalls = cpg.call("fputc").l
/* COMMON */ val fputsCalls = cpg.call("fputs").l
/* COMMON */ val putcCalls = cpg.call("putc").l
/* COMMON */ val putwCalls = cpg.call("putw").l

// -----------------------------------------------------------------------------------------------------------------------

/* COMMON */ val stdoutFd = cpg.expression.filter(_.code == "STDOUT_FILENO").l
/* COMMON */ val stdout = cpg.expression.filter(_.code == "stdout").l

// FREOPEN
/* PROGRAM */ val freopenCallsFromStdoutWithFlow = getCallsWithArgIdxEqReachableBy(freopenCallsWithFlow, 3, stdout)
/* PROGRAM */ freopenCallsFromStdoutWithFlow.length


// WRITE
/* PROGRAM */ val writeCallsAfterFreopen = getCallsAfterCalls(freopenCallsFromStdoutWithFlow, writeCalls)
/* PROGRAM */ writeCallsAfterFreopen.length
/* PROGRAM */ val writeCallsWithFlowAfterFreopen = getCallsWithArgIdxEqReachableBy(writeCallsAfterFreopen, 2, content)
/* PROGRAM */ writeCallsWithFlowAfterFreopen.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(writeCallsAfterFreopen, 2, content)

/* PROGRAM */ val writeCallsToStdoutWithFlow = getCallsWithArgIdxEqReachableBy(writeCallsWithFlowAfterFreopen, 1, stdoutFd) ::: writeCallsWithFlowAfterFreopen.where(_.argument.filter(exp => exp.argumentIndex == 1 && (exp.code == "1"))).l
/* PROGRAM */ writeCallsToStdoutWithFlow.length


// FWRITE
/* PROGRAM */ val fwriteCallsAfterFreopen = getCallsAfterCalls(freopenCallsFromStdoutWithFlow, fwriteCalls)
/* PROGRAM */ fwriteCallsAfterFreopen.length
/* PROGRAM */ val fwriteCallsWithFlowAfterFreopen = getCallsWithArgIdxEqReachableBy(fwriteCallsAfterFreopen, 1, content)
/* PROGRAM */ fwriteCallsWithFlowAfterFreopen.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fwriteCallsAfterFreopen, 1, content)

/* PROGRAM */ val fwriteCallsToStdoutWithFlow = getCallsWithArgIdxEqReachableBy(fwriteCallsWithFlowAfterFreopen, 4, stdout)
/* PROGRAM */ fwriteCallsToStdoutWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fwriteCallsWithFlowAfterFreopen, 4, stdout)

// FPRINTF
/* PROGRAM */ val fprintfCallsAfterFreopen = getCallsAfterCalls(freopenCallsFromStdoutWithFlow, fprintfCalls)
/* PROGRAM */ fprintfCallsAfterFreopen.length
/* PROGRAM */ val fprintfCallsWithFlowAfterFreopen = getCallsWithArgIdxEqReachableBy(fprintfCallsAfterFreopen, 3, content)
/* PROGRAM */ fprintfCallsWithFlowAfterFreopen.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fprintfCallsAfterFreopen, 3, content)

/* PROGRAM */ val fprintfCallsToStdoutWithFlow = getCallsWithArgIdxEqReachableBy(fprintfCallsWithFlowAfterFreopen, 1, stdout)
/* PROGRAM */ fprintfCallsToStdoutWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fprintfCallsWithFlowAfterFreopen, 1, stdout)


// VFPRINTF
/* PROGRAM */ val vfprintfCallsAfterFreopen = getCallsAfterCalls(freopenCallsFromStdoutWithFlow, vfprintfCalls)
/* PROGRAM */ vfprintfCallsAfterFreopen.length
/* PROGRAM */ val vfprintfCallsWithFlowAfterFreopen = getCallsWithArgIdxEqReachableBy(vfprintfCallsAfterFreopen, 3, content)
/* PROGRAM */ vfprintfCallsWithFlowAfterFreopen.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(vfprintfCallsAfterFreopen, 3, content)

/* PROGRAM */ val vfprintfCallsToStdoutWithFlow = getCallsWithArgIdxEqReachableBy(vfprintfCallsWithFlowAfterFreopen, 1, stdout)
/* PROGRAM */ vfprintfCallsToStdoutWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(vfprintfCallsWithFlowAfterFreopen, 1, stdout)

// FPUTC
/* PROGRAM */ val fputcCallsAfterFreopen = getCallsAfterCalls(freopenCallsFromStdoutWithFlow, fputcCalls)
/* PROGRAM */ fputcCallsAfterFreopen.length
/* PROGRAM */ val fputcCallsWithFlowAfterFreopen = getCallsWithArgIdxEqReachableBy(fputcCallsAfterFreopen, 1, content)
/* PROGRAM */ fputcCallsWithFlowAfterFreopen.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fputcCallsAfterFreopen, 1, content)

/* PROGRAM */ val fputcCallsToStdoutWithFlow = getCallsWithArgIdxEqReachableBy(fputcCallsWithFlowAfterFreopen, 2, stdout)
/* PROGRAM */ fputcCallsToStdoutWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fputcCallsWithFlowAfterFreopen, 2, stdout)

// FPUTS
/* PROGRAM */ val fputsCallsAfterFreopen = getCallsAfterCalls(freopenCallsFromStdoutWithFlow, fputsCalls)
/* PROGRAM */ fputsCallsAfterFreopen.length
/* PROGRAM */ val fputsCallsWithFlowAfterFreopen = getCallsWithArgIdxEqReachableBy(fputsCallsAfterFreopen, 1, content)
/* PROGRAM */ fputsCallsWithFlowAfterFreopen.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fputsCallsAfterFreopen, 1, content)

/* PROGRAM */ val fputsCallsToStdoutWithFlow = getCallsWithArgIdxEqReachableBy(fputsCallsWithFlowAfterFreopen, 2, stdout)
/* PROGRAM */ fputsCallsToStdoutWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fputsCallsWithFlowAfterFreopen, 2, stdout)

// PUTC
/* PROGRAM */ val putcCallsAfterFreopen = getCallsAfterCalls(freopenCallsFromStdoutWithFlow, putcCalls)
/* PROGRAM */ putcCallsAfterFreopen.length
/* PROGRAM */ val putcCallsWithFlowAfterFreopen = getCallsWithArgIdxEqReachableBy(putcCallsAfterFreopen, 1, content)
/* PROGRAM */ putcCallsWithFlowAfterFreopen.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(putcCallsAfterFreopen, 1, content)

/* PROGRAM */ val putcCallsToStdoutWithFlow = getCallsWithArgIdxEqReachableBy(putcCallsWithFlowAfterFreopen, 2, stdout)
/* PROGRAM */ putcCallsToStdoutWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(putcCallsWithFlowAfterFreopen, 2, stdout)

// PUTW
/* PROGRAM */ val putwCallsAfterFreopen = getCallsAfterCalls(freopenCallsFromStdoutWithFlow, putwCalls)
/* PROGRAM */ putwCallsAfterFreopen.length
/* PROGRAM */ val putwCallsWithFlowAfterFreopen = getCallsWithArgIdxEqReachableBy(putwCallsAfterFreopen, 1, content)
/* PROGRAM */ putwCallsWithFlowAfterFreopen.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(putwCallsAfterFreopen, 1, content)

/* PROGRAM */ val putwCallsToStdoutWithFlow = getCallsWithArgIdxEqReachableBy(putwCallsWithFlowAfterFreopen, 2, stdout)
/* PROGRAM */ putwCallsToStdoutWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(putwCallsWithFlowAfterFreopen, 2, stdout)


// FUNCTIONS ALWAYS WRITING TO STDOUT
// PRINTF
/* COMMON */ val printfCalls = cpg.call("printf").l

/* PROGRAM */ val printfCallsAfterFreopen = getCallsAfterCalls(freopenCallsFromStdoutWithFlow, printfCalls)
/* PROGRAM */ printfCallsAfterFreopen.length
/* PROGRAM */ val printfCallsWithFlowAfterFreopen = getCallsWithArgIdxEqOrGrReachableBy(printfCallsAfterFreopen, 2, content)
/* PROGRAM */ printfCallsWithFlowAfterFreopen.length
/* PROGRAM */ printCallsWithArgIdxEqOrGrReachableBy(printfCallsAfterFreopen, 2, content)

// VPRINTF
/* COMMON */ val vprintfCalls = cpg.call("vprintf").l

/* PROGRAM */ val vprintfCallsAfterFreopen = getCallsAfterCalls(freopenCallsFromStdoutWithFlow, vprintfCalls)
/* PROGRAM */ vprintfCallsAfterFreopen.length
/* PROGRAM */ val vprintfCallsWithFlowAfterFreopen = getCallsWithArgIdxEqReachableBy(vprintfCallsAfterFreopen, 2, content)
/* PROGRAM */ vprintfCallsWithFlowAfterFreopen.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(vprintfCallsAfterFreopen, 2, content)

// PUTCHAR
/* COMMON */ val putcharCalls = cpg.call("putchar").l

/* PROGRAM */ val putcharCallsAfterFreopen = getCallsAfterCalls(freopenCallsFromStdoutWithFlow, putcharCalls)
/* PROGRAM */ putcharCallsAfterFreopen.length
/* PROGRAM */ val putcharCallsWithFlowAfterFreopen = getCallsWithArgIdxEqReachableBy(putcharCallsAfterFreopen, 1, content)
/* PROGRAM */ putcharCallsWithFlowAfterFreopen.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(putcharCallsAfterFreopen, 1, content)

// PUTS
/* COMMON */ val putsCalls = cpg.call("puts").l

/* PROGRAM */ val putsCallsAfterFreopen = getCallsAfterCalls(freopenCallsFromStdoutWithFlow, putsCalls)
/* PROGRAM */ putsCallsAfterFreopen.length
/* PROGRAM */ val putsCallsWithFlowAfterFreopen = getCallsWithArgIdxEqReachableBy(putsCallsAfterFreopen, 1, content)
/* PROGRAM */ putsCallsWithFlowAfterFreopen.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(putsCallsAfterFreopen, 1, content)


// WRITE FLOWS
/* PROGRAM */ val writeToStdoutFileFlows = writeCallsToStdoutWithFlow ::: fwriteCallsToStdoutWithFlow ::: fprintfCallsToStdoutWithFlow ::: vfprintfCallsToStdoutWithFlow ::: fputcCallsToStdoutWithFlow ::: fputsCallsToStdoutWithFlow ::: putcCallsToStdoutWithFlow ::: putwCallsToStdoutWithFlow ::: printfCallsWithFlowAfterFreopen ::: vprintfCallsWithFlowAfterFreopen ::: putcharCallsWithFlowAfterFreopen ::: putsCallsWithFlowAfterFreopen
/* PROGRAM */ writeToStdoutFileFlows.length
