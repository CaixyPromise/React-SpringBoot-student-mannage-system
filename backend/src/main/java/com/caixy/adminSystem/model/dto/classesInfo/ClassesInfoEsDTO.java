package com.caixy.adminSystem.model.dto.classesInfo;

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
public class ClassesInfoEsDTO implements Serializable {

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
    public static ClassesInfoEsDTO objToDto(Post post) {
        if (post == null) {
            return null;
        }
        ClassesInfoEsDTO classesInfoEsDTO = new ClassesInfoEsDTO();
        BeanUtils.copyProperties(post, classesInfoEsDTO);
        String tagsStr = post.getTags();
        if (StringUtils.isNotBlank(tagsStr)) {
            classesInfoEsDTO.setTags(JSONUtil.toList(tagsStr, String.class));
        }
        return classesInfoEsDTO;
    }

    /**
     * 包装类转对象
     *
     * @param classesInfoEsDTO
     * @return
     */
    public static Post dtoToObj(ClassesInfoEsDTO classesInfoEsDTO) {
        if (classesInfoEsDTO == null) {
            return null;
        }
        Post post = new Post();
        BeanUtils.copyProperties(classesInfoEsDTO, post);
        List<String> tagList = classesInfoEsDTO.getTags();
        if (CollUtil.isNotEmpty(tagList)) {
            post.setTags(JSONUtil.toJsonStr(tagList));
        }
        return post;
    }
}
