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

package local.example.word.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import local.example.word.dao.WordDao;
import local.example.word.data.WordRoomDatabase;
import local.example.word.entity.Word;

public class WordRepository {

    private WordDao wordDao;
    private LiveData<List<Word>> listOfAllWords;

    public WordRepository(Application application) {
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        wordDao = db.wordDao();
        listOfAllWords = wordDao.getAllWords();
    }

    public LiveData<List<Word>> getAllWords() {
        return listOfAllWords;
    }

    public void insert(Word word) {
        new InsertAsyncTask(wordDao)
                .execute(word);
    }

    public void update(Word word)  {
        new UpdateWordAsyncTask(wordDao)
                .execute(word);
    }

    public void deleteAll()  {
        new DeleteAllWordsAsyncTask(wordDao)
                .execute();
    }

    public void deleteWord(Word word) {
        new DeleteSingleWordAsyncTask(wordDao)
                .execute(word);
    }

    /* static inner classes */

    private static class InsertAsyncTask
            extends AsyncTask<Word, Void, Void> {

        private WordDao asyncTaskDao;

        InsertAsyncTask(WordDao wordDao) {
            asyncTaskDao = wordDao;
        }

        @Override
        protected Void doInBackground(final Word... words) {
            asyncTaskDao.insert(words[0]);
            return null;
        }
    }

    private static class UpdateWordAsyncTask
            extends AsyncTask<Word, Void, Void> {
        private WordDao asyncTaskDao;

        UpdateWordAsyncTask(WordDao wordDao) {
            asyncTaskDao = wordDao;
        }

        @Override
        protected Void doInBackground(final Word... words) {
            asyncTaskDao.update(words[0]);
            return null;
        }
    }

    private static class DeleteAllWordsAsyncTask
            extends AsyncTask<Void, Void, Void> {
        private WordDao asyncTaskDao;

        DeleteAllWordsAsyncTask(WordDao wordDao) {
            asyncTaskDao = wordDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class DeleteSingleWordAsyncTask
            extends AsyncTask<Word, Void, Void> {
        private WordDao asyncTaskDao;

        DeleteSingleWordAsyncTask(WordDao wordDao) {
            asyncTaskDao = wordDao;
        }

        @Override
        protected Void doInBackground(final Word... words) {
            asyncTaskDao.deleteWord(words[0]);
            return null;
        }
    }
}
