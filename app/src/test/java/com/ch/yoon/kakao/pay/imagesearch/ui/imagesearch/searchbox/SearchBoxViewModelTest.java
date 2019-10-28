package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.ch.yoon.kakao.pay.imagesearch.R;
import com.ch.yoon.kakao.pay.imagesearch.RxSchedulerRule;

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
import kotlin.Unit;

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
    private ImageSearchRepository mockImageSearchRepository;
    @Mock
    private Observer<Unit> mockVoidObserver;

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
        searchBoxViewModel = new SearchBoxViewModel(mockApplication, mockImageSearchRepository);
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
        when(mockImageSearchRepository.insertOrUpdateSearchLog(any(String.class)))
                .thenReturn(Single.just(new SearchLog("테스트", 1)));

        // when
        searchBoxViewModel.onClickSearchButton("테스트");

        // then
        assertEquals(searchBoxViewModel.getSearchKeyword().getValue(), "테스트");
    }

    @Test
    public void 키워드_검색_버튼_클릭시_키워드가_비어있다면_거절_메시지가_반영되는지_테스트() {
        // when
        searchBoxViewModel.onClickSearchButton("");

        // then
        assertEquals(searchBoxViewModel.getShowMessageEvent().getValue(), EMPTY_KEYWORD_MESSAGE);
    }

    @Test
    public void 이미지_로드가_되는지_테스트() {
        // given
        when(mockImageSearchRepository.requestSearchLogList()).thenReturn(Single.just(createVirtualSearchLogList(3)));

        // when
        searchBoxViewModel.loadSearchLogList();

        // then
        searchBoxViewModel.getSearchLogList().observeForever(receivedList -> {
            assertEquals(receivedList.size(), 3);
        });
    }

    @Test
    public void 이미지_로드시_내림차순으로_정렬되는지_테스트() {
        // given
        List<SearchLog> searchLogList = createVirtualSearchLogList(3);

        List<SearchLog> expectedList = createVirtualSearchLogList(3);
        Collections.sort(expectedList);

        when(mockImageSearchRepository.requestSearchLogList()).thenReturn(Single.just(searchLogList));

        // when
        searchBoxViewModel.loadSearchLogList();

        // then
        searchBoxViewModel.getSearchLogList().observeForever(receivedList -> {
            assertEquals(receivedList, expectedList);
        });

    }

    @Test
    public void 검색상자_클릭시_포커스를_갖는지_테스트() {
        // given
        when(mockImageSearchRepository.requestSearchLogList()).thenReturn(Single.just(new ArrayList<>()));

        // when
        searchBoxViewModel.onClickSearchBox();

        //then
        assertEquals(searchBoxViewModel.getSearchBoxFocus().getValue(), true);
    }

    @Test
    public void 키워드_검색_버튼_클릭시_키워드가_비어있지_않다면_포커스를_잃는지_테스트() {
        // given
        when(mockImageSearchRepository.requestSearchLogList()).thenReturn(Single.just(new ArrayList<>()));

        when(mockImageSearchRepository.insertOrUpdateSearchLog(any(String.class)))
                .thenReturn(Single.just(new SearchLog("테스트", 1)));

        // when
        searchBoxViewModel.loadSearchLogList();
        searchBoxViewModel.onClickSearchBox();
        searchBoxViewModel.onClickSearchButton("테스트");

        // then
        assertEquals(searchBoxViewModel.getSearchBoxFocus().getValue(), false);
    }

    @Test
    public void 리스트가_비어있어도_포커스를_갖는지_테스트() {
        // given
        when(mockImageSearchRepository.requestSearchLogList()).thenReturn(Single.just(new ArrayList<>()));

        // when
        searchBoxViewModel.loadSearchLogList();
        searchBoxViewModel.onClickSearchBox();

        // then
        assertEquals(searchBoxViewModel.getSearchBoxFocus().getValue(), true);
    }

    @Test
    public void 키워드_검색_버튼_클릭시_키워드가_비어있어도_갖고있던_포커스를_계속_유지하는지_테스트() {
        // given
        when(mockImageSearchRepository.requestSearchLogList()).thenReturn(Single.just(new ArrayList<>()));

        // when
        searchBoxViewModel.loadSearchLogList();
        searchBoxViewModel.onClickSearchBox();
        searchBoxViewModel.onClickSearchButton("");

        //then
        assertEquals(searchBoxViewModel.getSearchBoxFocus().getValue(), true);
    }

    @Test
    public void 배경_클릭시_포커스를_잃는지_테스트() {
        // given
        when(mockImageSearchRepository.requestSearchLogList()).thenReturn(Single.just(new ArrayList<>()));

        // when
        searchBoxViewModel.onClickSearchBox();
        searchBoxViewModel.onClickBackground();

        //then
        assertEquals(searchBoxViewModel.getSearchBoxFocus().getValue(), false);
    }

    @Test
    public void 뒤로가기_클릭시_갖고있던_포커스를_잃는지_테스트() {
        // given
        when(mockImageSearchRepository.requestSearchLogList()).thenReturn(Single.just(new ArrayList<>()));

        // when
        searchBoxViewModel.onClickSearchBox();
        searchBoxViewModel.onClickBackPressButton();

        // then
        assertEquals(searchBoxViewModel.getSearchBoxFocus().getValue(), false);
    }

    @Test
    public void 뒤로가기_두번_클릭시_포커스가_존재했다면_종료_이벤트가_호출되는지_테스트() {
        // given
        when(mockImageSearchRepository.requestSearchLogList()).thenReturn(Single.just(new ArrayList<>()));
        searchBoxViewModel.getSearchBoxFinishEvent().observeForever(mockVoidObserver);

        // when
        searchBoxViewModel.onClickSearchBox();
        searchBoxViewModel.onClickBackPressButton();
        searchBoxViewModel.onClickBackPressButton();

        // then
        verify(mockVoidObserver, times(1)).onChanged(any());
    }

    @Test
    public void 뒤로가기_클릭시_포커스가_없다면_종료_이벤트가_호출되는지_테스트() {
        // given
        searchBoxViewModel.getSearchBoxFinishEvent().observeForever(mockVoidObserver);

        // when
        searchBoxViewModel.onClickBackPressButton();

        // then
        verify(mockVoidObserver, times(1)).onChanged(any());
    }

    @Test
    public void 키워드_삭제_버튼_클릭시_레파지토리에_삭제_요청을_하는지_테스트() {
        // given
        when(mockImageSearchRepository.deleteSearchLog("테스트"))
            .thenReturn(Completable.complete());

        // when
        searchBoxViewModel.onClickSearchLogDeleteButton(new SearchLog("테스트", 1));

        // then
        verify(mockImageSearchRepository, times(1))
            .deleteSearchLog("테스트");
    }

    @Test
    public void 키워드_삭제_버튼_클릭시_보유하던_검색_기록_목록에서_제거하는지_테스트() {
        // given
        List<SearchLog> searchLogList = createVirtualSearchLogList(3);
        List<SearchLog> expectedList = createVirtualSearchLogList(3);
        expectedList.remove(0);
        Collections.sort(expectedList);

        when(mockImageSearchRepository.requestSearchLogList()).thenReturn(Single.just(searchLogList));

        when(mockImageSearchRepository.deleteSearchLog(any(String.class)))
            .thenReturn(Completable.complete());

        // when
        searchBoxViewModel.loadSearchLogList();
        searchBoxViewModel.onClickSearchLogDeleteButton(searchLogList.get(0));

        // then
        searchBoxViewModel.getSearchLogList().observeForever(receivedList -> {
            assertEquals(expectedList, receivedList);
        });
    }

    @Test
    public void 키워드_검색_버튼_클릭시_기존에_검색했던_키워드라면_목록의_가장_앞쪽으로_이동시키는지_테스트() {
        // given
        when(mockImageSearchRepository.requestSearchLogList())
            .thenReturn(Single.just(createVirtualSearchLogList(3)));

        when(mockImageSearchRepository.insertOrUpdateSearchLog("테스트0"))
            .thenReturn(Single.just(new SearchLog("테스트0", 4)));

        // when
        searchBoxViewModel.loadSearchLogList();
        searchBoxViewModel.onClickSearchButton("테스트0");

        // then
        searchBoxViewModel.getSearchLogList().observeForever(searchLogs -> {
            assertEquals(searchLogs.get(0).getKeyword(), "테스트0");
            assertEquals(searchLogs.size(), 3);
        });
    }

    private List<SearchLog> createVirtualSearchLogList(int size) {
        ArrayList<SearchLog> searchLogList = new ArrayList<>();
        for(int i=0; i<size; i++) {
            searchLogList.add(new SearchLog("테스트" + i, i));
        }

        return searchLogList;
    }

}
