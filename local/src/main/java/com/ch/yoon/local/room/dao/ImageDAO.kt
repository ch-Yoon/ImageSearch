package com.ch.yoon.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ch.yoon.local.room.model.ImageDocumentEntity
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

    @Query("DELETE FROM imageDocumentEntities WHERE id = :id")
    fun deleteImageDocument(id: String): Completable

    @Query("SELECT * FROM imageDocumentEntities")
    fun selectAllImageDocument(): Single<List<ImageDocumentEntity>>

}