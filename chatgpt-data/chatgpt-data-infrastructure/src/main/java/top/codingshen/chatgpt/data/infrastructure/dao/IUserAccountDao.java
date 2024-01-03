package top.codingshen.chatgpt.data.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import top.codingshen.chatgpt.data.infrastructure.po.UserAccountPO;

/**
 * @ClassName IUserAccountDao
 * @Description 用户账户 dao
 * @Author alex_shen
 * @Date 2024/1/2 - 23:52
 */
@Mapper
public interface IUserAccountDao {

    UserAccountPO queryUserAccount(String openid);

    int subAccountQuota(String openid);
}
