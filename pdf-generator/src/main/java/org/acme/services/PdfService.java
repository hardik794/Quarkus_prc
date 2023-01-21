package org.acme.services;

import com.lowagie.text.DocumentException;

import java.io.File;
import java.io.IOException;

public class PdfService {
    private static final String PDF_RESOURCES = "/static/";

    public File generatePdf() throws IOException, DocumentException {
        Context context = getContext();
        String html = loadAndFillTemplate(context);
        return renderPdf(html);
    }

    private String loadAndFillTemplate(Context context) {
    }
}
