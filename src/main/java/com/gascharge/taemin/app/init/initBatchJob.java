package com.gascharge.taemin.app.init;

import com.gascharge.taemin.app.repository.charge.ChargeBatchRepositoryImpl;
import com.gascharge.taemin.app.repository.reservation.ReservationBatchRepositoryImpl;
import com.gascharge.taemin.app.repository.user.UserBatchRepositoryImpl;
import com.gascharge.taemin.common.oauth.AuthProvider;
import com.gascharge.taemin.domain.entity.charge.Charge;
import com.gascharge.taemin.domain.entity.reservation.Reservation;
import com.gascharge.taemin.domain.entity.user.User;
import com.gascharge.taemin.domain.enums.charge.ChargePlaceMembership;
import com.gascharge.taemin.domain.enums.user.UserAuthority;
import com.gascharge.taemin.domain.enums.user.UserEmailVerified;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class initBatchJob {

    @Autowired
    private UserBatchRepositoryImpl userBatchRepository;
    @Autowired
    private ChargeBatchRepositoryImpl chargeBatchRepository;
    @Autowired
    private ReservationBatchRepositoryImpl reservationBatchRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        List<User> users = new ArrayList<>();
        List<Reservation> reservations = new ArrayList<>();
        List<Charge> charges = new ArrayList<>();

        int total = 1000000;
        for (int i = 0; i < total; i++) {
            User user = User.builder()
                    .name("test" + i)
                    .email("test" + i + "@test.com")
                    .imageUrl("testImageUrl" + i)
                    .emailVerified(UserEmailVerified.UNVERIFIED)
                    .password("testPassword" + i)
                    .provider(AuthProvider.google)
                    .providerId("testProviderId" + i)
                    .userAuthority(UserAuthority.ROLE_USER)
                    .build();
            users.add(user);

            Charge charge = Charge.builder()
                    .chargePlaceId("test" + i)
                    .name("test" + i)
                    .totalCount(Long.valueOf(i))
                    .currentCount(Long.valueOf(i))
                    .membership(i % 2 == 1 ? ChargePlaceMembership.MEMBERSHIP : ChargePlaceMembership.NOT_MEMBERSHIP)
                    .build();
            charges.add(charge);
        }

        userBatchRepository.saveAll(users);
        chargeBatchRepository.saveAll(charges);

        for (int i = 0; i < total; i++) {
            int randomInt = (int) (Math.random() * total / 100);
            Reservation reservation = Reservation.builder()
                    .reservationValidationId("test" + i)
                    .user(users.get(randomInt))
                    .charge(charges.get(randomInt))
                    .reservationTime(LocalDateTime.now())
                    .build();
            reservations.add(reservation);
        }

        reservationBatchRepository.saveAll(reservations);
    }
}
