package com.zilch.interview.model.pojo;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class BlockData {
    private String from;
    private String to;
    private int amount;

    public static BlockData genesis(){
        return new BlockData("0", "0", 0);
    }
}
