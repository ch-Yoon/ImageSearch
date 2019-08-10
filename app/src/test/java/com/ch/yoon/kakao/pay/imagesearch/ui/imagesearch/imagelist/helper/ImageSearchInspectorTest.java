package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.helper;

import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSortType;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ImageSearchInspectorTest {

    @Test
    public void 최초_이미지_검색_요청시_승인_요청을_받아야함() {
        // given
        ImageSearchInspector imageSearchInspector = new ImageSearchInspector(1, 10, 20, 10);

        // when & then
        ImageSearchRequest expectedImageSearchRequest = new ImageSearchRequest("테스트", ImageSortType.ACCURACY, 1, 20);
        imageSearchInspector.observeImageSearchApprove(
                imageSearchRequest -> Assert.assertEquals(imageSearchRequest, expectedImageSearchRequest),
                () -> Assert.assertEquals(expectedImageSearchRequest, null)
        );

        imageSearchInspector.submitFirstImageSearchRequest("테스트", ImageSortType.ACCURACY);
    }

    @Test
    public void 프리로드_요청시_마지막_페이지보다_크다면_승인이_안되야함() {
        // given
        ImageSearchInspector imageSearchInspector = new ImageSearchInspector(1, 1, 20, 10);

        // when
        final ArrayList<ImageSearchRequest> receivedList = new ArrayList<>();
        imageSearchInspector.observeImageSearchApprove(receivedList::add);
        imageSearchInspector.submitFirstImageSearchRequest("테스트", ImageSortType.ACCURACY);
        imageSearchInspector.submitPreloadRequest(9, 10, ImageSortType.ACCURACY, 2);

        // then
        Assert.assertEquals(receivedList.size(), 1);
    }
    
    @Test
    public void 프리로드_요청시_마지막_페이지보다_작고_프리로드_허용조건에_만족한다면_승인_요청을_받아야함() {
        // given
        ImageSearchInspector imageSearchInspector = new ImageSearchInspector(1, 10, 20, 10);
        ArrayList<ImageSearchRequest> expectedList = new ArrayList<>();
        expectedList.add(new ImageSearchRequest("테스트", ImageSortType.ACCURACY, 1, 20));
        expectedList.add(new ImageSearchRequest("테스트", ImageSortType.ACCURACY, 2, 20));

        // when
        final ArrayList<ImageSearchRequest> receivedList = new ArrayList<>();
        imageSearchInspector.observeImageSearchApprove(receivedList::add);
        imageSearchInspector.submitFirstImageSearchRequest("테스트", ImageSortType.ACCURACY);
        imageSearchInspector.submitPreloadRequest(9, 10, ImageSortType.ACCURACY, 2);

        // then
        Assert.assertTrue(isSame(expectedList, receivedList));
    }

    private boolean isSame(List<ImageSearchRequest> base, List<ImageSearchRequest> target) {
        if(base == null || target == null) {
            return false;
        }

        if(base.size() != target.size()) {
            return false;
        }

        for(int i=0; i<base.size(); i++) {
            if(!base.get(i).equals(target.get(i))) {
                return false;
            }
        }

        return true;
    }

}
