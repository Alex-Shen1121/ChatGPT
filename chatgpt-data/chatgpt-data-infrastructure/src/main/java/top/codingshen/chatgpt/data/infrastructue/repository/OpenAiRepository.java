package top.codingshen.chatgpt.data.infrastructue.repository;

import org.springframework.stereotype.Repository;
import top.codingshen.chatgpt.data.domain.openai.model.entity.UserAccountEntity;
import top.codingshen.chatgpt.data.domain.openai.model.repository.IOpenAiRepository;
import top.codingshen.chatgpt.data.domain.openai.model.valobj.UserAccountStatusVO;
import top.codingshen.chatgpt.data.infrastructue.dao.IUserAccountDao;
import top.codingshen.chatgpt.data.infrastructue.po.UserAccountPO;

import javax.annotation.Resource;

/**
 * @ClassName OpenAiRepository
 * @Description openai仓储实现
 * @Author alex_shen
 * @Date 2024/1/3 - 01:10
 */
@Repository
public class OpenAiRepository implements IOpenAiRepository {
    @Resource
    IUserAccountDao userAccountDao;

    @Override
    public UserAccountEntity queryUserAccount(String openid) {
        UserAccountPO userAccountPO = userAccountDao.queryUserAccount(openid);
        if (userAccountPO == null) {
            return null;
        }

        UserAccountEntity userAccountEntity = new UserAccountEntity();
        userAccountEntity.setOpenid(userAccountPO.getOpenid());
        userAccountEntity.setTotalQuota(userAccountPO.getTotalQuota());
        userAccountEntity.setSurplusQuota(userAccountPO.getSurplusQuota());
        userAccountEntity.genModelTypes(userAccountPO.getModelTypes());
        userAccountEntity.setStatus(UserAccountStatusVO.get(userAccountPO.getStatus()));

        return userAccountEntity;
    }

    @Override
    public int subAccountQuota(String openid) {
        return userAccountDao.subAccountQuota(openid);
    }
}
