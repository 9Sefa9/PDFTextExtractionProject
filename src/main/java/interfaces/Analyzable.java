package interfaces;

public interface Analyzable extends PDFX {
    /**
     * startet die methode analyze
     */
    void start();
    /**
     * Die eigentliche Analyse des PDF Dokuments.
     */
    void analyze();

}
