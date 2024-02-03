package ru.kismi.transactionsdemo.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.kismi.transactionsdemo.repository.CarRepository;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final JdbcTemplate jdbcTemplate;

    @SneakyThrows
    @Transactional
    public void increaseCarPrice(Long carId) {
        increasePrice(carId);
    }

    @SneakyThrows
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void readCarPrice(Long carId) {
        readCarPrice1(carId);
    }

    @SneakyThrows
    private void increasePrice(Long carId) {
        var car = carRepository.findById(carId).orElseThrow();
        log.info("Price before increasing: {}", car.getPrice());

        car.setPrice(car.getPrice().add(BigDecimal.TEN));
        carRepository.save(car);

        Thread.sleep(300);

        log.info("Price after increasing: {}", car.getPrice());
    }

    @SneakyThrows
    private void readCarPrice1(Long carId) {
        var car = carRepository.findById(carId).orElseThrow();
        log.info("Car price: {}", car.getPrice());

        Thread.sleep(200);
        car = carRepository.findById(carId).orElseThrow();
        log.info("Car price: {}", car.getPrice());

        Thread.sleep(200);
        car = carRepository.findById(carId).orElseThrow();
        log.info("Car price: {}", car.getPrice());

        Thread.sleep(200);
        car = carRepository.findById(carId).orElseThrow();
        log.info("Car price: {}", car.getPrice());
    }

    @SneakyThrows
    private void readCarPrice2(Long carId) {
        var sql = "SELECT price FROM car WHERE id = " + carId;
        var price = jdbcTemplate.queryForObject(sql, String.class);
        log.info("Car price: {}", price);

        Thread.sleep(200);
        price = jdbcTemplate.queryForObject(sql, String.class);
        log.info("Car price: {}", price);

        Thread.sleep(200);
        price = jdbcTemplate.queryForObject(sql, String.class);
        log.info("Car price: {}", price);

        Thread.sleep(200);
        price = jdbcTemplate.queryForObject(sql, String.class);
        log.info("Car price: {}", price);
    }
}