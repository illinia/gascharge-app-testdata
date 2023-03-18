package com.gascharge.taemin.app.repository.user;

import com.gascharge.taemin.app.repository.AbstractBatchRepository;
import com.gascharge.taemin.domain.entity.user.User;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static com.gascharge.taemin.common.util.DateTimeFormat.timeStampFormatter;

@Repository
public class UserBatchRepositoryImpl extends AbstractBatchRepository<User> {
    public UserBatchRepositoryImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    protected int batchInsert(int batchCount, List<User> list) {
        this.jdbcTemplate.batchUpdate(
                "insert into users (`created_date`, `modified_date`, `name`, `email`, `image_url`, `email_verified`, `password`, `provider`, `provider_id`, `user_authority`) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, LocalDateTime.now().format(timeStampFormatter()));
                        ps.setString(2, LocalDateTime.now().format(timeStampFormatter()));
                        ps.setString(3, list.get(i).getName());
                        ps.setString(4, list.get(i).getEmail());
                        ps.setString(5, list.get(i).getImageUrl());
                        ps.setString(6, list.get(i).getEmailVerified().toString());
                        ps.setString(7, list.get(i).getPassword());
                        ps.setString(8, list.get(i).getProvider().toString());
                        ps.setString(9, list.get(i).getProviderId());
                        ps.setString(10, list.get(i).getUserAuthority().toString());
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
