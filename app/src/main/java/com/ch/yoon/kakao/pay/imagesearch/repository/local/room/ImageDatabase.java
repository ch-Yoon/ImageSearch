package com.ch.yoon.kakao.pay.imagesearch.repository.local.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.dao.ImageSearchDao;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.entity.LocalImageDocument;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.entity.SearchLog;

/**
 * Creator : ch-yoon
 * Date : 2019-08-05.
 */
@Database(entities = {LocalImageDocument.class, SearchLog.class}, version = 1, exportSchema = false)
public abstract class ImageDatabase extends RoomDatabase {

    private static final String databaseName = "app_database";

    private static ImageDatabase INSTANCE;

    public static synchronized ImageDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                context.getApplicationContext(),
                ImageDatabase.class,
                databaseName
            ).build();
        }

        return INSTANCE;
    }

    public abstract ImageSearchDao imageDocumentDao();

}
