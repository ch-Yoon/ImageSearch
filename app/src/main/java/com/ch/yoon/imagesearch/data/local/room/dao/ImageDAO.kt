package com.ch.yoon.imagesearch.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ch.yoon.imagesearch.data.local.room.entity.ImageDocumentEntity
import com.ch.yoon.imagesearch.data.local.room.entity.SearchLogEntity
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
@Dao
interface ImageDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateImageDocument(imageDocumentEntity: ImageDocumentEntity): Completable

    @Query("SELECT * FROM imageDocuments")
    fun selectAllImageDocument(): Single<List<ImageDocumentEntity>>

    @Query("DELETE FROM imageDocuments WHERE id = :id")
    fun deleteImageDocument(id: String): Completable

}