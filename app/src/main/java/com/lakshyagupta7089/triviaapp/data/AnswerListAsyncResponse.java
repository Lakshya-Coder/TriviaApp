package com.lakshyagupta7089.triviaapp.data;

import com.lakshyagupta7089.triviaapp.model.Question;

import java.util.ArrayList;

public interface AnswerListAsyncResponse {
    void processFinished(ArrayList<Question> questionArrayList);
}
