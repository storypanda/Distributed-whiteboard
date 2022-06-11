package hhzhu;

import javafx.scene.paint.Color;

/**
 * Author: Haohong Zhu
 * Student ID: 1305370
 */
public class Geometry {
    private Double startPointX;
    private Double startPointY;
    private Double endPointX;
    private Double endPointY;
    private Double lineWidth;
    private Color color;
    private String text;
    private String type;

    public Double getStartPointX() {
        return startPointX;
    }

    public void setStartPointX(Double startPointX) {
        this.startPointX = startPointX;
    }

    public Double getStartPointY() {
        return startPointY;
    }

    public void setStartPointY(Double startPointY) {
        this.startPointY = startPointY;
    }

    public Double getEndPointX() {
        return endPointX;
    }

    public void setEndPointX(Double endPointX) {
        this.endPointX = endPointX;
    }

    public Double getEndPointY() {
        return endPointY;
    }

    public void setEndPointY(Double endPointY) {
        this.endPointY = endPointY;
    }

    public Double getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(Double lineWidth) {
        this.lineWidth = lineWidth;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
