package ru.kismi.transactionsdemo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.kismi.transactionsdemo.repository.CarRepository;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecondTransactionService {

    private final CarRepository carRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void requiresNewExceptionally() {
        throw new RuntimeException();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void requiresNew() {
        var car = carRepository.findById(1L).orElseThrow();
        log.info("Start transaction, car price: {}", car.getPrice());
        car.setPrice(BigDecimal.ONE);
        carRepository.save(car);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void supports() {
        // TransactionAspectSupport.currentTransactionInfo().getTransactionStatus().hasTransaction()
        log.info("");
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void mandatory() {
        // TransactionAspectSupport.currentTransactionInfo().getTransactionStatus().hasTransaction()
        log.info("");
    }

    @Transactional(propagation = Propagation.NEVER)
    public void never() {
        // TransactionAspectSupport.currentTransactionInfo().getTransactionStatus().hasTransaction()
        log.info("");
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void notSupported() {
        // TransactionAspectSupport.currentTransactionInfo().getTransactionStatus().hasTransaction()
        log.info("");
    }

    @Transactional(propagation = Propagation.NESTED)
    public void nested() {
        log.info("");
    }
}