package com.hackerrank.api.repository;

import com.hackerrank.api.model.Scan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScanRepository extends JpaRepository<Scan, Long> {
}
