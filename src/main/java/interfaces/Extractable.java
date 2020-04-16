package interfaces;

import org.apache.pdfbox.pdmodel.PDDocument;

public abstract class Extractable implements PDFX {
    public abstract void importDocument(String path);
    public abstract void exportDocument(PDDocument pdfDocument, String path);
}