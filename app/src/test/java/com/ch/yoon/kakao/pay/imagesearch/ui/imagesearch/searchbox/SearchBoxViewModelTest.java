package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.ch.yoon.kakao.pay.imagesearch.R;
import com.ch.yoon.kakao.pay.imagesearch.RxSchedulerRule;
import com.ch.yoon.kakao.pay.imagesearch.repository.ImageRepository;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.entity.SearchLog;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, TextUtils.class})
public class SearchBoxViewModelTest {

    private static final String DELETE_SUCCESS_MESSAGE = "삭제 완료";
    private static final String EMPTY_KEYWORD_MESSAGE = "검색어를 입력해주세요";

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Rule
    public RxSchedulerRule rxSchedulerRule = new RxSchedulerRule();

    @Mock
    private Application mockApplication;
    @Mock
    private ImageRepository mockImageRepository;
    @Mock
    private Observer<Void> mockVoidObserver;

    private SearchBoxViewModel searchBoxViewModel;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        initApplication();
        initSearchBoxViewModel();
        initUtils();
    }

    private void initApplication() {
        when(mockApplication.getString(R.string.success_all_image_info_delete_by_keyword)).thenReturn(DELETE_SUCCESS_MESSAGE);
        when(mockApplication.getString(R.string.empty_keyword_guide)).thenReturn(EMPTY_KEYWORD_MESSAGE);
    }

    private void initSearchBoxViewModel() {
        searchBoxViewModel = new SearchBoxViewModel(mockApplication, mockImageRepository);
    }

    private void initUtils() {
        mockStatic(Log.class);
        when(Log.d(any(String.class), any(String.class))).thenReturn(0);
        
        mockStatic(TextUtils.class);
        when(TextUtils.isEmpty(any(CharSequence.class))).thenAnswer((Answer<Boolean>) invocation -> {
            CharSequence text = invocation.getArgument(0);
            if(text instanceof String) {
                return text.length() == 0;
            } else {
                return false;
            }
        });
    }

    @Test
    public void 키워드_검색_버튼_클릭시_입력한_키워드가_반영되는지_테스트() {
        // given
        when(mockImageRepository.insertOrUpdateSearchLog(any(String.class)))
                .thenReturn(Single.just(new SearchLog("테스트", 1)));

        // when
        searchBoxViewModel.clickSearchButton("테스트");

        // then
        assertEquals(searchBoxViewModel.observeSearchKeyword().getValue(), "테스트");
    }

    @Test
    public void 키워드_검색_버튼_클릭시_키워드가_비어있다면_거절_메시지가_반영되는지_테스트() {
        // when
        searchBoxViewModel.clickSearchButton("");

        // then
        assertEquals(searchBoxViewModel.observeShowMessage().getValue(), EMPTY_KEYWORD_MESSAGE);
    }

    @Test
    public void 검색상자_클릭시_수신한_검색_목록이_반영되는지_테스트() {
        // given
        List<SearchLog> expectedList = createVirtualSearchLogList(3);
        when(mockImageRepository.requestSearchLogList()).thenReturn(Single.just(expectedList));

        // when
        searchBoxViewModel.clickSearchBox();

        // then
        assertEquals(searchBoxViewModel.observeSearchLogList().getValue(), expectedList);
    }

    @Test
    public void 검색상자_클릭시_수신한_검색_목록이_내림차순으로_정렬되는지_테스트() {
        // given
        List<SearchLog> searchLogList = createVirtualSearchLogList(3);

        List<SearchLog> expectedList = createVirtualSearchLogList(3);
        Collections.sort(expectedList);

        when(mockImageRepository.requestSearchLogList()).thenReturn(Single.just(searchLogList));

        // when
        searchBoxViewModel.clickSearchBox();

        // then
        assertEquals(searchBoxViewModel.observeSearchLogList().getValue(), expectedList);
    }

    @Test
    public void 검색상자_클릭시_포커스를_갖는지_테스트() {
        // given
        when(mockImageRepository.requestSearchLogList()).thenReturn(Single.just(new ArrayList<>()));

        // when
        searchBoxViewModel.clickSearchBox();

        //then
        assertEquals(searchBoxViewModel.observeSearchBoxFocus().getValue(), true);
    }

    @Test
    public void 키워드_검색_버튼_클릭시_키워드가_비어있지_않다면_포커스를_잃는지_테스트() {
        // given
        when(mockImageRepository.requestSearchLogList()).thenReturn(Single.just(new ArrayList<>()));

        when(mockImageRepository.insertOrUpdateSearchLog(any(String.class)))
                .thenReturn(Single.just(new SearchLog("테스트", 1)));

        // when
        searchBoxViewModel.clickSearchBox();
        searchBoxViewModel.clickSearchButton("테스트");

        // then
        assertEquals(searchBoxViewModel.observeSearchBoxFocus().getValue(), false);
    }

    @Test
    public void 키워드_검색_버튼_클릭시_수신한_검색_리스트가_비어있어도_포커스를_유지하는지_테스트() {
        // given
        when(mockImageRepository.requestSearchLogList()).thenReturn(Single.just(new ArrayList<>()));

        // when
        searchBoxViewModel.clickSearchBox();

        // then
        assertEquals(searchBoxViewModel.observeSearchBoxFocus().getValue(), true);
    }

    @Test
    public void 키워드_검색_버튼_클릭시_키워드가_비어있다면_갖고있던_포커스를_계속_유지하는지_테스트() {
        // given
        when(mockImageRepository.requestSearchLogList()).thenReturn(Single.just(new ArrayList<>()));

        // when
        searchBoxViewModel.clickSearchBox();
        searchBoxViewModel.clickSearchButton("");

        //then
        assertEquals(searchBoxViewModel.observeSearchBoxFocus().getValue(), true);
    }

    @Test
    public void 배경_클릭시_포커스를_잃는지_테스트() {
        // given
        when(mockImageRepository.requestSearchLogList()).thenReturn(Single.just(new ArrayList<>()));

        // when
        searchBoxViewModel.clickSearchBox();
        searchBoxViewModel.clickBackground();

        //then
        assertEquals(searchBoxViewModel.observeSearchBoxFocus().getValue(), false);
    }

    @Test
    public void 뒤로가기_클릭시_갖고있던_포커스를_잃는지_테스트() {
        // given
        when(mockImageRepository.requestSearchLogList()).thenReturn(Single.just(new ArrayList<>()));

        // when
        searchBoxViewModel.clickSearchBox();
        searchBoxViewModel.clickBackPress();

        // then
        assertEquals(searchBoxViewModel.observeSearchBoxFocus().getValue(), false);
    }

    @Test
    public void 뒤로가기_두번_클릭시_포커스가_존재했다면_종료_이벤트가_호출되는지_테스트() {
        // given
        when(mockImageRepository.requestSearchLogList()).thenReturn(Single.just(new ArrayList<>()));
        searchBoxViewModel.observeSearchBoxFinish().observeForever(mockVoidObserver);

        // when
        searchBoxViewModel.clickSearchBox();
        searchBoxViewModel.clickBackPress();
        searchBoxViewModel.clickBackPress();

        // then
        verify(mockVoidObserver, times(1)).onChanged(any());
    }

    @Test
    public void 뒤로가기_클릭시_포커스가_없다면_종료_이벤트가_호출되는지_테스트() {
        // given
        searchBoxViewModel.observeSearchBoxFinish().observeForever(mockVoidObserver);

        // when
        searchBoxViewModel.clickBackPress();

        // then
        verify(mockVoidObserver, times(1)).onChanged(any());
    }

    @Test
    public void 키워드_삭제_버튼_클릭시_레파지토리에_삭제_요청을_하는지_테스트() {
        // given
        when(mockImageRepository.deleteSearchLog("테스트"))
            .thenReturn(Completable.complete());

        // when
        searchBoxViewModel.clickKeywordDeleteButton("테스트");

        // then
        verify(mockImageRepository, times(1))
            .deleteSearchLog("테스트");
    }

    @Test
    public void 키워드_삭제_버튼_클릭시_보유하던_검색_기록_목록에서_제거하는지_테스트() {
        // given
        List<SearchLog> searchLogList = createVirtualSearchLogList(3);
        List<SearchLog> expectedList = createVirtualSearchLogList(3);
        Collections.sort(expectedList);
        expectedList.remove(0);

        when(mockImageRepository.requestSearchLogList()).thenReturn(Single.just(searchLogList));

        when(mockImageRepository.deleteSearchLog(any(String.class)))
            .thenReturn(Completable.complete());

        // when
        searchBoxViewModel.clickSearchBox();
        searchBoxViewModel.clickKeywordDeleteButton(searchLogList.get(0).getKeyword());

        // then
        assertEquals(searchBoxViewModel.observeSearchLogList().getValue(), expectedList);
    }

    @Test
    public void 키워드_검색_버튼_클릭시_검색기록_가장_앞쪽에_삽입되는지_테스트() {
        // given
        when(mockImageRepository.requestSearchLogList())
            .thenReturn(Single.just(createVirtualSearchLogList(3)));

        when(mockImageRepository.insertOrUpdateSearchLog("테스트3"))
            .thenReturn(Single.just(new SearchLog("테스트3", 3)));

        // when
        searchBoxViewModel.clickSearchBox();

        // then
        List<SearchLog> beforeList = searchBoxViewModel.observeSearchLogList().getValue();
        assertEquals(beforeList.size(), 3);
        assertEquals(beforeList.get(0).getKeyword(), "테스트2");

        // when
        searchBoxViewModel.clickSearchButton("테스트3");

        // then
        List<SearchLog> afterList = searchBoxViewModel.observeSearchLogList().getValue();
        assertEquals(afterList.size(), 4);
        assertEquals(afterList.get(0).getKeyword(), "테스트3");
    }

    @Test
    public void 키워드_검색_버튼_클릭시_기존에_검색했던_키워드라면_목록의_가장_앞쪽으로_이동시키는지_테스트() {
        // given
        when(mockImageRepository.requestSearchLogList())
            .thenReturn(Single.just(createVirtualSearchLogList(3)));

        when(mockImageRepository.insertOrUpdateSearchLog("테스트0"))
            .thenReturn(Single.just(new SearchLog("테스트0", 4)));

        // when
        searchBoxViewModel.clickSearchBox();

        // then
        List<SearchLog> beforeList = searchBoxViewModel.observeSearchLogList().getValue();
        assertEquals(beforeList.get(0).getKeyword(), "테스트2");
        assertEquals(beforeList.size(), 3);

        // when
        searchBoxViewModel.clickSearchButton("테스트0");

        // then
        List<SearchLog> afterList = searchBoxViewModel.observeSearchLogList().getValue();
        assertEquals(afterList.get(0).getKeyword(), "테스트0");
        assertEquals(afterList.size(), 3);
    }

    private List<SearchLog> createVirtualSearchLogList(int size) {
        ArrayList<SearchLog> searchLogList = new ArrayList<>();
        for(int i=0; i<size; i++) {
            searchLogList.add(new SearchLog("테스트" + i, i));
        }

        return searchLogList;
    }

}
