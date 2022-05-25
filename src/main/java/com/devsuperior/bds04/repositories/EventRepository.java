package com.devsuperior.bds04.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devsuperior.bds04.entities.City;
import com.devsuperior.bds04.entities.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
	@Query("SELECT DISTINCT obj FROM Event obj INNER JOIN obj.cities cit WHERE "
			+ "(COALESCE(:cities) IS NULL OR cit IN :cities) AND "
			+ "(LOWER(obj.name) LIKE LOWER(CONCAT('%', :name, '%') ) )")
	Page<Event> find(List<City> cities, String name, Pageable pageable);
	
	@Query("SELECT obj FROM Event obj JOIN FETCH obj.cities WHERE obj IN :events")
	List<Event> find(List<Event> events);
}
