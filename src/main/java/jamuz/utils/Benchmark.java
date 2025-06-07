/*
 * Copyright (C) 2014 phramusca <phramusca@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published par
 * the Free Software Foundation, either version 3 of the License, ou
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

package jamuz.utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for benchmarking.
 * 
 * @autor phramusca <phramusca@gmail.com>
 */
public class Benchmark {

    private int size;
    private int index;
    private final long startTime;
    private long partialTime;
    private final List<Long> partialTimes;
    
    /**
     * Creates and starts a benchmark.
     * @param size the number of iterations to benchmark
     */
    public Benchmark(int size) {
        this.size = size;
        this.index = 0;
        this.partialTimes = new ArrayList<>();
        this.startTime = System.currentTimeMillis();
        this.partialTime = this.startTime;
    }

    /**
     * Returns the elapsed and remaining time.
     * @return a formatted string with elapsed and remaining time
     */
    public String get() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - this.startTime;
        long actionTime = currentTime - this.partialTime;
        this.partialTime = currentTime;
        this.partialTimes.add(actionTime);
        long remainingTime = mean(this.partialTimes) * (this.size - this.index);
        this.index++;
        
        String elapsed = StringManager.humanReadableSeconds(elapsedTime / 1000);
        String remaining = StringManager.humanReadableSeconds(remainingTime / 1000);
        
        return MessageFormat.format("{0}: {2}, {1}: {3}", Inter.get("Label.elapsed"), Inter.get("Label.remaining"), elapsed, remaining); //NOI18N
    }

    /**
     * Sets the size of the benchmark.
     * @param size the number of iterations to benchmark
     */
    public void setSize(int size) {
        this.size = size;
    }
 
    public int getSize() {
        return size;
    }

    /**
     * Calculates the mean of a list of numbers.
     * @param numbers the list of numbers
     * @return the mean of the numbers
     */
    public static long mean(List<Long> numbers) {
        return Math.round(sum(numbers) / (double) numbers.size());
    }
        
    /**
     * Calculates the sum of a list of numbers.
     * @param numbers the list of numbers
     * @return the sum of the numbers
     */
    public static long sum(List<Long> numbers) {
        long sum = 0L;
        for (long number : numbers) {
            sum += number;
        }
        return sum;
    }
}
