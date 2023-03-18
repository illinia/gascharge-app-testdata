package com.gascharge.taemin.app.repository;

import com.gascharge.taemin.domain.entity.reservation.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public abstract class AbstractBatchRepository<T> implements BatchRepository<T> {
    protected final JdbcTemplate jdbcTemplate;

    @Value("${batchSize}")
    private int batchSize;
    @Override
    public void saveAll(List<T> list) {
        int batchCount = 0;
        List<T> waitingReservations = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            waitingReservations.add(list.get(i));
            if ((i + 1) % batchSize == 0) {
                batchCount = batchInsert(batchCount, waitingReservations);
                log.info("{} batch = {} proceed", list.get(0).getClass().getSimpleName(), batchCount);
            }
        }
        if (!waitingReservations.isEmpty()) {
            batchCount = batchInsert(batchCount, waitingReservations);
            log.info("{} batch = {} completed", list.get(0).getClass().getSimpleName(), batchCount);
        }
    }

    protected abstract int batchInsert(int batchCount, List<T> list);
}
