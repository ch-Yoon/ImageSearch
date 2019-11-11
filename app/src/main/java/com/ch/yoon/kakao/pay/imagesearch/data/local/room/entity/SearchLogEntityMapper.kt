package com.ch.yoon.kakao.pay.imagesearch.data.local.room.entity

import com.ch.yoon.kakao.pay.imagesearch.data.repository.model.SearchLogModel

/**
 * Creator : ch-yoon
 * Date : 2019-10-30
 **/
object SearchLogEntityMapper {

    fun toEntity(searchLogEntity: SearchLogEntity): SearchLogModel = searchLogEntity.run { SearchLogModel(keyword, time) }

    fun toEntityList(searchLogEntityList: List<SearchLogEntity>) = searchLogEntityList.map { SearchLogModel(it.keyword, it.time) }

}