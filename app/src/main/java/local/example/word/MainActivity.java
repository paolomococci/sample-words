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

package local.example.word;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import local.example.word.adapter.WordListAdapter;
import local.example.word.entity.Word;
import local.example.word.view.WordViewModel;

import static local.example.word.R.id;
import static local.example.word.R.id.clear_data;
import static local.example.word.R.id.recycler_view;
import static local.example.word.R.layout;
import static local.example.word.R.string;

public class MainActivity
        extends AppCompatActivity {

    public static final int EDIT_WORD_REQUEST_CODE = 1;
    public static final int UPDATE_WORD_REQUEST_CODE = 2;

    public static final String DATA_UPDATE_WORD = "word_to_be_updated";
    public static final String DATA_ID = "data_id";

    private WordViewModel wordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        Toolbar toolbar = findViewById(id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(recycler_view);
        final WordListAdapter adapter = new WordListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        wordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        wordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(@Nullable final List<Word> words) {
                adapter.setWords(words);
            }
        });

        FloatingActionButton fab = findViewById(id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        MainActivity.this, 
                        EditWordActivity.class
                );
                startActivityForResult(intent, EDIT_WORD_REQUEST_CODE);
            }
        });

        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = Objects.requireNonNull(viewHolder).getAdapterPosition();
                        Word myWord = adapter.getWordAtPosition(position);
                        Toast.makeText(MainActivity.this,
                                getString(string.delete_word_preamble) + " " +
                                        myWord.getWord(), Toast.LENGTH_LONG).show();
                        wordViewModel.deleteWord(myWord);
                    }
                });
        helper.attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new WordListAdapter.ClickListener()  {

            @Override
            public void onItemClick(View view, int position) {
                Word word = adapter.getWordAtPosition(position);
                launchUpdateWordActivity(word);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == clear_data) {
            Toast.makeText(this, string.clear_data_toast_text, Toast.LENGTH_LONG).show();
            wordViewModel.deleteAll();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == EDIT_WORD_REQUEST_CODE && resultCode == RESULT_OK) {
            Word word = new Word(intent.getStringExtra(EditWordActivity.RESPONSE));
            wordViewModel.insert(word);
        } else if (requestCode == UPDATE_WORD_REQUEST_CODE
                && resultCode == RESULT_OK) {
            String word_data = intent.getStringExtra(EditWordActivity.RESPONSE);
            int id = intent.getIntExtra(EditWordActivity.RESPONSE_ID, -1);
            if (id != -1) {
                wordViewModel.update(new Word(id, word_data));
            } else {
                Toast.makeText(this, string.unable_to_update,
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(
                    this, string.empty_not_saved, Toast.LENGTH_LONG).show();
        }
    }

    public void launchUpdateWordActivity(Word word) {
        Intent intent = new Intent(this, EditWordActivity.class);
        intent.putExtra(DATA_UPDATE_WORD, word.getWord());
        intent.putExtra(DATA_ID, word.getId());
        startActivityForResult(intent, UPDATE_WORD_REQUEST_CODE);
    }
}
