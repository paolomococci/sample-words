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

package local.example.word.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import local.example.word.dao.WordDao;
import local.example.word.entity.Word;

@Database(entities = {Word.class}, version = 2,  exportSchema = false)
public abstract class WordRoomDatabase
        extends RoomDatabase {

    public abstract WordDao wordDao();

    private static WordRoomDatabase DATABASE_INSTANCE;

    public static WordRoomDatabase getDatabase(final Context context) {
        if (DATABASE_INSTANCE == null) {
            synchronized (WordRoomDatabase.class) {
                if (DATABASE_INSTANCE == null) {
                    DATABASE_INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WordRoomDatabase.class, "word_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return DATABASE_INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback(){

        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase database){
            super.onOpen(database);
            new PopulateDbAsync(DATABASE_INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final WordDao wordDao;
        String [] words = {"felix", "panther", "cat", "lynx"};

        PopulateDbAsync(WordRoomDatabase database) {
            wordDao = database.wordDao();
        }

        @Override
        protected Void doInBackground(final Void... voids) {
            wordDao.deleteAll();
            for( int i = 0; i <= words.length - 1; i++) {
                Word word = new Word(words[i]);
                wordDao.insert(word);
            }
            return null;
        }
    }
}
