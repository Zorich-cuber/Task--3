package com.bibik.airport.entity.impl;


import java.util.concurrent.locks.ReentrantLock;

public class Gate {
    private final int id;
    private boolean occupied = false;
    private final ReentrantLock lock = new ReentrantLock();

    public Gate(int id) {
        this.id = id;
    }

    public boolean isFree() {
        lock.lock();
        try {
            return !occupied;
        } finally {
            lock.unlock();
        }
    }

    public void occupy() {
        lock.lock();
        try {
            occupied = true;
        } finally {
            lock.unlock();
        }
    }

    public void release() {
        lock.lock();
        try {
            occupied = false;
        } finally {
            lock.unlock();
        }
    }

    public int getId() {
        return id;
    }
}