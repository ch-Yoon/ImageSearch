package com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch;

import androidx.annotation.NonNull;

import com.ch.yoon.kakao.pay.imagesearch.repository.local.entity.LocalImageDocument;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Creator : ch-yoon
 * Date : 2019-08-05.
 */
public class ImageDocumentConverter {

    public static List<LocalImageDocument> convertToLocalImageDocuments(@NonNull String keyword,
                                                                        int startDocumentNumber,
                                                                        @NonNull String imageSortType,
                                                                        @NonNull List<Document> documentList) {
        List<LocalImageDocument> localImageDocumentList = new ArrayList<>();
        for (int i = 0; i < documentList.size(); i++) {
            final Document document = documentList.get(i);
            final LocalImageDocument localImageDocument = new LocalImageDocument(
                keyword,
                startDocumentNumber + i + 1,
                imageSortType,
                document.getCollection(),
                document.getThumbnailUrl(),
                document.getImageUrl(),
                document.getWidth(),
                document.getHeight(),
                document.getDisplaySiteName(),
                document.getDocUrl(),
                document.getDateTime()
            );

            localImageDocumentList.add(localImageDocument);
        }

        return localImageDocumentList;
    }

    public static List<Document> convertToDocumentList(@NonNull List<LocalImageDocument> localImageDocumentList) {
        List<Document> documentList = new ArrayList<>();
        for(LocalImageDocument localImageDocument : localImageDocumentList) {
            Document document = new Document(
                localImageDocument.getCollection(),
                localImageDocument.getThumbnailUrl(),
                localImageDocument.getImageUrl(),
                localImageDocument.getWidth(),
                localImageDocument.getHeight(),
                localImageDocument.getDisplaySiteName(),
                localImageDocument.getDocUrl(),
                localImageDocument.getDateTime()
            );

            documentList.add(document);
        }

        return documentList;
    }

}
