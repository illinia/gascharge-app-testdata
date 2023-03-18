package com.gascharge.taemin.app.repository.charge;

import com.gascharge.taemin.app.repository.AbstractBatchRepository;
import com.gascharge.taemin.domain.entity.charge.Charge;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static com.gascharge.taemin.common.util.DateTimeFormat.timeStampFormatter;

@Repository
public class ChargeBatchRepositoryImpl extends AbstractBatchRepository<Charge> {
    public ChargeBatchRepositoryImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    protected int batchInsert(int batchCount, List<Charge> list) {
        this.jdbcTemplate.batchUpdate(
                "insert into charge (`created_date`, `modified_date`, `charge_place_id`, `name`, `current_count`, `total_count`, `membership`) values (?,?,?,?,?,?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, LocalDateTime.now().format(timeStampFormatter()));
                        ps.setString(2, LocalDateTime.now().format(timeStampFormatter()));
                        ps.setString(3, list.get(i).getChargePlaceId());
                        ps.setString(4, list.get(i).getName());
                        ps.setString(5, list.get(i).getCurrentCount().toString());
                        ps.setString(6, list.get(i).getTotalCount().toString());
                        ps.setString(7, list.get(i).getMembership().toString());
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
