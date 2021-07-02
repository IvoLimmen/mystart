package org.limmen.mystart.dto;

public class LabelDto implements Comparable<LabelDto> {

    private String label;
    private Long count;

    public LabelDto(String label, Long count) {
        this.label = label;
        this.count = count;
    }

    @Override
    public int compareTo(LabelDto o) {
        return String.CASE_INSENSITIVE_ORDER.compare(this.label, o.getLabel());
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}