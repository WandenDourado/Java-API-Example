package br.com.dourado.javaapiexample.controller;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.dourado.javaapiexample.model.Customer;
import br.com.dourado.javaapiexample.service.CustomerService;

@RestController
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	

	@GetMapping(path = "/customers")
	public ResponseEntity<List<Customer>> find() {
		if(customerService.find().isEmpty()) {
			return ResponseEntity.notFound().build(); 
		}
		return ResponseEntity.ok(customerService.find());
	}
	
	@DeleteMapping(path = "/customers")
	public ResponseEntity<Boolean> delete() {
		try {
			customerService.delete();
			return ResponseEntity.noContent().build();
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	@PostMapping(path = "/customers")
	@ResponseBody
	public ResponseEntity<Customer> create(@RequestBody JSONObject json) {
		try {
			if(customerService.isJSONValid(json.toString())) {
				Customer customer = customerService.create(json);
				var uri = ServletUriComponentsBuilder.fromCurrentRequest().path(customer.getLastname()).build().toUri();
				
				customerService.add(customer);
				return ResponseEntity.created(uri).body(null);
			}else {
				return ResponseEntity.badRequest().body(null);
			}
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
	}
	

	@PutMapping(path = "/customers/{id}", produces = { "application/json" })
	public ResponseEntity<Customer> update(@PathVariable("id") long id, @RequestBody JSONObject json) {
		try {
			if(customerService.isJSONValid(json.toString())) {
				Customer customer = customerService.findById(id);
				if(customer == null){
					return ResponseEntity.notFound().build(); 
				}else {
					Customer customerUpdated = customerService.update(customer, json);
					return ResponseEntity.ok(customerUpdated);
				}
			}else {
				return ResponseEntity.badRequest().body(null);
			}
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
	}

}
