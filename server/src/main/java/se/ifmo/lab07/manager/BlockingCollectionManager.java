package se.ifmo.lab07.manager;

import se.ifmo.lab07.entity.Flat;
import se.ifmo.lab07.entity.Furnish;
import se.ifmo.lab07.entity.House;

import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingCollectionManager extends CollectionManager {

    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public List<Flat> getCollection() {
        try {
            lock.lock();
            return super.getCollection();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void push(Flat element) {
        try {
            lock.lock();
            super.push(element);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void update(long id, Flat newFlat) {
        try {
            lock.lock();
            super.update(id, newFlat);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void removeById(long id) {
        try {
            lock.lock();
            super.removeById(id);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public long removeByFurnish(String username, Furnish furnish) {
        try {
            lock.lock();
            return super.removeByFurnish(username, furnish);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public long removeByOwnerId(int id) {
        try {
            return super.removeByOwnerId(id);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Flat get(long id) {
        try {
            lock.lock();
            return super.get(id);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Flat min() {
        try {
            lock.lock();
            return super.min();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void shuffle() {
        try {
            lock.lock();
            super.shuffle();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<Flat> filterByName(String name) {
        try {
            lock.lock();
            return super.filterByName(name);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Set<House> getUniqueHouses() {
        try {
            lock.lock();
            return super.getUniqueHouses();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        try {
            lock.lock();
            return super.toString();
        } finally {
            lock.unlock();
        }
    }
}
