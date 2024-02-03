package ru.kismi.transactionsdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kismi.transactionsdemo.entity.Car;

public interface CarRepository extends JpaRepository<Car, Long> {
}
