package interfaces;

import extractor.Document;
import extractor.DocumentParser;

public abstract class Extractable implements PDFX {
    public abstract void importDocument(DocumentParser handler, String path);
    public abstract void exportDocument(Document document, String path);

}