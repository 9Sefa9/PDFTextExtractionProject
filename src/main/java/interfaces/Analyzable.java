package interfaces;

public interface Analyzable extends PDFX {
    /**
     * Die eigentliche Analyse des PDF Dokuments.
     */
    void analyze();
    /**
     * startet die methode analyze
     */
    void start();
}
