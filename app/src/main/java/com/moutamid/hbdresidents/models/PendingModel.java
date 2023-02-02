package com.moutamid.hbdresidents.models;

public class PendingModel {
    boolean pending;

    public PendingModel() {
    }

    public PendingModel(boolean pending) {
        this.pending = pending;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }
}
