package com.appodeal.iab.mraid;


import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class OverlappingCalculator {
    private List<Rect> occlusionRectangles;

    OverlappingCalculator(List<Rect> overlappingRectList, int[] offset) {
        List<Integer> xEdges = getXEdges(overlappingRectList);

        occlusionRectangles = new ArrayList<>();
        for (int i = 0; i < xEdges.size() - 1; i++) {
            int x = xEdges.get(i);
            int nextX = xEdges.get(i + 1);

            if (x < nextX) {
                Range xRange = new Range(x, nextX);
                List<Range> yRanges = getYRanges(xRange, overlappingRectList);
                occlusionRectangles.addAll(createRectFromRanges(xRange, yRanges));
            }
        }

        Collections.sort(occlusionRectangles, new Comparator<Rect>() {
            @Override
            public int compare(Rect o1, Rect o2) {
                return o1.width() * o1.height() - o2.width() * o2.height();
            }
        });

        for (Rect rect : occlusionRectangles) {
            rect.offset(-offset[0], -offset[1]);
        }
    }

    private List<Integer> getXEdges(List<Rect> rectList) {
        List<Integer> xList = new ArrayList<>();
        for (Rect rect : rectList) {
            xList.add(rect.left);
            xList.add(rect.right);
        }

        Collections.sort(xList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        return xList;
    }

    private List<Range> getYRanges(Range xRange, List<Rect> rectList) {
        List<Range> yRanges = new ArrayList<>();
        for (Rect rect : rectList) {
            if (xRange.start() < rect.right && xRange.end() > rect.left) {
                yRanges = mergeRanges(yRanges, new Range(rect.top, rect.bottom));
            }
        }
        return yRanges;
    }

    private List<Range> mergeRanges(List<Range> rangeList, Range newRange) {
        List<Range> mergedRangeList = new ArrayList<>();
        for (int i = 0; i < rangeList.size(); i++) {
            Range range = rangeList.get(i);
            if (newRange.isOverlapping(range)) {
                newRange = newRange.merge(range);
            } else {
                mergedRangeList.add(range);
            }
        }
        mergedRangeList.add(newRange);
        return mergedRangeList;
    }

    private List<Rect> createRectFromRanges(Range xRange, List<Range> yRanges) {
        List<Rect> rectList = new ArrayList<>();
        for (Range yRange : yRanges) {
            rectList.add(new Rect(xRange.start(), yRange.start(), xRange.end(), yRange.end()));
        }
        return rectList;
    }

    List<Rect> getOcclusionRectangles() {
        return occlusionRectangles;
    }

    float getTotalArea() {
        float totalArea = 0;

        for (Rect rect : occlusionRectangles) {
            totalArea += rect.width() * rect.height();
        }
        return totalArea;
    }

    private static class Range {
        private final int start;
        private final int end;

        Range(int start, int end) {
            this.start = start;
            this.end = end;
        }

        boolean isOverlapping(Range range) {
            return !(start > range.end || end < range.start);
        }

        Range merge(Range range) {
            if (isOverlapping(range)) {
                return new Range(start < range.start ? start : range.start, end > range.end ? end : range.end);
            } else {
                return this;
            }
        }

        public int start() {
            return start;
        }

        public int end() {
            return end;
        }

        @Override
        public String toString() {
            return String.format("Range: %s - %s", start, end);
        }
    }

}
