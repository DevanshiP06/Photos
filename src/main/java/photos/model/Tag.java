package photos.model;

import java.io.Serializable;

public class Tag implements Serializable {
    private String type;
    private String value;

    public Tag(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() { return type; }
    public String getValue() { return value; }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tag)) return false;
        Tag other = (Tag) obj;
        return type.equals(other.type) && value.equals(other.value);
    }
}
