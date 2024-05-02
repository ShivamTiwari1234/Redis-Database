package com.example.redisDemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class EmployeeController {
    //String operation key string value string
    private static final String EMPLOYEE_KEY_PREFIX = "redisdemo:employee::";


    private static final String EMPLOYEE_LIST_KEY_PREFIX = "redisdemo:employee_list";

    private static final String EMPLOYEE_SET_KEY_PREFIX = "redisdemo:employee_set";

    private static final String EMPLOYEE_MAP_KEY_PREFIX = "redisdemo:employee_Hashmap";

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Autowired
    ObjectMapper objectMapper;
    @PostMapping("/employee")
    public void saveEmployee(@RequestBody Employee employee){
       redisTemplate.opsForValue().set(getKey(employee.getId()),employee);

    }
    @GetMapping("/employee/{employeeId}")
    public Employee getEmployee(@PathVariable("employeeId") long employeeId){
        return (Employee) redisTemplate.opsForValue().get(getKey(employeeId));
    }

    // List Operations (Key - String, Value - List<Employee>) lpush means leftPush
    @PostMapping("lpush/employee")
    public void lpush(@RequestBody Employee employee) {
        redisTemplate.opsForList().leftPush(EMPLOYEE_LIST_KEY_PREFIX, employee);
    }

    private String getKey(long id) {
        return EMPLOYEE_KEY_PREFIX + id;
    }

    @DeleteMapping("/lpop/employee")
    public Employee lpop() {
        return (Employee) redisTemplate.opsForList().leftPop(EMPLOYEE_LIST_KEY_PREFIX);
    }

    @GetMapping("lrange/employee")
    public List<Employee> lrange(@RequestParam(value = "start", required = false, defaultValue = "0") int start,
                                 @RequestParam(value = "stop", required = false, defaultValue = "-1") int stop) {
        return redisTemplate.opsForList().range(EMPLOYEE_LIST_KEY_PREFIX, start, stop)
                .stream().map(employeeObject -> (Employee) employeeObject)
                .collect(Collectors.toList());
    }
    // List Operation
    @PostMapping("spush/employee")
    public void spush(@RequestBody Employee employee) {
        redisTemplate.opsForSet().add(EMPLOYEE_SET_KEY_PREFIX, employee);
    }

    //HashMap operatrion
    private String getHashKey(long id) {
        return EMPLOYEE_MAP_KEY_PREFIX + id;
    }


    @PostMapping("/hash/employee")
    public void saveEmployeeInHash(@RequestBody List<Employee> employeeList){
        employeeList.stream().forEach(employee -> {
            Map map= objectMapper.convertValue(employee,Map.class);
            redisTemplate.opsForHash().putAll(getHashKey(employee.getId()), map);
        });

    }
    @GetMapping("/hash/employee/All")
    public List<Employee> getAllEmployee(@RequestParam("ids") List<Long> ids){
        return ids. stream()
                .map(i -> redisTemplate.opsForHash().entries(getHashKey(i)))
                .map(entryMap -> objectMapper.convertValue(entryMap, Employee.class))
                .collect(Collectors.toList());
    }




}
