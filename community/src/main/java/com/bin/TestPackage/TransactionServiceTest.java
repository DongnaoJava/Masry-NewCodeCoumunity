package com.bin.TestPackage;

import com.bin.bean.DiscussPost;
import com.bin.bean.User;
import com.bin.service.impl.DiscussPostServiceImpl;
import com.bin.service.impl.UserServiceImpl;
import com.bin.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import java.util.Date;

@Service
public class TransactionServiceTest {
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private DiscussPostServiceImpl discussPostService;
    @Autowired
    private TransactionTemplate transactionTemplate;

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public String save1() {
        //注册一个用户
        User user = new User();
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.Md5(12345 + user.getSalt()));
        user.setType(0);
        user.setStatus(1);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl("http://api.btstu.cn/sjtx/api.php?lx=c1&format=images");
        user.setCreateTime(new Date());
        userServiceImpl.insertUser(user);
        //自动发送第一条帖子
        DiscussPost discussPost = new DiscussPost();
        discussPost.setTitle("我是ALPHER，新人一个！");
        discussPost.setContent("新人报道！");
        discussPost.setCreateTime(new Date());
        discussPost.setUserId(user.getId());
        discussPostService.insertDiscussPost(discussPost);

        return "ok!";
    }

    public String save2() {
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        return transactionTemplate.execute(new TransactionCallback<String>() {
            @Override
            public String doInTransaction(TransactionStatus status) {
                //注册一个用户
                User user = new User();
                user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
                user.setPassword(CommunityUtil.Md5(56789 + user.getSalt()));
                user.setType(0);
                user.setStatus(1);
                user.setActivationCode(CommunityUtil.generateUUID());
                user.setHeaderUrl("http://api.btstu.cn/sjtx/api.php?lx=c1&format=images");
                user.setCreateTime(new Date());
                userServiceImpl.insertUser(user);
                //自动发送第一条帖子
                DiscussPost discussPost = new DiscussPost();
                discussPost.setTitle("我是BETA，新人一个！");
                discussPost.setContent("新人报道！");
                discussPost.setCreateTime(new Date());
                discussPost.setUserId(user.getId());
                discussPostService.insertDiscussPost(discussPost);

                return "ok!";
            }
        });
    }
}
