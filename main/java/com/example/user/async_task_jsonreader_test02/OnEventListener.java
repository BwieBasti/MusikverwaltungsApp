package com.example.user.async_task_jsonreader_test02;

import java.io.FileNotFoundException;

public interface OnEventListener<T> {

    public void OnSuccess(T object) throws FileNotFoundException;
    public void OnFailure(Exception e);

}
