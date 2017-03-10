package edu.nju.hostel.dao;

import edu.nju.hostel.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/10
 */
public interface PlanRepository extends JpaRepository<Plan,Integer>{
    List<Plan> findByHotelId(int hotelId);
}
