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

package local.example.word.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import local.example.word.R;
import local.example.word.entity.Word;

public class WordListAdapter
        extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    private final LayoutInflater layoutInflater;
    private List<Word> cachedWordList;
    private static ClickListener clickListener;

    public WordListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.view_item, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        if (cachedWordList != null) {
            Word current = cachedWordList.get(position);
            holder.wordItemView.setText(current.getWord());
        } else {
            holder.wordItemView.setText(R.string.no_word);
        }
    }

    public void setWords(List<Word> words) {
        cachedWordList = words;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (cachedWordList != null)
            return cachedWordList.size();
        else return 0;
    }

    public Word getWordAtPosition(int position) {
        return cachedWordList.get(position);
    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        private final TextView wordItemView;

        private WordViewHolder(View itemView) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.text_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        WordListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View view, int position);
    }
}
