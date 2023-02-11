package com.darya.compiment.telegram;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MoodType {
    MAD("mad"),
    GOOD("good"),
    TYPICAL("typical");

    private final String displayName;
}
