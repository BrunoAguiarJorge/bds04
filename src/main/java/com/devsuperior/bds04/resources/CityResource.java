package com.devsuperior.bds04.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.bds04.dto.CityDTO;
import com.devsuperior.bds04.services.CityService;

@RestController
@RequestMapping(value = "/city")
public class CityResource {

	// let resource layer access service layer.
		@Autowired 
		private CityService service;

		@GetMapping
		// create a Page with all categories in it! ReuestParam -> used for pagination!
		public ResponseEntity<Page<CityDTO>> findAll(Pageable pageable) {
			Page<CityDTO> list = service.findAllPaged(pageable);
			return ResponseEntity.ok().body(list);
		}

		@GetMapping(value = "/{id}")
		public ResponseEntity<CityDTO> findById(@PathVariable Long id) {
			CityDTO dto = service.findById(id);
			// "ok" return http code 200
			return ResponseEntity.ok().body(dto);
		}

		@PostMapping
		public ResponseEntity<CityDTO> insert(@RequestBody CityDTO dto) {
			dto = service.insert(dto);
			URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(dto.getId()).toUri();
			// created return http code 201
			return ResponseEntity.created(uri).body(dto);
		}

		@PutMapping(value = "/{id}")
		public ResponseEntity<CityDTO> update(@PathVariable Long id, @RequestBody CityDTO dto) {
			dto = service.update(id, dto);
			return ResponseEntity.ok().body(dto);
		}

		@DeleteMapping(value = "/{id}")
		public ResponseEntity<CityDTO> delete(@PathVariable Long id) {
			service.delete(id);
			return ResponseEntity.noContent().build();
		}
	}


