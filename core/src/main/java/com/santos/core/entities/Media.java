package com.santos.core.entities;

import lombok.Getter;

import java.util.Set;

@Getter
public class Media {
    private Set<String> audios;
    private Set<String> images;
    private Set<String> videos;
}
