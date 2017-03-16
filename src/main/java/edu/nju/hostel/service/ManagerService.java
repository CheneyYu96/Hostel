package edu.nju.hostel.service;

import edu.nju.hostel.entity.Manager;
import edu.nju.hostel.utility.ResultInfo;

/**
 *
 * @author yuminchen
 * @date 2017/3/5
 * @version V1.0
 */
public interface ManagerService {
    Manager verifyManager(Integer id, String password);

    Manager findManager(Integer managerId);

    ResultInfo modifyPassword(Integer managerId, String originPassword, String password);
}
