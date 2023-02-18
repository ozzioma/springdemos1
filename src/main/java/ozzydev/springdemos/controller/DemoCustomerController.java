package ozzydev.springdemos.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ozzydev.springdemos.repos.mysql.CustomerRepo;
import ozzydev.springdemos.models.mysql.DemoCustomer;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customer")
public class DemoCustomerController
{

    private final CustomerRepo customerRepo;

    DemoCustomerController(CustomerRepo repository)
    {
        customerRepo = repository;
    }

    @GetMapping
    public List<DemoCustomer> findAll()
    {
        return customerRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DemoCustomer> findById(@PathVariable(value = "id") long id)
    {
        Optional<DemoCustomer> single = customerRepo.findById(id);

        if (single.isPresent())
        {
            return ResponseEntity.ok().body(single.get());
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addNew")
    public ResponseEntity<DemoCustomer> addEmployee(@Validated @RequestBody DemoCustomer data)
    {
        var newEntity = customerRepo.save(data);
        return new ResponseEntity<>(newEntity, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DemoCustomer> updateEmployee(@PathVariable Long id, @Validated @RequestBody DemoCustomer data)
    {
        Boolean checkExists = customerRepo.existsById(id);

        if (checkExists)
        {
            var updatedEntity = customerRepo.save(data);
            return ResponseEntity.ok().body(updatedEntity);
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id)
    {
        Boolean checkExists = customerRepo.existsById(id);

        if (checkExists)
        {
            customerRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }
}


