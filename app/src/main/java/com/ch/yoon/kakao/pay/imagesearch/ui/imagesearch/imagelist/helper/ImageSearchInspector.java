package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.helper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSortType;

/**
 * Creator : ch-yoon
 * Date : 2019-08-03.
 */
public class ImageSearchInspector {

    private final int START_PAGE_NUMBER;
    private final int MAX_PAGE_NUMBER;
    private final int REQUIRED_DATA_SIZE;
    private final int PRELOAD_ALLOW_LINE_MULTIPLE;

    @NonNull
    private final ApproveRequestLog approveRequestLog = new ApproveRequestLog();

    @Nullable
    private OnImageSearchApproveListener onImageSearchApproveListener;

    public ImageSearchInspector(int startPageNumber,
                                int maxPageNumber,
                                int requiredDataSize,
                                int preloadAllowLineMultiple) {
        START_PAGE_NUMBER = startPageNumber;
        MAX_PAGE_NUMBER = maxPageNumber;
        REQUIRED_DATA_SIZE = requiredDataSize;
        PRELOAD_ALLOW_LINE_MULTIPLE = preloadAllowLineMultiple;
    }

    public void observeImageSearchApprove(@Nullable OnImageSearchApproveListener approveListener) {
        onImageSearchApproveListener = approveListener;
    }

    public void submitFirstImageSearchRequest(@NonNull String keyword,
                                              @NonNull ImageSortType imageSortType) {
        approveFirstImageSearch(keyword, imageSortType);
    }

    public void submitPreloadRequest(int displayPosition,
                                     int dataTotalSize,
                                     @NonNull ImageSortType imageSortType,
                                     int countOfItemInLine) {
        if(isPreloadPossible(displayPosition, dataTotalSize, countOfItemInLine)) {
            approvePreload(imageSortType, dataTotalSize);
        }
    }

    public void submitRetryRequest(@NonNull ImageSortType imageSortType) {
        approveImageSearchRetry(imageSortType);
    }

    private void approveFirstImageSearch(String keyword,
                                         ImageSortType imageSortType) {
        initApproveRequestLog();
        approveImageSearch(keyword, imageSortType, 0);
    }

    private void initApproveRequestLog() {
        approveRequestLog.setKeyword("");
        approveRequestLog.setDataTotalSize(0);
        approveRequestLog.setPageNumber(START_PAGE_NUMBER - 1);
    }

    private void approvePreload(ImageSortType imageSortType,
                                int dataTotalSize) {
        final String keyword = approveRequestLog.getKeyword();
        approveImageSearch(keyword, imageSortType, dataTotalSize);
    }

    private boolean isPreloadPossible(int displayPosition,
                                      int dataTotalSize,
                                      int countOfItemInLine) {
        return isNotMaxPage() &&
            isAllowPreloadRange(displayPosition, dataTotalSize, countOfItemInLine);
    }

    private boolean isNotMaxPage() {
        return approveRequestLog.getPageNumber() < MAX_PAGE_NUMBER;
    }

    private boolean isAllowPreloadRange(int displayPosition,
                                        int dataTotalSize,
                                        int countOfItemInLine) {
        final int preloadAllowLimit = dataTotalSize - (countOfItemInLine * PRELOAD_ALLOW_LINE_MULTIPLE);
        final int previousDataTotalSize = approveRequestLog.getDataTotalSize();

        return dataTotalSize > previousDataTotalSize && displayPosition > preloadAllowLimit;
    }

    private void approveImageSearchRetry(ImageSortType imageSortType) {
        final String keyword = approveRequestLog.getKeyword();
        final int pageNumber = approveRequestLog.getPageNumber();
        sendApproveToListener(keyword, imageSortType, pageNumber);
    }

    private void approveImageSearch(String keyword,
                                    ImageSortType imageSortType,
                                    int dataTotalSize) {
        final int requestPageNumber = approveRequestLog.getPageNumber() + 1;

        recordApproveRequest(keyword, dataTotalSize, requestPageNumber);
        sendApproveToListener(keyword, imageSortType, requestPageNumber);
    }

    private void sendApproveToListener(String keyword, ImageSortType imageSortType, int pageNumber) {
        if(onImageSearchApproveListener != null) {
            onImageSearchApproveListener.onImageSearchApprove(
                    new ImageSearchRequest(
                            keyword,
                            imageSortType,
                            pageNumber,
                            REQUIRED_DATA_SIZE
                    )
            );
        }
    }

    private void recordApproveRequest(String keyword, int dataTotalSize, int pageNumber) {
        approveRequestLog.setKeyword(keyword);
        approveRequestLog.setDataTotalSize(dataTotalSize);
        approveRequestLog.setPageNumber(pageNumber);
    }

}
