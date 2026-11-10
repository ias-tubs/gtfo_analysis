// WRITE (FILE OR STDOUT)

/* PROGRAM */ val content = source

// WRITE
/* COMMON */ val writeCalls = cpg.call("write").l

/* PROGRAM */ val writeCallsWithFlow = getCallsWithArgIdxEqReachableBy(writeCalls, 2, content)
/* PROGRAM */ writeCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(writeCalls, 2, content)

// FWRITE
/* COMMON */ val fwriteCalls = cpg.call("fwrite").l

/* PROGRAM */ val fwriteCallsWithFlow = getCallsWithArgIdxEqReachableBy(fwriteCalls, 1, content)
/* PROGRAM */ fwriteCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fwriteCalls, 1, content)

// FPRINTF
/* COMMON */ val fprintfCalls = cpg.call("fprintf").l

/* PROGRAM */ val fprintfCallsWithFlow = getCallsWithArgIdxEqOrGrReachableBy(fprintfCalls, 3, content)
/* PROGRAM */ fprintfCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqOrGrReachableBy(fprintfCalls, 3, content)

// VFPRINTF
/* COMMON */ val vfprintfCalls = cpg.call("vfprintf").l

/* PROGRAM */ val vfprintfCallsWithFlow = getCallsWithArgIdxEqReachableBy(vfprintfCalls, 3, content)
/* PROGRAM */ vfprintfCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(vfprintfCalls, 3, content)

// FPUTC
/* COMMON */ val fputcCalls = cpg.call("fputc").l

/* PROGRAM */ val fputcCallsWithFlow = getCallsWithArgIdxEqReachableBy(fputcCalls, 1, content)
/* PROGRAM */ fputcCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fputcCalls, 1, content)

// FPUTS
/* COMMON */ val fputsCalls = cpg.call("fputs").l

/* PROGRAM */ val fputsCallsWithFlow = getCallsWithArgIdxEqReachableBy(fputsCalls, 1, content)
/* PROGRAM */ fputsCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(fputsCalls, 1, content)

// PUTC
/* COMMON */ val putcCalls = cpg.call("putc").l

/* PROGRAM */ val putcCallsWithFlow = getCallsWithArgIdxEqReachableBy(putcCalls, 1, content)
/* PROGRAM */ putcCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(putcCalls, 1, content)

// PUTW
/* COMMON */ val putwCalls = cpg.call("putw").l

/* PROGRAM */ val putwCallsWithFlow = getCallsWithArgIdxEqReachableBy(putwCalls, 1, content)
/* PROGRAM */ putwCallsWithFlow.length
/* PROGRAM */ printCallsWithArgIdxEqReachableBy(putwCalls, 1, content)

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
/* PROGRAM */ val writeFlows = writeCallsWithFlow ::: fwriteCallsWithFlow ::: fprintfCallsWithFlow ::: vfprintfCallsWithFlow ::: fputcCallsWithFlow ::: fputsCallsWithFlow ::: putcCallsWithFlow ::: putwCallsWithFlow ::: printfCallsWithFlow ::: vprintfCallsWithFlow ::: putcharCallsWithFlow ::: putsCallsWithFlow
/* PROGRAM */ writeFlows.length

