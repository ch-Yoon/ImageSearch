package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.helper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ch.yoon.kakao.pay.imagesearch.repository.model.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.ImageSortType;

/**
 * Creator : ch-yoon
 * Date : 2019-08-03.
 */
public class ImageSearchInspector {

    private final int START_PAGE_NUMBER;
    private final int MAX_PAGE_NUMBER;
    private final int MAX_REQUIRED_DATA_SIZE;
    private final int REQUIRED_DATA_SIZE_MULTIPLE;
    private final int PRELOAD_ALLOW_LINE_COUNT;

    @NonNull
    private final ApproveRequestLog approveRequestLog = new ApproveRequestLog();

    @Nullable
    private OnImageSearchApproveListener onImageSearchApproveListener;

    public ImageSearchInspector(int startPageNumber,
                                int maxPageNumber,
                                int maxRequiredDataSize,
                                int requiredDataSizeMultiple,
                                int preloadAllowLineCount) {
        START_PAGE_NUMBER = startPageNumber;
        MAX_PAGE_NUMBER = maxPageNumber;
        MAX_REQUIRED_DATA_SIZE = maxRequiredDataSize;
        REQUIRED_DATA_SIZE_MULTIPLE = requiredDataSizeMultiple;
        PRELOAD_ALLOW_LINE_COUNT = preloadAllowLineCount;
    }

    public void observeImageSearchApprove(@Nullable OnImageSearchApproveListener listener) {
        onImageSearchApproveListener = listener;
    }

    public void submitFirstImageSearchRequest(@NonNull String keyword,
                                              @NonNull ImageSortType imageSortType,
                                              int countOfItemInLine) {
        approveFirstImageSearch(keyword, imageSortType, countOfItemInLine);
    }

    public void submitPreloadRequest(int displayPosition,
                                     int dataTotalSize,
                                     @NonNull ImageSortType imageSortType,
                                     int countOfItemInLine) {
        if(isPreloadPossible(displayPosition, dataTotalSize, countOfItemInLine)) {
            if(isNotMaxPage()) {
                approvePreload(imageSortType, dataTotalSize, countOfItemInLine);
            }
        }
    }

    private void approveFirstImageSearch(String keyword,
                                         ImageSortType imageSortType,
                                         int countOfItemInLine) {
        initApproveRequestLog();
        approveImageSearch(keyword, imageSortType, 0, countOfItemInLine);
    }

    private void initApproveRequestLog() {
        approveRequestLog.setKeyword("");
        approveRequestLog.setDataTotalSize(0);
        approveRequestLog.setPageNumber(START_PAGE_NUMBER - 1);
    }

    private void approvePreload(ImageSortType imageSortType,
                                int dataTotalSize,
                                int countOfItemInLine) {
        final String keyword = approveRequestLog.getKeyword();
        approveImageSearch(keyword, imageSortType, dataTotalSize, countOfItemInLine);
    }

    private boolean isPreloadPossible(int displayPosition,
                                      int dataTotalSize,
                                      int countOfItemInLine) {
        final int preloadAllowLimit = dataTotalSize - (countOfItemInLine * PRELOAD_ALLOW_LINE_COUNT);
        final int previousDataTotalSize = approveRequestLog.getDataTotalSize();

        return dataTotalSize > previousDataTotalSize && displayPosition > preloadAllowLimit;
    }

    private boolean isNotMaxPage() {
        return approveRequestLog.getPageNumber() < MAX_PAGE_NUMBER;
    }

    private void approveImageSearch(String keyword,
                                    ImageSortType imageSortType,
                                    int dataTotalSize,
                                    int countOfItemInLine) {
        final int requestPageNumber = approveRequestLog.getPageNumber() + 1;
        final int requiredDataSize = calculateRequiredDataSize(countOfItemInLine);

        recordApproveRequest(keyword, dataTotalSize, requestPageNumber);

        if(onImageSearchApproveListener != null) {
            onImageSearchApproveListener.onImageSearchApprove(
                new ImageSearchRequest(
                    keyword,
                    imageSortType,
                    requestPageNumber,
                    requiredDataSize
                )
            );
        }
    }

    private void recordApproveRequest(String keyword, int dataTotalSize, int pageNumber) {
        approveRequestLog.setKeyword(keyword);
        approveRequestLog.setDataTotalSize(dataTotalSize);
        approveRequestLog.setPageNumber(pageNumber);
    }

    private int calculateRequiredDataSize(int countOfItemInLine) {
        int requiredDataSize = countOfItemInLine * REQUIRED_DATA_SIZE_MULTIPLE;
        if(requiredDataSize > MAX_REQUIRED_DATA_SIZE) {
            requiredDataSize = MAX_REQUIRED_DATA_SIZE;
        }

        return requiredDataSize;
    }

}
