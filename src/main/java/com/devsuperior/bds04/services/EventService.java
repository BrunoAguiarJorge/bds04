package com.devsuperior.bds04.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brunoaguiar.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.bds04.dto.CityDTO;
import com.devsuperior.bds04.dto.EventDTO;
import com.devsuperior.bds04.entities.City;
import com.devsuperior.bds04.entities.Event;
import com.devsuperior.bds04.repositories.CityRepository;
import com.devsuperior.bds04.repositories.EventRepository;
import com.devsuperior.bds04.resources.exceptions.DataBaseException;

@Service
public class EventService {
	
		@Autowired
		private EventRepository repository;

		@Autowired
		private CityRepository cityRepository;


		@Transactional(readOnly = true)
		public Page<EventDTO> findAllPaged(Pageable pageable) {
			Page<Event> page = repository.findAll(pageable);
			
			return page.map(x -> new EventDTO(x, x.getCities()));
		}

		@Transactional(readOnly = true)
		public EventDTO findById(Long id) {
			Optional<Event> obj = repository.findById(id);
			Event entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity Not Found"));
			return new EventDTO(entity, entity.getCities());
		}

		@Transactional
		public EventDTO insert(EventDTO dto) {
			Event entity = new Event();
			copyDtoToEntity(dto, entity);
			if (entity.getCities().size() == 0) {
				City cat = cityRepository.getOne(1L);
				entity.getCities().add(cat);
			}
			entity = repository.save(entity);
			return new EventDTO(entity);
		}

		@Transactional
		public EventDTO update(Long id, EventDTO dto) {
			try {
				Event entity = repository.getOne(id);
				copyDtoToEntity(dto, entity);
				if (entity.getCities().size() == 0) {
					City cat = cityRepository.getOne(1L);
					entity.getCities().add(cat);
				}
				entity = repository.save(entity);
				return new EventDTO(entity);
			} catch (EntityNotFoundException e) {
				throw new ResourceNotFoundException("Id not found " + id);
			}
		}

		public void delete(Long id) {
			try {
				repository.deleteById(id);
			} catch (EmptyResultDataAccessException e) {
				throw new ResourceNotFoundException("Id not found " + id);
			} catch (DataIntegrityViolationException e) {
				throw new DataBaseException("Integrity violation");
			}

		}

		private void copyDtoToEntity(EventDTO dto, Event entity) {
			entity.setName(dto.getName());
			entity.setDate(dto.getDate());
			entity.setUrl(null);
			
			entity.getCities().clear();
			for (CityDTO catDto : dto.getCities()) {
				City city = cityRepository.getOne(catDto.getId());
				entity.getCities().add(city);
			}
		}
}
