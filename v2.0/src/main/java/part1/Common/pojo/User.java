package part1.Common.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * builder : 允许链式构造对象  user.builder().id(1)....
 * NoArgsConstructor : 自动生成一个无参构造器
 * AllArgsConstructor ：自动生成一个全参构造器
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    //客户端和服务端共有的
    private Integer id;
    private String userName;

    private Boolean sex;


}
