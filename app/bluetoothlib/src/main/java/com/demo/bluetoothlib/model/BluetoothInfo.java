package com.demo.bluetoothlib.model;

import java.util.Objects;

public class BluetoothInfo {
    private String name;
    private String address;
    private int type;
    private boolean connected;

    public BluetoothInfo(String name, String address, int type) {
        this.name = name;
        this.address = address;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BluetoothInfo that = (BluetoothInfo) o;
        return type == that.type && Objects.equals(name, that.name) && Objects.equals(address, that.address);
    }

    @Override
    public String toString() {
        return "BluetoothInfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, type);
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Type will depend on the BluetoothClass
     * @return BluetoothClass
     */
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
