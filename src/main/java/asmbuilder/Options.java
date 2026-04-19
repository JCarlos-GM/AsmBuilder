package asmbuilder;

// Guarda todas las opciones que el usuario pasó por la terminal
public class Options {

    private String fileName = "output.asm";

    private boolean print    = false;
    private boolean forLoop  = false;
    private boolean doWhile  = false;
    private boolean ifCond   = false;
    private boolean switchCond = false;
    private boolean vars     = false;

    public String getFileName()           { return fileName; }
    public void   setFileName(String n)   { this.fileName = n; }

    public boolean isPrint()              { return print; }
    public void    setPrint(boolean v)    { this.print = v; }

    public boolean isForLoop()            { return forLoop; }
    public void    setForLoop(boolean v)  { this.forLoop = v; }

    public boolean isDoWhile()            { return doWhile; }
    public void    setDoWhile(boolean v)  { this.doWhile = v; }

    public boolean isIfCond()             { return ifCond; }
    public void    setIfCond(boolean v)   { this.ifCond = v; }

    public boolean isSwitchCond()         { return switchCond; }
    public void    setSwitchCond(boolean v){ this.switchCond = v; }

    public boolean isVars()               { return vars; }
    public void    setVars(boolean v)     { this.vars = v; }
}
