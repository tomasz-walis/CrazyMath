package com.math.crazymath;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.annotation.NonNull;
import android.widget.*;
import java.util.*;


/**
 * Created by Tom on 04/03/2017.
 */

public class ActivityHighScore extends ListActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_high_score,getHighScoresFromStorage()));

        ListView listView = getListView();
        listView.setTextFilterEnabled(true);


    }

    @NonNull
    //get the high scores
    private String[] getHighScoresFromStorage() {
        SharedPreferences preferences = getSharedPreferences("scores", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = preferences.getAll();
        if(allEntries.size() == 0) {
            return new String[] {"No scores recorded yet"};
        }

        // otherwise we have some scores

        Set<String> keys = allEntries.keySet();
        List<Score> scores = new ArrayList<>();
        for(String key : keys) {
            scores.add(new Score(key, preferences.getInt(key, 0)));
        }
        //sort the high scores
        Collections.sort(scores, new Comparator<Score>() {
            @Override
            public int compare(Score o1, Score o2) {
                return o2.score - o1.score;
            }
        });

        String[] results = new String[scores.size()];
        int index = 0;
        for(Score score : scores) {
            results[index++] = score.toString();
        }

        return results;

    }

    //inner class for score
    class Score {
        String user;
        int score;

        public Score(String user, int score) {
            this.user = user;
            this.score = score;
        }

        //put the score to string
        @Override
        public String toString() {
            return String.format("%-30s%s point(s)", user, Integer.toString(score));
        }
    }
}