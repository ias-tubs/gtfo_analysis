// READ (FROM FILE OR STDIN)

// READ
/* COMMON */ val readCalls = cpg.call("read").l

/* PROGRAM */ val readCallsFileContent = getCallArgWithIdxEq(readCalls, 2)
/* PROGRAM */ readCallsFileContent.length


// FREAD
/* COMMON */ val freadCalls = cpg.call("fread").l

/* PROGRAM */ val freadCallsFileContent = getCallArgWithIdxEq(freadCalls, 1)
/* PROGRAM */ freadCallsFileContent.length


// VFSCANF
/* COMMON */ val vfscanfCalls = cpg.call("vfscanf").l

/* PROGRAM */ val vfscanfCallsFileContent = getCallArgWithIdxEq(vfscanfCalls, 3)
/* PROGRAM */ vfscanfCallsFileContent.length


// FSCANF
/* COMMON */ val fscanfCalls = cpg.call("fscanf").l

/* PROGRAM */ val fscanfCallsFileContent = getCallArgWithIdxEqOrGr(fscanfCalls, 3)
/* PROGRAM */ fscanfCallsFileContent.length


// FGETS
/* COMMON */ val fgetsCalls = cpg.call("fgets").l

/* PROGRAM */ val fgetsCallsFileContent = getCallArgWithIdxEqOrGr(fgetsCalls, 1)
/* PROGRAM */ fgetsCallsFileContent.length


// GETLINE
/* COMMON */ val getlineCalls = cpg.call("getline").l

/* PROGRAM */ val getlineCallsFileContent = getCallArgWithIdxEqOrGr(getlineCalls, 1)
/* PROGRAM */ getlineCallsFileContent.length


// GETDELIM
/* COMMON */ val getdelimCalls = cpg.call("getdelim").l

/* PROGRAM */ val getdelimCallsFileContent = getCallArgWithIdxEqOrGr(getdelimCalls, 1)
/* PROGRAM */ getdelimCallsFileContent.length


// GETW
/* COMMON */ val getwCalls = cpg.call("getw").l

/* PROGRAM */ val getwCallsFileContent = getCallReturnValueOfType(getwCalls, "int")
/* PROGRAM */ getwCallsFileContent.length


// FGETC
/* COMMON */ val fgetcCalls = cpg.call("fgetc").l

/* PROGRAM */ val fgetcCallsFileContent = getCallReturnValueOfType(fgetcCalls, "int")
/* PROGRAM */ fgetcCallsFileContent.length


// GETC
/* COMMON */ val getcCalls = cpg.call("getc").l

/* PROGRAM */ val getcCallsFileContent = getCallReturnValueOfType(getcCalls, "int")
/* PROGRAM */ getcCallsFileContent.length


// FUNCTIONS ALWAYS READING FROM STDIN
// SCANF
/* COMMON */ val scanfCalls = cpg.call("scanf").l

/* PROGRAM */ val scanfCallsFileContent = getCallArgWithIdxEqOrGr(scanfCalls, 2)
/* PROGRAM */ scanfCallsFileContent.length

// GETS
/* COMMON */ val getsCalls = cpg.call("gets").l

/* PROGRAM */ val getsCallsFileContent = getCallArgWithIdxEq(getsCalls, 1)
/* PROGRAM */ getsCallsFileContent.length

// VSCANF
/* COMMON */ val vscanfCalls = cpg.call("vscanf").l

/* PROGRAM */ val vscanfCallsFileContent = getCallArgWithIdxEq(vscanfCalls, 2)
/* PROGRAM */ vscanfCallsFileContent.length

// GETCHAR
/* COMMON */ val getcharCalls = cpg.call("getchar").l

/* PROGRAM */ val getcharCallsFileContent = getCallReturnValueOfType(getcharCalls, "int")
/* PROGRAM */ getcharCallsFileContent.length

// READ CONTENT
/* PROGRAM */ val readContent = readCallsFileContent ::: freadCallsFileContent ::: vfscanfCallsFileContent ::: fscanfCallsFileContent ::: fgetsCallsFileContent ::: getlineCallsFileContent ::: getdelimCallsFileContent ::: getwCallsFileContent ::: fgetcCallsFileContent ::: getcCallsFileContent ::: scanfCallsFileContent ::: getsCallsFileContent ::: vscanfCallsFileContent ::: getcharCallsFileContent
/* PROGRAM */ readContent.length