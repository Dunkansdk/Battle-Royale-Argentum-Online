package com.bonkan.brao.entity;

public abstract class Entity implements Comparable {

    private float x;
    private float y;

    public Entity(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

}
