package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.helper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSortType;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

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

    public void observeImageSearchApprove(@Nullable final OnImageSearchApproveListener approveListener) {
        onImageSearchApproveListener = approveListener;
    }

    public void submitFirstImageSearchRequest(@NonNull final String keyword,
                                              @NonNull final ImageSortType imageSortType) {
        approveFirstImageSearch(keyword, imageSortType);
    }

    public void submitPreloadRequest(final int displayPosition,
                                     final int dataTotalSize,
                                     final @NonNull ImageSortType imageSortType,
                                     final int countOfItemInLine) {
        if(isPreloadPossible(displayPosition, dataTotalSize, countOfItemInLine)) {
            approvePreload(imageSortType, dataTotalSize);
        }
    }

    public void submitRetryRequest(@NonNull final ImageSortType imageSortType) {
        approveImageSearchRetry(imageSortType);
    }

    private void approveFirstImageSearch(final String keyword,
                                         final ImageSortType imageSortType) {
        initApproveRequestLog();
        approveImageSearch(keyword, imageSortType, 0);
    }

    private void initApproveRequestLog() {
        approveRequestLog.setKeyword("");
        approveRequestLog.setDataTotalSize(0);
        approveRequestLog.setPageNumber(START_PAGE_NUMBER - 1);
    }

    private void approvePreload(final ImageSortType imageSortType,
                                final int dataTotalSize) {
        final String keyword = approveRequestLog.getKeyword();
        approveImageSearch(keyword, imageSortType, dataTotalSize);
    }

    private boolean isPreloadPossible(final int displayPosition,
                                      final int dataTotalSize,
                                      final int countOfItemInLine) {
        return isNotMaxPage() &&
            isAllowPreloadRange(displayPosition, dataTotalSize, countOfItemInLine);
    }

    private boolean isNotMaxPage() {
        return approveRequestLog.getPageNumber() < MAX_PAGE_NUMBER;
    }

    private boolean isAllowPreloadRange(final int displayPosition,
                                        final int dataTotalSize,
                                        final int countOfItemInLine) {
        final int preloadAllowLimit = dataTotalSize - (countOfItemInLine * PRELOAD_ALLOW_LINE_MULTIPLE);
        final int previousDataTotalSize = approveRequestLog.getDataTotalSize();

        return dataTotalSize > previousDataTotalSize && displayPosition > preloadAllowLimit;
    }

    private void approveImageSearchRetry(final ImageSortType imageSortType) {
        final String keyword = approveRequestLog.getKeyword();
        final int pageNumber = approveRequestLog.getPageNumber();
        sendApproveToListener(keyword, imageSortType, pageNumber);
    }

    private void approveImageSearch(final String keyword,
                                    final ImageSortType imageSortType,
                                    final int dataTotalSize) {
        final int requestPageNumber = approveRequestLog.getPageNumber() + 1;

        recordApproveRequest(keyword, dataTotalSize, requestPageNumber);
        sendApproveToListener(keyword, imageSortType, requestPageNumber);
    }

    private void sendApproveToListener(final String keyword,
                                       final ImageSortType imageSortType,
                                       final int pageNumber) {
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

    private void recordApproveRequest(final String keyword,
                                      final int dataTotalSize,
                                      final int pageNumber) {
        approveRequestLog.setKeyword(keyword);
        approveRequestLog.setDataTotalSize(dataTotalSize);
        approveRequestLog.setPageNumber(pageNumber);
    }

}
