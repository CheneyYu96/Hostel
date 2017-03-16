package edu.nju.hostel.service.impl;

import edu.nju.hostel.dao.*;
import edu.nju.hostel.entity.ApproveItem;
import edu.nju.hostel.entity.Hotel;
import edu.nju.hostel.entity.Manager;
import edu.nju.hostel.entity.Room;
import edu.nju.hostel.service.ManagerService;
import edu.nju.hostel.utility.ApproveType;
import edu.nju.hostel.utility.HotelStatus;
import edu.nju.hostel.utility.ResultInfo;
import edu.nju.hostel.vo.ApproveVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author yuminchen
 * @date 2017/3/5
 * @version V1.0
 */
@Service
public class ManagerServiceBean implements ManagerService{

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final ManagerRepository managerRepository;
    private final ApproveItemRepository approveItemRepository;
    private final PayItemRepository payItemRepository;

    @Autowired
    public ManagerServiceBean(HotelRepository hotelRepository,
                              RoomRepository roomRepository,
                              ManagerRepository managerRepository,
                              ApproveItemRepository approveItemRepository,
                              PayItemRepository payItemRepository) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.managerRepository = managerRepository;
        this.approveItemRepository = approveItemRepository;
        this.payItemRepository = payItemRepository;
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

    @Override
    public List<ApproveVO> getApprove() {
        return approveItemRepository
                .findAll()
                .stream()
                .filter(approveItem -> !approveItem.getHasApproved())
                .map( approveItem ->
                        {
                            ApproveVO approveVO = new ApproveVO();
                            approveVO.id = approveItem.getId();
                            int hotelId = approveItem.getHotelId();
                            int roomId = approveItem.getRoomId();
                            if(hotelId>0&&roomId>0){
                                approveVO.type = ApproveType.开店申请;
                                approveVO.hotelName = hotelRepository
                                        .findOne(hotelId)
                                        .getName();
                            }
                            else if(hotelId>0&&roomId<=0){
                                approveVO.type = ApproveType.信息修改;
                                approveVO.hotelName = hotelRepository
                                        .findOne(hotelId)
                                        .getName();
                            }
                            else {
                                approveVO.type = ApproveType.房间修改;
                                approveVO.hotelName = hotelRepository
                                        .findOne(roomRepository
                                                .findOne(roomId)
                                                .getHotelId())
                                        .getName();
                            }
                            return approveVO;
                        }
                )
                .collect(Collectors.toList());
    }

    @Override
    public Room getRoomInApprove(int id) {
        return roomRepository
                .findOne(approveItemRepository
                        .findOne(id)
                        .getRoomId());
    }

    @Override
    public Hotel getHotelInfoInApprove(int id) {
        return hotelRepository
                .findOne(approveItemRepository
                        .findOne(id)
                        .getHotelId());
    }

    @Override
    public List<Room> getHotelRoomInApprove(int hotelId) {
        return roomRepository
                .findByHotelId(hotelId)
                .stream()
                .filter( room -> room.getStatus()== HotelStatus.待审批)
                .collect(Collectors.toList());
    }


    @Override
    public ResultInfo approveItem(Integer approveId) {

        ApproveItem approveItem = approveItemRepository.findOne(approveId);
        approveItem.setHasApproved(true);

        if(approveItem.getRoomId()>0){
            Room room = roomRepository.findOne(approveItem.getRoomId());
            room.setStatus(HotelStatus.已批准);
            roomRepository.save(room);
        }
        approveItemRepository.save(approveItem);
        return new ResultInfo(true);
    }
}
