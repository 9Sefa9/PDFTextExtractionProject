package extractor;

import interfaces.Extractable;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

public class Document implements Extractable {
    public PDDocument pdfDocument;
    public PDPageContentStream contentStream;
    public PDPage pdPage;
}
