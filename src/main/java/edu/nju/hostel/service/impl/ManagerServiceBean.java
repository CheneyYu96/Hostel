package edu.nju.hostel.service.impl;

import edu.nju.hostel.dao.ManagerRepository;
import edu.nju.hostel.entity.Manager;
import edu.nju.hostel.service.ManagerService;
import edu.nju.hostel.utility.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author yuminchen
 * @date 2017/3/5
 * @version V1.0
 */
@Service
public class ManagerServiceBean implements ManagerService{
    private final ManagerRepository managerRepository;

    @Autowired
    public ManagerServiceBean(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    @Override
    public Manager verifyManager(Integer id, String password) {
        Manager manager = managerRepository.findOne(id);
        if(manager!=null&&manager.getPassword().equals(password)){
            return manager;
        }
        return null;
    }

    @Override
    public Manager findManager(Integer managerId) {
        return managerRepository.findOne(managerId);
    }

    @Override
    public ResultInfo modifyPassword(Integer managerId, String originPassword, String password) {
        Manager manager = managerRepository.findOne(managerId);
        if(originPassword.equals(manager.getPassword())){
            manager.setPassword(password);
            managerRepository.save(manager);
            return new ResultInfo(true);
        }
        return new ResultInfo(false,"密码不正确");
    }
}
