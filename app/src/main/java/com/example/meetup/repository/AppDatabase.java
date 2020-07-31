package com.example.meetup.repository;



import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.meetup.ulti.MyApplication;
import com.example.meetup.dao.NewsDAO;
import com.example.meetup.model.News;

@Database(entities = {News.class},version = AppDatabase.DATABASE_VERSION )
public abstract class AppDatabase extends RoomDatabase {
    public static AppDatabase database=null;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Meetup_database";



    public abstract NewsDAO getNewsDao();

    public static AppDatabase getInstance(){
        if(database == null){
            database= Room.databaseBuilder(MyApplication.getAppContext(),AppDatabase.class,DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }
}
