package com.lakshyagupta7089.triviaapp.data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.lakshyagupta7089.triviaapp.controller.AppController;
import com.lakshyagupta7089.triviaapp.model.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    public ArrayList<Question> questionArrayList = new ArrayList<>();
    private String url = "https://opentdb.com/api.php?amount=49&type=boolean";
    private Context context;

    public Repository(Context context) {
        this.context = context;
    }

    public ArrayList<Question> getQuestions(final AnswerListAsyncResponse callBack) {
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("results");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            Question question = new Question(
                                    jsonObject.getString("question"),
                                    jsonObject.getBoolean("correct_answer"),
                                    jsonObject.getString("category"),
                                    jsonObject.getString("difficulty")
                            );

                            questionArrayList.add(question);
                            Log.d("TAG", "getQuestions: " + question);
                        }
                    } catch (JSONException ignored) {}

                    if (null != callBack) {
                        callBack.processFinished(questionArrayList);
                    }

                }, error -> {});

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questionArrayList;
    }
}
