package com.ch.yoon.kakao.pay.imagesearch.data.source.local.room.entity

import com.ch.yoon.kakao.pay.imagesearch.data.repository.image.model.SearchLog

/**
 * Creator : ch-yoon
 * Date : 2019-10-30
 **/
object SearchLogEntityMapper {

    fun toEntity(searchLogModel: SearchLogModel): SearchLog = searchLogModel.run { SearchLog(keyword, time) }

    fun toEntityList(searchLogModelList: List<SearchLogModel>) = searchLogModelList.map { SearchLog(it.keyword, it.time) }

}