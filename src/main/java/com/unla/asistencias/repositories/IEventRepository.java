package com.unla.asistencias.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unla.asistencias.models.database.Event;

@Repository
public interface IEventRepository extends JpaRepository<Event,Serializable> {

}