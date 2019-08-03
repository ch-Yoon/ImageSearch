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

    private static final int MAX_PAGE_NUMBER = 50;
    private static final int MAX_REQUIRE_DATA_SIZE = 80;

    private static final int REQUIRED_DATA_SIZE_MULTIPLE = 20;
    private static final int PRELOAD_ALLOW_MULTIPLE = 10;

    private final ApproveRequestLog approveRequestLog = new ApproveRequestLog();

    @Nullable
    private OnImageSearchApproveListener onImageSearchApproveListener;

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
                                     int countOfItemInLine,
                                     @NonNull ImageSortType imageSortType) {
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
        approveRequestLog.setPageNumber(0);
    }

    private void approvePreload(ImageSortType imageSortType,
                                int dataTotalSize,
                                int countOfItemInLine) {
        String keyword = approveRequestLog.getKeyword();
        approveImageSearch(keyword, imageSortType, dataTotalSize, countOfItemInLine);
    }

    private boolean isPreloadPossible(int displayPosition,
                                      int dataTotalSize,
                                      int countOfItemInLine) {
        int preloadAllowLimit = dataTotalSize - (countOfItemInLine * PRELOAD_ALLOW_MULTIPLE);
        int previousDataTotalSize = approveRequestLog.getDataTotalSize();

        return dataTotalSize > previousDataTotalSize && displayPosition > preloadAllowLimit;
    }

    private boolean isNotMaxPage() {
        return approveRequestLog.getPageNumber() < MAX_PAGE_NUMBER;
    }

    private void approveImageSearch(String keyword,
                                    ImageSortType imageSortType,
                                    int dataTotalSize,
                                    int countOfItemInLine) {
        int requestPageNumber = approveRequestLog.getPageNumber() + 1;
        int requiredDataSize = calculateRequiredDataSize(countOfItemInLine);

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
        if(requiredDataSize > MAX_REQUIRE_DATA_SIZE) {
            requiredDataSize = MAX_REQUIRE_DATA_SIZE;
        }

        return requiredDataSize;
    }

}
