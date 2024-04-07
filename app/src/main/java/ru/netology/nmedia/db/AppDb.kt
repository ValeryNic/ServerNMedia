package ru.netology.nmedia.db

import android.content.Context
import androidx.databinding.adapters.Converters
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.entity.PostEntity

@Database(entities = [PostEntity::class], version = 2, exportSchema = false)
//@TypeConverters(Converters::class)
abstract class AppDb : RoomDatabase() {
    abstract fun postDao(): PostDao

    }
