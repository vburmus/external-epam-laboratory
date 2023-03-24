package com.epam.esm.giftcertificate.direction;

public enum DirectionEnum {
    ASC,
    DESC;

    public static DirectionEnum getDirection(String s){
        return s.contains("-")?DESC:ASC;
    }
}
