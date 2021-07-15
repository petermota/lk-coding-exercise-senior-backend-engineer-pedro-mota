package com.lingokids.mtg.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class ForeignName {
    private String name;
    private String text;
    private String type;
    private String flavor;
    private String imageUrl;
    private String language;
    private int multiverseid;
}
