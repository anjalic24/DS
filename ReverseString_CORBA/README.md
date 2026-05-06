## PS 3 - Run Instructions (Linux, from scratch)

### Prerequisites
- Java 8 JDK installed (CORBA tools like `idlj` and `orbd` are included in JDK 8).
- `java` and `javac` available in PATH.

### 1) Generate CORBA stubs/skeletons
From the PS 3 folder:

```
idlj -fall Reverse.idl
```

This creates the `ReverseModule/` sources.

### 2) Compile
From the PS 3 folder:

```
javac *.java ReverseModule/*.java
```

### 3) Run
Open three terminals in the PS 3 folder.

Terminal 1 (start Name Service):

```
orbd -ORBInitialPort 1050 -ORBInitialHost localhost
```

Terminal 2 (start server):

```
java Server -ORBInitialPort 1050 -ORBInitialHost localhost
```

Terminal 3 (run client):

```
java Client -ORBInitialPort 1050 -ORBInitialHost localhost
```

You should see "Server Ready" in the server terminal and the client will prompt for a string and print the reversed response.
