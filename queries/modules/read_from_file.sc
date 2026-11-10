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