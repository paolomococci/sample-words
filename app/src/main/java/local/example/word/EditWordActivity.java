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
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import static local.example.word.MainActivity.DATA_ID;
import static local.example.word.MainActivity.DATA_UPDATE_WORD;
import static local.example.word.R.id.button_save;
import static local.example.word.R.id.edit_word;
import static local.example.word.R.layout;

public class EditWordActivity
        extends AppCompatActivity {

    public static final String RESPONSE = "local.example.word.RESPONSE";
    public static final String RESPONSE_ID = "local.example.word.RESPONSE_ID";

    private EditText editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_edit_word);

        editText = findViewById(edit_word);

        final Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String word = extras.getString(DATA_UPDATE_WORD, "");
            if (!word.isEmpty()) {
                editText.setText(word);
                editText.setSelection(word.length());
                editText.requestFocus();
            }
        }


        final Button saveButton = findViewById(button_save);

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent responseIntent = new Intent();
                if (TextUtils.isEmpty(editText.getText())) {
                    setResult(RESULT_CANCELED, responseIntent);
                } else {
                    String word = editText.getText().toString();
                    responseIntent.putExtra(RESPONSE, word);
                    if (extras != null && extras.containsKey(DATA_ID)) {
                        int id = extras.getInt(DATA_ID, -1);
                        if (id != -1) {
                            responseIntent.putExtra(RESPONSE_ID, id);
                        }
                    }
                    setResult(RESULT_OK, responseIntent);
                }
                finish();
            }
        });
    }
}
