package com.lingokids.mtg.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Legal {
    private String format;
    private String legality;
}
