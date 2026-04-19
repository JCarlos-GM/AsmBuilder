package asmbuilder;

// Guarda todas las opciones que el usuario pasó por la terminal
public class Options {

    private String  fileName     = "output.asm";
    private String  outputPath   = null;   // null = directorio actual
    private boolean createFolder = false;  // crea subcarpeta y copia tasm/tlink

    // Templates genericos
    private boolean print      = false;
    private boolean forLoop    = false;
    private boolean doWhile    = false;
    private boolean ifCond     = false;
    private boolean switchCond = false;
    private boolean vars       = false;

    // Templates genericos adicionales
    private boolean whileLoop = false;
    private boolean array     = false;

    // Templates especificos
    private boolean cursor  = false;
    private boolean screen  = false;
    private boolean color   = false;
    private boolean delay   = false;
    private boolean stack   = false;
    private boolean string  = false;
    private boolean upper   = false;
    private boolean input   = false;
    private boolean rect     = false;
    private boolean diagonal = false;
    private boolean rectSwap = false;
    private boolean alphabet = false;

    public String getFileName()                   { return fileName; }
    public void   setFileName(String n)           { this.fileName = n; }

    public String getOutputPath()                 { return outputPath; }
    public void   setOutputPath(String v)         { this.outputPath = v; }

    public boolean isCreateFolder()               { return createFolder; }
    public void    setCreateFolder(boolean v)     { this.createFolder = v; }

    public boolean isPrint()                { return print; }
    public void    setPrint(boolean v)      { this.print = v; }

    public boolean isForLoop()              { return forLoop; }
    public void    setForLoop(boolean v)    { this.forLoop = v; }

    public boolean isDoWhile()              { return doWhile; }
    public void    setDoWhile(boolean v)    { this.doWhile = v; }

    public boolean isIfCond()               { return ifCond; }
    public void    setIfCond(boolean v)     { this.ifCond = v; }

    public boolean isSwitchCond()           { return switchCond; }
    public void    setSwitchCond(boolean v) { this.switchCond = v; }

    public boolean isVars()                 { return vars; }
    public void    setVars(boolean v)       { this.vars = v; }

    public boolean isWhileLoop()              { return whileLoop; }
    public void    setWhileLoop(boolean v)    { this.whileLoop = v; }

    public boolean isArray()                  { return array; }
    public void    setArray(boolean v)        { this.array = v; }

    public boolean isCursor()               { return cursor; }
    public void    setCursor(boolean v)     { this.cursor = v; }

    public boolean isScreen()               { return screen; }
    public void    setScreen(boolean v)     { this.screen = v; }

    public boolean isColor()                { return color; }
    public void    setColor(boolean v)      { this.color = v; }

    public boolean isDelay()                { return delay; }
    public void    setDelay(boolean v)      { this.delay = v; }

    public boolean isStack()                { return stack; }
    public void    setStack(boolean v)      { this.stack = v; }

    public boolean isString()               { return string; }
    public void    setString(boolean v)     { this.string = v; }

    public boolean isUpper()                { return upper; }
    public void    setUpper(boolean v)      { this.upper = v; }

    public boolean isInput()                { return input; }
    public void    setInput(boolean v)      { this.input = v; }

    public boolean isRect()                 { return rect; }
    public void    setRect(boolean v)       { this.rect = v; }

    public boolean isDiagonal()             { return diagonal; }
    public void    setDiagonal(boolean v)   { this.diagonal = v; }

    public boolean isRectSwap()             { return rectSwap; }
    public void    setRectSwap(boolean v)   { this.rectSwap = v; }

    public boolean isAlphabet()             { return alphabet; }
    public void    setAlphabet(boolean v)   { this.alphabet = v; }
}
