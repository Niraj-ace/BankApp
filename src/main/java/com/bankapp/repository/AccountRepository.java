package com.bankapp.repository;

import com.bankapp.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;

public interface AccountRepository extends JpaRepository<Account, Long> {
	
	@Override
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<Account> findById(Long id);
	
    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findByUserId(Long userId);

}
