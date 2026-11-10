// WRITE STDOUT

/* PROGRAM */ val content = source

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
/* PROGRAM */ val writeToFileStdoutFlows = writeCallsToStdoutWithFlow ::: fwriteCallsToStdoutWithFlow ::: fprintfCallsToStdoutWithFlow ::: vfprintfCallsToStdoutWithFlow ::: fputcCallsToStdoutWithFlow ::: fputsCallsToStdoutWithFlow ::: putcCallsToStdoutWithFlow ::: putwCallsToStdoutWithFlow ::: printfCallsWithFlow ::: vprintfCallsWithFlow ::: putcharCallsWithFlow ::: putsCallsWithFlow
/* PROGRAM */ writeToFileStdoutFlows.length

