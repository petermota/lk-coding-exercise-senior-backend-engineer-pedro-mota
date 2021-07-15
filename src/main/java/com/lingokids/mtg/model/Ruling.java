package com.lingokids.mtg.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Ruling {
    private String date;
    private String text;
}
