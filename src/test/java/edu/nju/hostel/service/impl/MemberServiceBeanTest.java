package edu.nju.hostel.service.impl;

import edu.nju.hostel.entity.MemberCard;
import edu.nju.hostel.service.MemberService;
import edu.nju.hostel.utility.ResultInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/6
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class MemberServiceBeanTest{

    @Autowired
    private MemberService memberServiceBean;

    @Test
    public void verifyMember() throws Exception {

    }

    @Test
    public void register() throws Exception {
        MemberCard card = memberServiceBean.register("123","123456","12312312311");
        ResultInfo resultInfo = memberServiceBean.activate(card,"12313231231",1213);
        assert !resultInfo.isSuccess();
    }

    @Test
    public void activate() throws Exception {
    }

}