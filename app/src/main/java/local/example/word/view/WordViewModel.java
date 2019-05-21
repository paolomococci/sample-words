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

package local.example.word.view;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import local.example.word.entity.Word;
import local.example.word.repository.WordRepository;

public class WordViewModel
        extends AndroidViewModel {

    private WordRepository wordRepository;

    private LiveData<List<Word>> listOfAllWords;

    public WordViewModel(Application application) {
        super(application);
        wordRepository = new WordRepository(application);
        listOfAllWords = wordRepository.getAllWords();
    }

    public LiveData<List<Word>> getAllWords() {
        return listOfAllWords;
    }

    public void insert(Word word) {
        wordRepository
                .insert(word);
    }

    public void update(Word word) {
        wordRepository
                .update(word);
    }

    public void deleteAll() {
        wordRepository
                .deleteAll();
    }

    public void deleteWord(Word word) {
        wordRepository
                .deleteWord(word);
    }
}
