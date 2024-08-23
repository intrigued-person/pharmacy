package com.sahana.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sahana.modal.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
