package com.gascharge.taemin.app.repository;

import java.util.List;

public interface BatchRepository<T> {
    void saveAll(List<T> list);
}
