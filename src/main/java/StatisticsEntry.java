final class StatisticsEntry implements Comparable<StatisticsEntry> {
    private final String word;
    private final Integer count;

    StatisticsEntry(String word, Integer count) {
        this.word = word;
        this.count = count;
    }

    String getWord() {
        return word;
    }
    Integer getCount() {
        return count;
    }

    @Override
    public int compareTo(StatisticsEntry o) {
        Integer currentCount = getCount();
        Integer thatCount = o.getCount();
        if (!currentCount.equals(thatCount)) {
            return currentCount.compareTo(thatCount);
        }
        return getWord().compareTo(o.getWord());
    }
}
