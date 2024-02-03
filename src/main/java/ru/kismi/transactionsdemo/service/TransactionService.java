package ru.kismi.transactionsdemo.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.kismi.transactionsdemo.repository.CarRepository;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final CarService carService;
    private final CarRepository carRepository;
    private final SecondTransactionService secondTransactionService;

    public void readAndIncreaseCarPrice() {
        var firstIncrease = CompletableFuture.runAsync(() -> {
            log.info("Start transaction");
            carService.increaseCarPrice(1L);
            log.info("End transaction");
        });
        var secondIncrease = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            carService.readCarPrice(1L);
        });

        CompletableFuture.allOf(firstIncrease, secondIncrease)
                .join();
    }

    @Transactional(readOnly = true)
    public void readOnlyTransaction() {
        var car = carRepository.findById(1L).orElseThrow();
        log.info("Start transaction, car price: {}", car.getPrice());
        car.setPrice(BigDecimal.ONE);
        carRepository.save(car);

        car = carRepository.findById(1L).orElseThrow();
        log.info("End transaction, car price: {}", car.getPrice());
    }

    @Transactional(noRollbackFor = RuntimeException.class)
    public void noRollbackFor() {
        var car = carRepository.findById(1L).orElseThrow();
        log.info("Start transaction, car price: {}", car.getPrice());
        car.setPrice(BigDecimal.ONE);
        carRepository.save(car);

        throw new RuntimeException();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void requiredExceptionally() {
        var car = carRepository.findById(1L).orElseThrow();
        log.info("Start transaction, car price: {}", car.getPrice());
        car.setPrice(BigDecimal.ONE);
        carRepository.save(car);

        throw new RuntimeException();
    }

    @SneakyThrows
    @Transactional(timeout = 1)
    public void timeout() {
        var car = carRepository.findById(1L).orElseThrow();
        log.info("Start transaction, car price: {}", car.getPrice());
        car.setPrice(BigDecimal.TEN);
        carRepository.save(car);

        Thread.sleep(1_000);
    }

    @Transactional
    public void requiresNewPropagationWithExceptionInNewTransaction() {
        var car = carRepository.findById(1L).orElseThrow();
        log.info("Start transaction, car price: {}", car.getPrice());
        car.setPrice(BigDecimal.ONE);
        carRepository.save(car);

        secondTransactionService.requiresNewExceptionally();
    }

    @Transactional
    public void requiresNewPropagationWithExceptionInMainTransaction() {
        secondTransactionService.requiresNew();
        throw new RuntimeException();
    }

    @Transactional
    public void supports() {
        secondTransactionService.supports();
    }

    @Transactional
    public void mandatory() {
        secondTransactionService.mandatory();
    }

    @Transactional
    public void never() {
        secondTransactionService.never();
    }

    @Transactional
    public void notSupported() {
        secondTransactionService.notSupported();
    }

    @Transactional
    public void nested() {
        var car = carRepository.findById(1L).orElseThrow();
        log.info("Start transaction, car price: {}", car.getPrice());
        car.setPrice(BigDecimal.TEN);
        carRepository.save(car);

        secondTransactionService.nested();
    }
}