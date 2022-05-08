package wooteco.subway.domain;

public class Line {

    private final Long id;
    private final String name;
    private final String color;

    public Line(final Long id, final String name, final String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Line(final String name, final String color) {
        this(null, name, color);
    }

    public boolean isSameId(final Long id) {
        return this.id.equals(id);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Line)) {
            return false;
        }

        Line line = (Line) o;
        return id.equals(line.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Line{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
