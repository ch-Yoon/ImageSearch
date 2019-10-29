package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.helper;

import com.ch.yoon.kakao.pay.imagesearch.data.remote.kakao.request.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.data.remote.kakao.request.ImageSortType;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class ImageSearchInspectorTest {

    @Test
    public void 최초_이미지_검색_요청시_승인을_하는지_테스트() {
        // given
        ImageSearchInspector imageSearchInspector = new ImageSearchInspector(1, 10, 20, 10);

        ArrayList<ImageSearchRequest> receivedList = new ArrayList<>();
        imageSearchInspector.observeImageSearchApprove(receivedList::add);

        // when
        imageSearchInspector.submitFirstImageSearchRequest("테스트", ImageSortType.ACCURACY);

        // then
        ArrayList<ImageSearchRequest> expectedList = new ArrayList<>();
        expectedList.add(new ImageSearchRequest("테스트", ImageSortType.ACCURACY, 1, 20, true));

        Assert.assertEquals(receivedList, expectedList);
    }

    @Test
    public void 프리로드_요청시_마지막_페이지보다_크다면_승인을_안하는지_테스트() {
        // given
        ImageSearchInspector imageSearchInspector = new ImageSearchInspector(1, 1, 20, 10);

        ArrayList<ImageSearchRequest> receivedList = new ArrayList<>();
        imageSearchInspector.observeImageSearchApprove(receivedList::add);

        // when
        imageSearchInspector.submitFirstImageSearchRequest("테스트", ImageSortType.ACCURACY);
        imageSearchInspector.submitPreloadRequest(9, 10, ImageSortType.ACCURACY, 2);

        // then
        Assert.assertEquals(receivedList.size(), 1);
    }

    @Test
    public void 프리로드_요청시_마지막_페이지보다_작고_프리로드_허용조건에_만족한다면_승인을_하는지_테스트() {
        // given
        ImageSearchInspector imageSearchInspector = new ImageSearchInspector(1, 10, 20, 10);

        ArrayList<ImageSearchRequest> receivedList = new ArrayList<>();
        imageSearchInspector.observeImageSearchApprove(receivedList::add);

        // when
        imageSearchInspector.submitFirstImageSearchRequest("테스트", ImageSortType.ACCURACY);
        imageSearchInspector.submitPreloadRequest(9, 10, ImageSortType.ACCURACY, 2);

        // then
        ArrayList<ImageSearchRequest> expectedList = new ArrayList<>();
        expectedList.add(new ImageSearchRequest("테스트", ImageSortType.ACCURACY, 1, 20, true));
        expectedList.add(new ImageSearchRequest("테스트", ImageSortType.ACCURACY, 2, 20, false));

        Assert.assertEquals(expectedList, receivedList);
    }

    @Test
    public void 프리로드_요청시_마지막_페이지보다_작아도_허용조건을_만족하지_못한다면_승인을_안하는지_테스트() {
        // given
        ImageSearchInspector imageSearchInspector = new ImageSearchInspector(1, 10, 20, 2);

        ArrayList<ImageSearchRequest> receivedList = new ArrayList<>();
        imageSearchInspector.observeImageSearchApprove(receivedList::add);

        // when
        imageSearchInspector.submitFirstImageSearchRequest("테스트", ImageSortType.ACCURACY);
        imageSearchInspector.submitPreloadRequest(1, 10, ImageSortType.ACCURACY, 2);

        // then
        ArrayList<ImageSearchRequest> expectedList = new ArrayList<>();
        expectedList.add(new ImageSearchRequest("테스트", ImageSortType.ACCURACY, 1, 20, true));

        Assert.assertEquals(receivedList, expectedList);
    }

    @Test
    public void 프리로드_요청시_직전_요청의_데이터크기와_같다면_승인을_안하는지_테스트() {
        // given
        ImageSearchInspector imageSearchInspector = new ImageSearchInspector(1, 10, 20, 2);

        ArrayList<ImageSearchRequest> receivedList = new ArrayList<>();
        imageSearchInspector.observeImageSearchApprove(receivedList::add);

        // when
        imageSearchInspector.submitFirstImageSearchRequest("테스트", ImageSortType.ACCURACY);
        imageSearchInspector.submitPreloadRequest(9, 10, ImageSortType.ACCURACY, 2);
        imageSearchInspector.submitPreloadRequest(9, 10, ImageSortType.ACCURACY, 2);

        // then
        ArrayList<ImageSearchRequest> expectedList = new ArrayList<>();
        expectedList.add(new ImageSearchRequest("테스트", ImageSortType.ACCURACY, 1, 20, true));
        expectedList.add(new ImageSearchRequest("테스트", ImageSortType.ACCURACY, 2, 20, false));

        Assert.assertEquals(receivedList, expectedList);
    }

    @Test
    public void 이미지요청_재시도시_직전에_승인된_요청과_동일한_승인을_하는지_테스트() {
        // given
        ImageSearchInspector imageSearchInspector = new ImageSearchInspector(1, 10, 20, 2);

        ArrayList<ImageSearchRequest> receivedList = new ArrayList<>();
        imageSearchInspector.observeImageSearchApprove(receivedList::add);

        // when
        imageSearchInspector.submitFirstImageSearchRequest("테스트", ImageSortType.ACCURACY);
        imageSearchInspector.submitPreloadRequest(9, 10, ImageSortType.ACCURACY, 2);
        imageSearchInspector.submitRetryRequest(ImageSortType.ACCURACY);

        // then
        int lastIndex = receivedList.size() - 1;
        int previousOfLastIndex = lastIndex - 1;
        Assert.assertEquals(receivedList.get(lastIndex), receivedList.get(previousOfLastIndex));
    }

}
