package com.ch.yoon.domain.repository

import com.ch.yoon.data.model.image.response.ImageDocument
import com.ch.yoon.data.model.image.response.ImageSearchResponse
import com.ch.yoon.domain.model.image.request.ImageSearchRequest
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
interface ImageRepository {

    fun getImages(imageSearchRequest: ImageSearchRequest): Single<ImageSearchResponse>

    fun getAllFavoriteImages(): Single<List<ImageDocument>>

    fun saveFavoriteImage(imageDocument: ImageDocument): Completable

    fun deleteFavoriteImage(imageDocument: ImageDocument): Completable

    fun observeChangingFavoriteImage(): Observable<ImageDocument>

}