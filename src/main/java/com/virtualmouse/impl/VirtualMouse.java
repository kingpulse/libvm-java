package com.virtualmouse.impl;

import com.virtualmouse.vmnative.vmMouseEvent;
import com.virtualmouse.vmnative.libvm;

import java.io.IOException;

public class VirtualMouse {

    private int fileDescriptor = -1;

    public VirtualMouse(String path) throws IOException {
               this.open(path);
    }

    private void throwIfNotOpen() throws IOException{
        if (!isOpen()) throw new IOException("VirtualMouse object has no open file descriptor");
    }

    public boolean isOpen() { return fileDescriptor >= 0; }

    public void open(String path) throws IOException{

        if (isOpen()) throw new VirtualMouseError("Already open!");

        if ((this.fileDescriptor = libvm.lib.getFileDesc(path)) < 0) {
            throw new IOException(String.format("VirtualMouse failed to open and retrieve" +
                    "a file descriptor. Error code: %d", fileDescriptor));
        }
    }

    public void close() {
        if (this.fileDescriptor >= 0) {
            libvm.lib.closeFileDesc(fileDescriptor);
        }
    }

    public int sendEvent(final vmMouseEvent event) throws IOException{
        throwIfNotOpen();
        return libvm.lib.fdSendIOCTLEvent(this.fileDescriptor, event);
    }

    public int sendEvents(final vmMouseEvent[] event) throws IOException{
        throwIfNotOpen();
        return libvm.lib.fdSendIOCTLEvents(this.fileDescriptor, event, event.length);
    }

    public int moveMouse(int x, int y) throws IOException {
        throwIfNotOpen();
        return libvm.lib.fdMoveMouse(this.fileDescriptor, x, y);
    }

    public int rightClick() throws IOException {
        throwIfNotOpen();
        return libvm.lib.fdRightClick(this.fileDescriptor);
    }

    public int leftClick() throws IOException {
        throwIfNotOpen();
        return libvm.lib.fdLeftClick(this.fileDescriptor);
    }



}
