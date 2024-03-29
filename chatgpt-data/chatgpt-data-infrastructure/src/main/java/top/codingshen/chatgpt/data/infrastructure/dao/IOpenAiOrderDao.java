package top.codingshen.chatgpt.data.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import top.codingshen.chatgpt.data.infrastructure.po.OpenAiOrderPO;

import java.util.List;

/**
 * @ClassName IOpenAi
 * @Description OpenAi 订单 dao
 * @Author alex_shen
 * @Date 2024/1/3 - 23:25
 */
@Mapper
public interface IOpenAiOrderDao {

    OpenAiOrderPO queryUnpaidOrder(OpenAiOrderPO openAiOrderPOReq);

    void updateOrderPayInfo(OpenAiOrderPO openAiOrderPO);

    void insert(OpenAiOrderPO openAIOrderPO);

    int changeOrderPaySuccess(OpenAiOrderPO openAIOrderPO);

    OpenAiOrderPO queryOrder(String orderId);

    int updateOrderStatusDeliverGoods(String orderId);

    List<String> queryReplenishmentOrder();

    List<String> queryNoPayNotifyOrder();

    List<String> queryTimeoutCloseOrderList();

    boolean changeOrderClose(String orderId);

    Integer queryPayMethodByOrderId(String orderId);
}
