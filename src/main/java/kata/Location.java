package kata;

import java.util.Objects;

public final class Location {
    private final float latitude;
    private final float longitude;

    public Location(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float latitude() {
        return latitude;
    }

    public float longitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Location) obj;
        return Float.floatToIntBits(this.latitude) == Float.floatToIntBits(that.latitude) &&
                Float.floatToIntBits(this.longitude) == Float.floatToIntBits(that.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @Override
    public String toString() {
        return "[" +
                 + latitude + "," +
                "" + longitude + ']';
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }
}
