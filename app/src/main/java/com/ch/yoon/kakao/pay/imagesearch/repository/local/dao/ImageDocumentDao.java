package com.ch.yoon.kakao.pay.imagesearch.repository.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ch.yoon.kakao.pay.imagesearch.repository.local.entity.LocalImageDocument;

import java.util.List;

import io.reactivex.Single;

/**
 * Creator : ch-yoon
 * Date : 2019-08-05.
 */
@Dao
public interface ImageDocumentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<LocalImageDocument> documents);

    @Query("SELECT * FROM documents " +
        "WHERE keyword == :keyword AND " +
        "number >= :startNumber AND " +
        "number <= :endNumber AND " +
        "image_sort_type == :imageSortType")
    Single<List<LocalImageDocument>> select(String keyword,
                                            int startNumber,
                                            int endNumber,
                                            String imageSortType);

    @Query("DELETE FROM documents WHERE keyword = :keyword")
    void deleteByKeyword(String keyword);

}
