package com.swpgavf.back.repository;

import com.swpgavf.back.entity.Kardex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IKardexRepository extends JpaRepository<Kardex, Long> {

}
