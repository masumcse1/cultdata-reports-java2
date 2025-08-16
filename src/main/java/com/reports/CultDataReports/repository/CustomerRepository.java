package com.reports.CultDataReports.repository;


import com.reports.CultDataReports.mapper.CustomerMapper;
import com.reports.CultDataReports.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query(value = """
        SELECT   c.id AS id, 
            c.firstname AS firstname, 
            c.lastname AS lastname, 
            c.email AS email, 
            c.birth_date AS birth_date, 
            c.addmission_date AS addmission_date FROM customer c   """, nativeQuery = true)
    List<CustomerMapper> findAllNativeRaw();

}
