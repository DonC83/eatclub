package com.donc.eatclub.model;

import java.util.List;

public record Restaurant(
    String objectId,
    String name,
    String address1,
    String suburb,
    List<String> cuisines,
    String imageLink,
    String open,
    String close,
    List<Deal> deals
) {

}