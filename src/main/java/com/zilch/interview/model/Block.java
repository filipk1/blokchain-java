package com.zilch.interview.model;

import com.zilch.interview.model.pojo.BlockData;

public interface Block {
    String getPreviousHash();

    String getHash();

    BlockData getData();

    long getTimeStamp();

    int getNonce();
}
