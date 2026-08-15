package com.neonajam.neotracker.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SizeClassTest {

    @Test
    void diameterBelow50_isSmall() {
        SizeClass result = SizeClass.fromDiameter(20,40);
        assertEquals(SizeClass.SMALL, result);
    }

    @Test
    void diameterExactly50_isMedium() {
        SizeClass result = SizeClass.fromDiameter(40,60);
        assertEquals(SizeClass.MEDIUM, result);
    }

    @Test
    void diameterBetween50And140_isMedium() {
        SizeClass result = SizeClass.fromDiameter(80,120);
        assertEquals(SizeClass.MEDIUM, result);
    }

    @Test
    void diameterExactly140_isLarge() {
        SizeClass result = SizeClass.fromDiameter(130,150);
        assertEquals(SizeClass.LARGE, result);
    }

    @Test
    void diameterAbove140_isLarge() {
        SizeClass result = SizeClass.fromDiameter(300,500);
        assertEquals(SizeClass.LARGE, result);
    }
}
