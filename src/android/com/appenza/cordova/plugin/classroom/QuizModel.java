package com.appenza.classroom;

/**
 * Created by marwansallam on 1/11/16.
 */
public class QuizModel {

    public String QuizString = "{\"_type\":\"Scoresheet\",\"id\":311,\"answers\":[{\"question_id\":4140,\"answer\":\"\"},{\"question_id\":4141,\"answer\":\"\"},{\"question_id\":4142,\"answer\":\"\"},{\"question_id\":4143,\"answer\":\"\"},{\"question_id\":4144,\"answer\":\"\"}],\"result\":{\"answers\":[{\"score\":null,\"status\":\"skipped\",\"question_id\":4140,\"points\":10},{\"score\":null,\"status\":\"skipped\",\"question_id\":4141,\"points\":10},{\"score\":null,\"status\":\"skipped\",\"question_id\":4142,\"points\":10},{\"score\":null,\"status\":\"skipped\",\"question_id\":4143,\"points\":10},{\"score\":null,\"status\":\"skipped\",\"question_id\":4144,\"points\":10}],\"statistics\":{\"score\":0,\"correct_answers\":0,\"incorrect_answers\":0,\"partial_answers\":0,\"pending_answers\":0,\"skipped_answers\":5},\"total_score\":50},\"user\":{\"_type\":\"User\",\"id\":1,\"name\":\"Omar\",\"email\":\"owahab@gmail.com\",\"last_sign_in_at\":\"2015-12-28T11:01:25.469+02:00\",\"is_student\":true,\"is_teacher\":false,\"is_parent\":false,\"license_key\":\"zwy8i1bdt0hqfxepml\",\"avatar\":{\"small\":\"https://aurora-files.s3.amazonaws.com/users/avatars/000/000/001/small/avatar1441878432.jpeg?AWSAcces...\",\"original\":\"https://aurora-files.s3.amazonaws.com/users/avatars/000/000/001/original/avatar1441878432.jpeg?AWSAc...\"},\"school\":{\"_type\":\"School\",\"id\":1,\"organization_id\":1,\"name\":\"New School\",\"description\":\"\"},\"grade\":{\"_type\":\"Grade\",\"id\":3,\"school_id\":1,\"name\":\"Grade 3\"},\"role\":{\"_type\":\"Role\",\"id\":1,\"name\":\"Student\"},\"class\":{\"_type\":\"Stream\",\"id\":5,\"name\":\"Class A\",\"grade_id\":1}}}";

    public String getQuizString() {
        return QuizString;
    }

    public void setQuizString(String text) {
        QuizString = text;
    }
}
