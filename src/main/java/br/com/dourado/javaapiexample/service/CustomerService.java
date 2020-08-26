package br.com.dourado.javaapiexample.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.dourado.javaapiexample.model.Customer;

@Service
public class CustomerService {
	
	private List<Customer> customers;
	
	CustomerService(){
		customers = new ArrayList<>();
	}
	
	public List<Customer> find() {
		return customers;
	}
	
	public boolean isJSONValid(String jsonInString) {
	    try {
	       return new ObjectMapper().readTree(jsonInString) != null;
	    } catch (IOException e) {
	       return false;
	    }
	}
	
	private long parseId(JSONObject json) {
		return Long.valueOf((int) json.get("id"));
	}
	
	public Customer findById(long id) {
		return customers.stream().filter(t -> id == t.getId()).collect(Collectors.toList()).get(0);
	}
	
	private void setCustomerValues(JSONObject json, Customer customer) {
		
		String name = (String) json.get("name");
		String lastname = (String) json.get("lastname");
		int age = (int) json.get("age");
		
		customer.setAge(age);
		customer.setLastname(lastname);
		customer.setName(name);
	}
	
	public void delete() {
		customers.clear();
	}
	
	public Customer create(JSONObject json) {		
		Customer customer = new Customer();
		customer.setId(parseId(json));
		setCustomerValues(json, customer);
		return customer;
	}
	

	public Customer update(Customer customer, JSONObject json) {
		setCustomerValues(json, customer);
		return customer;
	}

	public void add(Customer customer) {
		customers.add(customer);
	}

}
