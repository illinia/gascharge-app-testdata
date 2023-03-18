package com.gascharge.taemin.app.repository.reservation;

import com.gascharge.taemin.app.repository.AbstractBatchRepository;
import com.gascharge.taemin.app.repository.BatchRepository;
import com.gascharge.taemin.domain.entity.reservation.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.gascharge.taemin.common.util.DateTimeFormat.timeStampFormatter;

@Repository
public class ReservationBatchRepositoryImpl extends AbstractBatchRepository<Reservation> {
    public ReservationBatchRepositoryImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    protected int batchInsert(int batchCount, List<Reservation> list) {
        this.jdbcTemplate.batchUpdate(
                "insert into reservation (`created_date`, `modified_date`, `reservation_validation_id`, `reservation_time`, `status`, `user_id`, `charge_id`) values (?,?,?,?,?,?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, LocalDateTime.now().format(timeStampFormatter()));
                        ps.setString(2, LocalDateTime.now().format(timeStampFormatter()));
                        ps.setString(3, list.get(i).getReservationValidationId());
                        ps.setString(4, list.get(i).getReservationTime().format(timeStampFormatter()));
                        ps.setString(5, list.get(i).getStatus().toString());
                        ps.setString(6, String.valueOf((int) (Math.random() * 1000000)));
                        ps.setString(7, String.valueOf((int) (Math.random() * 1000000)));
                    }

                    @Override
                    public int getBatchSize() {
                        return list.size();
                    }
                });
        list.clear();
        batchCount++;
        return batchCount;
    }
}
