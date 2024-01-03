package top.codingshen.chatgpt.data.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import top.codingshen.chatgpt.data.infrastructure.po.OpenAiProductPO;

/**
 * @ClassName IOpenAiProductDao
 * @Description OpenAi 产品 dao
 * @Author alex_shen
 * @Date 2024/1/3 - 23:26
 */
@Mapper
public interface IOpenAiProductDao {
    OpenAiProductPO queryProductByProductId(Integer productId);
}
