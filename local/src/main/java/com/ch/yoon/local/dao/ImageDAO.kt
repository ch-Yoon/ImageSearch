package com.ch.yoon.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ch.yoon.local.model.image.LocalImageDocument
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
@Dao
interface ImageDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateImageDocument(localImageDocument: LocalImageDocument): Completable

    @Query("DELETE FROM imageDocumentEntities WHERE id = :id")
    fun deleteImageDocument(id: String): Completable

    @Query("SELECT * FROM imageDocumentEntities")
    fun selectAllImageDocument(): Single<List<LocalImageDocument>>

}