/*
 * Copyright (C) 2023 raph
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package jamuz;

import java.awt.Color;

/**
 */
public class StatItem {

    private final long countFile;
    private final long countPath;
    private long size = -1;
    private long length = -1;
    private double rating = -1;
    private float percentage = -1;
    private String label;
    private final String value;
    private final Color color;

    /**
     *
     * @param label
     * @param value
     * @param countFile
     * @param size
     * @param countPath
     * @param length
     * @param rating
     * @param color
     */
    public StatItem(String label, String value, long countFile, long countPath, long size, long length, double rating, Color color) {
        this.value = value;
        this.countFile = countFile;
        this.countPath = countPath;
        this.size = size;
        this.length = length;
        this.rating = rating;
        this.label = label;
        this.color = color;
    }

    /**
     *
     * @return
     */
    public long getCountFile() {
        return countFile;
    }

    /**
     *
     * @return
     */
    public long getCountPath() {
        return countPath;
    }

    /**
     *
     * @return
     */
    public long getSize() {
        return size;
    }

    /**
     *
     * @return
     */
    public long getLength() {
        return length;
    }

    /**
     *
     * @return
     */
    public double getRating() {
        return rating;
    }

    /**
     *
     * @return
     */
    public String getLabel() {
        return label;
    }

    /**
     *
     * @return
     */
    public String getLabelForChart() {
        String newLabel = label;
        newLabel = newLabel.replaceAll("%", "-");
        newLabel = newLabel.replaceAll("percent", "%");
        return newLabel;
        // return label.replaceAll("%", "-").replaceAll("percent", "%");
    }

    /**
     *
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     *
     * @return
     */
    public float getPercentage() {
        return percentage;
    }

    /**
     *
     * @param percentage
     */
    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    /**
     *
     * @param label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     *
     * @return
     */
    public Color getColor() {
        return color;
    }
    
}
