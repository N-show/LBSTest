package com.sony.material;

/**
 * @author:nsh
 * @data:2018/2/8. 上午1:05
 */

public class Fruit {

    private String fruitName;
    private int imageId;

    public Fruit(String fruitName, int imageId) {
        this.fruitName = fruitName;
        this.imageId = imageId;
    }

    public String getFruitName() {
        return fruitName;
    }

    public int getImageId() {
        return imageId;
    }
}
