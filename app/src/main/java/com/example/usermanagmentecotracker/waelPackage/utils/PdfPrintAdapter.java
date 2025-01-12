package com.example.usermanagmentecotracker.waelPackage.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfPrintAdapter extends PrintDocumentAdapter {

    private Context context;
    private String documentContent;

    public PdfPrintAdapter(Context context, String documentContent) {
        this.context = context;
        this.documentContent = documentContent;
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes,
                         CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {

        PrintDocumentInfo info = new PrintDocumentInfo.Builder("historique.pdf")
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                .build();

        callback.onLayoutFinished(info, true);
    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination,
                        CancellationSignal cancellationSignal, WriteResultCallback callback) {
        try {
            // Créez un fichier PDF dans le répertoire de stockage de l'appareil
            File pdfFile = new File(context.getExternalFilesDir(null), "historique.pdf");
            FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);

            // Utiliser iText pour créer le document PDF
            Document document = new Document();
            PdfWriter.getInstance(document, fileOutputStream);

            document.open();
            document.add(new Paragraph(documentContent));
            document.close();

            // Notifier l'utilisateur que le PDF a été créé
            Toast.makeText(context, "PDF généré et sauvegardé dans: " + pdfFile.getAbsolutePath(), Toast.LENGTH_LONG).show();

            callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
        } catch (Exception e) {
            callback.onWriteFailed(e.toString());
        }
    }
}
