package top.codingshen.chatgpt.data.types.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName Response
 * @Description description
 * @Author alex_shen
 * @Date 2023/12/26 - 01:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> implements Serializable {

    private String code;
    private String info;
    private T data;

}
