// READ FROM FILE WITH STDIN REDIRECT

// ALREADY IN read_from_file.sc ------------------------------------------------------------------------------------------
/* PROGRAM */ val fileNameRead = source

// FREOPEN
/* COMMON */ val freopenCalls = cpg.call("freopen").l

/* PROGRAM */ val freopenCallsWithFlow = getCallsWithArgIdxEqReachableBy(freopenCalls, 1, fileNameRead)
/* PROGRAM */ freopenCallsWithFlow.length
/* PROGRAM */ printCallsWithArgReachableBy(freopenCalls, 1, fileNameRead)

/* COMMON */ val readCalls = cpg.call("read").l
/* COMMON */ val freadCalls = cpg.call("fread").l
/* COMMON */ val vfscanfCalls = cpg.call("vfscanf").l
/* COMMON */ val fscanfCalls = cpg.call("fscanf").l
/* COMMON */ val fgetsCalls = cpg.call("fgets").l
/* COMMON */ val getlineCalls = cpg.call("getline").l
/* COMMON */ val getdelimCalls = cpg.call("getdelim").l
/* COMMON */ val getwCalls = cpg.call("getw").l
/* COMMON */ val fgetcCalls = cpg.call("fgetc").l
/* COMMON */ val getcCalls = cpg.call("getc").l


// -----------------------------------------------------------------------------------------------------------------------

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