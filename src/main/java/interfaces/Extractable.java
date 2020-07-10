package interfaces;

import extractor.Document;
import extractor.DocumentHandler;
import org.apache.pdfbox.pdmodel.PDDocument;

public abstract class Extractable implements PDFX {
    public abstract void importDocument(DocumentHandler handler, String path);
    public abstract void exportDocument(Document document, String path);

}