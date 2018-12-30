package com.example.edward.roomtest01

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.example.edward.roomtest01.model.BufferooFactory
import org.buffer.android.boilerplate.cache.db.BufferoosDatabase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Edward on 12/30/2018.
 */
@RunWith(AndroidJUnit4::class)
open class ExeCacheBufferooDaoTest {
    private lateinit var bufferoosDatabase: BufferoosDatabase

    @Before
    fun initDb() {
        val context = InstrumentationRegistry.getContext()
        bufferoosDatabase = Room.inMemoryDatabaseBuilder(context,
                BufferoosDatabase::class.java).build()
    }

    @After
    fun closeDb() {
        bufferoosDatabase.close()
    }

    @Test
    fun insertBufferoosSavesData() {
        val cachedBufferoo = BufferooFactory.makeCachedBufferoo()
        bufferoosDatabase.cachedBufferooDao().insertBufferoo(cachedBufferoo)

        val bufferoos = bufferoosDatabase.cachedBufferooDao().getBufferoos()
        assert(bufferoos.isNotEmpty())
    }

    @Test
    fun getBufferoosRetrievesData() {
        val cachedBufferoos = BufferooFactory.makeCachedBufferooList(5)

        cachedBufferoos.forEach {
            bufferoosDatabase.cachedBufferooDao().insertBufferoo(it) }

        val retrievedBufferoos = bufferoosDatabase.cachedBufferooDao().getBufferoos()
        assert(retrievedBufferoos == cachedBufferoos.sortedWith(compareBy({ it.id }, { it.id })))
    }

    @Test
    fun clearBufferoosClearsData() {
        val cachedBufferoo = BufferooFactory.makeCachedBufferoo()
        bufferoosDatabase.cachedBufferooDao().insertBufferoo(cachedBufferoo)

        bufferoosDatabase.cachedBufferooDao().clearBufferoos()
        assert(bufferoosDatabase.cachedBufferooDao().getBufferoos().isEmpty())
    }

}