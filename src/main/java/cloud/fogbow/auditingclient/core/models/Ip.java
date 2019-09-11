package cloud.fogbow.auditingclient.core.models;

import java.util.Objects;

public class Ip {
    private String address;

    public Ip(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ip ip = (Ip) o;
        return Objects.equals(address, ip.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}
