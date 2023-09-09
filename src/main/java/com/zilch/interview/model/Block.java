package com.zilch.interview.model;

public interface Block {
    String getPreviousHash();

    String getHash();

    String getData();

    long getTimeStamp();

    int getNonce();
}
