package interfaces;

public abstract class Analyzable implements PDFX {
    /**
     * Die eigentliche Analyse des PDF Dokuments.
     */
    abstract protected void analyze();
    /**
     * startet die methode analyze
     */
    abstract public void start();
}
