package com.caixy.adminSystem.model.dto.studentInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.caixy.adminSystem.model.entity.Post;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 帖子 ES 包装类
 *
 
 **/
// todo 取消注释开启 ES（须先配置 ES）
//@Document(indexName = "post")
@Data
public class StudentInfoEsDTO implements Serializable {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * id
     */
    @Id
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = DATE_TIME_PATTERN)
    private Date createTime;

    /**
     * 更新时间
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = DATE_TIME_PATTERN)
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    private static final long serialVersionUID = 1L;

    /**
     * 对象转包装类
     *
     * @param post
     * @return
     */
    public static StudentInfoEsDTO objToDto(Post post) {
        if (post == null) {
            return null;
        }
        StudentInfoEsDTO studentInfoEsDTO = new StudentInfoEsDTO();
        BeanUtils.copyProperties(post, studentInfoEsDTO);
        String tagsStr = post.getTags();
        if (StringUtils.isNotBlank(tagsStr)) {
            studentInfoEsDTO.setTags(JSONUtil.toList(tagsStr, String.class));
        }
        return studentInfoEsDTO;
    }

    /**
     * 包装类转对象
     *
     * @param studentInfoEsDTO
     * @return
     */
    public static Post dtoToObj(StudentInfoEsDTO studentInfoEsDTO) {
        if (studentInfoEsDTO == null) {
            return null;
        }
        Post post = new Post();
        BeanUtils.copyProperties(studentInfoEsDTO, post);
        List<String> tagList = studentInfoEsDTO.getTags();
        if (CollUtil.isNotEmpty(tagList)) {
            post.setTags(JSONUtil.toJsonStr(tagList));
        }
        return post;
    }
}
