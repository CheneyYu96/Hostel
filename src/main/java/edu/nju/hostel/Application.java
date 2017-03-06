package edu.nju.hostel;

import edu.nju.hostel.dao.ManagerRepository;
import edu.nju.hostel.entity.Manager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author yuminchen
 * @date 2017/2/20
 * @version V1.0
 */
@SpringBootApplication
public class Application {

//    @Autowired
//    ManagerRepository managerRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }

//    @Bean
//    public CommandLineRunner loadData(ManagerRepository repository) {
//        return (args) -> {
//            // save a couple of customers
//            String password = "123456";
//            repository.save(new Manager("Jack", password));
//            repository.save(new Manager("Chloe", password));
//            repository.save(new Manager("Kim", password));
//            repository.save(new Manager("David", password));
//            repository.save(new Manager("Michelle", password));
//
//            // fetch all customers
//            System.out.println("Manager found with findAll():");
//            System.out.println("-------------------------------");
//            for (Manager manager : repository.findAll()) {
//                System.out.println("id: "+manager.getId()+"  name:"+manager.getName()+"  password: "+manager.getPassword());
//            }
//
//        };
//    }
}
