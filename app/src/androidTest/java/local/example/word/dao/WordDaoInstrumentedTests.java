/**
 *
 * Copyright 2019 paolo mococci
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package local.example.word.dao;


import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import local.example.word.data.WordRoomDatabase;
import local.example.word.entity.Word;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class WordDaoInstrumentedTests {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private WordDao wordDao;
    private WordRoomDatabase wordRoomDatabase;

    @Before
    public void initDatabase()
            throws Exception {
        Context appContext = InstrumentationRegistry
                .getInstrumentation().getTargetContext();
        wordRoomDatabase = Room.inMemoryDatabaseBuilder(
                appContext,
                WordRoomDatabase.class
        ).allowMainThreadQueries().build();
        wordDao = wordRoomDatabase.wordDao();
    }

    @Test
    public void createAndReadWordTest()
            throws Exception {
        Word word = new Word("bobcat");
        wordDao.insert(word);
        List<Word> words = LiveDataUtil.getValue(wordDao.getAllWords());
        assertEquals(words.get(0).getWord(), word.getWord());
    }

    @Test
    public void readAllWordsTest()
            throws Exception {
        wordDao.insert(new Word("parrot"));
        wordDao.insert(new Word("raven"));
        wordDao.insert(new Word("magpie"));
        wordDao.insert(new Word("seagull"));
        List<Word> words = LiveDataUtil.getValue(wordDao.getAllWords());
        assertFalse(words.isEmpty());
        assertEquals(words.size(), 4);
    }

    @Test
    public void  deleteAllWordsTest()
            throws Exception {
        wordDao.insert(new Word("gorilla"));
        wordDao.insert(new Word("chimp"));
        wordDao.insert(new Word("baboon"));
        wordDao.deleteAll();
        List<Word> words = LiveDataUtil.getValue(wordDao.getAllWords());
        assertTrue(words.isEmpty());
    }

    @After
    public void closeDatabase()
            throws Exception {
        wordRoomDatabase.clearAllTables();
        wordRoomDatabase.close();
    }

    private static class LiveDataUtil {
        public static <T> T getValue(final LiveData<T> liveData)
                throws InterruptedException {
            final Object[] objects = new Object[1];
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            Observer<T> observer = new Observer<T>() {
                @Override
                public void onChanged(T t) {
                    objects[0] = t;
                    countDownLatch.countDown();
                    liveData.removeObserver(this);
                }
            };
            liveData.observeForever(observer);
            countDownLatch.await(2, TimeUnit.SECONDS);
            return (T) objects[0];
        }
    }
}
