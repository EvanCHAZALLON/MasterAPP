package net.evan.masterapp.packets;

public interface Packet {

    void invoke(String target);
    String channel();

}
